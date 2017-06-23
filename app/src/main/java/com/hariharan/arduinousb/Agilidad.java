package com.hariharan.arduinousb;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.hariharan.arduinousb.R;

public class Agilidad extends AppCompatActivity {
    Button AtrasButton;

    CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {

        public void onTick(long millisUntilFinished) {
            //TODO: Do something every second
        }

        public void onFinish() {

            finish();
            countDownTimer.cancel();
            startActivity(new Intent(getApplicationContext(), StartActivity.class));


        }
    }.start();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            countDownTimer.cancel();
            countDownTimer.start();
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AtrasButton = (Button) findViewById(R.id.buttonStart);
        setContentView(R.layout.activity_agilidad);
    }

    public void onClickAtras(View view) {
        startActivity(new Intent(getApplicationContext(), CategoriaActivity.class));
        countDownTimer.cancel();

    }
}
