package research;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Map;
import java.util.Set;

public class Redis {
    static RedissonClient redisson;
    private static Redis instance = null;
    private static final String rootname = "Servers";
    private Redis() {

        Config config = new Config();

        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        redisson = Redisson.create(config);
    }

    public static Redis getInstance(){
        if(instance == null){
            instance = new Redis();
        }
        return instance;
    }

    static void addServer(String server) {
        redisson.getSet(rootname).add(server);
    }

    static void removeServer(String server) {
        redisson.getSet(rootname).remove(server);
    }

    static void addResponse(String name, String url, Response response) {
        redisson.getMap(name).put(url, response);
    }

    static void updateResponse(String name, String url, Response response) {
        redisson.getMap(name).replace(url, response);
    }

    static void removeResponse(String name, String url) {
        redisson.getMap(name).remove(url);
    }

    public Set<String> allServers(){
        return redisson.getSet(rootname);
    }

    public Map<String, Response> allResponses(String server){
        return redisson.getMap(server);
    }
}
