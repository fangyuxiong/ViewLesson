package com.xfy.lesson;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

/**
 * Created by Xiong.Fangyu on 2020-05-08
 *
 * matrix set pre post polyToPoly 方法测试
 */
public class MatrixView extends ImageView {
    public MatrixView(Context context) {
        super(context);
    }

    public MatrixView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MatrixView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MatrixView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private Matrix matrix = new Matrix();

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

//        testSet(canvas);
//        testPrePost(canvas);
//        equalsToTestPrePost(canvas);
        testPolyToPoly(canvas);

        super.onDraw(canvas);
        canvas.restore();
    }

    private void testSet(Canvas canvas) {
        /// 不生效
        matrix.setRotate(45);
        /// 生效
        matrix.setTranslate(100, 0);

        canvas.concat(matrix);
    }

    private void testPrePost(Canvas canvas) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        matrix.setRotate(45);
        matrix.postTranslate(centerX, centerY);
        matrix.preTranslate(-centerX, -centerY);

        canvas.concat(matrix);
    }

    private void equalsToTestPrePost(Canvas canvas) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        canvas.translate(centerX, centerY);
        canvas.rotate(45);
        canvas.translate(-centerX, -centerY);
    }

    private void testPolyToPoly(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();

        /// 原始点，对应左上角，右上角，右下角，左下角
        float[] src = {
                0, 0,
                w, 0,
                w, h,
                0, h};
        float offset = w * 0.1f;
        /// 目标点，对应左上角，右上角，右下角，左下角
        float[] desc = {
                offset, offset,
                w - offset, 0,
                w - offset, h,
                offset, h - offset
        };

        matrix.setPolyToPoly(src, 0, desc, 0, 4);

        canvas.concat(matrix);
    }
}
