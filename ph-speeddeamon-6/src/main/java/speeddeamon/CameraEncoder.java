package speeddeamon;

import dto.Camera;
import dto.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CameraEncoder extends MessageToByteEncoder<Camera> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Camera msg, ByteBuf out) throws Exception {
		out.writeByte(Constants.CAMERA);
		out.writeShort(msg.getRoad());
		out.writeShort(msg.getMile());
		out.writeShort(msg.getLimit());
	}

}
