package com.hariharan.arduinousb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.CountDownTimer;
import  android.view.InputEvent;

import android.view.MotionEvent;



public class Genero extends Activity {
    public final String ACTION_USB_PERMISSION = "com.hariharan.arduinousb.USB_PERMISSION";
    ImageButton MujerButton, HombreButton;
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
        setContentView(R.layout.activity_genero);
        MujerButton = (ImageButton) findViewById(R.id.buttonMujer);
        HombreButton = (ImageButton) findViewById(R.id.buttonHombre);
        AtrasButton = (Button) findViewById(R.id.buttonAtras);
        setUiEnabled(true);

    }


    public void setUiEnabled(boolean bool) {
        MujerButton.setEnabled(bool);
        HombreButton.setEnabled(bool);
        AtrasButton.setEnabled(bool);
    }


    public void onClickHombre(View view) {
        countDownTimer.cancel();
        startActivity(new Intent(getApplicationContext(), Hombre.class));

    }

    public void onClickMujer(View view) {
        countDownTimer.cancel();
        startActivity(new Intent(getApplicationContext(), Mujer.class));

    }

    public void onClickAtras(View view) {
        countDownTimer.cancel();
        startActivity(new Intent(getApplicationContext(), FiltroActivity.class));

    }

    private void tvAppend(TextView tv, CharSequence text) {
        final TextView ftv = tv;
        final CharSequence ftext = text;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ftv.append(ftext);
            }
        });
    }

    //@Override
    //protected void onDestroy() {
    //    super.onDestroy();
    //   unregisterReceiver(mUsbReceiver);
    // }



}

