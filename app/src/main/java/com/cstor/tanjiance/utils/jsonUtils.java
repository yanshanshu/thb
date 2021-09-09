package com.cstor.tanjiance.utils;

import android.app.Activity;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class jsonUtils {
    /**
     * 保存json到本地
     * @param mActivity
     * @param filename
     * @param content
     */
    public static File dir = new File(Environment.getExternalStorageDirectory() + "/.Imageloader/json/");

    public static void saveToSDCard(Activity mActivity, String filename, String content) {
        String en = Environment.getExternalStorageState();
        //获取SDCard状态,如果SDCard插入了手机且为非写保护状态
        if (en.equals(Environment.MEDIA_MOUNTED)) {
            try {
                dir.mkdirs(); //create folders where write files
                File file = new File(dir, filename);

                OutputStream out = new FileOutputStream(file);
                out.write(content.getBytes());
                out.close();

            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {
            //提示用户SDCard不存在或者为写保护状态

        }
    }
}
