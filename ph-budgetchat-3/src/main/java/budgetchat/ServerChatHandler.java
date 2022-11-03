package budgetchat;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ServerChatHandler extends SimpleChannelInboundHandler<String> {
	private static final AttributeKey<String> NAMEKEY = AttributeKey.newInstance("name");

	private static Logger logger = LoggerFactory.getLogger(ServerChatHandler.class);
	static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("Client connected at remote address " + ctx.channel().remoteAddress());
		channels.add(ctx.channel());
		ctx.writeAndFlush("Welcome to budgetchat! What shall I call you?\n");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		String userName = ctx.channel().attr(NAMEKEY).get();

		// set first message as username
		if (userName == null) {
			ctx.channel().attr(NAMEKEY).set(msg);
			logger.info(msg + " joined the room");

			for (Channel channel : channels) {
				if (channel != ctx.channel()) {
					String userJoinedMessage = "* " + msg + " has entered the room\n";
					channel.writeAndFlush(userJoinedMessage);
				}
			}

			String message = "* The room contains: ";

			String usernames = channels.stream()
					.filter((channel) -> channel.attr(NAMEKEY).get() != null && channel != ctx.channel())
					.map((channel) -> channel.attr(NAMEKEY).get()).collect(Collectors.joining(","));

			ctx.writeAndFlush(message + usernames + "\n");

			return;
		}

		logger.info(userName + " sent " + msg + ", relaying to others");
		// Relay to others with username
		for (Channel channel : channels) {
			if (channel != ctx.channel()) {
				String message = "[" + userName + "] " + msg + "\n";
				channel.writeAndFlush(message);
			}
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		String userName = ctx.channel().attr(NAMEKEY).get();
		if (userName == null) {
			return;
		}
		String userLeftMessage = "* " + userName + " has left the room\n";
		logger.info(userName + " has left the room");
		channels.remove(ctx.channel());
		for (Channel channel : channels) {
			if (channel != ctx.channel()) {
				channel.writeAndFlush(userLeftMessage);
			}
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	}

}
