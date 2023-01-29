package speeddeamon;

import dto.Constants;
import dto.Dispatcher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class DispatcherEncoder extends MessageToByteEncoder<Dispatcher> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Dispatcher msg, ByteBuf out) throws Exception {
		out.writeByte(Constants.DISPATCHER);

		out.writeByte(msg.getNumRoads());
		for (int i = 0; i < msg.getNumRoads(); i++) {
			out.writeShort(msg.getRoads()[i]);
		}
	}

}
