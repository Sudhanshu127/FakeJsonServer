package test;

import research.FakeJsonServer;
import research.MyHttpServer;

import java.io.IOException;

public class testCopy {
    public static void main(String[] args) throws IOException {
        FakeJsonServer servers = FakeJsonServer.getInstance();
        MyHttpServer server = servers.addServer("localhost",8002);
        server.newResponse("/test", "{\"hello\" : \"world\"}")
                .newResponse("/teste","{\"hello\" : \"worlde\"}");
        server.build();
    }
}
