package research;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpHandlerForwarding implements HttpHandler {
    private final String forwardUri;

    // TODO: uri must not end with '/'
    // TODO: Add http:// in beginning
    public HttpHandlerForwarding(String uri){
        this.forwardUri = uri;
    }

    // TODO: Handle HTTPRequest Methods, etc
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Hello World");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(forwardUri + httpExchange.getRequestURI())).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            OutputStream outputStream = httpExchange.getResponseBody();
            httpExchange.getResponseHeaders().set("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(response.statusCode(), response.body().length());
            outputStream.write(response.body().getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
