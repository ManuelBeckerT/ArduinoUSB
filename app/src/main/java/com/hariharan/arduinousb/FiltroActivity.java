package com.hariharan.arduinousb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;
import android.os.CountDownTimer;
import  android.view.InputEvent;

import android.view.MotionEvent;



public class FiltroActivity extends Activity {
    public final String ACTION_USB_PERMISSION = "com.hariharan.arduinousb.USB_PERMISSION";
    Button deporteButton, categoriaButton, cuerpoButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);
        deporteButton = (Button) findViewById(R.id.buttonDeporte);
        categoriaButton = (Button) findViewById(R.id.buttonCategoria);
        cuerpoButton = (Button) findViewById(R.id.buttonCuerpo);
        setUiEnabled(true);
       // Timer timer = new Timer();
        //Tiempo(timer,5);
    }


    public void setUiEnabled(boolean bool) {
        deporteButton.setEnabled(bool);
        categoriaButton.setEnabled(bool);
        cuerpoButton.setEnabled(bool);
    }

   /* public void Tiempo(Timer timer,int segundos)
    {
        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                //TODO: Do something every second
            }

            public void onFinish() {

                finish();
                //YourActivity.finish();  outside the actvitiy

            }
        }.start();*/

     /*   @Override
        public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            countDownTimer.cancel();
            countDownTimer.start();
        }
        return super.onTouchEvent(event);
    }*/


    public void onClickDeporte(View view) {
        startActivity(new Intent(getApplicationContext(), DeporteActivity.class));

    }

    public void onClickCategoria(View view) {
        startActivity(new Intent(getApplicationContext(), CategoriaActivity.class));

    }

    public void onClickCuerpo(View view) {
        startActivity(new Intent(getApplicationContext(), Genero.class));

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

