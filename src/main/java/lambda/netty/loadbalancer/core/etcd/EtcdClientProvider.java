package lambda.netty.loadbalancer.core.etcd;


import com.coreos.jetcd.Client;
import com.coreos.jetcd.ClientBuilder;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.api.MemberListResponse;
import com.coreos.jetcd.api.RangeResponse;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.exception.AuthFailedException;
import com.coreos.jetcd.exception.ConnectException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

/**
 * Created by maanadev on 6/15/17.
 */
public class EtcdClientProvider {

    public static void main(String[] args) throws AuthFailedException, ConnectException, ExecutionException, InterruptedException {
           Client client = null;

            client = ClientBuilder.newBuilder().endpoints("http://localhost:2379").build();

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
