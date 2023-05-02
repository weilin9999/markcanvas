package com.weilin.markcanvas.markutils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import androidx.annotation.NonNull;

import com.alibaba.fastjson2.JSONObject;

import java.util.Objects;

public class DrawMask {

    /**
     * canvasDrawMask 蒙版绘制过程
     * 2023-05-02
     * @param canvas 画布
     * @param mPaint 画笔
     * @param object JSON数据
     * @param canvasWidth 画布宽度
     * @param canvasHeight 画布高度
     * @param context 上下文
     * @param mask_mode 蒙版模式
     */
    public static void canvasDrawMask(@NonNull Canvas canvas, Paint mPaint, @NonNull JSONObject object,
                                      int canvasWidth, int canvasHeight, Context context,
                                      @NonNull String mask_mode){
        if (Objects.equals(object.getString(("type_id")), ("text"))){
            DrawText.canvasDrawTextMaskElement(canvas, object, canvasWidth, canvasHeight,context);
        }else if (Objects.equals(object.getString(("type_id")), ("image"))){
            DrawBitmap.canvasDrawImageMaskElement(canvas, object, canvasWidth, canvasHeight,context);
        }else if (Objects.equals(object.getString(("type_id")), ("circle"))){
            DrawCircle.canvasDrawCircleMaskElement(canvas, object, canvasWidth, canvasHeight);
        }else if (Objects.equals(object.getString(("type_id")), ("rect"))){
            DrawRect.canvasDrawRectMaskElement(canvas, object, canvasWidth, canvasHeight,context);
        }

        //使用SRC_IN
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_OUT;
        switch (mask_mode){
            case "SRC_OVER":
                mode = PorterDuff.Mode.SRC_OVER;
                break;
            case "DST_OVER":
                mode = PorterDuff.Mode.DST_OVER;
                break;
            case "SRC_IN":
                mode = PorterDuff.Mode.SRC_IN;
                break;
            case "DST_IN":
                mode = PorterDuff.Mode.DST_IN;
                break;
            case "SRC_OUT":
                break;
            case "DST_OUT":
                mode = PorterDuff.Mode.DST_OUT;
                break;
            case "SRC_ATOP":
                mode = PorterDuff.Mode.SRC_ATOP;
                break;
            case "XOR":
                mode = PorterDuff.Mode.XOR;
                break;
            case "DARKEN":
                mode = PorterDuff.Mode.DARKEN;
                break;
            case "LIGHTEN":
                mode = PorterDuff.Mode.LIGHTEN;
                break;
            case "MULTIPLY":
                mode = PorterDuff.Mode.MULTIPLY;
                break;
            case "SCREEN":
                mode = PorterDuff.Mode.SCREEN;
                break;
            case "ADD":
                mode = PorterDuff.Mode.ADD;
                break;
            case "OVERLAY":
                mode = PorterDuff.Mode.OVERLAY;
                break;
        }
        mPaint.setXfermode(new PorterDuffXfermode(mode));
    }
}
