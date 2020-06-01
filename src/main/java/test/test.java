package test;

import research.FakeJsonServer;
import research.MyHttpServer;

import java.io.IOException;
import java.net.URISyntaxException;

public class test {
    public static void main(String[] args) throws IOException, URISyntaxException {
        FakeJsonServer servers = FakeJsonServer.getInstance(10);
        MyHttpServer server = servers.getServer("localhost",8001);
        MyHttpServer server2 = servers.getServer("localhost",8002);
        server.newResponse("/test", "{\"hello\" : \"world\"}")
                .newResponse("/news/worlde","{\"hello\" : \"worlde\"}");
        server.newResponse("/testUpdate","\"Working\" : true");
        server.forward("/news", "http://localhost:8002/");
        server2.newResponse("/news", "{\"hello\" : \"forwardedMessage\"}");
        server2.newResponse("/news/wow", "{\"hello\" : \"forwardedMessage\"}");
        System.out.println(server.urlAvailable());
    }
}
