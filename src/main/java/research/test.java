package research;

import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
        FakeJsonServer servers = FakeJsonServer.getInstance();
        servers.addServer("localhost",8001);
        servers.addServer("localhost",8002);
        servers.addServer("localhost",8003);
//        while(true){
            System.out.println("Running...");
//        }
    }
}
