import net.fec.openrq.parameters.FECParameters;
import net.fec.openrq.util.datatype.SizeOf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by luoy on 2/28/2017.
 */
public class ShareFEC {
    private int mSymbolSize = 1024;
    private int mNumOfSymbolPerBlock = 30;
    private long mFileSize = -1;
    private int mPacketSize = -1;

    public int getSymbolSize() {
        return mSymbolSize;
    }

    public void setSymbolSize(int mSymbolSize) {
        this.mSymbolSize = mSymbolSize;
    }

    public int getNumOfSymbolPerBlock() {
        return mNumOfSymbolPerBlock;
    }

    public void setNumOfSymbolPerBlock(int mNumOfSymbolPerBlock) {
        this.mNumOfSymbolPerBlock = mNumOfSymbolPerBlock;
    }

    public FECParameters getParameters() {
        FECParameters f = FECParameters.deriveParameters(mFileSize, mSymbolSize, mSymbolSize * mNumOfSymbolPerBlock);
        mSymbolSize = f.symbolSize();
        mPacketSize = f.symbolSize() + SizeOf.INT * 2;
        return f;
    }

    public long getFileSize() {
        return mFileSize;
    }

    public void setFileSize(long mFileSize) {
        this.mFileSize = mFileSize;
    }

    public long setFileSize(Path path) {
        try {
            this.mFileSize = Files.size(path);
        } catch (IOException e) {
            System.out.println("File do not exist");
            e.printStackTrace();
        }
        return this.mFileSize;
    }

    public int getPacketSize() {
        return mPacketSize;
    }
}
