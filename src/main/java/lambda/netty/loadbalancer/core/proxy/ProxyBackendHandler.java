package lambda.netty.loadbalancer.core.proxy;


import io.netty.channel.*;

public class ProxyBackendHandler extends ChannelInboundHandlerAdapter {

    Channel inboundChannel;

    public ProxyBackendHandler(Channel inboundChannel) {
        this.inboundChannel = inboundChannel;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        inboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    inboundChannel.close();
                    channelFuture.channel().close();
                }
            }
        });
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
}