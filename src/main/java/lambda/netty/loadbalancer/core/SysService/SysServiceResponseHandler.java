package lambda.netty.loadbalancer.core.SysService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;

import java.nio.charset.StandardCharsets;


public class SysServiceResponseHandler extends SimpleChannelInboundHandler<HttpObject> {

   private ChannelHandlerContext mainCtx;
    public SysServiceResponseHandler(ChannelHandlerContext mainCtx) {
        this.mainCtx=mainCtx;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse fullHttpResponse = (FullHttpResponse) msg;
            mainCtx.fireUserEventTriggered(fullHttpResponse.content().toString(StandardCharsets.UTF_8));
        }
        ctx.close();
    }
}

