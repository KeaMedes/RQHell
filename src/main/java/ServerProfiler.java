/**
 * Created by luoy on 17-4-7.
 */
public class ServerProfiler {
    private long mRecFECStart;
    private long mRecFECStop;

    public void starRecFEC() {
        mRecFECStart = System.currentTimeMillis();
    }

    public void stopRecFEC() {
        mRecFECStop = System.currentTimeMillis();
    }
}
