package com.weilin.markcanvas.markutils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;
import java.util.Objects;

/**
 * DrawText 文本绘制类
 */
public class DrawText {
    /**
     * canvasDrawTextElement Json数据转换为Canvas画布实体 Text文本
     * 2023-04-21
     * @param canvas 画布
     * @param object Json数据（type_id = "text" 的类型数据）
     * @param canvasWidth 画布宽度
     * @param canvasHeight 画布和高度
     * @param isVirtual 是否为虚对象
     * @param context 上下文
     * @param posList 其上的Element集合数据
     * @return PositionEntity
     */
    @NonNull
    public static PositionEntity canvasDrawTextElement(@NonNull Canvas canvas, @NonNull JSONObject object, int canvasWidth, int canvasHeight, boolean isVirtual, Context context, HashMap<String, PositionEntity> posList){
        // 参数获取
        String text = object.getString(("text")) == null ? "":object.getString(("text"));
        text = CanvasUtils.retChinese(text);
        int font_style_mode = object.getIntValue(("font_style_mode"));
        String font_style = object.getString(("font_style")) == null ? "":object.getString(("font_style"));
        String font_color = object.getString(("font_color")) == null ? "":object.getString(("font_color"));
        float font_size = object.getFloatValue(("font_size"));
        boolean font_stroke = object.getBooleanValue(("font_stroke"),(false));
        String font_align = object.getString(("font_align")) == null ? "left":object.getString(("font_align"));
        float stroke_width = object.getFloatValue(("stroke_width"));
        boolean font_bold = object.getBooleanValue(("font_bold"),(false));
        float font_scale = object.getFloatValue(("font_scale"));
        float skew_x = object.getFloatValue(("skew_x"));
        float shadow_width = object.getFloatValue(("shadow_width"));
        float shadow_x = object.getFloatValue(("shadow_x"));
        float shadow_y = object.getFloatValue(("shadow_y"));
        String shadow_color = object.getString(("shadow_color")) == null ? "":object.getString(("shadow_color"));
        float left = object.getFloatValue(("left"));
        float top = object.getFloatValue(("top"));
        float percent_left = object.getFloatValue(("left_percent"));
        float percent_top = object.getFloatValue(("top_percent"));
        float offset_top = object.getFloatValue(("offset_top"));
        float offset_left = object.getFloatValue(("offset_left"));
        float rotate = object.getFloatValue(("rotate"));
        float offset_top_percent = object.getFloatValue(("offset_top_percent"));
        float offset_left_percent = object.getFloatValue(("offset_left_percent"));
        String align_left_mode = object.getString(("align_left_mode")) == null ? "left":object.getString(("align_left_mode"));
        JSONArray align_left = object.getJSONArray(("align_left"));
        String align_top_mode = object.getString(("align_top_mode")) == null ? "top":object.getString(("align_top_mode"));
        JSONArray align_top = object.getJSONArray(("align_top"));
        int width = object.getIntValue(("width"));
        String margin_left_item = object.getString(("margin_left_item")) == null ? "":object.getString(("margin_left_item"));
        float margin_left_percent = object.getFloatValue(("margin_left_percent"));
        String margin_top_item = object.getString(("margin_top_item")) == null ? "":object.getString(("margin_top_item"));
        float margin_top_percent = object.getFloatValue(("margin_top_percent"));
        float max_width = object.getFloatValue(("max_width"));
        float max_width_percent = object.getFloatValue(("max_width_percent"));
        int line_height = object.getIntValue(("line_height"));
        int alpha = object.getIntValue(("alpha"), (255));
        if (alpha < 0) alpha = 0;
        if (alpha > 255) alpha = 255;

        float percent_width = object.getFloatValue(("width_percent"));
        if (percent_width > 0) {
            width = (int) CanvasUtils.retPercentWidth(canvasWidth, (float) (percent_width * 0.01)) + width;
        } else {
            if (width == -1) {
                width = canvasWidth;
            }
        }

        // 宽度限制处理
        if (max_width_percent != 0f){
            max_width = CanvasUtils.retPercentWidth(canvasWidth, (float) (max_width_percent * 0.01)) + max_width;
        }

        // 渲染文字
        TextPaint mPaint = new TextPaint();
        Rect rect = new Rect();
        mPaint.setAntiAlias(true); // 抗锯齿
        mPaint.setDither(true); // 防抖
        if (shadow_width != 0f || shadow_x != 0f || shadow_y != 0f || !Objects.equals(shadow_color, (""))){
            mPaint.setShadowLayer(shadow_width, shadow_x, shadow_y, Color.parseColor(("#"+shadow_color))); // 设置字体阴影
        }
        mPaint.setColor(Color.parseColor(("#"+font_color))); // 设置字体颜色
        mPaint.setTextSize(font_size); // 设置字体大小
        if (font_scale != 0f){
            mPaint.setTextScaleX(font_scale); // 字体拉伸
        }
        if (!Objects.equals(font_style, (""))){
            if (font_style_mode == 0){
                Typeface typeface=Typeface.createFromAsset(context.getResources().getAssets(), ("font/"+font_style));
                mPaint.setTypeface(typeface);
            }
        }else{
            Typeface typeface=Typeface.createFromAsset(context.getResources().getAssets(), ("font/HarmonyOS.ttf"));
            mPaint.setTypeface(typeface);
        }
        if (font_stroke){
            mPaint.setStyle(Paint.Style.STROKE); // 设置空心描边
            mPaint.setStrokeWidth(stroke_width);
        }else{
            mPaint.setStyle(Paint.Style.FILL);
        }
        if (font_bold){
            mPaint.setFakeBoldText(true);
        }
        if (skew_x != 0f){
            mPaint.setTextSkewX(skew_x);
        }
        mPaint.setAlpha(alpha); // 设置透明度
        Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
        if (Objects.equals(font_align, ("center"))){
            alignment = Layout.Alignment.ALIGN_CENTER;
        } else if (Objects.equals(font_align, ("right"))) {
            alignment = Layout.Alignment.ALIGN_OPPOSITE;
        }
        mPaint.getTextBounds(text, (0), text.length(), rect);
        if (width == -2){
            width = (int) (rect.width() * 1.15);
        }

        if (max_width > 0 || max_width_percent > 0){
            if (width > max_width){
                width = (int) max_width;
            }
        }
        StaticLayout layout;
        int line_count = (int) Math.ceil(mPaint.measureText(text) / width); // 文本总行数
        // 文本加载
        if (line_height > 0 && line_count > 1){
            double text_width = mPaint.getTextSize(); // 一个字占的宽度
            int line_text_count = (int) (width / text_width); // 一行多少个字
            int buf_end = (line_text_count * line_height);
            if (line_count <= line_height){
                buf_end = text.length();
            }
            layout = new StaticLayout(text, (0), buf_end, mPaint, width, alignment,(1.0f),(0.0f),(false), TextUtils.TruncateAt.END,(int) ((line_text_count  * text_width) - text_width));
        }else{
            layout = new StaticLayout(text, mPaint, width, alignment,(1.0f),(0.0f),(false));
        }

        // 获取文本实际在画布的大小
        float relay_width;
        if (layout.getLineCount() > 1){
            relay_width = layout.getWidth();
        }else{
            relay_width = mPaint.measureText(text);
        }

        // 定位位置与指定对象 align对象合集
        if (align_left != null && align_left.size() != 0){
            // align_left 处理
            float max_left = 0f, max_right = 0f;
            for (int i = 0; i< align_left.size(); i++){
                PositionEntity entity = Objects.requireNonNull(posList.get(align_left.get(i).toString()));
                if (entity.left > max_left) max_left = entity.left;
                if ((entity.left + entity.width) > max_right) max_right = (entity.left + entity.width);
            }
            if (Objects.equals(align_left_mode, ("left")))  left = max_left + left;
            else if (Objects.equals(align_left_mode, ("center"))) left = max_left + ((max_right - max_left)/2) - (float) (width/2) + left;
            else if (Objects.equals(align_left_mode, ("right"))) left = max_right + left;
        }
        if (align_top != null && align_top.size() != 0){
            // align_top 处理
            float max_top = 0f, max_bottom = 0f, min_top = Objects.requireNonNull(posList.get(align_top.get(0).toString())).top;
            for (int i = 0; i < align_top.size(); i++){
                PositionEntity entity = Objects.requireNonNull(posList.get(align_top.get(i).toString()));
                if (entity.top > max_top) max_top = entity.top;
                if ((entity.top + entity.height) > max_bottom) max_bottom = (entity.top + entity.height);
                if (entity.top < min_top) min_top = entity.top;
            }
            if (Objects.equals(align_top_mode, ("top"))) top = max_top + top;
            else if (Objects.equals(align_top_mode, ("center"))) top = min_top + ((max_bottom - min_top)/2) - (float) (layout.getHeight()/2) + top;
            else if (Objects.equals(align_top_mode, ("bottom"))) top = max_bottom + top;
        }
        // 百分比位置
        if (percent_left > 0) {
            left = CanvasUtils.retPercentWidth(canvasWidth, (float) (percent_left * 0.01)) + left;
        } else {
            if (left == -1) {
                left = CanvasUtils.retCenterCanvasX(canvasWidth) - (float) (layout.getWidth() / 2) + left + 1;
            }
        }
        if (percent_top > 0) {
            top = CanvasUtils.retPercentHeight(canvasHeight, (float) (percent_top * 0.01)) + top;
        } else {
            if (top == -1) {
                top = CanvasUtils.retCenterCanvasY(canvasHeight) - (float) (layout.getHeight() / 2) + top + 1;
            }
        }
        // 百分比偏移量
        if (offset_left_percent != 0f){
            offset_left = CanvasUtils.retPercentWidth(canvasWidth, (float) (offset_left_percent * 0.01)) + offset_left;
        }
        if (offset_top_percent != 0f){
            offset_top = CanvasUtils.retPercentHeight(canvasHeight, (float) (offset_top_percent * 0.01)) + offset_top;
        }

        // margin偏移
        if (!Objects.equals(margin_left_item, (""))){
            if (Objects.equals(margin_left_item, ("self"))){
                offset_left = CanvasUtils.retPercentWidth((int) mPaint.measureText(text), (float) (margin_left_percent * 0.01)) + offset_left;
            }else {
                PositionEntity entity = Objects.requireNonNull(posList.get(margin_left_item));
                offset_left = CanvasUtils.retPercentWidth((int) entity.width, (float) (margin_left_percent * 0.01)) + offset_left;
            }
        }
        if (!Objects.equals(margin_top_item, (""))){
            if (Objects.equals(margin_top_item, ("self"))){
                offset_top = CanvasUtils.retPercentHeight(layout.getHeight(), (float) (margin_top_percent * 0.01)) + offset_top;
            }else {
                PositionEntity entity = Objects.requireNonNull(posList.get(margin_top_item));
                offset_top = CanvasUtils.retPercentHeight((int) entity.height, (float) (margin_top_percent * 0.01)) + offset_top;
            }
        }

        if (!isVirtual){
            canvas.save();
            if (rotate != 0f){
                Matrix matrix = new Matrix();
                //matrix.setTranslate((left + offset_left), (top + offset_top));
                // 旋转参数
                matrix.preRotate(rotate, ((left + offset_left) + (relay_width / 2)), (top + offset_top));
                canvas.setMatrix(matrix);
            }
            canvas.translate((left + offset_left), (top + offset_top));
            layout.draw(canvas);
            canvas.restore();
        }

        PositionEntity position = new PositionEntity();
        position.height = layout.getHeight();
        position.width = relay_width;
        position.left = (left + offset_left);
        position.top = (top + offset_top);
        return position;
    }

