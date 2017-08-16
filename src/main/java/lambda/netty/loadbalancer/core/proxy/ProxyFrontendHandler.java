package lambda.netty.loadbalancer.core.proxy;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import org.apache.log4j.Logger;

public class ProxyFrontendHandler extends ChannelInboundHandlerAdapter {
    final static Logger logger = Logger.getLogger(ProxyFrontendHandler.class);

    Bootstrap b;
    Object requestToProxyServer;
    // As we use inboundChannel.eventLoop() when building the Bootstrap this does not need to be volatile as
    // the outboundChannel will use the same EventLoop (and therefore Thread) as the inboundChannel.
    private Channel outboundChannel;

    public ProxyFrontendHandler() {
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
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final Channel channel = ctx.channel();
        b = new Bootstrap();
        b.group(ctx.channel().eventLoop())
                .channel(ctx.channel().getClass())
                .handler(new ProxyBackendHandlersInit(channel));


    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) {

        requestToProxyServer = msg;
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

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof ProxyEvent) {
            logger.info("Received the event");
            ProxyEvent proxyEvent = (ProxyEvent) evt;
            ChannelFuture f = b.connect(proxyEvent.getDomain(), proxyEvent.getPort());

            f.addListener(new CustomListener());
        } else {
            System.out.println(evt);
        }
    }

    private final class CustomListener implements ChannelFutureListener {

        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            if (channelFuture.isSuccess()) {
                logger.info("Connected to the proxy server");
                outboundChannel = channelFuture.channel();
                outboundChannel.writeAndFlush(requestToProxyServer);
            }
        }
    }
}