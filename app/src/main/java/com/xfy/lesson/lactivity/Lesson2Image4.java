package com.xfy.lesson.lactivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.xfy.lesson.ColorChooser;
import com.xfy.lesson.R;
import com.xfy.lesson.RingView;

/**
 * Created by Xiong.Fangyu on 2020/7/14
 */
public class Lesson2Image4 extends FragmentActivity {
    private RingView mRingView;
    private View mSettingLayout;
    private Button mSetBtn;

    private int startColor = Color.RED;
    private int endColor = Color.GREEN;

    private boolean inSetting = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_l2_i4);
        mRingView = findViewById(R.id.ring_view);
        final RingView rect = findViewById(R.id.rect);
        rect.setClip(false);
        rect.setRing(false);
        rect.doAnim();
        final RingView circle = findViewById(R.id.circle);
        circle.setRing(false);
        circle.doAnim();

        mRingView.doAnim();
        View mStartColor = findViewById(R.id.start_color);
        View mEndColor = findViewById(R.id.end_color);
        mSettingLayout = findViewById(R.id.setting_layout);
        mSetBtn = findViewById(R.id.set_btn);

        mSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inSetting) {
                    mRingView.setColorStart(startColor);
                    mRingView.setColorEnd(endColor);
                    rect.setColorStart(startColor);
                    rect.setColorEnd(endColor);
                    circle.setColorStart(startColor);
                    circle.setColorEnd(endColor);
                }
                mSettingLayout.setVisibility(inSetting ? View.GONE : View.VISIBLE);
                inSetting = !inSetting;
                mSetBtn.setText(inSetting ? "设置并查看效果" : "打开设置");
            }
        });

        mStartColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                chooseColor(new ColorChooser.OnColorListener() {
                    @Override
                    public void onBack() {

                    }

                    @Override
                    public void onEnsure(int color) {
                        startColor = color;
                        v.setBackgroundColor(color);
                    }
                });
            }
        });

        mEndColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                chooseColor(new ColorChooser.OnColorListener() {
                    @Override
                    public void onBack() {

                    }

                    @Override
                    public void onEnsure(int color) {
                        endColor = color;
                        v.setBackgroundColor(color);
                    }
                });
            }
        });
    }

    private void chooseColor(ColorChooser.OnColorListener c) {
        ColorChooser cc = new ColorChooser();
        cc.show(getSupportFragmentManager(), "colordialog");
        cc.setOnColorChangeListenter(c);
    }
}
