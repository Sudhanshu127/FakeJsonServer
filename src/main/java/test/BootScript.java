package test;

import research.FakeJsonServer;
import research.MyHttpServer;
import research.Redis;
import research.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

public class BootScript {
    public static void main(String[] args) {
        Redis myObject = Redis.getInstance();
        Set<String> servers = myObject.allServers();
        FakeJsonServer fakeJsonServer = FakeJsonServer.getInstance(10);
        for(String server : servers){
            String hostname = server.substring(0,server.lastIndexOf(':'));
            int port = Integer.parseInt(server.substring(server.lastIndexOf(':')+1,server.lastIndexOf(':')+5));
            System.out.println(hostname);
            System.out.println(port);
            try {
                MyHttpServer myHttpServer =fakeJsonServer.getServer(hostname, port);
                for(Map.Entry<String, Response> entry : myObject.allResponses(server).entrySet()){
                    if(entry.getValue().getRedirectionUrl() != null)
                    {
                        myHttpServer.newResponse(entry.getKey(), entry.getValue());
                    }
                    else
                    {
                        try {
                            myHttpServer.forward(entry.getKey(), entry.getValue().getRedirectionUrl());
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("I am out");

    }
}
