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
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CategoriaActivity extends Activity {
    public final String ACTION_USB_PERMISSION = "com.hariharan.arduinousb.USB_PERMISSION";
    ImageButton C1Button, C2Button, C3Button, C4Button;
    ImageView Observa;
    Button clearButton, AtrasButton;
    TextView textView;
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;

    CountDownTimer countDownTimer = new CountDownTimer(15000, 1000) {

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

    UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() { //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(byte[] arg0) {
            String data = null;
            try {
                data = new String(arg0, "UTF-8");
                data.concat("/n");
                tvAppend(textView, data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        }
    };


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { //Broadcast Receiver to automatically start and stop the Serial connection.
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
                boolean granted = intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                if (granted) {
                    connection = usbManager.openDevice(device);
                    serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
                    if (serialPort != null) {
                        if (serialPort.open()) { //Set Serial Connection Parameters.
                            serialPort.setBaudRate(9600);
                            serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                            serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                            serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                            serialPort.read(mCallback);
                            tvAppend(textView,"Serial Connection Opened!\n");

                        } else {
                            Log.d("SERIAL", "PORT NOT OPEN");
                        }
                    } else {
                        Log.d("SERIAL", "PORT IS NULL");
                    }
                } else {
                    Log.d("SERIAL", "PERM NOT GRANTED");
                }
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                Start();
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                Stop();

            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        usbManager = (UsbManager) getSystemService(this.USB_SERVICE);
        C1Button = (ImageButton) findViewById(R.id.buttonC1);
        C2Button = (ImageButton) findViewById(R.id.buttonC2);
        C3Button = (ImageButton) findViewById(R.id.buttonC3);
        C4Button = (ImageButton) findViewById(R.id.buttonC4);
        AtrasButton = (Button) findViewById(R.id.buttonAtras);
        clearButton = (Button) findViewById(R.id.buttonClear);
        Observa = (ImageView) findViewById(R.id.Observa);
        Observa.setVisibility(View.GONE);

        textView = (TextView) findViewById(R.id.textView);
        //setUiEnabled(false);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(broadcastReceiver, filter);
        //tvAppend(textView,"Antes de Start");
        Start();
        // tvAppend(textView,"Despues de Start");


    }

    /*public void setUiEnabled(boolean bool) {
        C1Button.setEnabled(bool);
        C2Button.setEnabled(bool);
        C3Button.setEnabled(bool);
        C4Button.setEnabled(bool);
        textView.setEnabled(bool);

    }*/

    public void Start() {
        //tvAppend(textView,"Entro a start");

        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            boolean keep = true;
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                int deviceVID = device.getVendorId();
                if (deviceVID == 9025)//Arduino Vendor ID
                {
                    PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(device, pi);
                    keep = false;
                } else {
                    connection = null;
                    device = null;
                }

                if (!keep)
                    break;
            }
        }


    }

    public void Esconder(){
        C1Button.setVisibility(View.GONE);
        C2Button.setVisibility(View.GONE);
        C3Button.setVisibility(View.GONE);
        C4Button.setVisibility(View.GONE);

    }

    public void Mostrar(){
        C1Button.setVisibility(View.VISIBLE);
        C2Button.setVisibility(View.VISIBLE);
        C3Button.setVisibility(View.VISIBLE);
        C4Button.setVisibility(View.VISIBLE);

    }



    public void onClickC1(View view) {
        String string = "a";
        Esconder();
        Observa.setVisibility(View.VISIBLE);
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");

    }

    public void onClickC2(View view) {
        String string = "s";
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");
        Esconder();
        Observa.setVisibility(View.VISIBLE);


    }

    public void onClickC3(View view) {
        String string = "d";
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");
        Esconder();
        Observa.setVisibility(View.VISIBLE);

    }

    public void onClickC4(View view) {
        String string = "f";
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");
        Esconder();
        Observa.setVisibility(View.VISIBLE);

    }

    public void onClickAtras(View view){
        Stop();
        countDownTimer.cancel();
        startActivity(new Intent(getApplicationContext(), FiltroActivity.class));

    }

    public void Stop() {
        //setUiEnabled(false);
        serialPort.close();
        tvAppend(textView,"\nSerial Connection Closed! \n");

    }

    public void onClickClear(View view) {
        textView.setText(" ");
    }

    private void tvAppend(TextView tv, CharSequence text) {
       /* final TextView ftv = tv;
        final CharSequence ftext = text;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ftv.append(ftext);
            }
        });
    }*/}

    //@Override
    //protected void onDestroy() {
    //    super.onDestroy();
    //   unregisterReceiver(mUsbReceiver);
    // }



}

