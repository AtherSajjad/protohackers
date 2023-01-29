package speeddeamon;

import dto.Constants;
import dto.Ticket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import utils.Utils;

public class TicketEncoder extends MessageToByteEncoder<Ticket> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Ticket msg, ByteBuf out) throws Exception {
		out.writeByte(Constants.TICKET);

		out.writeBytes(Utils.getLengthPrefixedString(msg.getPlate()));
		out.writeShort(msg.getRoad());
		out.writeShort(msg.getMile1());
		out.writeInt(msg.getTimestamp1());
		out.writeShort(msg.getMile2());
		out.writeInt(msg.getTimestamp2());
		out.writeShort(msg.getSpeed());

	}

}
