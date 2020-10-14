package com.on.netty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * NIO读文件
 *
 * @author Nordlicht
 */
public class NIOFileChannelRead {

    public static void main(String[] args) throws IOException {
        File file = new File("E:\\a.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        //获取读入文件流的channel
        FileChannel channel = fileInputStream.getChannel();
        //为读入的数据分配空间
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        /**
         * Reads a sequence of bytes from this channel into the given buffer.
         *
         * <p> Bytes are read starting at this channel's current file position, and
         * then the file position is updated with the number of bytes actually
         * read.  Otherwise this method behaves exactly as specified in the {@link
         * ReadableByteChannel} interface. </p>
         */
        //将channel中的数据读入到ByteBuffer中
        channel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
