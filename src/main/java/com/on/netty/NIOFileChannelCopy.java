package com.on.netty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * 使用NIO实现边读边写
 */
public class NIOFileChannelCopy {

    public static void main(String[] args) throws IOException {
        File file = new File("E:\\a.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        File file2 = new File("E:\\b.txt");
        //首先创建一个文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        //获取读入文件流的channel
        FileChannel channel = fileInputStream.getChannel();
        //为读入的数据分配空间
        ByteBuffer byteBuffer = ByteBuffer.allocate(3);
        while (true) {
            //这里需要进行复位操作，如果不执行这个操作，执行到后面以后position和limit相等会导致读出的值一直为0，就会造成死循环
            byteBuffer.clear();
            int read = channel.read(byteBuffer);
            System.out.println(read);
            if (read == -1) {
                break;
            }
            byteBuffer.flip();
            //通过文件输出流获取到Channel
            FileChannel channel2 = fileOutputStream.getChannel();
            channel2.write(byteBuffer);
        }
        fileInputStream.close();
        fileOutputStream.close();
        List list = new LinkedList();
    }
}
