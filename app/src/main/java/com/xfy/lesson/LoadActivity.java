package com.xfy.lesson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.xfy.lesson.lactivity.Lesson1Image1;
import com.xfy.lesson.lactivity.Lesson1Image2;
import com.xfy.lesson.lactivity.Lesson2Image10;
import com.xfy.lesson.lactivity.Lesson2Image4;
import com.xfy.lesson.lactivity.Lesson2Image6;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Xiong.Fangyu on 2020/7/14
 */
public class LoadActivity extends Activity implements View.OnClickListener {
    private static final Map<Integer, Class<? extends Activity>> idActivity = new HashMap<>();

    static {
        idActivity.put(R.id.l1_i1, Lesson1Image1.class);
        idActivity.put(R.id.l1_i2, Lesson1Image2.class);
        idActivity.put(R.id.l2_i4, Lesson2Image4.class);
        idActivity.put(R.id.l2_i6, Lesson2Image6.class);
        idActivity.put(R.id.l2_i10, Lesson2Image10.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_load);
        for (Integer id : idActivity.keySet()) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Class<? extends Activity> clz = idActivity.get(id);
        if (clz == null)
            return;
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }
}
