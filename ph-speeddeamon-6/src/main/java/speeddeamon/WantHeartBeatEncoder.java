package speeddeamon;

import dto.Constants;
import dto.WantHeartBeat;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class WantHeartBeatEncoder extends MessageToByteEncoder<WantHeartBeat> {

	@Override
	protected void encode(ChannelHandlerContext ctx, WantHeartBeat msg, ByteBuf out) throws Exception {
		out.writeByte(Constants.WANT_HEARTBEAT);
		out.writeInt(msg.getInterval());
	}

}
