package com.weilin.markcanvas.markutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;
import java.util.Objects;

/**
 * DrawBitmap 画布Bitmap工具类
 */
public class DrawBitmap {
    /**
     * canvasDrawImageElement Json数据转换为Canvas画布实体 BitMap图片
     * 2023-04-20
     * @param canvas 画布
     * @param object Json数据（type_id = "image" 的类型数据）
     * @param canvasWidth 画布宽度
     * @param canvasHeight 画布和高度
     * @param isVirtual 是否为虚对象
     * @param context 上下文
     * @param posList 其上的Element集合数据
     * @param maskList 蒙版集合数据
     * @return PositionEntity
     */
    @NonNull
    public static PositionEntity canvasDrawImageElement(@NonNull Canvas canvas, @NonNull JSONObject object, int canvasWidth, int canvasHeight,
                                                        boolean isVirtual, Context context, HashMap<String, PositionEntity> posList,
                                                        HashMap<String, JSONObject> maskList){
        // 宽度高度适配处理
        int width = object.getIntValue(("width"));
        int height = object.getIntValue(("height"));
        float percent_width = object.getFloatValue(("width_percent"));
        float percent_height = object.getFloatValue(("height_percent"));
        if (percent_width > 0) {
            width = (int) CanvasUtils.retPercentWidth(canvasWidth, (float) (percent_width * 0.01)) + width;
        } else {
            if (width == -1) {
                width = canvasWidth;
            }
        }
        if (percent_height > 0) {
            height = (int) CanvasUtils.retPercentHeight(canvasHeight, (float) (percent_height * 0.01)) + height;
        } else {
            if (height == -1) {
                height = canvasHeight;
            }
        }
        // 位图创建
        String file_pre = object.getString(("file_pre")) == null ? "":object.getString(("file_pre"));
        String file_name = object.getString(("file_name")) == null ? "":object.getString(("file_name"));
        boolean is_file = object.getBooleanValue(("is_file"), (false));
        // Bitmap数据
        Bitmap bitmap = drawRadiusBitmap(width, height, object.getFloatValue(("round")), file_pre, file_name, is_file,context);
        // 模糊的参数
        boolean is_blur = object.getBooleanValue(("is_blur"), (false));
        int blur_percent = object.getIntValue(("blur_percent"), (0));
        // 相对位置适配处理
        float left = object.getFloatValue(("left"));
        float top = object.getFloatValue(("top"));
        float percent_left = object.getFloatValue(("left_percent"));
        float percent_top = object.getFloatValue(("top_percent"));
        float offset_top = object.getFloatValue(("offset_top"));
        float offset_top_percent = object.getFloatValue(("offset_top_percent"));
        float offset_left = object.getFloatValue(("offset_left"));
        float offset_left_percent = object.getFloatValue(("offset_left_percent"));
        String align_left_mode = object.getString(("align_left_mode")) == null ? "left":object.getString(("align_left_mode"));
        JSONArray align_left = object.getJSONArray(("align_left"));
        String align_top_mode = object.getString(("align_top_mode")) == null ? "top":object.getString(("align_top_mode"));
        JSONArray align_top = object.getJSONArray(("align_top"));
        String margin_left_item = object.getString(("margin_left_item")) == null ? "":object.getString(("margin_left_item"));
        float margin_left_percent = object.getFloatValue(("margin_left_percent"));
        String margin_top_item = object.getString(("margin_top_item")) == null ? "":object.getString(("margin_top_item"));
        float margin_top_percent = object.getFloatValue(("margin_top_percent"));
        // 蒙版
        String mask_item = object.getString(("mask_item")) == null ? "":object.getString(("mask_item"));
        String mask_mode = object.getString(("mask_mode")) == null ? "":object.getString(("mask_mode"));
        int alpha = object.getIntValue(("alpha"), (255));
        if (alpha < 0) alpha = 0;
        if (alpha > 255) alpha = 255;

        // 定位位置
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
            else if (Objects.equals(align_top_mode, ("center"))) top = min_top + ((max_bottom - min_top)/2) - (float) (height/2) + top;
            else if (Objects.equals(align_top_mode, ("bottom"))) top = max_bottom + top;
        }
        // 百分比
        if (percent_left > 0) {
            left = CanvasUtils.retPercentWidth(canvasWidth, (float) (percent_left * 0.01)) + left;
        } else {
            if (left == -1) {
                assert bitmap != null;
                left = CanvasUtils.retCenterCanvasX(canvasWidth) - (float) (bitmap.getWidth() / 2) + left + 1;
            }
        }
        if (percent_top > 0) {
            top = CanvasUtils.retPercentHeight(canvasHeight, (float) (percent_top * 0.01)) + top;
        } else {
            if (top == -1) {
                assert bitmap != null;
                top = CanvasUtils.retCenterCanvasY(canvasHeight) - (float) (bitmap.getHeight() / 2) + top + 1;
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
                offset_left = CanvasUtils.retPercentWidth(width, (float) (margin_left_percent * 0.01)) + offset_left;
            }else {
                PositionEntity entity = Objects.requireNonNull(posList.get(margin_left_item));
                offset_left = CanvasUtils.retPercentWidth((int) entity.width, (float) (margin_left_percent * 0.01)) + offset_left;
            }
        }
        if (!Objects.equals(margin_top_item, (""))){
            if (Objects.equals(margin_top_item, ("self"))){
                offset_top = CanvasUtils.retPercentHeight(height, (float) (margin_top_percent * 0.01)) + offset_top;
            }else {
                PositionEntity entity = Objects.requireNonNull(posList.get(margin_top_item));
                offset_top = CanvasUtils.retPercentHeight((int) entity.height, (float) (margin_top_percent * 0.01)) + offset_top;
            }
        }

