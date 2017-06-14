package com.hariharan.arduinousb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.CountDownTimer;
import  android.view.InputEvent;

import android.view.MotionEvent;



public class Genero extends Activity {
    public final String ACTION_USB_PERMISSION = "com.hariharan.arduinousb.USB_PERMISSION";
    Button MujerButton, HombreButton, AtrasButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genero);
        MujerButton = (Button) findViewById(R.id.buttonMujer);
        HombreButton = (Button) findViewById(R.id.buttonHombre);
        AtrasButton = (Button) findViewById(R.id.buttonAtras);
        setUiEnabled(true);

    }


    public void setUiEnabled(boolean bool) {
        MujerButton.setEnabled(bool);
        HombreButton.setEnabled(bool);
        AtrasButton.setEnabled(bool);
    }


    public void onClickHombre(View view) {
        startActivity(new Intent(getApplicationContext(), Hombre.class));

    }

    public void onClickMujer(View view) {
        startActivity(new Intent(getApplicationContext(), Mujer.class));

    }

    public void onClickAtras(View view) {
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

