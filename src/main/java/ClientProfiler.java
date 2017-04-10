import com.sun.org.apache.xml.internal.utils.XMLStringFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by luoy on 17-3-24.
 */
public class ClientProfiler {
    private static Logger LOG = Logger.getLogger("ClientProfiler");
    private long mClientLoadFileStart;
    private long mClientLoadFileStop;

    private long mClientSendFECStart;
    private long mClientSendFECStop;

    private long mClientGetResponseStart;
    private long mClientGetResponseStop;

    private long mClientCalculateFECStart;
    private long mClientCalculateFECStop;

    private long mClientPartitionDataStart;
    private long mClientPartitionDataStop;

    private List<Long> mClientPacketEncodeSourceStartArray = new ArrayList<>(1024);
    private List<Long> mClientPacketEncodeSourceStopArray = new ArrayList<>(1024);
    private List<Long> mClientPacketEncodeRepariStartArray = new ArrayList<>(1024);
    private List<Long> mClientPacketEncodeRepariStopArray = new ArrayList<>(1024);
//    private List<List<Integer>> mClientPacketSendTimePerBlock;

    public void clientLoadFileStart() {
        mClientLoadFileStart = System.currentTimeMillis();
    }

    public void clientLoadFileStop() {
        mClientLoadFileStop = System.currentTimeMillis();
    }

    public void clientSendFECStart() {
        mClientSendFECStart = System.currentTimeMillis();
    }

    public void clientSendFECStop() {
        mClientSendFECStop = System.currentTimeMillis();
    }

    public void clientGetResponseStart() {
        mClientGetResponseStart = System.currentTimeMillis();
    }

    public void clientGetResponseStop() {
        mClientGetResponseStop = System.currentTimeMillis();
    }

    public void clientCalculateFECStart() {
        mClientCalculateFECStart = System.currentTimeMillis();
    }

    public void clientCalculateFECSTop() {
        mClientCalculateFECStop = System.currentTimeMillis();
    }

    public void clientPartitionDataStart() {
        mClientPartitionDataStart = System.currentTimeMillis();
    }

    public void clientPartitionDataStop() {
        mClientPartitionDataStop = System.currentTimeMillis();
    }

    public void clientPacketEncodeSourceStart() {
        mClientPacketEncodeSourceStartArray.add(System.currentTimeMillis());
    }

    public void clientPacketEncodeSourceStop() {
        mClientPacketEncodeSourceStopArray.add(System.currentTimeMillis());
    }

    public void clientPacketEncodeRepariStart() {
        mClientPacketEncodeRepariStartArray.add(System.currentTimeMillis());
    }

    public void clientPacketEncodeRepariStop() {
        mClientPacketEncodeRepariStopArray.add(System.currentTimeMillis());
    }

    public void show() {
        StringBuilder builder = new StringBuilder();
        builder.append("Calculate FEC time usage: ");
        builder.append(mClientCalculateFECStop - mClientCalculateFECStart);
        builder.append("\n");
        builder.append("Load file time usage: ");
        builder.append(mClientLoadFileStop - mClientLoadFileStart);
        builder.append("\n");
        builder.append("Send FEC time usage: ");
        builder.append(mClientSendFECStop - mClientSendFECStart);
        builder.append("\n");
        builder.append("Get Response time usage: ");
        builder.append(mClientGetResponseStop - mClientGetResponseStart);
        builder.append("\n");
        builder.append("Partition data time usage: ");
        builder.append(mClientPartitionDataStop - mClientPartitionDataStart);
        builder.append("\n");

        int size = mClientPacketEncodeSourceStartArray.size();
        long minDuration = 100000;
        long maxDuration = -1;
        long totalDuration = 0;
        int zeroNum = 0;
        double avgDuration;
//        System.out.println(mClientPacketEncodeSourceStopArray.size());
        builder.append(String.format("source packet number: %d\n", mClientPacketEncodeSourceStartArray.size()));
        for (int i = 0; i < size; i++) {
            long startTime = mClientPacketEncodeSourceStartArray.get(i);
            long endTime = mClientPacketEncodeSourceStopArray.get(i);
            long diff = endTime - startTime;
            if (diff == 0) {
                zeroNum ++;
            } else {
                builder.append("index:").append(i).append(":").append(diff).append("\n");
            }
//            builder.append(String.format("%d\t", diff));
            if (diff > maxDuration) {
                maxDuration = diff;
            }
            if (diff < minDuration) {
                minDuration = diff;
            }
            totalDuration += diff;
        }
        avgDuration = totalDuration / size;
        builder.append(String.format("\nEncode source packet: avg: %f, max: %d, min: %d, zeros: %d\n",
                avgDuration, maxDuration, minDuration, zeroNum));

        minDuration = 100000;
        maxDuration = -1;
        totalDuration = 0;
        zeroNum = 0;
        size = mClientPacketEncodeRepariStartArray.size();
        builder.append(String.format("repair packet number: %d\n", mClientPacketEncodeRepariStartArray.size()));
        for (int i = 0; i < size; i++) {
            long startTime = mClientPacketEncodeRepariStartArray.get(i);
            long endTime = mClientPacketEncodeRepariStopArray.get(i);
            long diff = endTime - startTime;
            if (diff == 0) {
                zeroNum ++;
            } else {
                builder.append("index:").append(i).append(":").append(diff).append("\n");
            }
            if (diff == 0)
            if (diff > maxDuration) {
                maxDuration = diff;
            }
            if (diff < minDuration) {
                minDuration = diff;
            }
            totalDuration += diff;
        }
        avgDuration = totalDuration / size;
        builder.append(String.format("\nEncode repair packet: avg: %f, max: %d, min: %d, zeros: %d\n",
                avgDuration, maxDuration, minDuration, zeroNum));
        LOG.info(builder.toString());
    }
}
