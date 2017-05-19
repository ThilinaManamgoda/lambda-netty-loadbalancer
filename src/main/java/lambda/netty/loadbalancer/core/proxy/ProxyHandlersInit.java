package lambda.netty.loadbalancer.core.proxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;

/**
 * Created by maanadev on 5/19/17.
 */
public class ProxyHandlersInit extends ChannelInitializer<SocketChannel> {
    private Channel inboundChannel;
    ProxyHandlersInit(Channel inboundChannel){
        this.inboundChannel = inboundChannel;
    }
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new HttpRequestEncoder(),new ProxyBackendHandler(inboundChannel));
    }
}
