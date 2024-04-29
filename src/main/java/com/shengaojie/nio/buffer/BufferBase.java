package com.shengaojie.nio.buffer;

import com.shengaojie.nio.utils.BufferUtils;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;

/**
 * @description: buffer基础
 * @author: shengaojie
 * @create: 2024-04-28
 **/

public class BufferBase {

    public static CharBuffer charBuffer = CharBuffer.allocate(10);


    @Test
    public void testAttribute(){
        CharBuffer charBuffer = CharBuffer.allocate(10);
        System.out.println("初始化buffer：");
        System.out.println("buffer position: " + charBuffer.position());
        System.out.println("buffer limit: " + charBuffer.limit());
        System.out.println("buffer capacity: " + charBuffer.capacity());

        //设置buffer的position
        charBuffer.position(4);
        System.out.println("修改buffer的position：");
        System.out.println("buffer position: " + charBuffer.position());
        System.out.println("buffer limit: " + charBuffer.limit());
        System.out.println("buffer capacity: " + charBuffer.capacity());

        //调用mark和reset方法
        //mark方法将mark的值设置为position的值
        //但是mark属性是私有属性，没有办法直接访问，可以通过debug的方式查看
        charBuffer.mark();
        System.out.println("调用mark方法：");
        System.out.println("buffer position: " + charBuffer.position());
        System.out.println("buffer limit: " + charBuffer.limit());
        System.out.println("buffer capacity: " + charBuffer.capacity());

        //reset方法将position的值设置为mark的值
        charBuffer.reset();
        System.out.println("调用mark方法：");
        System.out.println("buffer position: " + charBuffer.position());
        System.out.println("buffer limit: " + charBuffer.limit());
        System.out.println("buffer capacity: " + charBuffer.capacity());

    }

    @Test
    public void testGet(){
        CharBuffer charBuffer = CharBuffer.wrap(new char[]{'a', 'b', 'c', 'd','e','f'});
        BufferUtils.getBufferInfo(charBuffer);

        //使用get方法获取相对position位置的元素
        //使用相对方法获取，会让position的值 + 1
        System.out.println("get()方法获取元素：" + charBuffer.get());
        BufferUtils.getBufferInfo(charBuffer);
        //使用get(int index)方法获取指定位置的元素
        //使用绝对位置获取不会影响position的值
        System.out.println("get(int index方法获取指定位置的元素：)" + charBuffer.get(3));
        //超出位置的上界会抛出异常IndexOutOfBoundsException
        //charBuffer.get(10);
    }


    @Test
    public void testPut(){
        CharBuffer charBuffer = CharBuffer.allocate(5);
        BufferUtils.getBufferInfo(charBuffer);

        //使用put()方法向buffer中添加元素
        //想用相对位置存储会让position的值 + 1
        charBuffer.put('a');
        BufferUtils.getBufferInfo(charBuffer);
        //使用put(int index)方法向buffer的指定位置添加元素
        //使用绝对位置存储不会影响position的值
        charBuffer.put(3, 'd');
        BufferUtils.getBufferInfo(charBuffer);

        /*
         给定一个叫存储hello的ByteBuffer，修改为Mellow
         */
        char[] chars = {'h', 'e', 'l', 'l', 'o','m'};
        //将一个char数组转换为byte数组
        ByteBuffer byteBuffer = ByteBuffer.wrap(new String(chars).getBytes());
        BufferUtils.getBufferInfo(byteBuffer);
        byteBuffer.put(0, (byte) 'M').put((byte) 'w');
        BufferUtils.printBuffer(byteBuffer);
        //这个buffer是通过String.getBytes来创建的，并不是直接通过chars来创建的，因此
        //不会对于buffer的修改不会影响到chars
        System.out.println(Arrays.toString(chars));

    }

