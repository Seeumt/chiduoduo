package com.cumt.chiduoduo.utils;

import java.util.Random;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：生成唯一的主键工具类
 *
 */
public class KeyUtil {
    /*生成唯一的主键
    * 格式：时间+六位随机数
    * */
    //加入synchronized，因为尽管已经以毫秒级别来，但是多线程的并发仍会出现重复
    public static synchronized String genUniqueKey(){
        Random random=new Random();
        Integer number = random.nextInt(900000) + 100000;
        return System.currentTimeMillis()+String.valueOf(number);
    }
}
