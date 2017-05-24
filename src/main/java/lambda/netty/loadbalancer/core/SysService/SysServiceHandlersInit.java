package lambda.netty.loadbalancer.core.SysService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import org.apache.log4j.Logger;


/**
 * Created by maanadev on 5/19/17.
 */
public class SysServiceHandlersInit extends ChannelInitializer<SocketChannel> {
    final static Logger logger = Logger.getLogger(SysServiceHandlersInit.class);
    private ChannelHandlerContext mainCtx;
    public SysServiceHandlersInit(ChannelHandlerContext mainCtx) {
        this.mainCtx=mainCtx;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        logger.info("Initiating System Service Handlers");
        socketChannel.pipeline().addLast(new HttpRequestEncoder(),new HttpResponseDecoder(),new HttpContentDecompressor(),new HttpObjectAggregator(104867), new SysServiceResponseHandler(mainCtx));

    }
}
