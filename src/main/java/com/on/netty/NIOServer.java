package com.on.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) throws IOException {
        //首先生成一个ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //创建一个Selector
        Selector selector = Selector.open();
        //服务器端绑定端口
        serverSocketChannel.bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //因为ServerSocketChannel也是一个channel，所以也需要注册到selector中
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            //每次阻塞一秒
            int select = selector.select(1000);
            if (select == 0) {
                System.out.println("服务器等待了一秒钟");
                continue;
            }
            //说明该Selector监听的channel有事件发生
            //获取发生事件的SelectionKey
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            //遍历发生事件的SelectionKey
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //发生了连接事件
                if (key.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("一个客户端连接进来了");
                }
                //发生炉读事件
                if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    socketChannel.read(byteBuffer);
                    System.out.println("来自客户端的消息" + new String(byteBuffer.array()));
                }
                //在最后需要移除当前SelectionKey，为了避免每次连接导致的重复SelectionKey
                iterator.remove();
            }
        }
    }

}