        // 模糊处理
        if (is_blur){
            if (blur_percent > 100) blur_percent = 100;
            if (blur_percent < 0) blur_percent = 0;
            bitmap = FastBlur.doBlur(bitmap,blur_percent,(true));
        }

        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true); // 抗锯齿
        mPaint.setDither(true); // 防抖
        mPaint.setAlpha(alpha); // 设置透明度

        //画布创建处理
        if (!isVirtual){
            canvas.save();
            if (!Objects.equals(mask_item, (""))){
                int canvasLayoutID = canvas.saveLayer((0),(0),canvasWidth,canvasHeight,mPaint);
                DrawMask.canvasDrawMask(canvas,mPaint, Objects.requireNonNull(maskList.get(mask_item)),canvasWidth,canvasHeight,context,mask_mode);
                Matrix matrix = new Matrix();
                matrix.setTranslate((left + offset_left), (top + offset_top));
                // 旋转参数
                matrix.preRotate(object.getFloatValue(("rotate")),((float) width/2), ((float) height/2));
                canvas.drawBitmap(bitmap, matrix, mPaint);
                mPaint.setXfermode(null);
                canvas.restoreToCount(canvasLayoutID);
            }else{
                Matrix matrix = new Matrix();
                matrix.setTranslate((left + offset_left), (top + offset_top));
                // 旋转参数
                matrix.preRotate(object.getFloatValue(("rotate")),((float) width/2), ((float) height/2));
                canvas.drawBitmap(bitmap, matrix, mPaint);
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
     * canvasDrawImageMaskElement BitMap图片的蒙版
     * 2023-05-20
     * @param canvas 画布
     * @param object Json数据（type_id = "image" 的类型数据）
     * @param canvasWidth 画布宽度
     * @param canvasHeight 画布和高度
     * @param context 上下文
     */
    public static void canvasDrawImageMaskElement(@NonNull Canvas canvas, @NonNull JSONObject object, int canvasWidth, int canvasHeight, Context context){
        // 宽度高度适配处理
        int width = object.getIntValue(("width"));
        int height = object.getIntValue(("height"));
        float percent_width = object.getFloatValue(("width_percent"));
        float percent_height = object.getFloatValue(("height_percent"));
        if (percent_width > 0) {
            width = (int) CanvasUtils.retPercentWidth(canvasWidth, (float) (percent_width * 0.01)) + width;
        } else {
            if (width == -1) {
                width = canvasWidth;
            }
        }
        if (percent_height > 0) {
            height = (int) CanvasUtils.retPercentHeight(canvasHeight, (float) (percent_height * 0.01)) + height;
        } else {
            if (height == -1) {
                height = canvasHeight;
            }
        }
        // 位图创建
        String file_pre = object.getString(("file_pre")) == null ? "":object.getString(("file_pre"));
        String file_name = object.getString(("file_name")) == null ? "":object.getString(("file_name"));
        boolean is_file = object.getBooleanValue(("is_file"), (false));
        // Bitmap数据
        Bitmap bitmap = drawRadiusBitmap(width, height, object.getFloatValue(("round")), file_pre, file_name, is_file,context);
        // 模糊的参数
        boolean is_blur = object.getBooleanValue(("is_blur"), (false));
        int blur_percent = object.getIntValue(("blur_percent"), (0));
        // 相对位置适配处理
        float left = object.getFloatValue(("left"));
        float top = object.getFloatValue(("top"));
        float percent_left = object.getFloatValue(("left_percent"));
        float percent_top = object.getFloatValue(("top_percent"));
        float offset_top = object.getFloatValue(("offset_top"));
        float offset_top_percent = object.getFloatValue(("offset_top_percent"));
        float offset_left = object.getFloatValue(("offset_left"));
        float offset_left_percent = object.getFloatValue(("offset_left_percent"));
        String margin_left_item = object.getString(("margin_left_item")) == null ? "":object.getString(("margin_left_item"));
        float margin_left_percent = object.getFloatValue(("margin_left_percent"));
        String margin_top_item = object.getString(("margin_top_item")) == null ? "":object.getString(("margin_top_item"));
        float margin_top_percent = object.getFloatValue(("margin_top_percent"));
        int alpha = object.getIntValue(("alpha"), (255));
        if (alpha < 0) alpha = 0;
        if (alpha > 255) alpha = 255;

        // 定位位置
        // 百分比
        if (percent_left > 0) {
            left = CanvasUtils.retPercentWidth(canvasWidth, (float) (percent_left * 0.01)) + left;
        } else {
            if (left == -1) {
                assert bitmap != null;
                left = CanvasUtils.retCenterCanvasX(canvasWidth) - (float) (bitmap.getWidth() / 2) + left + 1;
            }
        }
        if (percent_top > 0) {
            top = CanvasUtils.retPercentHeight(canvasHeight, (float) (percent_top * 0.01)) + top;
        } else {
            if (top == -1) {
                assert bitmap != null;
                top = CanvasUtils.retCenterCanvasY(canvasHeight) - (float) (bitmap.getHeight() / 2) + top + 1;
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
                offset_left = CanvasUtils.retPercentWidth(width, (float) (margin_left_percent * 0.01)) + offset_left;
            }
        }
        if (!Objects.equals(margin_top_item, (""))){
            if (Objects.equals(margin_top_item, ("self"))){
                offset_top = CanvasUtils.retPercentHeight(height, (float) (margin_top_percent * 0.01)) + offset_top;
            }
        }

        // 模糊处理
        if (is_blur){
            if (blur_percent > 100) blur_percent = 100;
            if (blur_percent < 0) blur_percent = 0;
            bitmap = FastBlur.doBlur(bitmap,blur_percent,(true));
        }

        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true); // 抗锯齿
        mPaint.setDither(true); // 防抖
        mPaint.setAlpha(alpha); // 设置透明度

        //画布创建处理
        canvas.save();
        Matrix matrix = new Matrix();
        matrix.setTranslate((left + offset_left), (top + offset_top));
        // 旋转参数
        matrix.preRotate(object.getFloatValue(("rotate")),((float) width/2), ((float) height/2));
        canvas.drawBitmap(bitmap, matrix, mPaint);
        canvas.restore();
    }

    /**
     * drawRadiusBitmap 绘制圆角图片
     * 2023-04-19
     * @param width    宽度
     * @param height   高度
     * @param roundPx  圆角度
     * @param perName  前缀目录 (结尾不要有"/")
     * @param fileName 文件名称 (结尾不要有.png) 只支持png文件
     * @param isFile   是否在Assets目录内
     * @param context  上下文
     * @return Bitmap资源
     */
    @Nullable
    private static Bitmap drawRadiusBitmap(int width, int height, float roundPx, String perName, String fileName, boolean isFile, Context context) {
        try {
            Bitmap imageBitmap = drawSizeOfBitmap(width, height, perName, fileName, isFile, context); // 原始图
            assert imageBitmap != null;
            Bitmap output = Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), Bitmap.Config.ARGB_8888); // 蒙版
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect((0), (0), imageBitmap.getWidth(), imageBitmap.getHeight());
            final RectF rectF = new RectF(new Rect((0), (0), imageBitmap.getWidth(), imageBitmap.getHeight()));
            paint.setAntiAlias(true); // 抗锯齿
            paint.setDither(true);
            canvas.drawARGB((0), (0), (0), (0));
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            final Rect src = new Rect((0), (0), imageBitmap.getWidth(), imageBitmap.getHeight());
            canvas.drawBitmap(imageBitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * drawSizeOfBitmap 绘制指定大小的图片
     * 2023-04-19
     * @param width    宽度
     * @param height   高度
     * @param perName  前缀目录 (结尾不要有"/")
     * @param fileName 文件名称 (结尾不要有.png) 只支持png文件
     * @param isFile   是否在Assets目录内
     * @param context  上下文
     * @return Bitmap资源
     */
    @Nullable
    private static Bitmap drawSizeOfBitmap(int width, int height, String perName, String fileName, boolean isFile, Context context) {
        try {
            Bitmap imageBitmap = CanvasUtils.getImageFromAssetsFile(perName, fileName, isFile, context);
            int bitMapWidth = imageBitmap.getWidth();
            int bitMapHeight = imageBitmap.getHeight();
            float widthScale = width * 1.0f / bitMapWidth;
            float heightScale = height * 1.0f / bitMapHeight;
            Matrix matrix = new Matrix();
            matrix.postScale(widthScale, heightScale, (0), (0));
            Bitmap bmpRet = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); // ARGB_8888 色深32位压缩
            Canvas canvas = new Canvas(bmpRet);
            Paint paint = new Paint();
            canvas.drawBitmap(imageBitmap, matrix, paint);
            return bmpRet;
        } catch (Exception e) {
            return null;
        }
    }

}
