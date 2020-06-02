package research;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


// TODO: Implement request parameters
class MyHttpHandler implements HttpHandler {
    private final Response response;

    public MyHttpHandler(Response value) {
        this.response = value;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String,String> requestParamValue = new HashMap<>();
        String finalResponse = "";

        if("GET".equalsIgnoreCase(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest(httpExchange);
            finalResponse = response.getGetResponse();
        }
        else if("POST".equalsIgnoreCase(httpExchange.getRequestMethod())){
            requestParamValue = handlePostRequest(httpExchange);
            finalResponse = response.getPostResponse();
        }
//        else if("PUT".equals(httpExchange.getRequestMethod())){
//            requestParamValue = "Put";
//        }
//        else if("DELETE".equals(httpExchange.getRequestMethod())){
//            requestParamValue = "Delete";
//        }
        handleResponse(httpExchange,requestParamValue, finalResponse);
    }
    private Map<String, String> handleGetRequest(HttpExchange httpExchange) {
        Map<String,String> requestParameters = new HashMap<>();
        for(NameValuePair value: URLEncodedUtils.parse(httpExchange.getRequestURI(),StandardCharsets.UTF_8)){
            requestParameters.put(value.getName(),value.getValue());
        }
        return requestParameters;
    }

    private Map<String, String> handlePostRequest(HttpExchange httpExchange) throws IOException {
        Map<String,String> requestParameters = new HashMap<>();

        InputStream stream = httpExchange.getRequestBody();

        StringBuilder myString = new StringBuilder();
        int num;

        while( (num = stream.read()) != -1){
            myString.append((char) num);
        }

        // TODO: Problem with content-type. Returning urlencoded even with formdata while using postman
        String contentType = httpExchange.getRequestHeaders().get("Content-Type").toString();
        if(contentType.equalsIgnoreCase("[application/x-www-form-urlencoded]")){
            return urlEncodedPost(myString.toString());
        }
        else if(contentType.equalsIgnoreCase("[multipart/form-data]")){
            return formDataPost(myString.toString());
        }

        return requestParameters;
    }

    // TODO: Sanitation remaining
    private Map<String, String> formDataPost(String myString) {
        Map<String, String> requestParameters = new HashMap<>();
        String[] parameters = myString.split("\\r?\\n");
        String key = null;
        for(int i = 0; i < parameters.length; i++){

            if(i%4 == 1){
                key = parameters[i].split(";")[1].split("=")[1];
            }
            else if(i%4 == 3){
                requestParameters.put(key, parameters[i]);
            }
        }
        return requestParameters;
    }

    // TODO: Sanitation remaining
    private Map<String, String> urlEncodedPost(String myString) {
        Map<String, String> requestParameters = new HashMap<>();

        String[] parameters = myString.split("&");
        for(String parameter : parameters){
            requestParameters.put(parameter.split("=")[0], parameter.split("=")[1]);
        }
        return requestParameters;
    }

    private void handleResponse(HttpExchange httpExchange, Map<String, String> requestParamValue, String finalResponse)  throws  IOException {
        OutputStream outputStream = httpExchange.getResponseBody();

        // encode HTML content

        // this line is a must
        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, finalResponse.length());
        outputStream.write(finalResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}