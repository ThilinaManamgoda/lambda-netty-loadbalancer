package lambda.netty.loadbalancer.core.SysService;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import lambda.netty.loadbalancer.core.etcd.EtcdClientException;
import lambda.netty.loadbalancer.core.etcd.EtcdUtil;
import lambda.netty.loadbalancer.core.loadbalance.LoadBalanceUtil;
import lambda.netty.loadbalancer.core.loadbalance.StateImplJsonHelp;
import lambda.netty.loadbalancer.core.loadbalance.statemodels.InstanceStates;
import lambda.netty.loadbalancer.core.loadbalance.statemodels.State;
import lambda.netty.loadbalancer.core.proxy.DecoderException;
import lambda.netty.loadbalancer.core.proxy.ProxyEvent;
import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;

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

        b.connect(SYS_HOST, SYS_PORT).addListeners(new CustomListener(mainChannel));
        super.channelActive(ctx);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            EtcdUtil.getValue("localhost").thenAccept(x -> {

                String val = String.valueOf(x.getKvs(0).getValue().toString(StandardCharsets.UTF_8));
                State stateImpl = StateImplJsonHelp.getObject(val);

                if (stateImpl.getState() == InstanceStates.DOWN) {
                    logger.info("No instance is up ! informing Sys-service ");
                    requestIp();
                } else if (stateImpl.getState() == InstanceStates.RUNNING) {
                    logger.info("These instances are up and running");
                    LoadBalanceUtil.getRemoteHost(stateImpl);
                    try {
                        EtcdUtil.putValue("localhost", StateImplJsonHelp.toString(stateImpl));
                    } catch (EtcdClientException e) {
                        logger.error("Cannot connect to ETCD !", e);
                    }

                    ProxyEvent proxyEvent = new ProxyEvent("localhost:8082");
                    ctx.fireUserEventTriggered(proxyEvent);
                }
            });
        } else {
            logger.error("Decoder doesn't work. Not a FullHttpRequest Object !");
            throw new DecoderException();
        }
        ctx.fireChannelRead(msg);
    }

    private void requestIp() {

        // Prepare the HTTP request.
        HttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, "http://127.0.0.1:8081");
        request.headers().set(HttpHeaderNames.HOST, "127.0.0.1");
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);

        // Send the HTTP request.
        remoteHostChannel.writeAndFlush(request);
        // Wait for the server to close the connection.
        try {
            remoteHostChannel.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("Couldn't close the connection with the Sys-service !", e);
        }
        logger.info("Request sent to the System Service");
    }

    private final class CustomListener implements ChannelFutureListener {
        private Channel mainChannel;

        CustomListener(Channel mainChannel) {
            this.mainChannel = mainChannel;
        }

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
    }
}
