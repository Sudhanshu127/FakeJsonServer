package research;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpHandlerForwarding implements HttpHandler {
    private final URI forwardUri;

    // TODO: Handle complete service forwarding
    // TODO: Store URI in Redis

    // Saving sanitized uri in forward uri.
    public HttpHandlerForwarding(String uri) throws URISyntaxException, MalformedURLException {
        URL myUrl = new URL(uri);
        this.forwardUri = myUrl.toURI();
        System.out.println(forwardUri);
    }

    // TODO: Handle HTTPRequest Methods, etc
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpClient client = HttpClient.newHttpClient();
        String newUrl = forwardUri.getPath() + "/" + httpExchange.getRequestURI();

        HttpRequest request = HttpRequest.newBuilder().uri(forwardUri.resolve(newUrl)).build();

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
