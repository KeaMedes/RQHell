import net.fec.openrq.EncodingPacket;
import net.fec.openrq.OpenRQ;
import net.fec.openrq.Parsed;
import net.fec.openrq.decoder.DataDecoder;
import net.fec.openrq.decoder.SourceBlockDecoder;
import net.fec.openrq.parameters.FECParameters;
import net.fec.openrq.util.datatype.SizeOf;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Created by luoy on 2/27/2017.
 */
public class Server {
    private static Logger LOG = Logger.getLogger("Server");
    private Socket mSocket = null;
    private InputStream mSocketIs = null;
    private OutputStream mSocketOs = null;
    public Server() {}


    public void run() {
        LOG.info("Server start to run");

        LOG.info("wait for bytes to get fecParameters");
        byte[] fecParameterBuffer = new byte[12];
        int fecParameterBufferSize = 0;
        while (true) {
            try {
                fecParameterBufferSize += mSocketIs.read(fecParameterBuffer, fecParameterBufferSize, 12);
                if (fecParameterBufferSize == 12) {
                    LOG.info("Get 12 bytes for fecParamters");
                    break;
                } else {
                    LOG.info(String.format("Read %d bytes, not enough", fecParameterBufferSize));
                }
            } catch (IOException e) {
                e.printStackTrace();
                LOG.severe("Fail to get bytes for fecParameters");
                return;
            }
        }

        LOG.info("Decode the 12 bytes into fecParameters");
        Parsed<FECParameters> parsedFecParameters = FECParameters.parse(fecParameterBuffer);
        FECParameters fecParameters = null;
        if (parsedFecParameters.isValid()) {
            fecParameters = parsedFecParameters.value();
            LOG.info(String.format("Success to decode the fecParameters, data len: %d, block num: %d",
                    fecParameters.dataLength(), fecParameters.numberOfSourceBlocks()));
        } else {
            LOG.severe(String.format("Fail to decode the fecParameters, reason: %s", parsedFecParameters.failureReason()));
            return;
        }

        LOG.info("Response to the client");
        byte[] buf = "OK".getBytes();
        try {
            mSocketOs.write(buf);
            LOG.info("Success send the response");
        } catch (IOException e) {
            LOG.severe("Fail to send the response");
        }

        LOG.info("Start to receive packets");
        DataDecoder decoder = OpenRQ.newDecoder(fecParameters, 2);
        int packetSize = fecParameters.symbolSize() + SizeOf.INT * 2;
        byte[] buffer = new byte[packetSize];
        while (true) {
            int bufferLen = 0;
            while (true) {
                try {
                    int ret = mSocketIs.read(buffer, bufferLen, packetSize);
                    if (ret <= 0) {
                        LOG.info("No data to receive");
                        return;
                    }
                    bufferLen += ret;
                    if (bufferLen < packetSize) {
                        LOG.info(String.format("Fail to get a full packet, current size: %d", bufferLen));
                    } else {
                        LOG.info("Get a full packet");
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Parsed<EncodingPacket> parsedEncodingPacket = decoder.parsePacket(buffer, false);
            if (!parsedEncodingPacket.isValid()) {
                LOG.severe("Fail to parse packet, reason: " + parsedEncodingPacket.failureReason());
                return;
            }
            EncodingPacket encodingPacket = parsedEncodingPacket.value();
            LOG.info(String.format("Success to decode a packet, belong to block: %d", encodingPacket.sourceBlockNumber()));
            SourceBlockDecoder sourceBlockDecoder = decoder.sourceBlock(encodingPacket.sourceBlockNumber());
            sourceBlockDecoder.putEncodingPacket(encodingPacket);
            if (sourceBlockDecoder.isSourceBlockDecoded()) {
                LOG.info(String.format("Block %d is decoded", sourceBlockDecoder.sourceBlockNumber()));
            }
            if (decoder.isDataDecoded()) {
                LOG.info("Data is decodable, done!");
                break;
            }
        }
    }


    public void establish(int hostPort) {
        try {
            ServerSocket serverSocket = new ServerSocket(hostPort);
            LOG.info("Listening");
            mSocket = serverSocket.accept();
            mSocketIs = mSocket.getInputStream();
            mSocketOs = mSocket.getOutputStream();
            LOG.info("Receive connection");
        } catch (IOException e) {
            e.printStackTrace();
            mSocket = null;
        }
    }
}
