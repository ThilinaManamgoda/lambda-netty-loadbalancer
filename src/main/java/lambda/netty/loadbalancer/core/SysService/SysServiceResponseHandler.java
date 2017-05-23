package lambda.netty.loadbalancer.core.SysService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class SysServiceResponseHandler extends SimpleChannelInboundHandler<HttpObject> {

    private int i=0;
    ChannelHandlerContext mainCtx;
    public SysServiceResponseHandler(ChannelHandlerContext mainCtx) {
        this.mainCtx=mainCtx;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        System.out.println(++i);
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse fullHttpResponse = (FullHttpResponse) msg;
            System.out.println();
            mainCtx.fireUserEventTriggered(fullHttpResponse.content().toString(StandardCharsets.UTF_8));
        }
        ctx.close();
    }
}

