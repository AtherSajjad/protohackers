package meanstoend;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dto.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.AttributeKey;

@Sharable
public class MeansToEndHandler extends ChannelInboundHandlerAdapter {

	private static final AttributeKey<ArrayList<Message>> sessionKey = AttributeKey.newInstance("session");
	private static final Logger logger = LoggerFactory.getLogger(MeansToEndHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		Message request = (Message) msg;

		if (request.getOp() == 'I') {
			ArrayList<Message> stored = ctx.channel().attr(sessionKey).get();
			if (stored == null) {
				ArrayList<Message> newEntries = new ArrayList<Message>();
				newEntries.add(request);
				ctx.channel().attr(sessionKey).set(newEntries);
			} else {
				boolean alreadyAvailableTimestamp = stored.stream()
						.anyMatch(message -> message.getArg1() == request.getArg1());
				if (!alreadyAvailableTimestamp) {
					stored.add(request);
					ctx.channel().attr(sessionKey).set(stored);
				}
			}

			logger.info("Rececived insert " + request);

		} else if (request.getOp() == 'Q') {
			ArrayList<Message> data = ctx.channel().attr(sessionKey).get();

			if (data == null) {
				logger.error("Received query, when no data is available so it returns 0");
				ByteBuf response = ctx.alloc().buffer(4);
				response.writeInt(0);
				ctx.writeAndFlush(response);
				return;
			}

			List<Message> filtered = data.stream().filter(
					(message) -> message.getArg1() >= request.getArg1() && message.getArg1() <= request.getArg2())
					.collect(Collectors.toList());

			long count = filtered.size();

			if (count == 0) {
				logger.error("Received query, there is no data in between the time range, so it returns 0" + request);
				ByteBuf response = ctx.alloc().buffer(4);
				response.writeInt(0);
				ctx.writeAndFlush(response);
				return;
			}

			Integer sum = filtered.stream().mapToInt((message) -> message.getArg2()).sum();

			ByteBuf response = ctx.alloc().buffer(4);
			logger.info("Received query" + request + " and responding with answer " + ((int) (sum / count)));
			response.writeInt((int) (sum / count));
			ctx.writeAndFlush(response);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(cause.getLocalizedMessage());
		cause.printStackTrace();
	}
}
