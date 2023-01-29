package speeddeamon;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dto.HeartBeat;
import dto.ProtocolError;
import dto.WantHeartBeat;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.ScheduledFuture;

@Sharable
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(SpeedDaemonHandler.class);
	private static final AttributeKey<Boolean> heartBeatSet = AttributeKey.newInstance("heartBeat");
	private static final AttributeKey<ScheduledFuture<?>> scheduledFuture = AttributeKey.newInstance("futureHandler");

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (!(msg instanceof WantHeartBeat)) {
			ctx.fireChannelRead(msg);
			return;
		}

		WantHeartBeat wantHeartBeat = (WantHeartBeat) msg;

		if (ctx.channel().attr(heartBeatSet).get() == null && wantHeartBeat.getInterval() > 0) {

			// setup heart beat for this handler
			ScheduledFuture<?> scheduleFuture = ctx.channel().eventLoop().scheduleWithFixedDelay(new Runnable() {

				@Override
				public void run() {
					if (ctx.channel().isActive()) {
						ctx.writeAndFlush(new HeartBeat());
					}
				}
			}, 0, wantHeartBeat.getInterval() * 100, TimeUnit.MILLISECONDS);

			ctx.channel().attr(heartBeatSet).set(true);
			ctx.channel().attr(scheduledFuture).set(scheduleFuture);
		} else {
			ctx.writeAndFlush(new ProtocolError("Already heartbeat set"));
		}

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);

		if (ctx.channel().attr(heartBeatSet) != null) {
			ScheduledFuture<?> future = ctx.channel().attr(scheduledFuture).get();
			future.cancel(true);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(cause.getLocalizedMessage());
		cause.printStackTrace();
	}
}
