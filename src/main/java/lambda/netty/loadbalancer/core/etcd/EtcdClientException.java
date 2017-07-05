package lambda.netty.loadbalancer.core.etcd;


public class EtcdClientException extends Exception {
    EtcdClientException() {
        super("Etcd client is not initialized");
    }
}
