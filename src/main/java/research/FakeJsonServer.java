package research;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

public class FakeJsonServer{
    private static FakeJsonServer instance = null;
    private static HttpServer server = null;
    private static HttpServer server2 = null;
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    static Logger logger = Logger.getLogger(FakeJsonServer.class.getName());

    private FakeJsonServer() throws IOException {
            server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
            server.createContext("/test", new  MyHttpHandler());
            server.setExecutor(threadPoolExecutor);
            server.start();
            logger.info(" Server started on port 8001");
        server = HttpServer.create(new InetSocketAddress("localhost", 8002), 0);
        server.createContext("/test", new  MyHttpHandler());
        server.setExecutor(threadPoolExecutor);
        server.start();
        logger.info(" Server started on port 8002");
    }

    private void closeServer(){
        instance = null;
        server = null;
    }

    public static FakeJsonServer getInstance() throws IOException {
        if(instance == null)
        {
            instance = new FakeJsonServer();
        }
        return instance;
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