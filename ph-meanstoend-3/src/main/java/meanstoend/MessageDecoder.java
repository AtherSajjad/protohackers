package meanstoend;

import java.util.List;

import dto.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessageDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < 9) {
			return;
		}

		char op = (char) in.readByte();
		int arg1 = in.readInt();
		int arg2 = in.readInt();

		Message request = new Message(op, arg1, arg2);

		out.add(request);

	}

}
