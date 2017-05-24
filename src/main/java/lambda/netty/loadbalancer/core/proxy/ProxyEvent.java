package lambda.netty.loadbalancer.core.proxy;

/**
 * Created by maanadev on 5/19/17.
 */
public class ProxyEvent {



    private String domain;
    private int port;

    public ProxyEvent(String host) {
        String[] domainConfig = host.split(":");
        setDomain(domainConfig[0]);
        setPort(Integer.parseInt(domainConfig[1]));
    }


    public String getDomain() {
        return domain;
    }

    private void setDomain(String domain) {
        this.domain = domain;
    }

    public int getPort() {
        return port;
    }

    private void setPort(int port) {
        this.port = port;
    }

}
