package client;

import dto.Camera;
import dto.Plate;
import dto.WantHeartBeat;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import speeddeamon.CameraEncoder;
import speeddeamon.DispatcherEncoder;
import speeddeamon.ErrorEncoder;
import speeddeamon.HeartBeatEncoder;
import speeddeamon.MessageDecoder;
import speeddeamon.PlateEncoder;
import speeddeamon.TicketEncoder;
import speeddeamon.WantHeartBeatEncoder;

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
					ch.pipeline().addLast(new ErrorEncoder(), new HeartBeatEncoder(), new TicketEncoder(),
							new WantHeartBeatEncoder(), new DispatcherEncoder(), new PlateEncoder(),
							new CameraEncoder(), new MessageDecoder(), new ClientHandler());
				}
			});

			// Start the client.
			Channel ch = b.connect("localhost", 4000).sync().channel();

			Camera camera1 = new Camera((short) 123, (short) 8, (short) 60);
			Plate plate = new Plate("UN1X", 0);
			

//			Camera camera2 = new Camera((short) 123, (short) 9, (short) 60);
//			Plate plate2 = new Plate("UN1X", 45);

//			Dispatcher dispatcher = new Dispatcher((byte) 1, new short[] { 123 });
//			Dispatcher dispatcher2 = new Dispatcher((byte) 3, new short[] { 66, 368, 5000 });
//
//			ch.writeAndFlush(dispatcher);
//			ch.writeAndFlush(dispatcher2);
//			ch.writeAndFlush(camera2);
//			ch.writeAndFlush(plate2);

			ch.writeAndFlush(new WantHeartBeat(50));
			
//			ch.writeAndFlush(camera1);
//			ch.writeAndFlush(plate);

			ch.closeFuture().sync(); // wait

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			workerGroup.shutdownGracefully();
		}

	}

}
