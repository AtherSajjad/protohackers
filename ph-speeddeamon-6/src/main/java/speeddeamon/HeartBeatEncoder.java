package speeddeamon;

import dto.Constants;
import dto.HeartBeat;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class HeartBeatEncoder extends MessageToByteEncoder<HeartBeat> {

	@Override
	protected void encode(ChannelHandlerContext ctx, HeartBeat msg, ByteBuf out) throws Exception {
		out.writeByte(Constants.HEARTBEAT);
	}

}
