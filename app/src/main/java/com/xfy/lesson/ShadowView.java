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
 * Created by Xiong.Fangyu on 2020-05-06
 *
 * 黑色阴影
 */
public class ShadowView extends View {
    public ShadowView(Context context) {
        this(context, null);
    }

    public ShadowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private LinearGradient gradient;

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setShader(gradient);
        canvas.drawRect(0,0, getWidth(), getHeight(), paint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            gradient = new LinearGradient(0, 0, 0, bottom - top, Color.TRANSPARENT, Color.BLACK, Shader.TileMode.CLAMP);
        }
    }
}
