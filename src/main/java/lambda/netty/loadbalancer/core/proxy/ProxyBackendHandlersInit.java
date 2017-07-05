package lambda.netty.loadbalancer.core.proxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;


public class ProxyBackendHandlersInit extends ChannelInitializer<SocketChannel> {
    Channel channel;

    public ProxyBackendHandlersInit(Channel channel) {
        this.channel = channel;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new HttpRequestEncoder(), new ProxyBackendHandler(channel));
    }
}
