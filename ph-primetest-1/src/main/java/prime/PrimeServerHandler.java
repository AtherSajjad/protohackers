package prime;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import util.PrimeChecker;
import util.Utils;

public class PrimeServerHandler extends SimpleChannelInboundHandler<String> {
	private static final Logger logger = LoggerFactory.getLogger(PrimeServerHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

		logger.info("Received message " + msg);
		if (!Utils.isValidRequest(msg)) {
			logger.error("Received malformed input " + msg);
			ChannelFuture future = ctx.writeAndFlush("Malformed Response\n");
			future.addListener(ChannelFutureListener.CLOSE);
			return;
		}

		JSONObject request = new JSONObject(msg);

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
