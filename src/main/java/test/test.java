package test;

import research.FakeJsonServer;
import research.MyHttpServer;

import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
        FakeJsonServer servers = FakeJsonServer.getInstance(10);
        MyHttpServer server = servers.getServer("localhost",8001);
        MyHttpServer server2 = servers.getServer("localhost",8002);
        server.newResponse("/test", "{\"hello\" : \"world\"}")
                .newResponse("/teste","{\"hello\" : \"worlde\"}");
        server.forward("/news", "http://localhost:8002");
        server.newResponse("/testUpdate","\"Working\" : true");
        server2.newResponse("/news", "{\"hello\" : \"forwardedMessage\"}");
        System.out.println(server.urlAvailable());
    }
}
