package com.on.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {

    public static void main(String[] args) throws IOException {
        //生成一个SocketChannel
        SocketChannel socketChannel = SocketChannel.open();
        //设置为非阻塞
        socketChannel.configureBlocking(false);
        //连接服务器
        boolean connect = socketChannel.connect(new InetSocketAddress("127.0.0.1", 6666));
        if (!connect) {
            //检测是否连接成功
            while (!socketChannel.finishConnect()) {
                //如果没有连接成功
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他事情");
            }
        }
        String messag = "hello";
        ByteBuffer byteBuffer = ByteBuffer.wrap(messag.getBytes());
        //向服务端发送消息
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
