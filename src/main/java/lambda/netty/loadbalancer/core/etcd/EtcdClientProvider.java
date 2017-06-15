package lambda.netty.loadbalancer.core.etcd;


import com.coreos.jetcd.Client;
import com.coreos.jetcd.ClientBuilder;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.api.MemberListResponse;
import com.coreos.jetcd.api.RangeResponse;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.exception.AuthFailedException;
import com.coreos.jetcd.exception.ConnectException;
import org.apache.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

/**
 * Created by maanadev on 6/15/17.
 */
public class EtcdClientProvider {
    final static Logger logger = Logger.getLogger(EtcdClientProvider.class);

   private static Client client = null;

    public KV newEtcdKVClient(){
        if(client==null){
            try {
                client = ClientBuilder.newBuilder().endpoints("http://localhost:2379").build();
            } catch (ConnectException e) {
                e.printStackTrace();
            } catch (AuthFailedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws AuthFailedException, ConnectException, ExecutionException, InterruptedException {




        KV kvClient = client.getKVClient();
        ByteSequence key = ByteSequence.fromString("test_key");
        ByteSequence value = ByteSequence.fromString("test_value");
        kvClient.put(key,value);
//// get the CompletableFuture
        CompletableFuture<RangeResponse> getFuture = kvClient.get(key);

//// get the value from CompletableFuture
//                getFuture.get().getKvsList();
            RangeResponse response = getFuture.get();
        System.out.println(response);
    }

}
