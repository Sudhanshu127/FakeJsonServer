package research;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

// TODO: Exception handling
// TODO: /test2 is also working in case of /test
// TODO: Add Functionality to SlackBot
// TODO: JUnit Testing
// TODO: Docker Support
// TODO: Persistence json data
// TODO: Redis cache
// TODO: Tomcat Exposure

public class FakeJsonServer{
    // Created a singleton because on one machine only one instance or controller should live
    private static FakeJsonServer instance = null;

    // Created MyHttpServer to prevent malicious start/stop of HttpServer
    private static final Map<String, MyHttpServer> servers = new HashMap<>();

    // A global thread pool so that if one server isn't in use then other servers can have more CPU.
    private  static ThreadPoolExecutor threadPoolExecutor;
    static Logger logger = Logger.getLogger(FakeJsonServer.class.getName());

    private FakeJsonServer(int nThreads){
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads);
    }

    // Create a new server or retrieve an already existing one
    public MyHttpServer getServer(String hostname, int port) throws IOException {
        String name = hostname + ":" + port;
        if(servers.containsKey(name))
        {
            logger.info(name + " already exists");
            return servers.get(name);
        }
        HttpServer server = HttpServer.create(new InetSocketAddress(hostname, port), 0);
        server.setExecutor(threadPoolExecutor);

        MyHttpServer myHttpServer = new MyHttpServer();
        myHttpServer.setServer(server);
        servers.put(name, myHttpServer);
        logger.info(" Server started on port " + port);
        return myHttpServer;
    }

    // Close server once the work is done
    public void closeServer(String hostname, int port){
        String name = hostname + ":" + port;
        if(!servers.containsKey(name))
        {
            logger.info(name + " not found");
            return;
        }

        logger.info("Removing " + name);
        servers.get(name).stopServer();
        servers.remove(name);
        if(servers.size() == 0) {
            logger.info("Removing json server");
            instance = null;
        }
    }

    public static FakeJsonServer getInstance(int nThreads) {
        if(instance == null)
        {
            logger.info("Creating new json server");
            instance = new FakeJsonServer(nThreads);
        }
        return instance;
    }
}
