package com.xfy.lesson.lactivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.xfy.lesson.ColorChooser;
import com.xfy.lesson.FanView;
import com.xfy.lesson.R;

/**
 * Created by Xiong.Fangyu on 2020/7/14
 */
public class Lesson1Image2 extends FragmentActivity {

    private SeekBar mTextSeek;
    private View mTextColorAll;
    private CheckBox mTextPathCheck;
    private FanView mFan;
    private View setting_layout;

    private int textColorAll = Color.WHITE;
    private Integer[] textColors;
    private int[] bgColors;
    private FanView.Data[] data;

    private boolean inSetting = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_l1_i2);
        setting_layout = findViewById(R.id.setting_layout);
        mTextSeek = findViewById(R.id.text_seek);
        mTextColorAll = findViewById(R.id.text_color_all);
        mTextPathCheck = findViewById(R.id.text_path_check);
        View mTextColor1 = findViewById(R.id.text_color1);
        View mTextColor2 = findViewById(R.id.text_color2);
        View mTextColor3 = findViewById(R.id.text_color3);
        View mTextColor4 = findViewById(R.id.text_color4);
        View mColor1 = findViewById(R.id.color1);
        View mColor2 = findViewById(R.id.color2);
        View mColor3 = findViewById(R.id.color3);
        View mColor4 = findViewById(R.id.color4);
        final Button mSetBtn = findViewById(R.id.set_btn);
        mFan = findViewById(R.id.fan);

        mTextColorAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseColor(new ColorChooser.OnColorListener() {
                    @Override
                    public void onBack() {

                    }

                    @Override
                    public void onEnsure(int color) {
                        textColorAll = color;
                        mTextColorAll.setBackgroundColor(color);
                    }
                });
            }
        });
        textColors = new Integer[4];
        mTextColor1.setOnClickListener(new TextColorN(0));
        mTextColor2.setOnClickListener(new TextColorN(1));
        mTextColor3.setOnClickListener(new TextColorN(2));
        mTextColor4.setOnClickListener(new TextColorN(3));

        mColor1.setOnClickListener(new BGColorN(0));
        mColor2.setOnClickListener(new BGColorN(1));
        mColor3.setOnClickListener(new BGColorN(2));
        mColor4.setOnClickListener(new BGColorN(3));

        bgColors = new int[] {
                Color.RED, Color.GREEN, Color.BLUE, Color.BLACK
        };

        data = new FanView.Data[]{
                new FanView.Data("一季度", 0.15f, Color.RED),
                new FanView.Data("二季度", 0.3f, Color.GREEN),
                new FanView.Data("三季度", 0.35f, Color.BLUE),
                new FanView.Data("四季度", 0.2f, Color.BLACK)
        };

        for (FanView.Data d : data) {
            mFan.addData(d);
        }

        mSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inSetting) {
                    mFan.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSeek.getProgress());
                    mFan.setTextColor(textColorAll);
                    mFan.setDrawInPath(mTextPathCheck.isChecked());
                    for (int i = 0; i < 4; i++) {
                        if (textColors[i] != null)
                            data[i].setTextColor(textColors[i]);
                        data[i].color = bgColors[i];
                    }
                    mFan.invalidate();
                }
                setting_layout.setVisibility(inSetting ? View.GONE : View.VISIBLE);
                inSetting = !inSetting;
                mSetBtn.setText(inSetting ? "设置并查看效果" : "打开设置");
            }
        });
    }

    private final class BGColorN implements View.OnClickListener {
        final int n;
        BGColorN(int n) {
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
                    bgColors[n] = color;
                    v.setBackgroundColor(color);
                }
            });
        }
    }

    private final class TextColorN implements View.OnClickListener {
        int n;

        TextColorN(int n) {
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
                    textColors[n] = color;
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
