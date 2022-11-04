package prime;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import util.Utils;
import util.PrimeChecker;

public class PrimeServerHandler extends SimpleChannelInboundHandler<String> {
	private static final Logger logger = LoggerFactory.getLogger(PrimeServerHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		if (!Utils.isJSON(msg)) {
			logger.error("Received malformed input " + msg);
			ChannelFuture future = ctx.writeAndFlush("Malformed Response, Not a valid json\n");
			future.addListener(ChannelFutureListener.CLOSE);
		}

		JSONObject request = new JSONObject(msg);
		if (!request.has("method") || !request.getString("method").equals("isPrime")) {
			logger.error("Received malformed input " + msg);

			ChannelFuture future = ctx.writeAndFlush("Malformed Response, Invalid method name\n");
			future.addListener(ChannelFutureListener.CLOSE);
		}

		if (!request.has("number") || !Utils.isANumber(request)) {
			logger.error("Received malformed input " + msg);

			ChannelFuture future = ctx.writeAndFlush("Malformed Response, Not a number\n");
			future.addListener(ChannelFutureListener.CLOSE);
		}

		Number number = request.getNumber("number");
		JSONObject response = new JSONObject();
		response.put("method", "isPrime");
		response.put("prime", PrimeChecker.checkIsPrime(number.intValue()));

		logger.info("Received message " + msg + " and response with " + response.toString());

		ctx.writeAndFlush(response.toString() + "\n");
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(cause.getLocalizedMessage());
		cause.printStackTrace();
	}

}
