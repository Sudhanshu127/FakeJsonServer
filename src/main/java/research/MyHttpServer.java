package research;

import com.sun.net.httpserver.HttpServer;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

// TODO: Redis support for forward
public class MyHttpServer{
    private HttpServer server;
    private String name;
    private final Map<String, Response> responses = new HashMap<>();
    static Logger logger = Logger.getLogger(FakeJsonServer.class.getName());

    void setServer(HttpServer server) {
        this.server = server;
        this.server.start();
    }

    void stopServer(){
        server.stop(100);
        Redis.removeServer(name);
        this.server = null;
    }

    public MyHttpServer newResponse(String url, Response response){
        if(!url.endsWith("/")){
            url = url + "/";
        }
        if(responses.containsKey(url))
        {
            System.out.println("Conflicting url:- " + url + " -: Try using updateResponse");
        }
        this.responses.put(url, response);
        logger.info("Creating context for " + url);
        this.server.createContext(url, new MyHttpHandler(response));
        Redis.addResponse(name, url, response);
        return this;
    }

    public MyHttpServer forward(String url, String forwardUri) throws MalformedURLException, URISyntaxException {
        if(!url.endsWith("/")){
            url = url + "/";
        }
        if(responses.containsKey(url))
        {
            System.out.println("Conflicting url:- " + url + " -: Try using updateForward");
        }
        Response forward = new Response().setRedirectionUrl(forwardUri);
        this.responses.put(url, forward);
        server.createContext(url, new HttpHandlerForwarding(forward));
        Redis.addResponse(name, url, forward);
        return this;
    }

    public MyHttpServer updateResponse(String url, Response response){
        if(!url.endsWith("/")){
            url = url + "/";
        }
        if(!responses.containsKey(url))
        {
            System.out.println("Can't find url:- " + url + " -: Try using newResponse");
        }
        this.responses.replace(url, response);
        logger.info("Updating context for " + url);
        this.server.removeContext(url);
        this.server.createContext(url, new MyHttpHandler(response));
        Redis.updateResponse(name, url, response);
        return this;
    }

    public MyHttpServer remove(String url){
        if(!url.endsWith("/")){
            url = url + "/";
        }
        if(responses.containsKey(url))
        {
            System.out.println("Can't find url:- " + url);
        }
        this.responses.remove(url);
        logger.info("Creating context for " + url);
        this.server.removeContext(url);
        Redis.removeResponse(name, url);
        return this;
    }

    public MyHttpServer updateForward(String url, String forwardUri) throws MalformedURLException, URISyntaxException {
        if(!url.endsWith("/")){
            url = url + "/";
        }
        if(!responses.containsKey(url))
        {
            System.out.println("Can't find url:- " + url);
        }
        Response forward = new Response().setRedirectionUrl(forwardUri);
        this.responses.replace(url, forward);
        logger.info("Updating context for " + url);
        this.server.removeContext(url);
        server.createContext(url, new HttpHandlerForwarding(forward));
        Redis.addResponse(name, url, forward);
        return this;
    }

    public Response getResponse(String url){
        if(!url.endsWith("/")){
            url = url + "/";
        }
        return new Response(this.responses.get(url));
    }

    public String urlAvailable(){
        StringBuilder urls = new StringBuilder();
        for(Map.Entry<String, Response> response : responses.entrySet()){
            urls.append(response.getKey()).append(" : ").append(response.getValue()).append("\n");
        }
        return String.valueOf(urls);
    }

    void setServerName(String name) {
        this.name = name;
        Redis.addServer(name);
    }
}
