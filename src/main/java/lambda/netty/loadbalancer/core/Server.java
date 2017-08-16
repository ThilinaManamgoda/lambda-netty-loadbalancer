package lambda.netty.loadbalancer.core;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lambda.netty.loadbalancer.core.sslconfigs.SSLHandlerProvider;

public class Server {
    static final int LOCAL_PORT = Integer.parseInt(System.getProperty("localPort", "8080"));

    public static void main(String[] args) {
        // Configure the bootstrap.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        EventLoopGroup remoteHostEventLoopGroup = new NioEventLoopGroup();

        //Load SSL certs
        SSLHandlerProvider.initSSLContext();
        try {

            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerHandlersInit(remoteHostEventLoopGroup))
                    .childOption(ChannelOption.AUTO_READ, false)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .bind(LOCAL_PORT).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            remoteHostEventLoopGroup.shutdownGracefully();
        }

    }

}
