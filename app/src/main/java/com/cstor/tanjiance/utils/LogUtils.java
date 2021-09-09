package com.cstor.tanjiance.utils;
import android.util.Log;

/**
 * Log统一管理类
 */
public class LogUtils {
    private LogUtils()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isDebug = true;  // 是否需要打印bug，可以在application的onCreate函数里面初始化
    private static final String TAG = "LI--";

    // 下面四个是默认tag的函数
    public static void i(String msg)
    {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg)
    {
        if (isDebug)
            Log.d(TAG, msg);
    }
    public static void is(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.i(tag, msg);
    }
    public static void e(String msg)
    {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg)
    {
        if (isDebug)
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg)
    {
        if (isDebug)
            Log.i(TAG+tag, msg);
    }

    public static void d(String tag, String msg)
    {
        if (isDebug)
            Log.i(TAG+tag, msg);

    }
    public static void e(String tag, String msg)
    {
        if (isDebug)
            Log.i(TAG+tag, msg);
    }

    public static void v(String tag, String msg)
    {
        if (isDebug)
            Log.i(TAG+tag, msg);
    }
}
