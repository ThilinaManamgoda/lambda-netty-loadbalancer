package lambda.netty.loadbalancer.core.redis_cache;

import redis.clients.johm.Attribute;
import redis.clients.johm.Id;
import redis.clients.johm.Model;

/**
 * Created by maanadev on 5/21/17.
 */
@Model
public class User {
    @Id
    private int name;
    @Attribute
    private int age;

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return name+" "+age;
    }
}
