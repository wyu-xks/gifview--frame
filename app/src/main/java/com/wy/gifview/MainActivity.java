package com.wy.gifview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.wy.gifview.config.ApngConstants;
import com.wy.gifview.utils.FrameUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FrameUtil frameUtil;
    private Button start;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageview);
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        frameUtil = new FrameUtil(MainActivity.this, ApngConstants.list, imageView, 60, 1);
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                frameUtil.startAnimation();
            }
        }, 500);

    }
}
