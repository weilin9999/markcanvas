package com.weilin.markcanvas.markutils;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class JsonFileUtils {

    /**
     * readJsonFile 获取Json文件内容
     * @param filePath 文件位置
     * @return 返回String字符串
     */
    @NonNull
    public static String readJsonFile(String filePath, Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File file = new File(filePath);
            InputStream in;
            if(context != null){
                AssetManager am = context.getResources().getAssets();
                in = am.open(filePath);
            }else{
                in = Files.newInputStream(file.toPath());
            }
            int templates;
            while ((templates = in.read()) != -1) {
                stringBuilder.append((char) templates);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
