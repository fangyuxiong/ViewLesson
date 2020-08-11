package com.xfy.lesson.lactivity;

import android.graphics.Color;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.xfy.lesson.ColorChooser;
import com.xfy.lesson.R;
import com.xfy.lesson.ShaderTypeView;

/**
 * Created by Xiong.Fangyu on 2020/7/14
 */
public class Lesson2Image6 extends FragmentActivity {
    private ShaderTypeView mClamp;
    private ShaderTypeView mRepeat;
    private ShaderTypeView mMirror;

    private View mSettingLayout;
    private Button mSetBtn;

    private int startColor = Color.RED;
    private int endColor = Color.GREEN;

    private boolean inSetting = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_l2_i6);
        mClamp = findViewById(R.id.clamp);
        mClamp.setMode(Shader.TileMode.CLAMP);
        mRepeat = findViewById(R.id.repeat);
        mRepeat.setMode(Shader.TileMode.REPEAT);
        mMirror = findViewById(R.id.mirror);
        mMirror.setMode(Shader.TileMode.MIRROR);

        View mStartColor = findViewById(R.id.start_color);
        View mEndColor = findViewById(R.id.end_color);
        mSettingLayout = findViewById(R.id.setting_layout);
        mSetBtn = findViewById(R.id.set_btn);

        mSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inSetting) {
                    mClamp.setStartColor(startColor);
                    mClamp.setEndColor(endColor);
                    mRepeat.setStartColor(startColor);
                    mRepeat.setEndColor(endColor);
                    mMirror.setStartColor(startColor);
                    mMirror.setEndColor(endColor);
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
