package research;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

// TODO: Implement request parameters
// TODO: Resend Json parameters instead of String
class MyHttpHandler implements HttpHandler {
    private final String getResponse;
    public MyHttpHandler(String value) {
        this.getResponse = value;
    }

//    private String httpExchangeToString(HttpExchange httpExchange){
//        return "Request Header: " + httpExchange.getRequestHeaders() + "\n" +
//                "Request Method" + httpExchange.getRequestMethod() + "\n" +
//                "Request Body" + httpExchange.getRequestBody()  + "\n" +
//                "Request Uri" + httpExchange.getRequestURI();
//    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue=null;
        if("GET".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest(httpExchange);
        }
        else if("POST".equals(httpExchange.getRequestMethod())){
            requestParamValue = "Post";
        }
        else if("PUT".equals(httpExchange.getRequestMethod())){
            requestParamValue = "Put";
        }
        else if("DELETE".equals(httpExchange.getRequestMethod())){
            requestParamValue = "Delete";
        }
        handleResponse(httpExchange,requestParamValue);
    }
    private String handleGetRequest(HttpExchange httpExchange) {
        return httpExchange.
                getRequestURI()
                .toString();
//                .split("\\?")[1]
//                .split("=")[1];
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