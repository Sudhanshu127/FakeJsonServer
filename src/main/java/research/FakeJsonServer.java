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
// TODO: (H) Add method handling for MyHttpServer
// TODO: No of threads is public

public class FakeJsonServer{
    // Created a singleton because on one machine only one instance or controller should live
    private static FakeJsonServer instance = null;

    // Created MyHttpServer to prevent malicious start/stop of HttpServer
    private static final Map<String, MyHttpServer> servers = new HashMap<>();

    private  static final ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    static Logger logger = Logger.getLogger(FakeJsonServer.class.getName());

    public MyHttpServer addServer(String hostname, int port) throws IOException {
        String name = hostname + ":" + port;
        if(servers.containsKey(name))
        {
            // TODO: Exit Statement
            logger.info(name + " already exists");
        }
        HttpServer server = HttpServer.create(new InetSocketAddress(hostname, port), 0);
        server.setExecutor(threadPoolExecutor);

        MyHttpServer myHttpServer = new MyHttpServer();
        myHttpServer.setServer(server);
        servers.put(name, myHttpServer);
        logger.info(" Server started on port " + port);
        return myHttpServer;
    }

    public void closeServer(String hostname, int port){
        String name = hostname + ":" + port;
        if(!servers.containsKey(name))
        {
            logger.info(name + " not found");
            return;
        }

        logger.info("Removing port " + port);
        servers.get(name).stopServer();
        servers.remove(name);
        if(servers.size() == 0) {
            instance = null;
        }
    }

    public static FakeJsonServer getInstance() {
        if(instance == null)
        {
            instance = new FakeJsonServer();
        }
        return instance;
    }
}
