package com.xfy.lesson;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by Xiong.Fangyu on 2020/7/14
 *
 * 测试渐变色类型
 */
public class ShaderTypeView extends View {
    public ShaderTypeView(Context context) {
        super(context);
    }

    public ShaderTypeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShaderTypeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ShaderTypeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int startColor = Color.RED;
    private int endColor = Color.GREEN;

    private LinearGradient gradient;
    private Shader.TileMode mode = Shader.TileMode.CLAMP;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            int w = right - left;
            int start = w / 3;
            gradient = new LinearGradient(start, 0, start * 2, 0, startColor, endColor, mode);
        }
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
        int start = getWidth() / 3;
        gradient = new LinearGradient(start, 0, start * 2, 0, startColor, endColor, mode);
        invalidate();
    }

    public void setEndColor(int endColor) {
        this.endColor = endColor;
        int start = getWidth() / 3;
        gradient = new LinearGradient(start, 0, start * 2, 0, startColor, endColor, mode);
        invalidate();
    }

    public void setMode(Shader.TileMode mode) {
        this.mode = mode;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setShader(gradient);
        canvas.drawRect(0,0,getWidth(),getHeight(), paint);
    }
}
