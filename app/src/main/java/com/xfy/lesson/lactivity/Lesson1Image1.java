package com.xfy.lesson.lactivity;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.xfy.lesson.ColorChooser;
import com.xfy.lesson.PolylineView;
import com.xfy.lesson.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiong.Fangyu on 2020/7/14
 */
public class Lesson1Image1 extends FragmentActivity {
    private PolylineView mPolyline;
    private SeekBar mLineSeek;
    private View mLineColor1;
    private View mLineColor2;
    private View mLineColor3;
    private CheckBox mSmoothPath;
    private Button mSetBtn;
    private View mSettingLayout;

    private int[] colors;
    private boolean inSetting = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_l1_i1);
        mPolyline = findViewById(R.id.polyline);
        initPolyline();
        mLineSeek = findViewById(R.id.line_seek);
        mLineColor1 = findViewById(R.id.line_color1);
        mLineColor2 = findViewById(R.id.line_color2);
        mLineColor3 = findViewById(R.id.line_color3);
        mSmoothPath = findViewById(R.id.smooth_path);
        mSetBtn = findViewById(R.id.set_btn);
        mSettingLayout = findViewById(R.id.setting_layout);

        colors = new int[] {
                Color.RED, Color.GREEN, Color.BLUE
        };

        mLineColor1.setOnClickListener(new ColorN(0));
        mLineColor2.setOnClickListener(new ColorN(1));
        mLineColor3.setOnClickListener(new ColorN(2));

        mSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inSetting) {
                    mPolyline.setLineWidth(TypedValue.COMPLEX_UNIT_DIP, mLineSeek.getProgress());
                    for (int i = 0; i < 3; i ++) {
                        mPolyline.setLineColor(i, colors[i]);
                    }
                    mPolyline.setSmoothPath(mSmoothPath.isChecked());
                    mPolyline.invalidate();
                }
                mSettingLayout.setVisibility(inSetting ? View.GONE : View.VISIBLE);
                inSetting = !inSetting;
                mSetBtn.setText(inSetting ? "设置并查看效果" : "打开设置");
            }
        });
    }

    private void initPolyline() {
        List<PointF> test = new ArrayList<>(10);
        mPolyline.setMinX(0);
        mPolyline.setMaxX(10);
        mPolyline.setMinY(0);
        mPolyline.setMaxY(10);
        mPolyline.setXAxisName("X");
        mPolyline.setYAxisName("Y");
        mPolyline.setAxisTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
        test.add(new PointF(1, 3));
        test.add(new PointF(3, 5));
        test.add(new PointF(5, 6));
        test.add(new PointF(8, 9));
        mPolyline.addLine(test, Color.RED);

        test = new ArrayList<>(10);
        test.add(new PointF(1, 1));
        test.add(new PointF(3, 2));
        test.add(new PointF(5, 5));
        test.add(new PointF(8, 7));
        mPolyline.addLine(test, Color.GREEN);

        test = new ArrayList<>(10);
        test.add(new PointF(1, 0));
        test.add(new PointF(3, 3));
        test.add(new PointF(5, 4));
        test.add(new PointF(8, 10));
        mPolyline.addLine(test, Color.BLUE);
    }

    private final class ColorN implements View.OnClickListener {
        final int n;

        private ColorN(int n) {
            this.n = n;
        }

        @Override
        public void onClick(final View v) {
            chooseColor(new ColorChooser.OnColorListener() {

                @Override
                public void onBack() {

                }

                @Override
                public void onEnsure(int color) {
                    colors[n] = color;
                    v.setBackgroundColor(color);
                }
            });
        }
    }

    private void chooseColor(ColorChooser.OnColorListener c) {
        ColorChooser cc = new ColorChooser();
        cc.show(getSupportFragmentManager(), "colordialog");
        cc.setOnColorChangeListenter(c);
    }
}
