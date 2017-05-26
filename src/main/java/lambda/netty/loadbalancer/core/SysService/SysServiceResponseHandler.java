package lambda.netty.loadbalancer.core.SysService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import lambda.netty.loadbalancer.core.proxy.ProxyEvent;
import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;


public class SysServiceResponseHandler extends SimpleChannelInboundHandler<HttpObject> {
    final static Logger logger = Logger.getLogger(SysServiceResponseHandler.class);
    private ChannelHandlerContext mainCtx;

    public SysServiceResponseHandler(ChannelHandlerContext mainCtx) {
        this.mainCtx = mainCtx;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof FullHttpResponse) {
            logger.info("Sys response has received triggering the proxyEvent ");
            FullHttpResponse fullHttpResponse = (FullHttpResponse) msg;
            ProxyEvent proxyEvent = new ProxyEvent(fullHttpResponse.content().toString(StandardCharsets.UTF_8));
            mainCtx.fireUserEventTriggered(proxyEvent);
        }
        ctx.close();
    }
}

