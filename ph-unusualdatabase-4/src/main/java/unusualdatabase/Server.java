package unusualdatabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class Server {

	public static final int PORT = 4000;

	private static Logger logger = LoggerFactory.getLogger(Server.class);

	public static void main(String[] args) {

		logger.info("Starting Unusual database Server at port " + PORT);

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true)
					.handler(new DatabaseHandler());

			ChannelFuture channelFuture = bootstrap.bind(PORT).sync();

			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			System.out.println(e);
			logger.error("Argh!!!. Server Exception");
		} finally {
			logger.error("Shutdown server");
			group.shutdownGracefully();
		}
	}
}
