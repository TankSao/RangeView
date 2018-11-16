package com.example.administrator.rangeview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements RangeChangeListener {

    private RangeView range;
    private TextView startNum,endNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        range = findViewById(R.id.range);
        startNum = findViewById(R.id.startNum);
        endNum = findViewById(R.id.endNum);
        range.setOnRangeChangeListener(this);
    }

    @Override
    public void onRangeChange(float start, float end) {
        startNum.setText(start+"元");
        endNum.setText(end+"元");
    }
}
