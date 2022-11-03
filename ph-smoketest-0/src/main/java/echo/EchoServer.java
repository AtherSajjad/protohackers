package echo;

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

public class EchoServer {

	public static final int PORT = 4000;

	private static Logger logger = LoggerFactory.getLogger(EchoServer.class);

	public static void main(String[] args) {

		logger.info("Starting Echo Server at port " + PORT);

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
							pipeline.addLast(new EchoServerHandler());
						}
					});

			ChannelFuture channelFuture = bootstrap.bind(PORT).sync();

			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			logger.error("Argh!!!. Server Exception");
		} finally {
			logger.error("Shutdown server");
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
