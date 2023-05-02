package com.weilin.markcanvas.markutils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class DrawRect {

    /**
     * canvasDrawRectElement Json数据转换为Canvas画布实体 矩形
     * 2023-05-02
     * @param canvas 画布
     * @param object Json数据（type_id = "rect" 的类型数据）
     * @param canvasWidth 画布宽度
     * @param canvasHeight 画布和高度
     * @param isVirtual 是否为虚对象
     * @param context 上下文
     * @param posList 其上的Element集合数据
     * @param maskList 蒙版集合数据
     * @return PositionEntity
     */
    @NonNull
    static PositionEntity canvasDrawRectElement(@NonNull Canvas canvas, @NonNull JSONObject object,
                                                int canvasWidth, int canvasHeight, boolean isVirtual, Context context,
                                                HashMap<String, PositionEntity> posList, HashMap<String, JSONObject> maskList) {
        String color = object.getString(("color")) == null ? "" : object.getString(("color"));
        float width = object.getIntValue(("width"));
        float width_percent = object.getFloatValue(("width_percent"));
        float height = object.getFloatValue(("height"));
        float height_percent = object.getFloatValue(("height_percent"));
        boolean stroke = object.getBooleanValue(("stroke"), (false));
        float stroke_width = object.getFloatValue(("stroke_width"));
        // 固定参数 相对位置参数 共计 16 个参数
        float left = object.getFloatValue(("left"));
        float top = object.getFloatValue(("top"));
        float percent_left = object.getFloatValue(("left_percent"));
        float percent_top = object.getFloatValue(("top_percent"));
        float offset_top = object.getFloatValue(("offset_top"));
        float offset_left = object.getFloatValue(("offset_left"));
        float offset_top_percent = object.getFloatValue(("offset_top_percent"));
        float offset_left_percent = object.getFloatValue(("offset_left_percent"));
        String align_left_mode = object.getString(("align_left_mode")) == null ? "left" : object.getString(("align_left_mode"));
        JSONArray align_left = object.getJSONArray(("align_left"));
        String align_top_mode = object.getString(("align_top_mode")) == null ? "top" : object.getString(("align_top_mode"));
        JSONArray align_top = object.getJSONArray(("align_top"));
        String margin_left_item = object.getString(("margin_left_item")) == null ? "" : object.getString(("margin_left_item"));
        float margin_left_percent = object.getFloatValue(("margin_left_percent"));
        String margin_top_item = object.getString(("margin_top_item")) == null ? "" : object.getString(("margin_top_item"));
        float margin_top_percent = object.getFloatValue(("margin_top_percent"));
        int alpha = object.getIntValue(("alpha"), (255));
        if (alpha < 0) alpha = 0;
        if (alpha > 255) alpha = 255;
        // 蒙版
        String mask_item = object.getString(("mask_item")) == null ? "" : object.getString(("mask_item"));
        String mask_mode = object.getString(("mask_mode")) == null ? "" : object.getString(("mask_mode"));

        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true); // 抗锯齿
        mPaint.setDither(true); // 防抖
        mPaint.setColor(Color.parseColor(("#" + color))); // 设置颜色
        mPaint.setAlpha(alpha);
        if (stroke) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(stroke_width);
        }

        if (width_percent > 0) {
            width = (CanvasUtils.retPercentWidth(canvasWidth, (float) (width_percent * 0.01)) + width);
        } else {
            if (width == -1) {
                width = (float) canvasWidth;
            }
        }

        if (height_percent > 0) {
            height = (CanvasUtils.retPercentHeight(canvasHeight, (float) (height_percent * 0.01)) + height);
        } else {
            if (height == -1) {
                height = (float) canvasHeight;
            }
        }

        // 定位位置与指定对象 align对象合集
        if (align_left != null && align_left.size() != 0) {
            // align_left 处理
            float max_left = 0f, max_right = 0f;
            for (int i = 0; i < align_left.size(); i++) {
                PositionEntity entity = Objects.requireNonNull(posList.get(align_left.get(i).toString()));
                if (entity.left > max_left) max_left = entity.left;
                if ((entity.left + entity.width) > max_right)
                    max_right = (entity.left + entity.width);
            }
            if (Objects.equals(align_left_mode, ("left"))) left = max_left + left;
            else if (Objects.equals(align_left_mode, ("center")))
                left = max_left + ((max_right - max_left) / 2) - (width/2) + left;
            else if (Objects.equals(align_left_mode, ("right"))) left = max_right + left;
        }
        if (align_top != null && align_top.size() != 0) {
            // align_top 处理
            float max_top = 0f, max_bottom = 0f, min_top = Objects.requireNonNull(posList.get(align_top.get(0).toString())).top;
            for (int i = 0; i < align_top.size(); i++) {
                PositionEntity entity = Objects.requireNonNull(posList.get(align_top.get(i).toString()));
                if (entity.top > max_top) max_top = entity.top;
                if ((entity.top + entity.height) > max_bottom)
                    max_bottom = (entity.top + entity.height);
                if (entity.top < min_top) min_top = entity.top;
            }
            if (Objects.equals(align_top_mode, ("top"))) top = max_top + top;
            else if (Objects.equals(align_top_mode, ("center")))
                top = min_top + ((max_bottom - min_top) / 2) - (height/2) + top;
            else if (Objects.equals(align_top_mode, ("bottom"))) top = max_bottom + top;
        }
        // 百分比位置
        if (percent_left > 0) {
            left = CanvasUtils.retPercentWidth(canvasWidth, (float) (percent_left * 0.01)) + left;
        } else {
            if (left == -1) {
                left = CanvasUtils.retCenterCanvasX(canvasWidth) + left + 1;
            }
        }
        if (percent_top > 0) {
            top = CanvasUtils.retPercentHeight(canvasHeight, (float) (percent_top * 0.01)) + top;
        } else {
            if (top == -1) {
                top = CanvasUtils.retCenterCanvasY(canvasHeight) + top + 1;
            }
        }
        // 百分比偏移量
        if (offset_left_percent != 0f) {
            offset_left = CanvasUtils.retPercentWidth(canvasWidth, (float) (offset_left_percent * 0.01)) + offset_left;
        }
        if (offset_top_percent != 0f) {
            offset_top = CanvasUtils.retPercentHeight(canvasHeight, (float) (offset_top_percent * 0.01)) + offset_top;
        }

        // margin偏移
        if (!Objects.equals(margin_left_item, (""))) {
            if (Objects.equals(margin_left_item, ("self"))) {
                offset_left = CanvasUtils.retPercentWidth((int) width, (float) (margin_left_percent * 0.01)) + offset_left;
            } else {
                PositionEntity entity = Objects.requireNonNull(posList.get(margin_left_item));
                offset_left = CanvasUtils.retPercentWidth((int) entity.width, (float) (margin_left_percent * 0.01)) + offset_left;
            }
        }
        if (!Objects.equals(margin_top_item, (""))) {
            if (Objects.equals(margin_top_item, ("self"))) {
                offset_top = CanvasUtils.retPercentHeight((int) height, (float) (margin_top_percent * 0.01)) + offset_top;
            } else {
                PositionEntity entity = Objects.requireNonNull(posList.get(margin_top_item));
                offset_top = CanvasUtils.retPercentHeight((int) entity.height, (float) (margin_top_percent * 0.01)) + offset_top;
            }
        }

        if (!isVirtual) {
            canvas.save();
            if (!Objects.equals(mask_item, (""))) {
                int canvasLayoutID = canvas.saveLayer((0), (0), canvasWidth, canvasHeight, mPaint);
                DrawMask.canvasDrawMask(canvas,mPaint, Objects.requireNonNull(maskList.get(mask_item)),canvasWidth,canvasHeight,context,mask_mode);
                Matrix matrix = new Matrix();
                matrix.setTranslate((left + offset_left), (top + offset_top));
                // 旋转参数
                matrix.preRotate(object.getFloatValue(("rotate")),((float) width/2), ((float) height/2));
                canvas.setMatrix(matrix);
                canvas.drawRoundRect((left + offset_left), (top + offset_top), (left+offset_left+width), (top+offset_top+height),object.getFloatValue(("round")),object.getFloatValue(("round")), mPaint);
                mPaint.setXfermode(null);
                canvas.restoreToCount(canvasLayoutID);
            } else {
                Matrix matrix = new Matrix();
                matrix.setTranslate((left + offset_left), (top + offset_top));
                // 旋转参数
                matrix.preRotate(object.getFloatValue(("rotate")),((float) width/2), ((float) height/2));
                canvas.setMatrix(matrix);
                canvas.drawRoundRect((left + offset_left), (top + offset_top), (left+offset_left+width), (top+offset_top+height),object.getFloatValue(("round")),object.getFloatValue(("round")), mPaint);
            }
            canvas.restore();
        }

        PositionEntity position = new PositionEntity();
        position.height = height;
        position.width = width;
        position.left = (left + offset_left);
        position.top = (top + offset_top);
        return position;
    }

    /**
     * canvasDrawRectMaskElement 矩形的蒙版
     * 2023-05-02
     * @param canvas 画布
     * @param object Json数据（type_id = "rect" 的类型数据）
     * @param canvasWidth 画布宽度
     * @param canvasHeight 画布和高度
     * @param context 上下文
     */
    public static void canvasDrawRectMaskElement(@NonNull Canvas canvas, @NonNull JSONObject object,
                                         int canvasWidth, int canvasHeight, Context context) {
        String color = object.getString(("color")) == null ? "" : object.getString(("color"));
        float width = object.getIntValue(("width"));
        float width_percent = object.getFloatValue(("width_percent"));
        float height = object.getFloatValue(("height"));
        float height_percent = object.getFloatValue(("height_percent"));
        boolean stroke = object.getBooleanValue(("stroke"), (false));
        float stroke_width = object.getFloatValue(("stroke_width"));
        // 固定参数 相对位置参数 共计 16 个参数
        float left = object.getFloatValue(("left"));
        float top = object.getFloatValue(("top"));
        float percent_left = object.getFloatValue(("left_percent"));
        float percent_top = object.getFloatValue(("top_percent"));
        float offset_top = object.getFloatValue(("offset_top"));
        float offset_left = object.getFloatValue(("offset_left"));
        float offset_top_percent = object.getFloatValue(("offset_top_percent"));
        float offset_left_percent = object.getFloatValue(("offset_left_percent"));
        String margin_left_item = object.getString(("margin_left_item")) == null ? "" : object.getString(("margin_left_item"));
        float margin_left_percent = object.getFloatValue(("margin_left_percent"));
        String margin_top_item = object.getString(("margin_top_item")) == null ? "" : object.getString(("margin_top_item"));
        float margin_top_percent = object.getFloatValue(("margin_top_percent"));
        int alpha = object.getIntValue(("alpha"), (255));
        if (alpha < 0) alpha = 0;
        if (alpha > 255) alpha = 255;

        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true); // 抗锯齿
        mPaint.setDither(true); // 防抖
        mPaint.setColor(Color.parseColor(("#" + color))); // 设置颜色
        mPaint.setAlpha(alpha);
        if (stroke) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(stroke_width);
        }

        if (width_percent > 0) {
            width = (CanvasUtils.retPercentWidth(canvasWidth, (float) (width_percent * 0.01)) + width);
        } else {
            if (width == -1) {
                width = (float) canvasWidth;
            }
        }

        if (height_percent > 0) {
            height = (CanvasUtils.retPercentHeight(canvasHeight, (float) (height_percent * 0.01)) + height);
        } else {
            if (height == -1) {
                height = (float) canvasHeight;
            }
        }

        // 百分比位置
        if (percent_left > 0) {
            left = CanvasUtils.retPercentWidth(canvasWidth, (float) (percent_left * 0.01)) + left;
        } else {
            if (left == -1) {
                left = CanvasUtils.retCenterCanvasX(canvasWidth) + left + 1;
            }
        }
        if (percent_top > 0) {
            top = CanvasUtils.retPercentHeight(canvasHeight, (float) (percent_top * 0.01)) + top;
        } else {
            if (top == -1) {
                top = CanvasUtils.retCenterCanvasY(canvasHeight) + top + 1;
            }
        }
        // 百分比偏移量
        if (offset_left_percent != 0f) {
            offset_left = CanvasUtils.retPercentWidth(canvasWidth, (float) (offset_left_percent * 0.01)) + offset_left;
        }
        if (offset_top_percent != 0f) {
            offset_top = CanvasUtils.retPercentHeight(canvasHeight, (float) (offset_top_percent * 0.01)) + offset_top;
        }

        // margin偏移
        if (!Objects.equals(margin_left_item, (""))) {
            if (Objects.equals(margin_left_item, ("self"))) {
                offset_left = CanvasUtils.retPercentWidth((int) width, (float) (margin_left_percent * 0.01)) + offset_left;
            }
        }
        if (!Objects.equals(margin_top_item, (""))) {
            if (Objects.equals(margin_top_item, ("self"))) {
                offset_top = CanvasUtils.retPercentHeight((int) height, (float) (margin_top_percent * 0.01)) + offset_top;
            }
        }

        canvas.save();
        Matrix matrix = new Matrix();
        matrix.setTranslate((left + offset_left), (top + offset_top));
        // 旋转参数
        matrix.preRotate(object.getFloatValue(("rotate")),((float) width/2), ((float) height/2));
        canvas.setMatrix(matrix);
        canvas.drawRoundRect((left + offset_left), (top + offset_top), (left+offset_left+width), (top+offset_top+height),object.getFloatValue(("round")),object.getFloatValue(("round")), mPaint);
        canvas.restore();

    }
}
