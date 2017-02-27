/**
 * Created by luoy on 2/27/2017.
 */
public class RQHell {
    public static void main(String[] args) {
        String command = args[0];
        if (command.equals("client")) {
            String pathName = "D:\\code\\RQHell\\data\\data1";
            Client client = new Client();
            client.sendFile(pathName);
        }
    }
}
