package com.xfy.lesson.lactivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.xfy.lesson.ColorChooser;
import com.xfy.lesson.R;
import com.xfy.lesson.XFermodeTestView;

/**
 * Created by Xiong.Fangyu on 2020/7/14
 */
public class Lesson2Image10 extends FragmentActivity {
    private View mSrcColor;
    private View mDestColor;
    private View mSettingLayout;
    private Button mSetBtn;
    private XFermodeTestView mXfermodeVier;
    private int startColor = Color.RED;
    private int endColor = Color.GREEN;

    private boolean inSetting = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_l2_i10);
        mSrcColor = findViewById(R.id.src_color);
        mDestColor = findViewById(R.id.dest_color);
        mSettingLayout = findViewById(R.id.setting_layout);
        mSetBtn = findViewById(R.id.set_btn);
        mXfermodeVier = findViewById(R.id.xfermode_vier);

        mSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inSetting) {
                    mXfermodeVier.setSrcColor(startColor);
                    mXfermodeVier.setDestColor(endColor);
                }
                mSettingLayout.setVisibility(inSetting ? View.GONE : View.VISIBLE);
                inSetting = !inSetting;
                mSetBtn.setText(inSetting ? "设置并查看效果" : "打开设置");
            }
        });

        mSrcColor.setOnClickListener(new View.OnClickListener() {
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

        mDestColor.setOnClickListener(new View.OnClickListener() {
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
