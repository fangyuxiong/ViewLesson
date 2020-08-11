package com.xfy.lesson;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by XiongFangyu on 2017/5/31.
 * 绘制圆形或方形，主要用来重合绘制，配合XFermode使用
 */
public class RoundOrRectDrawable extends Drawable {

    private boolean isDrawRect = true;
    private int offset = 0;
    private Paint mPaint;

    private int displayX, displayY;

    private Rect drawRect;

    private int srcColor = 0xff0000ff;
    private int destColor = 0xffff0000;

    public RoundOrRectDrawable() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        drawRect = new Rect();
    }

    @Override
    public void setBounds(int l, int t, int r, int b) {
        super.setBounds(l, t, r, b);
        drawRect.set(l, t, r, b);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        final Rect rect = drawRect;
        rect.offset(displayX, displayY);
        if (isDrawRect) {
            mPaint.setColor(srcColor);
            canvas.drawRect(rect.left, rect.top, rect.right - offset, rect.bottom - offset, mPaint);
        } else {
            mPaint.setColor(destColor);
            canvas.drawCircle(rect.centerX() + (offset >> 1), rect.centerY() + (offset >> 1), (rect.width() >> 1) - (offset >> 1), mPaint);
        }
        rect.offset(-displayX, -displayY);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        if (mPaint != null)
            mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        if (mPaint != null)
            mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    public void setDrawRect(boolean drawRect) {
        this.isDrawRect = drawRect;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setMode(Xfermode mode) {
        mPaint.setXfermode(mode);
    }

    public void setDisplayX(int displayX) {
        this.displayX = displayX;
    }

    public void setDisplayY(int displayY) {
        this.displayY = displayY;
    }

    public void setSrcColor(int srcColor) {
        this.srcColor = srcColor;
    }

    public void setDestColor(int destColor) {
        this.destColor = destColor;
    }
}