    /**
     * flip()：用于从写模式切换到读模式
     * rewind()
     */
    @Test
    public void testReverse(){
        /*
         * 如果此时的缓冲区已经填满，通过get方法来获取其中的数据，需要进行如下的设置
         *   1. limit的值设置为position的值
         *   2. 将position的值置为0
         */
        CharBuffer charBuffer = CharBuffer.allocate(4);
        charBuffer.put('a').put('b').put('c').put('d');
        //在遍历之前需要进行上述的设置
        //charBuffer.limit(charBuffer.position()).position(0);
        //等价于
        //如果调用两次flip相当于实际大小变成了0，因为position和limit都变成了0
        charBuffer.flip();

        BufferUtils.getBufferInfo(charBuffer);
        //遍历charBuffer
        while (charBuffer.hasRemaining()) {
            System.out.println(charBuffer.get());
        }

        /*
        rewind方法和flip方法类似,不同之处在于：
            rewind方法不会影响limit的值，但是flip方法会
         */
        BufferUtils.getBufferInfo(charBuffer);
        charBuffer.rewind();
        BufferUtils.getBufferInfo(charBuffer);

    }


    /**
     * 1.clear()
     * 2. 遍历buffer
     */
    @Test
    public void testTraverseAndClear(){
        charBuffer.put('a').put('b').put('c').put('d');
        BufferUtils.getBufferInfo(charBuffer);
        //需要先先position和limit的位置
        charBuffer.flip();
        //方法1：通过hasRemaining
        while (charBuffer.hasRemaining()) {
            System.out.println(charBuffer.get());
        }
        System.out.println("------------我是分割线--------------");
        //方法2：通过remaining
        charBuffer.flip();
        int count = charBuffer.remaining();
        for (int i = 0; i < count; i++) {
            System.out.println(charBuffer.get());
        }

        //使用clear方法，将缓冲区置为空，该方法用于从读模式切换回写模式
        //1. position的值设置为0
        //2. limit的值设置为capacity的值
        charBuffer.clear();
        BufferUtils.getBufferInfo(charBuffer);
    }


    /**
     * compact():
     *  如果每次读取的数据并不是缓冲区中的所有的数据，在读取完毕之后，想要继续写入。
     *  因此compact方法会：
     *      1. 将limit的值设置为capacity的值
     *      2. 将为读取到的值移动到buffer的开头
     *      3. position的值设置为为读取元素的个数
     */
    @Test
    public void testCompact(){
        charBuffer.put('a')
                    .put('b')
                    .put('c')
                    .put('d')
                    .put('e');
        //使用flip方法，准备释放
        charBuffer.flip();
        //释放1个元素，此时position = 1
        charBuffer.get();
        BufferUtils.getBufferInfo(charBuffer);

        //position的值设置为未读取元素的个数，这里是4
        charBuffer.compact();
        BufferUtils.getBufferInfo(charBuffer);


    }


    /**
     * mark():能使得缓冲区记住一个位置之后，用于在合适的时候返回。在执行该方法之前，
     *        buffer中是没有标记的
     * reset():将函数的position的值设置为mark的值，如果没有执行过mark方法，
     *         会抛出InvalidMarkException
     *
     */
    @Test
    public void testMark(){
        charBuffer.put('h').put('e').put('l').put('l').put('o').put('w');
        charBuffer.position(2).mark().position(4);
        //此时mark的值为2，position的值为4
        BufferUtils.getBufferInfo(charBuffer);
        charBuffer.reset();
        //此时position的值为2
        BufferUtils.getBufferInfo(charBuffer);
    }


