package unusualdatabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

@Sharable
public class DatabaseHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	private static Logger logger = LoggerFactory.getLogger(DatabaseHandler.class);

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		System.out.println(msg.content().toString(CharsetUtil.UTF_8));
	}

}
