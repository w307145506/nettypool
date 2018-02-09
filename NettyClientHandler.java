package com.wpm.cloud.pool;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter{
	
//	    private final ByteBuf byteBuf;  
//     
//	    public NettyClientHandler() {  
//	        byte[] bytes = "i love you".getBytes();  
//	        byteBuf = Unpooled.buffer(bytes.length);  
//	        byteBuf.writeBytes(bytes);//写入buffer   
//	    }  
	      
	      
	    @Override  
	    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
	        //向服务端发送数据  
//	        ctx.writeAndFlush(byteBuf); 
	    }  
	  
	    @Override  
	    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { 
	        //读取服务端发过来的数据  
	        ByteBuf buf = (ByteBuf)msg;  
	        byte[] bytes = new byte[buf.readableBytes()];  
	        buf.readBytes(bytes);  
	        String message = new String(bytes, "UTF-8"); 
	        Msg msgObj = (Msg) JSON.parseObject(message,Msg.class);
	        // 释放channel
	        Pool.release(msgObj.getUuId());
	        System.out.println("客户端收到的消息： " + message);  
	    }  

}
