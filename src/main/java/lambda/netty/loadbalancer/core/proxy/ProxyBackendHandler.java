package lambda.netty.loadbalancer.core.proxy;


import io.netty.channel.*;
import org.apache.log4j.Logger;

public class ProxyBackendHandler extends ChannelInboundHandlerAdapter {

    final static Logger logger = Logger.getLogger(ProxyBackendHandler.class);

    private final Channel inboundChannel;


    public ProxyBackendHandler(Channel inboundChannel) {
        this.inboundChannel = inboundChannel;
    }

    @Override

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        inboundChannel.writeAndFlush(msg).addListener(new CustomListener());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ProxyFrontendHandler.closeOnFlush(inboundChannel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ProxyFrontendHandler.closeOnFlush(ctx.channel());
    }

    private final class CustomListener implements ChannelFutureListener {

        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            if (channelFuture.isSuccess()) {
                logger.info("Message redirected to the Client");
                inboundChannel.close();
                channelFuture.channel().close();
            }
        }
    }
}