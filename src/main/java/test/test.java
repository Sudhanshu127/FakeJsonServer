package test;

import research.FakeJsonServer;
import research.MyHttpServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class test {
    public static void main(String[] args) throws IOException, InterruptedException {
        FakeJsonServer servers = FakeJsonServer.getInstance();
        MyHttpServer server = servers.addServer("localhost",8001);
        MyHttpServer server2 = servers.addServer("localhost",8002);
        server.newResponse("/test", "{\"hello\" : \"world\"}")
                .newResponse("/teste","{\"hello\" : \"worlde\"}");
        server.forward("/news", "http://localhost:8002");
        server.build();
        server2.newResponse("/news", "{\"hello\" : \"forwardedMessage\"}");
        server2.build();
    }
}
