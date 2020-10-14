package com.on.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.net.SocketAddress;
import java.util.ArrayList;

/**
 * @author Nordlicht
 * 自定义一个Handler，但是要继承netty提供的某个HandlerAdapter
 */
public class NettySeverHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        System.out.println("客户端是" + socketAddress);
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发来的消息" + byteBuf.toString(CharsetUtil.UTF_8));
        //通过这一步操作可以用户自定义的将任务加入到taskQueue中，可以异步执行
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("我等了十秒钟");
            }
        });
        ArrayList<Object> objects = new ArrayList<>();
        objects.stream().distinct();

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello 客户端", CharsetUtil.UTF_8);
        ctx.writeAndFlush(byteBuf);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
