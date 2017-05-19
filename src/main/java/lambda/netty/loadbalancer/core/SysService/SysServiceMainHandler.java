package lambda.netty.loadbalancer.core.SysService;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.ClientCookieEncoder;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.util.AttributeKey;
import lambda.netty.loadbalancer.core.proxy.ProxyHandlersInit;

/**
 * Created by maanadev on 5/18/17.
 */
public class SysServiceMainHandler extends ChannelInboundHandlerAdapter{
    AttributeKey attributeKey = AttributeKey.valueOf("lambda");
    private final static String HOST = "Host";
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            String host = request.headers().get(HOST);
            RemoteHost remoteHost = new RemoteHost(host);

            System.out.println(remoteHost.getDomain()+" "+remoteHost.getPort());
           // System.out.println(getIp(ctx).getHost());

//            ctx.channel().attr(attributeKey).set(getIp(ctx).getHost());
        }
        ctx.fireChannelRead(msg);
    }


//    private void getIp( ChannelHandlerContext ctx) throws InterruptedException {
//
//        Bootstrap b = new Bootstrap();
//        b.group(new NioEventLoopGroup())
//                .channel(ctx.channel().getClass())
//                .handler(new SysServiceHandlersInit());
////                .option(ChannelOption.AUTO_READ, false);
//        Channel ch = null;
//        try {
//            ch = b.connect("127.0.0.1", Integer.parseInt("8081")).sync().channel();
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // Prepare the HTTP request.
//        HttpRequest request = new DefaultFullHttpRequest(
//                HttpVersion.HTTP_1_1, HttpMethod.GET, "http://127.0.0.1:8081");
//        request.headers().set(HttpHeaderNames.HOST, "127.0.0.1");
//        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
//        request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
//
//
//        // Send the HTTP request.
//        ch.writeAndFlush(request);
//
//        // Wait for the server to close the connection.
//        ch.closeFuture().sync();
//
//    }
}
