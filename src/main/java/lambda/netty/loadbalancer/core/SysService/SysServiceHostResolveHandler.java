package lambda.netty.loadbalancer.core.SysService;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import lambda.netty.loadbalancer.core.etcd.EtcdUtil;
import lambda.netty.loadbalancer.core.proxy.ProxyEvent;
import org.apache.log4j.Logger;

/**
 * Created by maanadev on 5/18/17.
 */
public class SysServiceHostResolveHandler extends ChannelInboundHandlerAdapter {
    final static Logger logger = Logger.getLogger(SysServiceHostResolveHandler.class);
    private final static String HOST = "Host";
    private static final String SYS_HOST = "127.0.0.1";
    private static final int SYS_PORT = 8081;
    Channel remoteHostChannel = null;
    EventLoopGroup remoteHostEventLoopGroup;

    public SysServiceHostResolveHandler(EventLoopGroup remoteHostEventLoopGroup) {
        this.remoteHostEventLoopGroup = remoteHostEventLoopGroup;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final Channel mainChannel = ctx.channel();
        Bootstrap b = new Bootstrap();

        b.group(remoteHostEventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new SysServiceHandlersInit(ctx));

        b.connect(SYS_HOST, SYS_PORT).addListeners(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    logger.info("connected to the System service");
                    remoteHostChannel = channelFuture.channel();
                    //Reading the main channel after Sys service is connected
                    mainChannel.read();
                } else {
                    logger.error("Cannot connect to the System Service !");
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
            ProxyEvent proxyEvent = new ProxyEvent(host);

            logger.info(proxyEvent.getDomain() + " " + proxyEvent.getPort());
            getIp(ctx);
            EtcdUtil.getValue("dddd")
                    .thenAccept(System.out::println);
        } else {
            System.out.println(msg);
        }
        ctx.fireChannelRead(msg);
    }


    private void getIp(ChannelHandlerContext ctx) throws InterruptedException {

        // Prepare the HTTP request.
        HttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, "http://127.0.0.1:8081");
        request.headers().set(HttpHeaderNames.HOST, "127.0.0.1");
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);

        // Send the HTTP request.
        remoteHostChannel.writeAndFlush(request);
        // Wait for the server to close the connection.
        remoteHostChannel.closeFuture().sync();
        logger.info("Request sent to the System Service");

    }
}
