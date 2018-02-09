package com.wpm.cloud.pool;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class PoolTest {
	
	public static void main(String[] args) throws Exception{
		
		 ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
		 
		 for (int i = 0; i < 5000; i++) {
			 fixedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						 Channel channel = Pool.getChannel("连接");
						 String uuid = UUID.randomUUID().toString();
						 // 放入channel到map中
						 Pool.putChannel(uuid, channel);
						 Msg msg = new Msg();
						 msg.setUuId(uuid);
						 msg.setMsg(uuid);
						 String str = JSON.toJSONString(msg);
						 byte[] bytes = str.getBytes(); 
						 channel.writeAndFlush(Unpooled.buffer(bytes.length).writeBytes(bytes));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		
	}
}
