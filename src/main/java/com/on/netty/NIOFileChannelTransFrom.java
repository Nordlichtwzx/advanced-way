package com.on.netty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class NIOFileChannelTransFrom {
    public static void main(String[] args) throws IOException {
        File file = new File("E:\\a.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        File file2 = new File("E:\\c.txt");
        //首先创建一个文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        //获取读入文件流的channel
        FileChannel channel = fileInputStream.getChannel();
        //通过文件输出流获取到Channel
        FileChannel channel2 = fileOutputStream.getChannel();
        channel2.transferFrom(channel, 0, channel.size());
        channel.close();
        channel2.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
