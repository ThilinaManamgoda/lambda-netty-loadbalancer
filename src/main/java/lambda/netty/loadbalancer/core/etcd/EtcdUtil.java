package lambda.netty.loadbalancer.core.etcd;

import com.coreos.jetcd.ClientBuilder;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.api.PutResponse;
import com.coreos.jetcd.api.RangeResponse;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.exception.AuthFailedException;
import com.coreos.jetcd.exception.ConnectException;
import com.coreos.jetcd.options.GetOption;
import org.apache.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class EtcdUtil {
    final static Logger logger = Logger.getLogger(EtcdUtil.class);
    public static String ETCD_CLUSTER = "http://localhost:2379";
    private static KV kvClient = null;
    private static GetOption getOption = GetOption.newBuilder().withSerializable(true).build();

    static {

        if (kvClient == null) {
            try {
                kvClient = ClientBuilder.newBuilder().endpoints(ETCD_CLUSTER).build().getKVClient();
            } catch (ConnectException e) {
                logger.error("Cannot connect to the Etcd cluster", e);
            } catch (AuthFailedException e) {
                logger.error("Authentication is failed with Etcd cluster");
            }
        }
    }

    private EtcdUtil() {
    }

    public static CompletableFuture<PutResponse> putValue(String s_key, String s_value) throws EtcdClientException {
        ByteSequence key = ByteSequence.fromString(s_key);
        ByteSequence value = ByteSequence.fromString(s_value);
        CompletableFuture<PutResponse> responseCompletableFuture;
        if (kvClient == null) {
            throw new EtcdClientException();
        } else {
            responseCompletableFuture = kvClient.put(key, value);
        }
        return responseCompletableFuture;
    }

    public static CompletableFuture<RangeResponse> getValue(String s_key) throws EtcdClientException {
        ByteSequence key = ByteSequence.fromString(s_key);
        CompletableFuture<RangeResponse> rangeResponse = null;
        if (kvClient == null) {
            throw new EtcdClientException();
        } else {
            rangeResponse = kvClient.get(key, getOption);
        }
        return rangeResponse;
    }

//    public static void main(String[] args) {
//        try {
//            EtcdUtil.putValue("localhost","state=DOWN;addr=localhost:8082");
//
//            EtcdUtil.getValue("localhost").thenAccept(x-> System.out.println(x.getKvs(0).getValue()));
//
//
//            Thread.sleep(20000);
//        } catch (EtcdClientException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
