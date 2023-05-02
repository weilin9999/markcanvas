package com.weilin.markcanvas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.weilin.markcanvas.markutils.JsonFileUtils;
import com.weilin.markcanvas.markutils.MarkDraw;

public class MainActivity extends AppCompatActivity {

    MarkDraw markDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String local = JsonFileUtils.readJsonFile(("mark_templates/mark_t2.json"),getApplicationContext());
        markDraw = findViewById(R.id.markDraw);
        markDraw.drawSet(local,(0),(0));
        Button button1 = findViewById(R.id.setting_1);
        Button button2 = findViewById(R.id.setting_2);
        Button button3 = findViewById(R.id.setting_3);
        Button button4 = findViewById(R.id.setting_4);
        button1.setOnClickListener(view -> markDraw.drawSet(local,(0),(0)));
        button2.setOnClickListener(view -> markDraw.drawSet(local,(1),(0)));
        button3.setOnClickListener(view -> markDraw.drawSet(local,(2),(0)));
        button4.setOnClickListener(view -> markDraw.drawSet(local,(0),(1)));

        SeekBar seekBarBlur = findViewById(R.id.seekBarBlur);
        TextView seekBarBlurText = findViewById(R.id.seekBarBlurText);
        seekBarBlur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) { // 当滑块进度改变时，会执行该方法下的代码
                seekBarBlurText.setText((String) ("当前模糊度："+i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { // 当开始滑动滑块时，会执行该方法下的代码

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { // 当结束滑动滑块时，会执行该方法下的代码

            }
        });

        Button button5 = findViewById(R.id.setting_5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                //最大分配内存
                int memory = activityManager.getMemoryClass();
                System.out.println("最大分配内存 memory: "+memory);
                //最大分配内存获取方法2
                float maxMemory = (float) (Runtime.getRuntime().maxMemory() * 1.0/ (1024 * 1024));
                //当前分配的总内存
                float totalMemory = (float) (Runtime.getRuntime().totalMemory() * 1.0/ (1024 * 1024));
                //剩余内存
                float freeMemory = (float) (Runtime.getRuntime().freeMemory() * 1.0/ (1024 * 1024));
                System.out.println("最大分配内存 maxMemory: "+maxMemory);
                System.out.println("当前分配的总内存 totalMemory: "+totalMemory);
                System.out.println("剩余内存 freeMemory: "+freeMemory);
            }
        });
    }
}