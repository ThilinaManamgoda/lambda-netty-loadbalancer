package lambda.netty.loadbalancer.core.redis_cache;

import org.apache.commons.pool.impl.GenericObjectPool;
import redis.clients.jedis.JedisPool;
import redis.clients.johm.JOhm;

/**
 * Created by maanadev on 5/21/17.
 */
public class RedisDAO {
    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool(new GenericObjectPool.Config(),"localhost");
        JOhm.setPool(jedisPool);

        User user = new User();
        user.setName(22);
        user.setAge(33);
        JOhm.save(user);
        System.out.println(JOhm.get(User.class,22));
        user.setAge(4444);
        System.out.println(JOhm.get(User.class,22));
    }
}
