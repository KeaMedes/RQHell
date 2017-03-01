import net.fec.openrq.EncodingPacket;
import net.fec.openrq.OpenRQ;
import net.fec.openrq.Parsed;
import net.fec.openrq.decoder.DataDecoder;
import net.fec.openrq.decoder.SourceBlockDecoder;
import net.fec.openrq.parameters.FECParameters;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

/**
 * Created by luoy on 2/27/2017.
 */
public class Server {
    private Socket mSocket = null;
    private InputStream mSocketIs = null;
    private Path mFilePath = null;
    private ShareFEC mShareFEC = null;
    public Server() {}

    public void receiveFile() {
        System.out.println("Start receive file");
        mShareFEC = new ShareFEC();
        if (mShareFEC.setFileSize(mFilePath) == -1) {
            System.out.println("Fail to load file");
            return;
        }
        FECParameters fecParameters = mShareFEC.getParameters();
        DataDecoder decoder = OpenRQ.newDecoder(fecParameters, 2);
        while (true) {
            byte[] data = null;
            try {
                data = getData(mSocketIs);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if (data.length == 0) {
                return;
            }
            Parsed<EncodingPacket> parsedEncodingPacket = decoder.parsePacket(data, false);
            if (!parsedEncodingPacket.isValid()) {
                System.out.println("Fail to parse packet, reason: " + parsedEncodingPacket.failureReason());
            }
            EncodingPacket encodingPacket = parsedEncodingPacket.value();
            SourceBlockDecoder sourceBlockDecoder = decoder.sourceBlock(encodingPacket.sourceBlockNumber());
            sourceBlockDecoder.putEncodingPacket(encodingPacket);
            if (sourceBlockDecoder.isSourceBlockDecoded()) {
                System.out.println(String.format("Block %d is decoded", sourceBlockDecoder.sourceBlockNumber()));
            }
        }
    }

    private byte[] getData(InputStream inputStream) throws IOException {
        int packetSize = mShareFEC.getPacketSize();
        if (packetSize == -1) {
            System.out.println("Packet size unknown");
            return new byte[0];
        }
        int size = -1;
        byte[] buffer = new byte[packetSize];
        while (true) {
            size = inputStream.read(buffer);
            if (size != packetSize) {
                System.out.println(size);
                return new byte[0];
            }
            break;
        }
        return buffer;
    }

    public void establish(int hostPort) {
        try {
            ServerSocket serverSocket = new ServerSocket(hostPort);
            System.out.println("Listening");
            mSocket = serverSocket.accept();
            mSocketIs = mSocket.getInputStream();
            System.out.println("Receive connection");
        } catch (IOException e) {
            e.printStackTrace();
            mSocket = null;
        }
    }

    public void setFile(Path file) {
        this.mFilePath = file;
    }
}
