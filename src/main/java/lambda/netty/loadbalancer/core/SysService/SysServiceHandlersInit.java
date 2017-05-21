package lambda.netty.loadbalancer.core.SysService;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;

/**
 * Created by maanadev on 5/19/17.
 */
public class SysServiceHandlersInit extends ChannelInitializer<SocketChannel> {


    public SysServiceHandlersInit() {

    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        socketChannel.pipeline().addLast(new HttpClientCodec(), new HttpContentDecompressor(), new SysServiceResponseHandler());

    }
}
