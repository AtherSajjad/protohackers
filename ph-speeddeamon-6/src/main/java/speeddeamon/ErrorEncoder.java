package speeddeamon;

import dto.Constants;
import dto.ProtocolError;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import utils.Utils;

public class ErrorEncoder extends MessageToByteEncoder<ProtocolError> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ProtocolError msg, ByteBuf out) throws Exception {
		out.writeByte(Constants.ERROR);
		out.writeBytes(Utils.getLengthPrefixedString(msg.getMessage()));

		return;

	}

}
