package com.on.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    /**
     * 客户端需要一个事件循环组就可以了
     *
     * @param args
     */
    public static void main(String[] args) {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            System.out.println("客户端启动了");

            try {
                ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6666).sync();
                //给关闭通道增加一个监听
                channelFuture.channel().closeFuture();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }

}
