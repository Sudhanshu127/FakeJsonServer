package research;

import com.sun.net.httpserver.HttpServer;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MyHttpServer{
    private HttpServer server;
    private Map<String, String> responses = new HashMap<>();
    static Logger logger = Logger.getLogger(FakeJsonServer.class.getName());

    void setServer(HttpServer server) {
        this.server = server;
    }

    void stopServer(){
        server.stop(100);
        this.server = null;
    }

    public MyHttpServer newResponse(String url, String response){
        this.responses.put(url, response);
        return this;
    }

    public MyHttpServer build(){
        for (Map.Entry<String, String> response : responses.entrySet()){
            logger.info("Adding " + response.getKey() + ":" + response.getValue());
            server.createContext(response.getKey(), new MyHttpHandler(response.getValue()));
        }
        logger.info("Starting Server");
        server.start();
        return this;
    }

    public MyHttpServer forward(String url, String forwardUri){
        server.createContext(url, new HttpHandlerForwarding(forwardUri));
        return this;
    }
}
