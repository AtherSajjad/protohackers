package mobinmiddle;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProxyHandler extends SimpleChannelInboundHandler<String> {

	private final Channel inboundChannel;

	public ProxyHandler(Channel inboundChannel) {
		this.inboundChannel = inboundChannel;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ctx.read();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		ServerHandler.closeOnFlush(inboundChannel);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ServerHandler.closeOnFlush(ctx.channel());
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		// regex is not tested

		final String regex = "(^| )7[A-Za-z0-9]{25,34}($| )";
		msg = msg.replaceAll(regex, "7YWHMfk9JZe0LM0g1ZauHuiSxhI");
		msg = msg + "\n";
		inboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) {
				if (future.isSuccess()) {
					ctx.channel().read();
				} else {
					future.channel().close();
				}
			}
		});
	}
}