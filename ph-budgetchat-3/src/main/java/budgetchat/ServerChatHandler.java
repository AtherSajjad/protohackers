package budgetchat;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import utils.Validator;

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

		if (userName == null && !Validator.isValidUserName(msg)) {
			logger.info("Username =" + msg);
			logger.error("Invalid username");
			ChannelFuture future = ctx.writeAndFlush("Invalid username sent\n");
			future.addListener(ChannelFutureListener.CLOSE);
			channels.remove(ctx.channel());
			return;

		}

		// set first message as username
		if (userName == null) {
			ctx.channel().attr(NAMEKEY).set(msg);

			logger.info(msg + " joined the room");

			String userJoinedMessage = "* " + msg + " has entered the room\n";
			broadCast(ctx.channel(), userJoinedMessage);

			String message = "* The room contains: ";

			String usernames = channels.stream()
					.filter((channel) -> channel.attr(NAMEKEY).get() != null && channel != ctx.channel())
					.map((channel) -> channel.attr(NAMEKEY).get()).collect(Collectors.joining(","));

			ctx.writeAndFlush(message + usernames + "\n");

			return;
		}

		logger.info(userName + " sent " + msg + ", relaying to others");
		// Relay to others with username
		String message = "[" + userName + "] " + msg + "\n";
		broadCast(ctx.channel(), message);
	}

	public void broadCast(Channel except, String message) {
		for (Channel channel : channels) {
			if (channel != except && channel.attr(NAMEKEY).get() != null) {
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
		broadCast(ctx.channel(), userLeftMessage);
		channels.remove(ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	}

}
