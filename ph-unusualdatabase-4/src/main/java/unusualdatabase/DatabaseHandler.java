package unusualdatabase;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

@Sharable
public class DatabaseHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	private static Logger logger = LoggerFactory.getLogger(DatabaseHandler.class);
	private static Map<String, String> dataStore = new HashMap<>();

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
		String message = packet.content().toString(CharsetUtil.UTF_8);
		message = message.replace("\n", "");
		message = message.replace("\r", "");
		
		logger.info("Received " + message);
		if (message.equals("version")) {
			String versionResponse = "version=Ken's Key-Value Store 1.0";
			sendResponse(versionResponse, packet, ctx);
			return;
		}

		System.out.println(message.length());
		if (message.contains("=") && message.length() > 1) {
			// its an insert
			String key = message.substring(0, message.indexOf("="));
			if (key.equals("version")) {
				// ignore overriding version
				return;
			}
			String value = message.substring(message.indexOf("=") + 1);
			System.out.println(key + " = " + value);

			dataStore.put(key, value);
		} else {
			// its a retrieve request
			String value = dataStore.get(message);
			sendResponse(message + "=" + (value == null ? "" : value), packet, ctx);
		}
	}

	void sendResponse(String message, DatagramPacket packet, ChannelHandlerContext ctx) {
		ByteBuf response = ctx.alloc().buffer();
		response.writeCharSequence(message, CharsetUtil.UTF_8);
		ctx.writeAndFlush(new DatagramPacket(response, packet.sender()));
	}

}
