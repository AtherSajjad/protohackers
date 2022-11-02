package client;

import java.util.ArrayList;

import dto.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	public static void main(String[] args) throws Exception {

		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ClientMessageDecoder(), new ClientHandler());
				}
			});

			// Start the client.
			Channel ch = b.connect("localhost", 4000).sync().channel();

			ArrayList<Message> session = buildSession();
			for (Message m : session) {
				ByteBuf request = ch.alloc().buffer(9);
				request.writeByte((byte) m.getOp());
				request.writeInt(m.getArg1());
				request.writeInt(m.getArg2());
				ch.writeAndFlush(request);
			}

			ch.closeFuture().sync(); // wait

		} finally {
			workerGroup.shutdownGracefully();
		}

	}

	public static ArrayList<Message> buildSession() {
		ArrayList<Message> messages = new ArrayList<>();
		messages.add(new Message('I', 12345, 101));
		messages.add(new Message('I', 12346, 102));
		messages.add(new Message('I', 12347, 100));
		messages.add(new Message('I', 40960, 5));
		messages.add(new Message('Q', 12288, 16384));

		return messages;
	}
}
