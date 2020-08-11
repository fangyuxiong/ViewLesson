package com.xfy.lesson;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Xiong.Fangyu on 2020-04-21
 *
 * 折线图
 */
public class PolylineView extends View {
    public PolylineView(Context context) {
        this(context, null);
    }

    public PolylineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PolylineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        if (isInEditMode()) {
            List<PointF> test = new ArrayList<>(10);
            setMinX(0);
            setMaxX(10);
            setMinY(0);
            setMaxY(10);
            setXAxisName("X");
            setYAxisName("Y");
            setAxisTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
            test.add(new PointF(1, 3));
            test.add(new PointF(3, 5));
            test.add(new PointF(5, 6));
            test.add(new PointF(8, 9));
            addLine(test, Color.RED);

            test = new ArrayList<>(10);
            test.add(new PointF(1, 1));
            test.add(new PointF(3, 2));
            test.add(new PointF(5, 5));
            test.add(new PointF(8, 7));
            addLine(test, Color.GREEN);

            test = new ArrayList<>(10);
            test.add(new PointF(1, 0));
            test.add(new PointF(3, 3));
            test.add(new PointF(5, 4));
            test.add(new PointF(8, 10));
            addLine(test, Color.BLUE);
        }
    }

    private static final int AXIS_TEXT_PADDING = 5;

    ///未使用，可以考虑绘制到坐标轴上
    private String xAxisName;
    private String yAxisName;
    private List<LineData> lineDataList;
    ///宽高比
    private float radius = 1f;
    private float axisTextSize = 20;

    private float minX;
    private float minY;
    private float maxX;
    private float maxY;

    private String minXName;
    private String minYName;
    private String maxXName;
    private String maxYName;

    private int[] axisPadding = new int[2];

    private final Paint paint;
    private float lineWidth = 5;
    private boolean smoothPath = false;

    public void setXAxisName(String name) {
        xAxisName = name;
    }

    public void setYAxisName(String name) {
        yAxisName = name;
    }

    public void setMinX(float minX) {
        this.minX = minX;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    public void setMaxX(float maxX) {
        this.maxX = maxX;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public void setMinXName(String minXName) {
        this.minXName = minXName;
    }

    public void setMinYName(String minYName) {
        this.minYName = minYName;
    }

    public void setMaxXName(String maxXName) {
        this.maxXName = maxXName;
    }

    public void setMaxYName(String maxYName) {
        this.maxYName = maxYName;
    }

    public void setAxisTextSize(float axisTextSize) {
        this.axisTextSize = axisTextSize;
    }

    public void addLine(List<PointF> points, int color) {
        LineData lineData = new LineData(points, color);
        if (lineDataList == null) {
            lineDataList = new ArrayList<>();
        }
        lineDataList.add(lineData);
    }

    public void setLineColor(int index, int color) {
        LineData data = lineDataList.get(index);
        data.color = color;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setLineWidth(int unit, float num) {
        lineWidth = TypedValue.applyDimension(unit, num, getResources().getDisplayMetrics());
        invalidate();
    }

    public void setSmoothPath(boolean smoothPath) {
        this.smoothPath = smoothPath;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wmode = MeasureSpec.getMode(widthMeasureSpec);
        int wsize = MeasureSpec.getSize(widthMeasureSpec);
        int hmode = MeasureSpec.getMode(heightMeasureSpec);
        int hsize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0, height = 0;
        /// 有绝对值的情况
        if (wmode == MeasureSpec.EXACTLY || hmode == MeasureSpec.EXACTLY) {
            if (wmode == MeasureSpec.EXACTLY && hmode == MeasureSpec.EXACTLY) {
                width = wsize;
                height = hsize;
            } else if (wmode == MeasureSpec.EXACTLY) {
                width = wsize;
                height = (int) (width / radius);
                if (hmode == MeasureSpec.AT_MOST) {
                    height = Math.min(height, hsize);
                }
            } else {
                height = hsize;
                width = (int) (height * radius);
                if (wmode == MeasureSpec.AT_MOST) {
                    width = Math.min(width, wsize);
                }
            }
        }
        /// 都没有绝对值情况
        else if (wmode == MeasureSpec.AT_MOST) {
            width = wsize;
            height = (int) (width / radius);
            if (hmode == MeasureSpec.AT_MOST) {
                height = Math.min(height, hsize);
            }
        } else if (hmode == MeasureSpec.AT_MOST) {
            height = hsize;
            width = (int) (height * radius);
        }
        /// 剩下的情况，宽取屏幕宽
        else {
            width = getResources().getDisplayMetrics().widthPixels;
            height = (int) (width / radius);
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int width = getWidth() - paddingLeft - getPaddingRight();
        int height = getHeight() - paddingTop - getPaddingBottom();

        final int rc = canvas.save();
        canvas.translate(paddingLeft, paddingTop);
        drawAxis(width, height, canvas);
        canvas.translate(axisPadding[0], 0);
        drawLines(width - axisPadding[0], height - axisPadding[1], canvas);
        canvas.restoreToCount(rc);
    }

    private void initName() {
        if (minXName == null)
            minXName = minX + "";
        if (minYName == null)
            minYName = minY + "";
        if (maxXName == null)
            maxXName = maxX + "";
        if (maxYName == null)
            maxYName = maxY + "";
    }

    private void drawAxis(int w, int h, Canvas canvas) {
        initName();

        paint.setTextSize(axisTextSize);
        float maxYTextSize = paint.measureText(maxYName);
        float minYTextSize = paint.measureText(minYName);
        float maxTextSize = Math.max(maxYTextSize, minYTextSize);

        float startX = maxTextSize + AXIS_TEXT_PADDING;
        float startY = h - axisTextSize - AXIS_TEXT_PADDING;
        float endX = w;
        float endY = 0;

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        ///绘制X轴
        canvas.drawLine(startX, startY, endX, startY, paint);
        ///绘制Y轴
        canvas.drawLine(startX, startY, startX, endY, paint);
        paint.setStyle(Paint.Style.FILL);
        ///绘制X轴最小值和最大值
        canvas.drawText(minXName, startX, startY + axisTextSize, paint);
        canvas.drawText(maxXName, endX - paint.measureText(maxXName), startY + axisTextSize, paint);
        ///绘制Y轴最小值和最大值
        canvas.drawText(minYName, Math.max(0, startX - minYTextSize - AXIS_TEXT_PADDING), startY, paint);
        canvas.drawText(maxYName, Math.max(0, startX - maxYTextSize - AXIS_TEXT_PADDING), endY + axisTextSize, paint);

        axisPadding[0] = (int) startX;
        axisPadding[1] = (int) (axisTextSize + AXIS_TEXT_PADDING);
    }

    private void drawLines(int w, int h, Canvas canvas) {
        if (lineDataList == null)
            return;
        int len = lineDataList.size();
        paint.setStyle(Paint.Style.STROKE);
        float old = paint.getStrokeWidth();
        paint.setStrokeWidth(lineWidth);
        for (int i = 0; i < len; i ++) {
            final LineData lineData = lineDataList.get(i);
            paint.setColor(lineData.color);
            if (smoothPath) {
                canvas.drawPath(lineData.generateBesierPath(w, h, minX, minY, maxX, maxY),
                        paint);
            } else {
                canvas.drawPath(
                        lineData.generatePath(w, h, minX, minY, maxX, maxY),
                        paint);
            }
        }
        paint.setStrokeWidth(old);
    }

    private static final class LineData {
        final List<PointF> points;
        int color;
        private Path path;

        private static final float control = 0.6f;

        private LineData(List<PointF> points, int color) {
            this.color = color;
            this.points = points;
        }

        Path generatePath(int w, int h,
                          float minX, float minY, float maxX, float maxY) {
            if (path == null)
                path = new Path();
            path.reset();

            final float x = maxX - minX;
            final float y = maxY - minY;

            PointF pf = points.get(0);
            path.moveTo(getX(w, minX, x, pf.x), getY(h, minY, y, pf.y));
            for (int l = points.size(), i = 1; i < l; i ++) {
                pf = points.get(i);
                path.lineTo(getX(w, minX, x, pf.x), getY(h, minY, y, pf.y));
            }
            return path;
        }

        private float getX(int w, float minX, float xOffset, float x) {
            return w * (x - minX) / xOffset;
        }

        private float getY(int h, float minY, float yOffset, float y) {
            return h - h * (y - minY) / yOffset;
        }

        Path generateBesierPath(int w, int h,
                                float minX, float minY, float maxX, float maxY) {
            if (path == null)
                path = new Path();
            path.reset();

            final float x = maxX - minX;
            final float y = maxY - minY;

            PointF pf = points.get(0);
            float preX = pf.x;
            float preY = pf.y;
            path.moveTo(getX(w, minX, x, pf.x), getY(h, minY, y, pf.y));
            for (int l = points.size(), i = 1; i < l; i ++) {
                pf = points.get(i);
                boolean odd = i % 2 == 1;
                float cx1 = odd ? preX + (pf.x - preX) * control : preX;
                float cy1 = odd ? preY : preY + (pf.y - preY ) * control;
                float cx2 = odd ? pf.x : pf.x - (pf.x - preX) * control;
                float cy2 = odd ? pf.y - (pf.y - preY) * control : pf.y;
                path.cubicTo(getX(w, minX, x, cx1), getY(h, minY, y, cy1),
                        getX(w, minX, x, cx2), getY(h, minY, y, cy2),
                        getX(w, minX, x, pf.x), getY(h, minY, y, pf.y));
                preX = pf.x;
                preY = pf.y;
            }
            return path;
        }
    }
}
