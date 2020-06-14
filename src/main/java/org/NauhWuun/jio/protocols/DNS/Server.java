package org.NauhWuun.jio.protocols.DNS;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.dns.*;

import java.util.ArrayList;
import java.util.List;

public class Server extends SimpleChannelInboundHandler<DatagramDnsQuery>
{
	private final NioEventLoopGroup group = new NioEventLoopGroup();
	private Bootstrap bootstrap = new Bootstrap();

	private List<String> dnsQuery = new ArrayList<>();

	public Server(int port) {
		try {
			bootstrap.group(group).channel(NioDatagramChannel.class)
					.handler(new ChannelInitializer<NioDatagramChannel>() {
						@Override
						protected void initChannel(NioDatagramChannel nioDatagramChannel) {
							nioDatagramChannel.pipeline().addLast(new DatagramDnsQueryDecoder());
							nioDatagramChannel.pipeline().addLast(new DatagramDnsResponseEncoder());
						}
					}).option(ChannelOption.SO_BROADCAST, true);

			ChannelFuture future = bootstrap.bind(port).sync();
			future.channel().closeFuture().sync();

		} catch (InterruptedException e) {
			e.getMessage();
		} finally {
			group.shutdownGracefully();
		}
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, DatagramDnsQuery query) {
		DatagramDnsResponse response = new DatagramDnsResponse(query.recipient(), query.sender(), query.id());

		try {
			DefaultDnsQuestion question = query.recordAt(DnsSection.QUESTION);
			response.addRecord(DnsSection.QUESTION, question);

			System.out.println("Dns Name：" + question.name());

			if (! dnsQuery.contains(question.name())) {
				dnsQuery.add(question.name());
			}

			ByteBuf buf = Unpooled.directBuffer(8192);;
			DefaultDnsRawRecord queryAnswer = new DefaultDnsRawRecord(question.name(),DnsRecordType.A, 10, buf);
			response.addRecord(DnsSection.ANSWER, queryAnswer);
		} catch (Exception e) {
			e.getMessage();
		} finally {
			ctx.writeAndFlush(response);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
	}
}