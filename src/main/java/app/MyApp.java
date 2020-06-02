package app;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import research.FakeJsonServer;
import research.Response;

import java.net.URISyntaxException;

// TODO: View available json schema and requests
public class MyApp {
    public static void main(String[] args) throws Exception {
        FakeJsonServer fakeJsonServer = FakeJsonServer.getInstance(10);

        // App expects env variables (SLACK_BOT_TOKEN, SLACK_SIGNING_SECRET)
        App app = new App();

        app.command("/hello", (req, ctx) -> {
            return ctx.ack(":wave: Hello!");
        });

        app.command("/newServer".toLowerCase(), (req, ctx) -> {
            fakeJsonServer.getServer("localhost",8001);
            return ctx.ack("New Server Started");
        });

        app.command("/closeServer".toLowerCase(), (req, ctx) -> {
            fakeJsonServer.closeServer("localhost",8001);
            return ctx.ack("Server ShutDown");
        });


        app.command("/getAvailableUrls".toLowerCase(), (req, ctx) -> {
            return ctx.ack(fakeJsonServer.getServer("localhost",8001).urlAvailable());
        });

        app.command("/addResponse".toLowerCase(), (req, ctx) -> {
            fakeJsonServer.getServer("localhost", 8001).newResponse("/test", new Response().setGetResponse("{ \"test\" : \"cleared\" }"));
            return ctx.ack("Added a new Response");
        });

        app.command("/forwardResponse".toLowerCase(), (req, ctx) -> {
            try {
                fakeJsonServer.getServer("localhost", 8001).forward("/test", "http://localhost:8002");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return ctx.ack("Added a new redirectionUrl");
        });

        app.command("/removeResponse".toLowerCase(), (req, ctx) -> {
           fakeJsonServer.getServer("localhost", 8001).remove("/test");
           return ctx.ack("Removed a response");
        });

        SlackAppServer server = new SlackAppServer(app);
        server.start(); // http://localhost:3000/slack/events
    }
}
