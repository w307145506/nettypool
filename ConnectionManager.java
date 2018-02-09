package com.wpm.cloud.pool;

import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingQueue;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public final class ConnectionManager {
	
	private final static ConnectionManager connectionManager = new ConnectionManager(10);
	
	private static LinkedBlockingQueue<Channel> concurrentLinkedQueue;
	
	private final EventLoopGroup group = new NioEventLoopGroup();
	private final Bootstrap strap = new Bootstrap();
	private InetSocketAddress addr1 = new InetSocketAddress("localhost", 8888);

	
	public ConnectionManager(int inticount) {
	   strap.group(group).channel(NioSocketChannel.class)
	   .option(ChannelOption.TCP_NODELAY, true)
       .option(ChannelOption.SO_KEEPALIVE, true) 
	   .handler(new NettyClientChildChannelHandler());
	  
		try {
			concurrentLinkedQueue = new LinkedBlockingQueue<Channel>(100);
			inti(inticount, strap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public  void inti(int inticount, Bootstrap strap) throws Exception {
		for (int i = 0; i < inticount; i++) {
			Channel channel = strap.connect(addr1).sync().channel();
			concurrentLinkedQueue.put(channel);
		}
	}
	
	
	public  Channel getConnection() throws Exception {
		return concurrentLinkedQueue.take();
	}
	
	public  LinkedBlockingQueue<Channel> getQueue(){
		return concurrentLinkedQueue;
	}
	
	
	public static ConnectionManager getSingle() {
		return connectionManager;
	}
	
   private class NettyClientChildChannelHandler extends ChannelInitializer<SocketChannel>{  
        @Override  
        protected void initChannel(SocketChannel ch) throws Exception {  
  
            ch.pipeline().addLast(new NettyClientHandler());  
        }  
    }  
}
