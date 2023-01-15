package meanstoend;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dto.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

@Sharable
public class MeansToEndHandler extends ChannelInboundHandlerAdapter {

	private static final AttributeKey<Map<Integer, Integer>> sessionKey = AttributeKey.newInstance("session");
	private static final Logger logger = LoggerFactory.getLogger(MeansToEndHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		Message request = (Message) msg;

		if (request.getOp() == 'I') {
			logger.info("Rececived insert " + request);

			Map<Integer, Integer> stored = ctx.channel().attr(sessionKey).get();

			if (stored == null) {
				stored = new HashMap<>();
			}
			
			stored.put(request.getArg1(), request.getArg2());
			ctx.channel().attr(sessionKey).set(stored);

		} else if (request.getOp() == 'Q') {
			Map<Integer, Integer> stored = ctx.channel().attr(sessionKey).get();

			if (stored == null) {
				logger.error("Received query, when no data is available so it returns 0");
				ByteBuf response = ctx.alloc().buffer(4);
				response.writeInt(0);
				ctx.writeAndFlush(response);
				return;
			}

			int sum = 0, count = 0, avg = 0;
			for (Map.Entry<Integer, Integer> entry : stored.entrySet()) {
				int time = entry.getKey();
				int value = entry.getValue();

				if (time >= request.getArg1() && time <= request.getArg2()) {
					sum += value;
					count++;
				}
			}

			if (count != 0) {
				avg = sum / count;
			}

			ByteBuf response = ctx.alloc().buffer(4);
			logger.info("Received query" + request + " and responding with answer " + avg);
			response.writeInt(avg);
			ctx.writeAndFlush(response);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(cause.getLocalizedMessage());
		cause.printStackTrace();
	}
}
