package lambda.netty.loadbalancer.core.cache;

import net.spy.memcached.MemcachedClient;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;


public class MemcachedDAO {
    final static Logger logger = Logger.getLogger(MemcachedDAO.class);

    private static String MEMCACHED_HOST = "localhost";
    private static int MEMCACHED_PORT = 11211;
    private static int MEMCACHED_TIMEOUT = 3600;
    static private MemcachedClient memcachedClient = null;

    private MemcachedDAO() {

    }

    public static MemcachedDAO getInstance() {

        if (memcachedClient == null) {
            try {
                memcachedClient = new MemcachedClient(new InetSocketAddress(MEMCACHED_HOST, MEMCACHED_PORT));
            } catch (IOException e) {
                logger.error("Cannot connect !", e);
            }
        }
        return new MemcachedDAO();
    }

    public void shutDown() {
        memcachedClient.shutdown();
    }

    public void save(String key, Object object) {
        memcachedClient.add(key, MEMCACHED_TIMEOUT, object);
    }

    public Object get(String key) {
        return memcachedClient.get(key);
    }

}
