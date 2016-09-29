package wy.gifview_test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import wy.gifview_test.views.GifView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button start;
    private GifView gifView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gifView = (GifView) findViewById(R.id.work_gif);
        Button start =  (Button) findViewById(R.id.start);
        Button stop = (Button) findViewById(R.id.stop);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start:
                gifView.startAnimation();
                break;
            case R.id.stop:
                gifView.stopAnimation();
                break;
        }
    }
}
