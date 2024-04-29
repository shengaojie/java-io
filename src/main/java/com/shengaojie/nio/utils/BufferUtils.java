package com.shengaojie.nio.utils;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * @description: 缓冲区的工具类
 * @author: shengaojie
 * @create: 2024-04-28
 **/

public class BufferUtils {

    public static void printBuffer(CharBuffer buffer){
        //打印buffer
        System.out.println("\n打印charBuffer: ");
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get() + " ,");

        }
    }

    public static void printBuffer(ByteBuffer buffer){
        //打印buffer
        System.out.println("\n打印charBuffer: ");
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.print((char) buffer.get() + " ,");

        }
    }


    public static void printBufferNoFlip(CharBuffer buffer){
        //打印buffer
        System.out.println("\n打印charBuffer: ");
        //buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get() + " ,");

        }

    }



    public static void getBufferInfo(Buffer buffer){
        System.out.println("\nposition的值：" + buffer.position());
        System.out.println("limit的值：" + buffer.limit());

        System.out.println("capacity的值：" + buffer.capacity());


    }
}
