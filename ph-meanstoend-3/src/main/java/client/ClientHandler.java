package client;

import dto.QueryResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		QueryResponse response = (QueryResponse)msg;
		System.out.println(response.getResponse());
	}
}
