package com.wpm.cloud.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.FutureTask;

import io.netty.channel.Channel;

public final class Pool {
	
	
	private static ConcurrentMap<String , FutureTask<ConnectionManager>> map = new ConcurrentHashMap<String , FutureTask<ConnectionManager>>();
	
	private static ConcurrentMap<String , Channel> activeChannelMap = new ConcurrentHashMap<String , Channel>();
	
	
	public static Channel getChannel(String key) throws Exception {
		
		FutureTask<ConnectionManager> cFuture = map.get(key);
		
		if(cFuture != null) {
			return cFuture.get().getConnection();
		}else {
			
			Callable<ConnectionManager> callable = new Callable<ConnectionManager>() {

				@Override
				public ConnectionManager call() throws Exception {
					return createConnectionManager();
				}
				
			};
			
			FutureTask<ConnectionManager> cFuture2 = new FutureTask<ConnectionManager>(callable);
			
			cFuture = map.putIfAbsent(key, cFuture2);
			
			if(cFuture == null) {
				cFuture = cFuture2;
				cFuture.run();
			}
			
			return cFuture.get().getConnection();
		}
		
	}
	
	
	private static ConnectionManager createConnectionManager()  throws Exception{
		return ConnectionManager.getSingle();
	}
	
	public static void release(String channelId) throws Exception {
		// 放回队列中
		ConnectionManager.getSingle().getQueue().put(activeChannelMap.get(channelId));
		// 移除channel,避免内存过大
		activeChannelMap.remove(channelId);
	}

	public  static void putChannel(String channelId, Channel channel) {
		 activeChannelMap.putIfAbsent(channelId, channel);
	}

	
}

