package com.hariharan.arduinousb;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class StartActivity extends Activity {
    public final String ACTION_USB_PERMISSION = "com.hariharan.arduinousb.USB_PERMISSION";
    Button startButton, clearButton, stopButton;
    //sendButton
    TextView textView;
    EditText editText;
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        startButton = (Button) findViewById(R.id.buttonStart);
        textView = (TextView) findViewById(R.id.textView);
        setUiEnabled(true);



    }

    public void setUiEnabled(boolean bool) {
        startButton.setEnabled(bool);
        textView.setEnabled(bool);

    }

    public void onClickStart(View view) {
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

