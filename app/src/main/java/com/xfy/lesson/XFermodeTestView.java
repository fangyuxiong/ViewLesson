package com.xfy.lesson;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by XiongFangyu on 2017/5/31.
 *
 * 测试XFermode
 */
public class XFermodeTestView extends View {
    private static final int HORIZONTAL_COUNT = 4;

    private RoundOrRectDrawable drawable;
    private int drawableWidth;
    private int modeCount = 0;
    private PorterDuff.Mode[] modes;
    private int horizontalMargin = 0;
    private int verticalMargin = 0;

    private Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    private Canvas bCanvas = new Canvas(bitmap);

    public XFermodeTestView(Context context) {
        this(context, null);
    }

    public XFermodeTestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XFermodeTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public XFermodeTestView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Paint textpaint;

    private void init() {
        drawable = new RoundOrRectDrawable();
        drawable.setOffset(dp2px(10));
        modes = PorterDuff.Mode.values();
        modeCount = modes.length;
        horizontalMargin = dp2px(2);
        verticalMargin = horizontalMargin;
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        textpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textpaint.setTextSize(40);
    }

    @Override
    protected void onMeasure(int w, int h) {
        super.onMeasure(w, h);
        final int mw = getMeasuredWidth();
        final int eachW = (mw - (HORIZONTAL_COUNT - 1) * horizontalMargin) / HORIZONTAL_COUNT;
        drawable.setBounds(0, 0, eachW, eachW);
        drawableWidth = eachW;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < modeCount ; i ++) {
            drawRectAndRound(canvas, i);
        }
    }

    private void drawRectAndRound(Canvas canvas, int i) {
        canvas.save();
        final Xfermode xfermode = new PorterDuffXfermode(modes[i]);
        final int hc = i % HORIZONTAL_COUNT;
        final int vc = i / HORIZONTAL_COUNT;
        final int dx = hc * (drawableWidth + horizontalMargin);
        final int dy = vc * (drawableWidth + verticalMargin);
        drawable.setDisplayX(dx);
        drawable.setDisplayY(dy);
        drawSrcDest(canvas, xfermode);

        /// 取刚绘制的中心点颜色，用来设置字体颜色
        bCanvas.save();
        bCanvas.translate(-dx-(drawableWidth>>1),-dy-(drawableWidth>>1));
        drawSrcDest(bCanvas, xfermode);
        bCanvas.restore();
        textpaint.setColor(getBitmapColor());

        String text = modes[i].toString();
        float x = dx + drawableWidth / 2f - textpaint.measureText(text) / 2;
        float y = dy + drawableWidth / 2f;
        canvas.drawText(text, x, y, textpaint);
        canvas.restore();
    }

    private void drawSrcDest(Canvas canvas, Xfermode xfermode) {
        drawable.setDrawRect(true);
        drawable.setMode(null);
        drawable.draw(canvas);
        drawable.setDrawRect(false);
        drawable.setMode(xfermode);
        drawable.draw(canvas);
    }

    /**
     * 获取bitmap中颜色，并取反色，但alpha保持1
     */
    private int getBitmapColor() {
        int color = bitmap.getPixel(0,0);
        int a = Color.alpha(color);
        if (a == 0)
            return Color.BLACK;
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.argb(255, 255-r,255-g,255-b);
    }

    private int dp2px(float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }

    public void setSrcColor(int srcColor) {
        drawable.setSrcColor(srcColor);
        invalidate();
    }

    public void setDestColor(int destColor) {
        drawable.setDestColor(destColor);
        invalidate();
    }
}
