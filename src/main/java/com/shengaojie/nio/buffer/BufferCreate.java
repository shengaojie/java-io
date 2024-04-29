package com.shengaojie.nio.buffer;


import com.shengaojie.nio.utils.BufferUtils;
import org.junit.Test;

import java.nio.CharBuffer;
import java.util.Arrays;

/**
 * @description: 创建缓冲区
 * @author: shengaojie
 * @create: 2024-04-28
 **/

public class BufferCreate {

    @Test
    public void test(){
        // 使用allocate(int capacity)，创建一个指定大小的缓冲区
        // 创建一个position为0，limit为10，capacity为10的字节缓冲区
        // 使用该方法底层会帮我们创建一个长度为100的char数组，该数组就是缓冲区的数据存储容器
        CharBuffer charBuffer = CharBuffer.allocate(100);
        BufferUtils.printBuffer(charBuffer);

        /*
        1.注意使用wrap和allocate方法创建缓冲区都是间接的
        2.wrap方法创建的缓冲区，修改数组或者是修改缓冲区对方都会影响对方
         */
        //使用warp(char[] array)
        char[] chars = new char[100];
        chars[3] = 'd';
        System.out.println("\n初始化char数组：" + Arrays.toString(chars));
        CharBuffer charBuffer1 = CharBuffer.wrap(chars);
        //查看此时的position、limit、capacity
        BufferUtils.getBufferInfo(charBuffer1);
        //charBuffer1.put('a');
        System.out.println("使用wrap建立该char数组的缓冲区：" + Arrays.toString(chars));
        BufferUtils.printBuffer(charBuffer1);
        //创建一个position为12，limit为54的字节缓冲区
        CharBuffer charBuffer2 = CharBuffer.wrap(chars, 0, 42);
        BufferUtils.printBuffer(charBuffer2);

        //通过hasArray方法判断一个缓冲区是否有一个可存取的备份数组
        //通过allocate和wrap方法创建的缓冲区的hasArray方法都会返回true
        System.out.println("\ncharBuffer是否有可存取的备份数组：" + charBuffer.hasArray());
        System.out.println("charBuffer1是否有可存取的备份数组：" + charBuffer1.hasArray());
        System.out.println("charBuffer2是否有可存取的备份数组：" + charBuffer2.hasArray());


        //array()方法返回这个缓冲区锁使用的数组的引用
        System.out.println("charBuffer 引用数组为：" + charBuffer.array());
        System.out.println("charBuffer1 引用数组为：" + charBuffer1.array());
        System.out.println("charBuffer2 引用数组为：" + charBuffer2.array());
        System.out.println("charBuffer1 和 charBuffer2引用的是否是同一个数组：" + (charBuffer1.array().equals(charBuffer2.array())));

        //arrayOffset方法返回缓冲区数据在数组中存储的开始的偏移量
        //基于allocate和warp方法是实现的缓冲区中的arrayOffset方法返回0
        System.out.println("charBuffer 数组偏移量为：" + charBuffer.arrayOffset());
        System.out.println("charBuffer1 数组偏移量为：" + charBuffer1.arrayOffset());
        System.out.println("charBuffer2 数组偏移量为：" + charBuffer2.arrayOffset());

        //wrap(CharSequence csq)和wrap(CharSequence csq,int start,int end)是CharBuffer特有的方法
        CharBuffer charBuffer3 = CharBuffer.wrap("hello java nio");
        //charBuffer3.position("hello java nio".length());
        BufferUtils.getBufferInfo(charBuffer3);
        BufferUtils.printBufferNoFlip(charBuffer3);
        BufferUtils.getBufferInfo(charBuffer3);
    }

    public static void main(String[] args) {

    }



}
