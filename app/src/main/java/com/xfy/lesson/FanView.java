package com.xfy.lesson;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiong.Fangyu on 2020-04-21
 *
 * 饼状图测试
 */
public class FanView extends View {
    public FanView(Context context) {
        this(context, null);
    }

    public FanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        if (isInEditMode()) {
            addData("一季度", 0.15f, Color.RED);
            addData("二季度", 0.3f, Color.GREEN);
            addData("三季度", 0.35f, Color.BLUE);
            addData("四季度", 0.2f, Color.BLACK);
        }

        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.FanView, defStyleAttr, 0);

        float textSize = 40;

        final int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.FanView_fv_textSize:
                    textSize = a.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.FanView_fv_textColor:
                    setTextColor(a.getColor(attr, Color.WHITE));
                    break;
            }
        }
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        a.recycle();
    }

    private List<Data> data;
    private final Paint paint;
    private RectF fanRect;
    private int textColor = Color.WHITE;
    private boolean drawInPath = false;

    public void setTextSize(int unit, float size) {
        Context c = getContext();
        Resources r;

        if (c == null) {
            r = Resources.getSystem();
        } else {
            r = c.getResources();
        }

        paint.setTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()));
        invalidate();
    }

    public void setTextColor(int color) {
        textColor = color;
        invalidate();
    }

    public void addData(String name, float percent, int color) {
        addData(new Data(name, percent, color));
    }

    public void setDrawInPath(boolean drawInPath) {
        this.drawInPath = drawInPath;
        invalidate();
    }

    public void addData(Data d) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.add(d);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ///比较简单，使用默认就行
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /// 可以考虑通过设置半径来自定义宽高
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (data == null)
            return;
        checkData();
        int w = getWidth();
        int h = getHeight();
        int l = getPaddingLeft();
        int t = getPaddingTop();
        w = w - l - getPaddingRight();
        h = h - t - getPaddingBottom();
        int diameter = Math.min(w, h);
        l = l + (w - diameter) / 2;
        t = t + (h - diameter) / 2;
        if (fanRect == null) {
            fanRect = new RectF(l, t, l + diameter, t + diameter);
        } else {
            fanRect.set(l, t, l + diameter, t + diameter);
        }

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        float startAngle = 0;
        float halfTextSize = paint.getTextSize() / 2;
        for (Data d : data) {
            paint.setColor(d.color);
            float angle = d.percent * 360;
            canvas.drawArc(fanRect, startAngle, angle, true, paint); /// 绘制扇形
            /// 绘制文字
            if (d.textColorSetted) {
                paint.setColor(d.textColor);
            } else {
                paint.setColor(textColor);
            }
            String text = d.name + String.format("(%.1f%%)", d.percent * 100);
            float textAngle = angle / 2 + startAngle;
            if (drawInPath) {
//            第二种绘制方式，沿着扇形中心绘制文本
                float textR = diameter / 10;
                float textX = (float) (fanRect.centerX() + diameter * Math.cos(Math.toRadians(textAngle)));
                float textY = (float) (fanRect.centerY() + diameter * Math.sin(Math.toRadians(textAngle)));
                d.setPath(fanRect.centerX(), fanRect.centerY(), textX, textY);
                canvas.drawTextOnPath(text, d.path, textR, halfTextSize, paint);
            } else {
//            第一种绘制方式，对齐文字中心和扇形中心
                float textR = diameter / 3;
                float textX = (float) (fanRect.centerX() + textR * Math.cos(Math.toRadians(textAngle)));
                float textY = (float) (fanRect.centerY() + textR * Math.sin(Math.toRadians(textAngle)));
                canvas.drawText(text, textX - paint.measureText(text) / 2, textY + halfTextSize, paint);
            }
            startAngle += angle;
        }
    }

    private void checkData() {
        float all = 0;
        for (Data d : data) {
            all += d.percent;
        }
        if (all == 1)
            return;
        for (Data d : data) {
            d.percent = d.percent / all;
        }
    }

    public static final class Data {
        final String name;
        float percent;
        public int color;
        private int textColor;
        private boolean textColorSetted = false;
        final Path path = new Path();

        public Data(String name, float percent, int color) {
            this.name = name;
            this.percent = percent;
            this.color = color;
        }

        public void setTextColor(int color) {
            textColor = color;
            textColorSetted = true;
        }

        private void setPath(float x1, float y1, float x2, float y2) {
            path.reset();
            path.moveTo(x1, y1);
            path.lineTo(x2, y2);
            path.close();
        }
    }
}
