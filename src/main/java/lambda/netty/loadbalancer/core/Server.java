package lambda.netty.loadbalancer.core;


import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lambda.netty.loadbalancer.core.SysService.SysServiceHandlersInit;

public class Server {
    static final int LOCAL_PORT = Integer.parseInt(System.getProperty("localPort", "8080"));
    static final String REMOTE_HOST = System.getProperty("remoteHost", "127.0.0.1");
    static final int REMOTE_PORT = Integer.parseInt(System.getProperty("remotePort", "8082"));

    public static void main(String[] args) {
        // Configure the bootstrap.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        EventLoopGroup proxyEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup remoteHostEventLoopGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap b= new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerHandlersInit(REMOTE_HOST, REMOTE_PORT, remoteHostEventLoopGroup, proxyEventLoopGroup))
                    .childOption(ChannelOption.AUTO_READ, false)
                    .bind(LOCAL_PORT).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            proxyEventLoopGroup.shutdownGracefully();
        }

    }

}
