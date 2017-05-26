package lambda.netty.loadbalancer.core;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import lambda.netty.loadbalancer.core.SysService.SysServiceHostResolveHandler;
import lambda.netty.loadbalancer.core.proxy.ProxyFrontendHandler;

/**
 * Created by maanadev on 5/18/17.
 */
public class ServerHandlersInit extends ChannelInitializer<SocketChannel> {

    private EventLoopGroup remoteHostEventLoopGroup;

    public ServerHandlersInit(EventLoopGroup remoteHostEventLoopGroup) {

        this.remoteHostEventLoopGroup = remoteHostEventLoopGroup;
    }

    protected void initChannel(SocketChannel socketChannel) throws Exception {


        ChannelPipeline channelPipeline = socketChannel.pipeline();

        channelPipeline.addLast(new HttpRequestDecoder(),
                new HttpObjectAggregator(1048576),
                new SysServiceHostResolveHandler(remoteHostEventLoopGroup),
                new ProxyFrontendHandler());

    }

}
