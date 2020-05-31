package research;

import com.sun.net.httpserver.HttpServer;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MyHttpServer{
    private HttpServer server;
    private final Map<String, String> responses = new HashMap<>();
    static Logger logger = Logger.getLogger(FakeJsonServer.class.getName());

    void setServer(HttpServer server) {
        this.server = server;
        this.server.start();
    }

    void stopServer(){
        server.stop(100);
        this.server = null;
    }

    public MyHttpServer newResponse(String url, String response){
        this.responses.put(url, response);
        logger.info("Creating context for " + url);
        this.server.createContext(url, new MyHttpHandler(response));
        return this;
    }

    public MyHttpServer forward(String url, String forwardUri){
        server.createContext(url, new HttpHandlerForwarding(forwardUri));
        return this;
    }
}
