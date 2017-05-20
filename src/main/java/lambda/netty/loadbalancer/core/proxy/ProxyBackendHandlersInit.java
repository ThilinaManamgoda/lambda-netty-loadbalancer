package lambda.netty.loadbalancer.core.proxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;

/**
 * Created by maanadev on 5/20/17.
 */
public class TestInit extends ChannelInitializer<SocketChannel>{
    Channel channel;
    public TestInit(Channel channel) {
        this.channel=channel;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new HttpRequestEncoder(),new ProxyBackendHandler(channel));
    }
}
