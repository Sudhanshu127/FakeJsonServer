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
// TODO: (H) Add method handling for MyHttpServer
// TODO: No of threads is public

public class FakeJsonServer{
    private static FakeJsonServer instance = null;
    private static final Map<String, MyHttpServer> servers = new HashMap<>();
    private  static final ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    static Logger logger = Logger.getLogger(FakeJsonServer.class.getName());

    public MyHttpServer addServer(String hostname, int port) throws IOException {
        String name = hostname + ":" + port;
        if(servers.containsKey(name))
        {
            // TODO: Exit Statement
            logger.info(name + " already exists");
        }
        HttpServer server = HttpServer.create(new InetSocketAddress(hostname, port), 0);
        server.setExecutor(threadPoolExecutor);

        MyHttpServer myHttpServer = new MyHttpServer();
        myHttpServer.setServer(server);
        servers.put(name, myHttpServer);
        logger.info(" Server started on port " + port);
        return myHttpServer;
    }

    public void closeServer(String hostname, int port){
        String name = hostname + ":" + port;
        if(!servers.containsKey(name))
        {
            logger.info(name + " not found");
            return;
        }

        logger.info("Removing port " + port);
        servers.get(name).stopServer();
        servers.remove(name);
        if(servers.size() == 0) {
            instance = null;
        }
    }

    public static FakeJsonServer getInstance() {
        if(instance == null)
        {
            instance = new FakeJsonServer();
        }
        return instance;
    }
}

class MyHttpHandler implements HttpHandler {
    private String getResponse = "";
    public MyHttpHandler(String value) {
        this.getResponse = value;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue=null;
//        if("GET".equals(httpExchange.getRequestMethod())) {
//            requestParamValue = handleGetRequest(httpExchange);
//        }
        handleResponse(httpExchange,"");
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
        String htmlResponse = getResponse;

        // this line is a must
        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}