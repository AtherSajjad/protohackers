package prime;

import org.json.JSONObject;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import util.Utils;
import util.PrimeChecker;

public class PrimeServerHandler extends SimpleChannelInboundHandler<String> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		if (!Utils.isJSON(msg)) {
			ChannelFuture future = ctx.writeAndFlush("Malformed Response, Not a valid json\n");
			future.addListener(ChannelFutureListener.CLOSE);
		}

		JSONObject request = new JSONObject(msg);
		if (!request.has("method") || !request.getString("method").equals("isPrime")) {
			ChannelFuture future = ctx.writeAndFlush("Malformed Response, Invalid method name\n");
			future.addListener(ChannelFutureListener.CLOSE);
		}

		if (!request.has("number") || !Utils.isANumber(request)) {
			ChannelFuture future = ctx.writeAndFlush("Malformed Response, Not a number\n");
			future.addListener(ChannelFutureListener.CLOSE);
		}

		Number number = request.getNumber("number");
		JSONObject response = new JSONObject();
		response.put("method", "isPrime");
		response.put("prime", PrimeChecker.checkIsPrime(number.intValue()));

		ctx.writeAndFlush(response.toString() + "\n");
	}

}
