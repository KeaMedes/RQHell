import java.util.logging.Logger;

/**
 * Created by luoy on 17-3-24.
 */
public class Profiler {
    private static Logger LOG = Logger.getLogger("Profiler");
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
        LOG.info(builder.toString());
    }
}
