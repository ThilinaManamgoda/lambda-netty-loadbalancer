package lambda.netty.loadbalancer.core.SysService;

/**
 * Created by maanadev on 5/19/17.
 */
public class RemoteHost {


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

    private String domain;
    private int port;


    public RemoteHost(String host) {
       String [] domainConfig = host.split(":");
       setDomain(domainConfig[0]);
       setPort(Integer.parseInt(domainConfig[1]));
    }
}
