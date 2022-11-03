package echo;

import io.netty.channel.ChannelHandler.Sharable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(EchoServerHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("Client connected " + ctx.channel().remoteAddress());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("Recived message from " + ctx.channel().remoteAddress() + ", echoing it back");
		ctx.writeAndFlush(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

}