    /**
     *　canvasDrawTextMaskElement　Text的蒙版
     * 2023-05-01
     * @param canvas 画布
     * @param object JSON数据
     * @param canvasWidth　画布宽度
     * @param canvasHeight　画布高度
     * @param context　上下文
     */
    public static void canvasDrawTextMaskElement(@NonNull Canvas canvas, @NonNull JSONObject object, int canvasWidth, int canvasHeight, Context context){
        // 参数获取
        String text = object.getString(("text")) == null ? "":object.getString(("text"));
        text = CanvasUtils.retChinese(text);
        int font_style_mode = object.getIntValue(("font_style_mode"));
        String font_style = object.getString(("font_style")) == null ? "":object.getString(("font_style"));
        String font_color = object.getString(("font_color")) == null ? "":object.getString(("font_color"));
        float font_size = object.getFloatValue(("font_size"));
        boolean font_stroke = object.getBooleanValue(("font_stroke"),(false));
        String font_align = object.getString(("font_align")) == null ? "left":object.getString(("font_align"));
        float stroke_width = object.getFloatValue(("stroke_width"));
        boolean font_bold = object.getBooleanValue(("font_bold"),(false));
        float font_scale = object.getFloatValue(("font_scale"));
        float skew_x = object.getFloatValue(("skew_x"));
        float shadow_width = object.getFloatValue(("shadow_width"));
        float shadow_x = object.getFloatValue(("shadow_x"));
        float shadow_y = object.getFloatValue(("shadow_y"));
        String shadow_color = object.getString(("shadow_color")) == null ? "":object.getString(("shadow_color"));
        float left = object.getFloatValue(("left"));
        float top = object.getFloatValue(("top"));
        float percent_left = object.getFloatValue(("left_percent"));
        float percent_top = object.getFloatValue(("top_percent"));
        float offset_top = object.getFloatValue(("offset_top"));
        float offset_left = object.getFloatValue(("offset_left"));
        float rotate = object.getFloatValue(("rotate"));
        float offset_top_percent = object.getFloatValue(("offset_top_percent"));
        float offset_left_percent = object.getFloatValue(("offset_left_percent"));
        int width = object.getIntValue(("width"));
        String margin_left_item = object.getString(("margin_left_item")) == null ? "":object.getString(("margin_left_item"));
        float margin_left_percent = object.getFloatValue(("margin_left_percent"));
        String margin_top_item = object.getString(("margin_top_item")) == null ? "":object.getString(("margin_top_item"));
        float margin_top_percent = object.getFloatValue(("margin_top_percent"));
        float max_width = object.getFloatValue(("max_width"));
        float max_width_percent = object.getFloatValue(("max_width_percent"));
        int line_height = object.getIntValue(("line_height"));
        int alpha = object.getIntValue(("alpha"), (255));
        if (alpha < 0) alpha = 0;
        if (alpha > 255) alpha = 255;

        float percent_width = object.getFloatValue(("width_percent"));
        if (percent_width > 0) {
            width = (int) CanvasUtils.retPercentWidth(canvasWidth, (float) (percent_width * 0.01)) + width;
        } else {
            if (width == -1) {
                width = canvasWidth + 1;
            }
        }

        // 宽度限制处理
        if (max_width_percent != 0f){
            max_width = CanvasUtils.retPercentWidth(canvasWidth, (float) (max_width_percent * 0.01)) + max_width;
        }

        // 渲染文字
        TextPaint mPaint = new TextPaint();
        Rect rect = new Rect();
        mPaint.setAntiAlias(true); // 抗锯齿
        mPaint.setDither(true); // 防抖
        if (shadow_width != 0f || shadow_x != 0f || shadow_y != 0f || !Objects.equals(shadow_color, (""))){
            mPaint.setShadowLayer(shadow_width, shadow_x, shadow_y, Color.parseColor(("#"+shadow_color))); // 设置字体阴影
        }
        mPaint.setColor(Color.parseColor(("#"+font_color))); // 设置字体颜色
        mPaint.setTextSize(font_size); // 设置字体大小
        if (font_scale != 0f){
            mPaint.setTextScaleX(font_scale); // 字体拉伸
        }
        if (!Objects.equals(font_style, (""))){
            if (font_style_mode == 0){
                Typeface typeface=Typeface.createFromAsset(context.getResources().getAssets(), ("font/"+font_style));
                mPaint.setTypeface(typeface);
            }
        }else{
            Typeface typeface=Typeface.createFromAsset(context.getResources().getAssets(), ("font/HarmonyOS.ttf"));
            mPaint.setTypeface(typeface);
        }
        if (font_stroke){
            mPaint.setStyle(Paint.Style.STROKE); // 设置空心描边
            mPaint.setStrokeWidth(stroke_width);
        }else{
            mPaint.setStyle(Paint.Style.FILL);
        }
        if (font_bold){
            mPaint.setFakeBoldText(true);
        }
        if (skew_x != 0f){
            mPaint.setTextSkewX(skew_x);
        }
        mPaint.setAlpha(alpha); // 设置透明度
        Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
        if (Objects.equals(font_align, ("center"))){
            alignment = Layout.Alignment.ALIGN_CENTER;
        } else if (Objects.equals(font_align, ("right"))) {
            alignment = Layout.Alignment.ALIGN_OPPOSITE;
        }
        mPaint.getTextBounds(text, (0), text.length(), rect);
        if (width == -2){
            width = (int) (rect.width() * 1.15);
        }

        if (max_width > 0 || max_width_percent > 0){
            if (width > max_width){
                width = (int) max_width;
            }
        }
        StaticLayout layout;
        int line_count = (int) Math.ceil(mPaint.measureText(text) / width); // 文本总行数
        // 文本加载
        if (line_height > 0 && line_count > 1){
            double text_width = mPaint.getTextSize(); // 一个字占的宽度
            int line_text_count = (int) (width / text_width); // 一行多少个字
            int buf_end = (line_text_count * line_height);
            if (line_count <= line_height){
                buf_end = text.length();
            }
            layout = new StaticLayout(text, (0), buf_end, mPaint, width, alignment,(1.0f),(0.0f),(false), TextUtils.TruncateAt.END,(int) ((line_text_count  * text_width) - text_width));
        }else{
            layout = new StaticLayout(text, mPaint, width, alignment,(1.0f),(0.0f),(false));
        }

        // 获取文本实际在画布的大小
        float relay_width;
        if (layout.getLineCount() > 1){
            relay_width = layout.getWidth();
        }else{
            relay_width = mPaint.measureText(text);
        }
        // 百分比位置
        if (percent_left > 0) {
            left = CanvasUtils.retPercentWidth(canvasWidth, (float) (percent_left * 0.01)) + left;
        } else {
            if (left == -1) {
                left = CanvasUtils.retCenterCanvasX(canvasWidth) - (float) (layout.getWidth() / 2) + left + 1;
            }
        }
        if (percent_top > 0) {
            top = CanvasUtils.retPercentHeight(canvasHeight, (float) (percent_top * 0.01)) + top;
        } else {
            if (top == -1) {
                top = CanvasUtils.retCenterCanvasY(canvasHeight) - (float) (layout.getHeight() / 2) + top + 1;
            }
        }
        // 百分比偏移量
        if (offset_left_percent != 0f){
            offset_left = CanvasUtils.retPercentWidth(canvasWidth, (float) (offset_left_percent * 0.01)) + offset_left;
        }
        if (offset_top_percent != 0f){
            offset_top = CanvasUtils.retPercentHeight(canvasHeight, (float) (offset_top_percent * 0.01)) + offset_top;
        }

        // margin偏移
        if (!Objects.equals(margin_left_item, (""))){
            if (Objects.equals(margin_left_item, ("self"))){
                offset_left = CanvasUtils.retPercentWidth((int) mPaint.measureText(text), (float) (margin_left_percent * 0.01)) + offset_left;
            }
        }
        if (!Objects.equals(margin_top_item, (""))){
            if (Objects.equals(margin_top_item, ("self"))){
                offset_top = CanvasUtils.retPercentHeight(layout.getHeight(), (float) (margin_top_percent * 0.01)) + offset_top;
            }
        }
        canvas.save();
        if (rotate != 0f){
            Matrix matrix = new Matrix();
            //matrix.setTranslate((left + offset_left), (top + offset_top));
            // 旋转参数
            matrix.preRotate(rotate, ((left + offset_left) + (relay_width / 2)), (top + offset_top));
            canvas.setMatrix(matrix);
        }
        canvas.translate((left + offset_left), (top + offset_top));
        layout.draw(canvas);
        canvas.restore();
    }
}
