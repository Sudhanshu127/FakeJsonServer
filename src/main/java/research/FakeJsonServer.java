package research;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

// TODO: Allow port forwarding
// TODO: Exception handling
// TODO: /test2 is also working in case of /test

public class FakeJsonServer{
    private static FakeJsonServer instance = null;
    private static final Map<String, MyHttpServer> servers = new HashMap<>();
    private  static ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    static Logger logger = Logger.getLogger(FakeJsonServer.class.getName());

    private FakeJsonServer() throws IOException {
    }
    HttpServer addServer(String hostname, int port) throws IOException {
        String name = hostname + ":" + port;
        if(servers.containsKey(name))
        {
            logger.info(name + " already exists");
        }
        HttpServer server = HttpServer.create(new InetSocketAddress(hostname, port), 0);
        MyHttpServer myHttpServer = new MyHttpServer();
        server.createContext("/test", new  MyHttpHandler());
        server.setExecutor(threadPoolExecutor);
        server.start();
        myHttpServer.setServer(server);
        servers.put(name, myHttpServer);
        logger.info(" Server started on port " + port);
        return server;
    }

    void closeServer(String hostname, int port){
        String name = hostname + ":" + port;
        if(!servers.containsKey(name))
        {
            logger.info(name + " not found");
            return;
        }
        servers.get(name).stopServer();
        servers.remove(name);
        if(servers.size() == 0) {
            instance = null;
        }
    }

    public static FakeJsonServer getInstance() throws IOException {
        if(instance == null)
        {
            instance = new FakeJsonServer();
        }
        return instance;
    }
}

class MyHttpServer{
    private HttpServer server;

    void setServer(HttpServer server) {
        this.server = server;
    }

    void stopServer(){
        this.server = null;
    }
}

class MyHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue=null;
        if("GET".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest(httpExchange);
        }
        handleResponse(httpExchange,requestParamValue);
    }
    private String handleGetRequest(HttpExchange httpExchange) {
        return httpExchange.
        getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("=")[1];
    }
    private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {
        OutputStream outputStream = httpExchange.getResponseBody();

        // encode HTML content
        String htmlResponse = "{\"Hello\": \"World\"}";

        // this line is a must
        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}