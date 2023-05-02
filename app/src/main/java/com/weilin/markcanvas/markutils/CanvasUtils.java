package com.weilin.markcanvas.markutils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * CanvasUtils 画布公用工具类
 */
public class CanvasUtils {

    /**
     * retChinese Json中文乱码修复
     * 2023-04-21
     * @param str UTF8文本
     * @return 中文字符串
     */
    static String retChinese(@NonNull String str){
        byte[] uft8;
        try {
            uft8 = str.getBytes(("ISO_8859_1"));
        } catch (UnsupportedEncodingException e) {
            return "";
        }
        try {
            str = new String(uft8, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
        return str;
    }

    /**
     * getImageFromAssetsFile 获取Assets或外部存储文件图片(APP目录)
     * 2023-04-19
     * @param perName  前缀目录 (结尾不要有"/")
     * @param fileName 文件名称 (结尾不要有.png) 只支持png文件
     * @param isFile   是否在Assets目录内
     * @param context  上下文
     * @return Bitmap资源
     */
    static Bitmap getImageFromAssetsFile(String perName, String fileName, boolean isFile, @NonNull Context context) {
        Bitmap image;
        AssetManager am = context.getResources().getAssets();
        try {
            if (!isFile) {
                InputStream is = am.open((perName + "/" + fileName + ".png"));
                image = BitmapFactory.decodeStream(is);
                is.close();
            } else {
                image = BitmapFactory.decodeFile(fileName);
            }
        } catch (IOException e) {
            //e.printStackTrace();
            image = null;
        }
        return image;
    }

    /**
     * retPercentWidth 相对位置百分比宽度转换
     * 2023-04-19
     * @param canvasWidth 画布宽度
     * @param percent     指定相对百分比
     * @return 百分比宽度
     */
    static float retPercentWidth(int canvasWidth, float percent) {
        return canvasWidth * percent;
    }

    /**
     * retPercentHeight 相对位置百分比高度转换
     * 2023-04-19
     * @param canvasHeight 画布高度
     * @param percent      指定相对百分比
     * @return 百分比高度
     */
    static float retPercentHeight(int canvasHeight, float percent) {
        return canvasHeight * percent;
    }

    /**
     * retCenterCanvasX 获取Canvas的居中宽度位置
     * 2023-04-19
     * @param width Canvas宽度
     * @return Width居中位置
     */
    static int retCenterCanvasX(int width) {
        return width / 2;
    }

    /**
     * retCenterCanvasY 获取Canvas的居中高度位置
     * 2023-04-19
     * @param height Canvas高度
     * @return Height居中位置
     */
    static int retCenterCanvasY(int height) {
        return height / 2;
    }
}
