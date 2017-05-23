package lambda.netty.loadbalancer.core.proxy;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

import io.netty.channel.nio.NioEventLoopGroup;
import lambda.netty.loadbalancer.core.SysService.RemoteHost;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ProxyFrontendHandler extends ChannelInboundHandlerAdapter {

    private final String remoteHost;
    private final int remotePort;
    Queue<Object> queue = new ConcurrentLinkedQueue<Object>();
    Bootstrap b;
    boolean connected = false;
    Object m ;
    // As we use inboundChannel.eventLoop() when building the Bootstrap this does not need to be volatile as
    // the outboundChannel will use the same EventLoop (and therefore Thread) as the inboundChannel.
    private Channel outboundChannel;
    private EventLoopGroup proxyEventLoop;

    public ProxyFrontendHandler(String remoteHost, int remotePort ,EventLoopGroup proxyEventLoop) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
        this.proxyEventLoop=proxyEventLoop;
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final Channel channel = ctx.channel();
        b = new Bootstrap();
        b.group(proxyEventLoop)
                .channel(ctx.channel().getClass())
                .handler(new ProxyBackendHandlersInit(channel));


    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
        m=msg;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (outboundChannel != null) {
            closeOnFlush(outboundChannel);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        closeOnFlush(ctx.channel());
    }

    public void send(){

    }
    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    static void closeOnFlush(Channel ch) {
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("triggerd");
        final Channel channel = ctx.channel();
        RemoteHost remoteHost = new RemoteHost((String)evt);
        if (!connected) {
            final Object m1 = m;
            connected = true;
            ChannelFuture f = b.connect(remoteHost.getDomain(), remoteHost.getPort());

            f.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {

                    if (channelFuture.isSuccess()) {
                        outboundChannel = channelFuture.channel();
                        outboundChannel.writeAndFlush(m1).addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                                if (channelFuture.isSuccess()) {
//                                    channel.read();
                                }
                            }
                        });

                    }
                }
            });


        }
    }
}