package unusualdatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

@Sharable
public class DatabaseHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	private static Logger logger = LoggerFactory.getLogger(DatabaseHandler.class);
	private static ConcurrentHashMap<String, String> dataStore = new ConcurrentHashMap<>();

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
		String message = packet.content().toString(CharsetUtil.UTF_8);
		message = message.replace("\n", "");
		message = message.replace("\r", "");

		if (message.equals("version")) {
			String versionResponse = "version=Ken's Key-Value Store 1.0";
			sendResponse(versionResponse, packet, ctx);
			return;
		}

		// System.out.println(message.length());
		if (message.contains("=") && message.length() > 1) {
			logger.info("Insert " + message);

			// its an insert
			String key = message.substring(0, message.indexOf("="));
			if (key.equals("version") || key.equals("=")) {
				// ignore overriding version
				return;
			}
			String value = message.substring(message.indexOf("=") + 1);

			dataStore.put(key.trim(), value);
		} else {
			// its a retrieve request
			String value = dataStore.get(message);
			logger.info("Retrieve " + message + "=" + value == null ? "" : value);
			sendResponse(message + "=" + (value == null ? "" : value), packet, ctx);
		}
	}

	void sendResponse(String message, DatagramPacket packet, ChannelHandlerContext ctx) {
		ChannelFuture future = ctx
				.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8), packet.sender()));

	}

}
