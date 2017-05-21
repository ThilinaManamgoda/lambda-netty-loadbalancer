package lambda.netty.loadbalancer.core.proxy;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

import io.netty.channel.nio.NioEventLoopGroup;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ProxyFrontendHandler extends ChannelInboundHandlerAdapter {

    private final String remoteHost;
    private final int remotePort;
    Queue<Object> queue = new ConcurrentLinkedQueue<Object>();
    Bootstrap b;
    boolean connected = false;

    // As we use inboundChannel.eventLoop() when building the Bootstrap this does not need to be volatile as
    // the outboundChannel will use the same EventLoop (and therefore Thread) as the inboundChannel.
    private Channel outboundChannel;

    public ProxyFrontendHandler(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }



    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final Channel channel = ctx.channel();
        b = new Bootstrap();
        b.group(new NioEventLoopGroup())
                .channel(ctx.channel().getClass())
                .handler(new ProxyBackendHandlersInit(channel));
        ctx.channel().read();
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
        final Channel channel = ctx.channel();

        if (!connected) {
            final Object m = msg;
            connected = true;
            ChannelFuture f = b.connect(remoteHost, remotePort);

            f.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {

                    if (channelFuture.isSuccess()) {
                        outboundChannel = channelFuture.channel();
                        outboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                                if (channelFuture.isSuccess()) {
                                    channel.read();
                                }
                            }
                        });

                    }
                }
            });


        }
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


    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    static void closeOnFlush(Channel ch) {
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }
}