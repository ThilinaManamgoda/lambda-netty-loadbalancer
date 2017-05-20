package lambda.netty.loadbalancer.core.SysService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

/**
 * Created by maanadev on 5/19/17.
 */
public class SysServiceResponseHandler extends SimpleChannelInboundHandler<HttpObject> {
    ;

    public SysServiceResponseHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;

            System.err.print(content.content().toString(CharsetUtil.UTF_8));

            System.err.flush();

            if (content instanceof LastHttpContent) {
                ctx.close();
            }
        }
    }
}

