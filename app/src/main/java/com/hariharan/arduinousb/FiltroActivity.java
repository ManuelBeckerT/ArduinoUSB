package com.hariharan.arduinousb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;


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
        Tiempo(3);
    }


    public void setUiEnabled(boolean bool) {
        deporteButton.setEnabled(bool);
        categoriaButton.setEnabled(bool);
        categoriaButton.setEnabled(bool);
    }

    public void Tiempo(int segundos)
    {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {

                
            }

        }, segundos*1000);
    }

    public void onClickDeporte(View view) {
        startActivity(new Intent(getApplicationContext(), DeporteActivity.class));

    }

    public void onClickCategoria(View view) {
        startActivity(new Intent(getApplicationContext(), CategoriaActivity.class));

    }

    public void onClickCuerpo(View view) {
        startActivity(new Intent(getApplicationContext(), CuerpoActivity.class));

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

