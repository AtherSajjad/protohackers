package speeddeamon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dto.ProtocolError;
import dto.Ticket;
import dto.WantHeartBeat;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class SpeedDaemonHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(SpeedDaemonHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);

//		Ticket ticket = new Ticket("XYZ", (short) 10, (short) 20, 22, (short) 2, 22, (short) 10);
//		ProtocolError error = new ProtocolError("Test");
//
//		ctx.writeAndFlush(ticket);
//		ctx.writeAndFlush(error);
		System.out.println("Client connected");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		System.out.println(msg);

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(cause.getLocalizedMessage());
		cause.printStackTrace();
	}
}
