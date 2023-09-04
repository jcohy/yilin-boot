package com.yilin.reactive.utils.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.AbstractReferenceCountedByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.AbstractEventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright: Copyright (c) 2023 <a href="https://www.jcohy.com" target="_blank">jcohy.com</a>
 *
 * <p> Description:
 *
 * @author jiac
 * @version 2023.0.1 2023/7/21:11:36
 * @since 2023.0.1
 */
public class NettyNetworkFileSync implements NetworkFileSync {

	private static void shutdown(List<NioEventLoopGroup> groups) {
		groups.forEach(AbstractEventExecutorGroup::shutdownGracefully);
	}

	@Override
	public void start(int port, Consumer<byte[]> bytesHandler) throws IOException, InterruptedException {

		var bossEventLoopGroup = new NioEventLoopGroup(1);
		var nioEventLoopGroup = new NioEventLoopGroup();
		var serverHandler = new NettyNetworkFileSyncServerHandler(bytesHandler);
		try {
			var serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossEventLoopGroup, nioEventLoopGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 100).handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) {
							var channelPipeline = ch.pipeline();
							channelPipeline.addLast(serverHandler);
						}
					});
			var channelFuture = serverBootstrap.bind(port).sync();
			channelFuture.channel().closeFuture().sync();
		} //
		finally {
			shutdown(List.of(bossEventLoopGroup, nioEventLoopGroup));
		}
	}
}

@ChannelHandler.Sharable
class NettyNetworkFileSyncServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger log = LoggerFactory.getLogger(NettyNetworkFileSyncServerHandler.class);

	private final Consumer<byte[]> consumer;

	private final AtomicReference<ByteArrayOutputStream> byteArrayOutputStream = new AtomicReference<>(
			new ByteArrayOutputStream());

	NettyNetworkFileSyncServerHandler(Consumer<byte[]> consumer) {
		this.consumer = consumer;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
		if (msg instanceof AbstractReferenceCountedByteBuf buf) {
			var bytes = new byte[buf.readableBytes()];
			buf.readBytes(bytes);
			this.byteArrayOutputStream.get().write(bytes);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws IOException {
		var baos = this.byteArrayOutputStream.get();
		if (null != baos) {
			try {
				var bytes = baos.toByteArray();
				if (bytes.length != 0) {
					this.consumer.accept(bytes);
				}
				// we've read the bytes,
				// time to reset for a new request
				this.byteArrayOutputStream.set(new ByteArrayOutputStream());
			} //
			finally {
				ctx.flush();
				baos.close();
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error("oh no!", cause);
		ctx.close();
	}

}