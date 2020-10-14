package com.on.netty;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * NIO写文件
 *
 * @author Nordlicht
 */
public class NIOFileChannelWrite {
    public static void main(String[] args) throws IOException {
        String message = "hello 我的妈呀";
        File file = new File("E:\\a.txt");
        //首先创建一个文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        //通过文件输出流获取到Channel
        FileChannel channel = fileOutputStream.getChannel();
        //分配一个ByteBuffer，用来存放数据
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        //将字符串转换为byte数组，放入到ByteBuffer中
        allocate.put(message.getBytes());
        //因为之前是写入ByteBuffer，但是后面需要从ByteBuffer中读出数据写入到Channel中，所以一定要flip
        allocate.flip();
        /**
         * Writes a sequence of bytes to this channel from the given buffer.
         *
         * <p> Bytes are written starting at this channel's current file position
         * unless the channel is in append mode, in which case the position is
         * first advanced to the end of the file.  The file is grown, if necessary,
         * to accommodate the written bytes, and then the file position is updated
         * with the number of bytes actually written.  Otherwise this method
         * behaves exactly as specified by the {@link WritableByteChannel}
         * interface. </p>
         */
        //将ByteBuffer中的数据写入到管道中
        channel.write(allocate);
        fileOutputStream.close();
    }
}
