import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by luoy on 2/27/2017.
 */
public class RQHell {
    public static void main(String[] args) {
        String command = args[0];
        String hostAddr = "127.0.0.1";
        int hostPort = 8088;
        if (command.equals("client")) {
            if (args.length == 3) {
                hostAddr = args[1];
                hostPort = Integer.valueOf(args[2]);
            }
            String cwd = System.getProperty("user.dir");
            Path path = Paths.get(cwd, "data", "data1");
            Client client = new Client();
            client.establish(hostAddr, hostPort);
            client.sendFile(path);
        } else if (command.equals("server")) {
            if (args.length == 3) {
                hostAddr = args[1];
                hostPort = Integer.valueOf(args[2]);
            }
            Server server = new Server();
            server.establish(hostPort);
            server.run();
        }
    }
}