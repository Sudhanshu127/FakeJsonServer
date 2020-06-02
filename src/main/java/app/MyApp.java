package app;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import research.FakeJsonServer;
import research.MyHttpServer;
import research.Response;

import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class MyApp {
    public static void main(String[] args) throws Exception {
        FakeJsonServer fakeJsonServer = FakeJsonServer.getInstance(10);

        // App expects env variables (SLACK_BOT_TOKEN, SLACK_SIGNING_SECRET)
        App app = new App();

        app.command("/hello", (req, ctx) -> {
            return ctx.ack(":wave: Hello!");
        });

        app.command("/newServer".toLowerCase(), (req, ctx) -> {
            String[] parameters = requestParameter(req);
            if(parameters.length != 2){
                return ctx.ack("Invalid use. Used as \"<hostname> <port>\"");
            }
            fakeJsonServer.getServer(parameters[0],Integer.parseInt(parameters[1]));
            return ctx.ack("New Server Started");
        });

        app.command("/closeServer".toLowerCase(), (req, ctx) -> {
            String[] parameters = requestParameter(req);
            if(parameters.length != 2){
                return ctx.ack("Invalid use. Used as \"<hostname> <port>\"");
            }
            fakeJsonServer.closeServer(parameters[0],Integer.parseInt(parameters[1]));
            return ctx.ack("Server ShutDown");
        });


        app.command("/getAvailableUrls".toLowerCase(), (req, ctx) -> {
            String[] parameters = requestParameter(req);
            if(parameters.length != 2){
                return ctx.ack("Invalid use. Used as \"<hostname> <port>\"");
            }
            return ctx.ack(fakeJsonServer.getServer(parameters[0],Integer.parseInt(parameters[1])).urlAvailable());
        });

        app.command("/addResponse".toLowerCase(), (req, ctx) -> {
            String[] parameters = requestParameter(req,5);
            if(parameters.length != 5){
                return ctx.ack("Invalid use. Used as \"<hostname> <port> <url> <request_type> <json_response>\"");
            }

            MyHttpServer myHttpServer = fakeJsonServer.getServer(parameters[0], Integer.parseInt(parameters[1]));
            if("GET".equalsIgnoreCase(parameters[3])){
                    myHttpServer.newResponse(parameters[2], new Response().setGetResponse(parameters[4]));
            }
            else if("POST".equalsIgnoreCase(parameters[3])){
                myHttpServer.newResponse(parameters[2], new Response().setPostResponse(parameters[4]));

            }
            return ctx.ack("Added a new Response");
        });

        app.command("/forwardResponse".toLowerCase(), (req, ctx) -> {
            String[] parameters = requestParameter(req);
            if(parameters.length != 4){
                return ctx.ack("Invalid use. Used as \"<hostname> <port> <url> <forward_url>\"");
            }

            try {
                fakeJsonServer.getServer(parameters[0], Integer.parseInt(parameters[1])).forward(parameters[2], parameters[3]);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return ctx.ack("Added a new redirectionUrl");
        });

        app.command("/removeResponse".toLowerCase(), (req, ctx) -> {
            String[] parameters = requestParameter(req);
            if(parameters.length != 3){
                return ctx.ack("Invalid use. Used as \"<hostname> <port> <url>\"");
            }

           fakeJsonServer.getServer(parameters[0], Integer.parseInt(parameters[1])).remove(parameters[2]);
           return ctx.ack("Removed a response");
        });

        SlackAppServer server = new SlackAppServer(app);
        server.start(); // http://localhost:3000/slack/events
    }

    private static String[] requestParameter(SlashCommandRequest req){
        for (String value : URLDecoder.decode(req.getRequestBodyAsString(), StandardCharsets.UTF_8).split("&")){
            if(value.startsWith("text")){
                if(value.split("=").length > 1){
                    return value.split("=")[1].split(" ");
                }
                else
                {
                    return new String[0];
                }
            }
        }
        return new String[0];
    }

    private static String[] requestParameter(SlashCommandRequest req, int cuts){
        for (String value : URLDecoder.decode(req.getRequestBodyAsString(), StandardCharsets.UTF_8).split("&")){
            if(value.startsWith("text")){
                return value.split("=")[1].split(" ",cuts);
            }
        }
        return new String[0];
    }
}
