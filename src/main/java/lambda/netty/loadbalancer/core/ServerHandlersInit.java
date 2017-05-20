package lambda.netty.loadbalancer.core;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import lambda.netty.loadbalancer.core.SysService.SysServiceHostResolveHandler;
import lambda.netty.loadbalancer.core.proxy.ProxyFrontendHandler;

/**
 * Created by maanadev on 5/18/17.
 */
public class ServerHandlersInit extends ChannelInitializer<SocketChannel> {
    private final String remoteHost;
    private final int remotePort;

    public ServerHandlersInit(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    protected void initChannel(SocketChannel socketChannel) throws Exception {


        ChannelPipeline channelPipeline = socketChannel.pipeline();
        channelPipeline.addLast(new HttpRequestDecoder(), new HttpObjectAggregator(1048576), new SysServiceHostResolveHandler(), new ProxyFrontendHandler(remoteHost, remotePort));
    }
}
