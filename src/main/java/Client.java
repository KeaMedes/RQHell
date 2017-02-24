/*
 * This Java source file was generated by the Gradle 'init' task.
 */
import net.fec.openrq.parameters.FECParameters;

import java.io.File;

public class Client {
    public Client() {}

    public void sendFile(String pathName) {
        File f = new File(pathName);
        long fSize = f.length();
        System.out.println(fSize);
        FECParameters fecParameters = FECParameters.deriveParameters(fSize, 1024, 1024 * 1024 * 10);
        int symbolSize = fecParameters.symbolSize();
        int numSourceBlocks = fecParameters.numberOfSourceBlocks();
        System.out.println(String.format("file size: %d, symbol size: %d, number of source blocks: %d", fSize, symbolSize, numSourceBlocks));
    }

    public static void main(String[] args) {
        String pathName = "D:\\code\\RQHell\\data\\data1";
        Client client = new Client();
        client.sendFile(pathName);
    }
}
