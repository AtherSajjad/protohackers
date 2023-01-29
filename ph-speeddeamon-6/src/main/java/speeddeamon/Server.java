package speeddeamon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
	public static final int PORT = 4000;
	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	public static void main(String[] args) {

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap().group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 5)
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new TicketEncoder());
							pipeline.addLast(new ErrorEncoder());
							pipeline.addLast(new MessageDecoder());
							pipeline.addLast(new HeartBeatEncoder());
							pipeline.addLast(new HeartBeatHandler());
							pipeline.addLast(new SpeedDaemonHandler());
							
						}
					});

			ChannelFuture channelFuture = bootstrap.bind(PORT).sync();
			logger.info("Server Deamon Started at port " + PORT);

			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			System.out.println("Argh!!!. Server Exception");
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
