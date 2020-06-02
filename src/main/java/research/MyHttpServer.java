package research;

import com.sun.net.httpserver.HttpServer;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MyHttpServer{
    private HttpServer server;
    private final Map<String, Response> responses = new HashMap<>();
    static Logger logger = Logger.getLogger(FakeJsonServer.class.getName());

    void setServer(HttpServer server) {
        this.server = server;
        this.server.start();
    }

    void stopServer(){
        server.stop(100);
        this.server = null;
    }

    public MyHttpServer newResponse(String url, Response response){
        if(responses.containsKey(url))
        {
            System.out.println("Conflicting url:- " + url + " -: Try using updateResponse");
        }
        this.responses.put(url, response);
        logger.info("Creating context for " + url);
        this.server.createContext(url, new MyHttpHandler(response));
        return this;
    }

    public MyHttpServer forward(String url, String forwardUri) throws MalformedURLException, URISyntaxException {
        if(responses.containsKey(url))
        {
            System.out.println("Conflicting url:- " + url + " -: Try using updateForward");
        }

        this.responses.put(url, new Response().setRedirectionUrl(forwardUri));
        server.createContext(url, new HttpHandlerForwarding(forwardUri));
        return this;
    }

    public MyHttpServer updateResponse(String url, Response response){
        if(!responses.containsKey(url))
        {
            System.out.println("Can't find url:- " + url + " -: Try using newResponse");
        }
        this.responses.replace(url, response);
        logger.info("Updating context for " + url);
        this.server.removeContext(url);
        this.server.createContext(url, new MyHttpHandler(response));
        return this;
    }

    public MyHttpServer remove(String url){
        if(responses.containsKey(url))
        {
            System.out.println("Can't find url:- " + url);
        }
        this.responses.remove(url);
        logger.info("Creating context for " + url);
        this.server.removeContext(url);
        return this;
    }

    public MyHttpServer updateForward(String url, String forwardUri) throws MalformedURLException, URISyntaxException {
        if(!responses.containsKey(url))
        {
            System.out.println("Can't find url:- " + url);
        }
        this.responses.replace(url, new Response().setRedirectionUrl(forwardUri));
        logger.info("Updating context for " + url);
        this.server.removeContext(url);
        server.createContext(url, new HttpHandlerForwarding(forwardUri));
        return this;
    }

    public Response getResponse(String url){
        return new Response(this.responses.get(url));
    }

    public String urlAvailable(){
        StringBuilder urls = new StringBuilder();
        for(Map.Entry<String, Response> response : responses.entrySet()){
            urls.append(response.getKey()).append(" : ").append(response.getValue()).append("\n");
        }
        return String.valueOf(urls);
    }
}