    /**
     * 使用equals和compareTo进行buffer之间的比较
     * equals()，使用equals比较buffer相当的充要条件是：
     *      1. buffer的类型必须相同
     *      2. buffer的剩余容量（limit - position）必须相同
     *      3. buffer中剩余的元素必须相同,也就是调用get()方法的返回结果必须一致
     *
     * compareTo:用于逐个比较从position位置开始到limit位置的元素，不需要两个buffer中的剩余元素个数相同
     *           如果第一个相等，则开始比较第二个，以此类推。
     *      如buffer.compareTo(buffer1),返回结果如下：
     *      负数：buffer小于buffer1
     *      0：buffer等于buffer1
     *      正数：buffer大于buffer1
     */
    @Test
    public void testCompare(){
        CharBuffer charBuffer1 = CharBuffer.allocate(5);
        charBuffer1.put('a').put('b').put('c').put('d').put('e');
        charBuffer1.flip();
        CharBuffer charBuffer2 = CharBuffer.allocate(5);
        charBuffer2.put('a').put('b').put('c').put('d').put('e');
        charBuffer2.flip();
        //虽然两个buffer中的元素是相同的，但是如果剩余元素不一样还是判定为不一样
        //charBuffer2.position(1);
        //比较前，先查看两个buffer中的属性值
        BufferUtils.getBufferInfo(charBuffer1);
        BufferUtils.getBufferInfo(charBuffer2);
        System.out.println("判断两个buffer是否相等：" + charBuffer1.equals(charBuffer2));
        //查看一下执行完equals方法会不会改变buffer的属性值
        BufferUtils.getBufferInfo(charBuffer1);
        BufferUtils.getBufferInfo(charBuffer2);
        System.out.println("--------------我是分界线---------------");

        //定义charBuffer3，只有最后一个元素和charBuffer1中不相同
        CharBuffer charBuffer3 = CharBuffer.allocate(5);
        charBuffer3.put('a').put('b').put('c').put('d').put('f');
        charBuffer3.flip();
        BufferUtils.getBufferInfo(charBuffer1);
        BufferUtils.getBufferInfo(charBuffer3);
        System.out.println("使用compareTo方法进行比较：" + charBuffer1.compareTo(charBuffer3));

    }


    /**
     * 批量移动使用get或者put方法：
     *     CharBuffer get (char [] dst)：可供从缓冲区到数组进行的数据复制使用
     *     CharBuffer get (char [] dst, int offset, int length)
     *
     *     CharBuffer put (char[] src)：可供从数组到缓冲区进行的数据复制使用
     *     CharBuffer put (char [] src, int offset, int length)
     *     CharBuffer put (CharBuffer src)
     *     CharBuffer put (String src, int start, int end)
     */
    @Test
    public void testBatchMove(){
        char[] char1s = {'a', 'b', 'c', 'e', 'f'};
        //***通过该方法创建的buffer的大小被固定了
        charBuffer.put(char1s);
        //此时的position的值为5
        BufferUtils.getBufferInfo(charBuffer);

        char[] char2s = new char[5];
        charBuffer.flip();
        charBuffer.get(char2s);
        System.out.println("char2s: " + Arrays.toString(char2s));

        //对于String类型，提供了便利的put(String str)方法
        String str = "hello world";
        CharBuffer charBuffer1 = CharBuffer.allocate(15);
        charBuffer1.put(str);
        BufferUtils.getBufferInfo(charBuffer1);

        BufferUtils.getBufferInfo(charBuffer1);
        BufferUtils.printBuffer(charBuffer1);
    }

    /**
     * 根据数组和缓冲区的大小，如果想要将buffer中的数据移动到数组中，需要采取不同的方法：
     */
    @Test
    public void testMoveDataToArray(){
        CharBuffer charBuffer1 = CharBuffer.allocate(10);
        for (int i = 0; i < 10; i++) {
            charBuffer1.put((char) (65 + i));
        }
        charBuffer1.flip();
        char[] chars = new char[5];
        //如果缓冲区的大小大于数组
        //此时通过循环，不断往往数组中赋值，每次赋值完成都进行数据的处理，再进行下一次赋值
        while (charBuffer.hasRemaining()) {
            int length = Math.min(charBuffer1.remaining(), chars.length);
            charBuffer1.get(chars, 0,length);
            //处理数据
            //processData(chars, length);
        }

        //如果缓冲区的大小小于数组,此时应该根据缓冲区的剩余数量来给数组赋值
        int remaining = charBuffer1.remaining();
        charBuffer1.get(chars, 0 ,remaining);
        //处理数据
        //processData(chars, remaining)
    }

}
