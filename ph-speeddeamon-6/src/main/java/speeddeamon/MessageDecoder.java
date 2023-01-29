package speeddeamon;

import java.util.List;

import dto.Camera;
import dto.Constants;
import dto.Dispatcher;
import dto.HeartBeat;
import dto.Plate;
import dto.ProtocolError;
import dto.Ticket;
import dto.WantHeartBeat;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessageDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		// first byte contains the message type
		if (in.readableBytes() < 1) {
			return;
		}

		in.markReaderIndex();

		byte messageType = in.readByte();

		if (messageType == Constants.CAMERA) {
			if (in.readableBytes() < 6) {
				in.resetReaderIndex();
				return;
			}

			out.add(new Camera(in.readShort(), in.readShort(), in.readShort()));
			return;
		}

		if (messageType == Constants.DISPATCHER) {
			if (in.readableBytes() < 1) {
				in.resetReaderIndex();
				return;
			}
			byte numRoads = in.readByte();

			if (in.readableBytes() < numRoads * 2) {
				in.resetReaderIndex();
				return;
			}

			short[] roads = new short[numRoads];

			for (int i = 0; i < numRoads; i++) {
				roads[i] = in.readShort();
			}

			out.add(new Dispatcher(numRoads, roads));
			return;
		}

		if (messageType == Constants.WANT_HEARTBEAT) {
			if (in.readableBytes() < 4) {
				in.resetReaderIndex();
				return;
			}

			out.add(new WantHeartBeat(in.readInt()));
			return;
		}

		if (messageType == Constants.PLATE) {
			if (in.readableBytes() < 1) {
				in.resetReaderIndex();
				return;
			}

			byte stringLength = in.readByte();

			if (in.readableBytes() < (stringLength + 4)) {
				// read all the bytes to load the string.
				in.resetReaderIndex();
				return;
			}

			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < stringLength; i++) {
				builder.append((char) in.readByte());
			}

			int timestamp = in.readInt();

			out.add(new Plate(builder.toString(), timestamp));
		}

		if (messageType == Constants.ERROR) {
			if (in.readableBytes() < 1) {
				in.resetReaderIndex();
				return;
			}

			byte stringLength = in.readByte();

			if (in.readableBytes() < stringLength) {
				// read all the bytes to load the string.
				in.resetReaderIndex();
				return;
			}

			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < stringLength; i++) {
				builder.append((char) in.readByte());
			}

			out.add(new ProtocolError(builder.toString()));

		}

		if (messageType == Constants.TICKET) {

			if (in.readableBytes() < 1) {
				in.resetReaderIndex();
				return;
			}

			byte stringLength = in.readByte();

			int totalLength = stringLength + 2 + 2 + 4 + 2 + 4 + 2;

			if (in.readableBytes() < totalLength) {
				// read all the bytes to load the string.
				in.resetReaderIndex();
				return;
			}

			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < stringLength; i++) {
				builder.append((char) in.readByte());
			}

			Ticket ticket = new Ticket(builder.toString(), in.readShort(), in.readShort(), in.readInt(), in.readShort(),
					in.readInt(), in.readShort());

			out.add(ticket);
		}

		if (messageType == Constants.HEARTBEAT) {
			out.add(new HeartBeat());
		}

	}

}
