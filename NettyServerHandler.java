package com.wpm.cloud.pool;

import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter  {
	
    static AtomicInteger count = new AtomicInteger(1);

    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
      
        ByteBuf buf = (ByteBuf)msg;  
        byte[] bytes = new byte[buf.readableBytes()];  
        buf.readBytes(bytes);  
        String message = new String(bytes, "UTF-8");
//        System.out.println("服务端收到的消息: " + message);  
        
        try {
//        	Msg m = JSON.parseObject(message, Msg.class);
        	System.out.println("服务端收到的消息---"+message);
		} catch (Exception e) {
			e.printStackTrace();
		}
//        String str = JSON.toJSONString(msg);
        //向客户端写数据  
//        String response = "hello client---客户端:"+count.getAndIncrement();  
        ByteBuf buffer = Unpooled.copiedBuffer(message.getBytes());  
        ctx.write(buffer);//写入缓冲数组  
        System.out.println("接受到" + count.getAndIncrement() + "条消息");
    }  
  
    @Override  
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {     
        System.out.println("channelReadComplete...");  
        ctx.flush();//将缓冲区数据写入SocketChannel  
    }  
  
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
        System.out.println("exceptionCaught...");  
    }  
  
    
}
