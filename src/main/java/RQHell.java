import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by luoy on 2/27/2017.
 */
public class RQHell {
    public static void main(String[] args) {
        String command = args[0];
        if (command.equals("client")) {
            String cwd = System.getProperty("user.dir");
            Path path = Paths.get(cwd, "data", "data1");
            Client client = new Client();
            client.sendFile(path);
        }
    }
}