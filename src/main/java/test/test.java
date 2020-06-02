package test;

import research.FakeJsonServer;
import research.MyHttpServer;
import research.Response;

import java.io.IOException;
import java.net.URISyntaxException;

public class test {
    public static void main(String[] args) throws IOException, URISyntaxException {
        FakeJsonServer servers = FakeJsonServer.getInstance(10);
        MyHttpServer server = servers.getServer("localhost",8001);
        MyHttpServer server2 = servers.getServer("localhost",8002);

        server.newResponse("/test", new Response().setGetResponse("{\"method\" : \"get\"}").setPostResponse("{\"method\" : \"post\"}"))
                .newResponse("/news/worlde",new Response().setGetResponse("{\"method\" : \"get\"}").setPostResponse("{\"method\" : \"post\"}"));

        server.newResponse("/testUpdate",server.getResponse("/test").setGetResponse("{\"method\" : \"getUpdate\"}"));
        server.forward("/news", "http://localhost:8002/");
        System.out.println(server.urlAvailable());
    }
}
