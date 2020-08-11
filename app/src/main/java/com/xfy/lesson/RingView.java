package com.xfy.lesson;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

/**
 * Created by Xiong.Fangyu on 2020-05-06
 *
 * 圆环渐变view
 */
public class RingView extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    public RingView(Context context) {
        this(context, null);
    }

    public RingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /// 由于要使用CLEAR模式，View不能开启硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    private int colorStart = Color.RED;
    private int colorEnd = Color.GREEN;

    //<editor-fold desc="Not Important">
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private LinearGradient gradient;

    private float percent;

    private Path clipPath;
    //</editor-fold>

    Xfermode mode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    private boolean clip = true;
    private boolean ring = true;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            int w = right - left;
            /// 通过设置mode为MIRROR，让渐变色变成 红-绿-红-绿 效果
            gradient = new LinearGradient(0, 0, w, 0, colorStart, colorEnd, Shader.TileMode.MIRROR);

            int h = bottom - top;
            if (clipPath == null) {
                clipPath = new Path();
            } else {
                clipPath.reset();
            }
            /// 以view中心点为中心画圆
            clipPath.addCircle(w >> 1, h >> 1, Math.min(w, h) >> 1, Path.Direction.CW);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        /// 绘制矩形长度为3倍view宽，让gradient在矩形中能多绘制2次镜面效果
        float w = width * 3;
        canvas.save();
        /// 切割圆形
        if (clip)
        canvas.clipPath(clipPath);
        /// 动画让画布往左移动，最大移动到两倍view宽，显示出第二次镜面效果后，动画重新开始
        canvas.translate(-2 * width * percent, 0);
        paint.setXfermode(null);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(gradient);
        canvas.drawRect(0, 0, w, height, paint);
        canvas.restore();

        if (ring) {
            paint.setXfermode(mode);
            /// 在中心绘制圆，由于MODE为CLEAR，相当于在中心切了一个圆形
            canvas.drawCircle(width >> 1, height >> 1, Math.min(width, height) >> 2, paint);
        }
    }

    public void doAnim() {
        ValueAnimator a = ValueAnimator.ofFloat(0, 1).setDuration(5000);
        a.setRepeatCount(ValueAnimator.INFINITE);
        a.setInterpolator(new LinearInterpolator());
        a.addUpdateListener(this);
        a.addListener(this);
        a.start();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        percent = (float) animation.getAnimatedValue();
        invalidate();
    }

    public void setColorStart(int colorStart) {
        this.colorStart = colorStart;
        gradient = new LinearGradient(0, 0, getWidth(), 0, colorStart, colorEnd, Shader.TileMode.MIRROR);
    }

    public void setColorEnd(int colorEnd) {
        this.colorEnd = colorEnd;
        gradient = new LinearGradient(0, 0, getWidth(), 0, colorStart, colorEnd, Shader.TileMode.MIRROR);
    }

    public void setClip(boolean clip) {
        this.clip = clip;
    }

    public void setRing(boolean ring) {
        this.ring = ring;
    }
}
