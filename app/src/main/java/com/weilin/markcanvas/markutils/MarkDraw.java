package com.weilin.markcanvas.markutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * MarkDraw 组件MarkDrawCanvas （自定义VIEW)
 */
public class MarkDraw extends View {

    private JSONArray jsonGlobal; // 处理过的数据对象
    private boolean loadConfig = false;
    private final HashMap<String, PositionEntity> posList = new HashMap<>(); // 按顺序存储每个Element的属性
    private final HashMap<String, JSONObject> maskElements = new HashMap<>(); // 保存MaskElement的参数

    public MarkDraw(Context context) {
        super(context);
    }

    public MarkDraw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public MarkDraw(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    // 开始绘制图
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || !loadConfig) return;

        // 开辟线程单独处理Draw
        ExecutorService singleThreadPool = Executors.newFixedThreadPool((2));
        Future<String> task = singleThreadPool.submit(new startDrawIng(canvas));
        try {
            String timeData = task.get();//阻塞等待结果
            System.out.println(timeData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class startDrawIng implements Callable<String> {
        private final Canvas canvas;

        public startDrawIng(Canvas canvas) {
            this.canvas = canvas;
        }

        @Override
        public String call() {
            try {
                int canvasWidth = getWidth(); // 画布宽度
                int canvasHeight = getHeight(); // 画布高度
                // 清空Element初始化
                posList.clear();
                // Josn 数据处理
                for (int i = 0; i < jsonGlobal.size(); i++) {
                    JSONObject object = jsonGlobal.getJSONObject(i);
                    boolean display = Objects.equals(object.getString(("display")), ("none"));

                    if (Objects.equals(object.getString(("type_id")), ("image"))){
                        try {
                            // 使用Try安全处理 === BitMap图像绘制
                            PositionEntity position = DrawBitmap.canvasDrawImageElement(canvas,object,canvasWidth,canvasHeight,display,getContext(),posList,maskElements);
                            posList.put(object.getString(("element_id")),position);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else if (Objects.equals(object.getString(("type_id")), ("text"))){
                        try {
                            // 使用Try安全处理 === Text文本绘制
                            String context_id = object.getString(("context_id")) == null ? "":object.getString(("context_id"));
                            PositionEntity position = DrawText.canvasDrawTextElement(canvas,object,canvasWidth,canvasHeight,display,getContext(),posList);
                            posList.put(object.getString(("element_id")),position);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else if (Objects.equals(object.getString(("type_id")), ("rect"))){
                        try {
                            // 使用Try安全处理 === Rect 绘制矩形
                            PositionEntity position = DrawRect.canvasDrawRectElement(canvas,object,canvasWidth,canvasHeight,display,getContext(),posList,maskElements);
                            posList.put(object.getString(("element_id")),position);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else if (Objects.equals(object.getString(("type_id")), ("circle"))){
                        try {
                            // 使用Try安全处理 === Circle 绘制圆形
                            PositionEntity position = DrawCircle.canvasDrawCircleElement(canvas,object,canvasWidth,canvasHeight,display,getContext(),posList,maskElements);
                            posList.put(object.getString(("element_id")),position);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                Looper.prepare();
                e.printStackTrace();
            }
            return "done";
        }
    }

    /**
     * drawSet 绘制画布
     * 2023-04-19
     * @param jsonStr Json数据文本
     * @param showContent 显示模式 0:默认模式 1:仅标题 2:仅内容
     * @param showDays 显示详细年月日 0:默认只显示数字 1:显示详细的年月日
     */
    public void drawSet(String jsonStr, int showContent, int showDays) {
        loadConfig = true;
        jsonGlobal = coverLocalConfig(jsonStr,(showContent == 1 ? "mode_only_show_title" : showContent == 2 ? "mode_only_show_content" : ""),
                (showDays == 1 ? "mode_show_detail_days" : ""));
        loadMaskElements(jsonStr);
        reSetDraw();
    }

    /**
     * coverLocalConfig 覆盖Json配置
     * 2023-04-24
     * @param jsonData 原始Json字符串
     * @param modeStr 显示模式
     * @param modeDays 显示详细年月日
     * @return JSONArray
     */
    @Nullable
    private JSONArray coverLocalConfig(String jsonData, String modeStr, String modeDays){
        try {
            // Json 数据读取
            JSONObject data = JSON.parseObject(jsonData);

            // 获取组件的集合
            JSONArray data_array = null;
            JSONArray data_days_array = null;
            if (Objects.equals(modeStr, ("mode_only_show_title")) || modeStr.equals("mode_only_show_content")){
                data_array = data.getJSONArray(modeStr);
            }
            if (Objects.equals(modeDays, ("mode_show_detail_days"))){
                data_days_array = data.getJSONArray(modeDays);
            }
            if (data_array == null && data_days_array == null){
                return data.getJSONArray(("element"));
            }
            // 获取组件的集合
            JSONArray data_array_element = data.getJSONArray(("element"));
            if (data_array != null){
                for (int i = 0; i < data_array.size(); i++) {
                    JSONObject object_cover = data_array.getJSONObject(i);
                    for (int j = 0; j < data_array_element.size(); j++) {
                        JSONObject object_element = data_array_element.getJSONObject(j);
                        if (Objects.equals(object_element.getString(("element_id")), object_cover.getString(("element_id")))){
                            object_element.putAll(object_cover);
                        }
                    }
                }
            }
            if (data_days_array != null){
                for (int i = 0; i < data_days_array.size(); i++) {
                    JSONObject object_cover = data_days_array.getJSONObject(i);
                    for (int j = 0; j < data_array_element.size(); j++) {
                        JSONObject object_element = data_array_element.getJSONObject(j);
                        if (Objects.equals(object_element.getString(("element_id")), object_cover.getString(("element_id")))){
                            object_element.putAll(object_cover);
                        }
                    }
                }
            }
            return data_array_element;
        }catch (Exception e){
            return null;
        }
    }

    private void loadMaskElements(String jsonData){
        try {
            // Json 数据读取
            JSONObject data = JSON.parseObject(jsonData);
            // 获取MaskElement的集合
            JSONArray data_array = data.getJSONArray(("mask_element"));
            if (data_array != null){
                for (int i = 0; i < data_array.size(); i++) {
                    JSONObject object = data_array.getJSONObject(i);
                    maskElements.put(object.getString(("mask_id")) == null ? "":object.getString(("mask_id")),object);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * reSetDraw 重新绘制画布
     * 2023-04-19
     */
    public void reSetDraw() {
        invalidate();
    }

}
