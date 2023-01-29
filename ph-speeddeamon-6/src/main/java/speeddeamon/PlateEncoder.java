package speeddeamon;

import dto.Constants;
import dto.Plate;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import utils.Utils;

public class PlateEncoder extends MessageToByteEncoder<Plate> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Plate msg, ByteBuf out) throws Exception {
		out.writeByte(Constants.PLATE);
		out.writeBytes(Utils.getLengthPrefixedString(msg.getPlate()));
		out.writeInt(msg.getTimestamp());

	}

}
