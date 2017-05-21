package lambda.netty.loadbalancer.core.SysService;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;

/**
 * Created by maanadev on 5/18/17.
 */
public class SysServiceHostResolveHandler extends ChannelInboundHandlerAdapter {
    private final static String HOST = "Host";
    AttributeKey attributeKey = AttributeKey.valueOf("lambda");
    Bootstrap b;
    Channel ch = null;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        b = new Bootstrap();
        b.group(new NioEventLoopGroup())
                .channel(ctx.channel().getClass())
                .handler(new SysServiceHandlersInit());

        ChannelFuture future = b.connect("127.0.0.1", Integer.parseInt("8081"));

        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {

                    ch = channelFuture.channel();
                }
            }
        });

        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            String host = request.headers().get(HOST);
            RemoteHost remoteHost = new RemoteHost(host);

            System.out.println(remoteHost.getDomain() + " " + remoteHost.getPort());
            getIp();
        } else {
            System.out.println(msg);
        }
        ctx.fireChannelRead(msg);
    }


    private void getIp() throws InterruptedException {

        // Prepare the HTTP request.
        HttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, "http://127.0.0.1:8081");
        request.headers().set(HttpHeaderNames.HOST, "127.0.0.1");
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);


        // Send the HTTP request.
        ch.writeAndFlush(request);

        // Wait for the server to close the connection.
        ch.closeFuture().sync();

    }
}
