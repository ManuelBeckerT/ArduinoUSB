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

public class HabilidadActivity extends Activity {
    public final String ACTION_USB_PERMISSION = "com.hariharan.arduinousb.USB_PERMISSION";
    ImageButton D1Button, D2Button, D3Button, D4Button, D5Button;
    Button AtrasButton;
    TextView textView;
    ImageView Observa;
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;

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
                            //setUiEnabled(true);
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
        D1Button = (ImageButton) findViewById(R.id.buttonD1);
        D2Button = (ImageButton) findViewById(R.id.buttonD2);
        D3Button = (ImageButton) findViewById(R.id.buttonD3);
        D4Button = (ImageButton) findViewById(R.id.buttonD4);
        D5Button = (ImageButton) findViewById(R.id.buttonD5);
        AtrasButton = (Button) findViewById(R.id.buttonAtras);
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
        D1Button.setEnabled(bool);
        D2Button.setEnabled(bool);
        D3Button.setEnabled(bool);
        D4Button.setEnabled(bool);
        D5Button.setEnabled(bool);
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



    public void Botones_mostrar(){
        D1Button.setVisibility(View.VISIBLE);
        D2Button.setVisibility(View.VISIBLE);
        D3Button.setVisibility(View.VISIBLE);
        D4Button.setVisibility(View.VISIBLE);
        D5Button.setVisibility(View.VISIBLE);
    }

    public void Botones_esconder(){
        D1Button.setVisibility(View.GONE);
        D2Button.setVisibility(View.GONE);
        D3Button.setVisibility(View.GONE);
        D4Button.setVisibility(View.GONE);
        D5Button.setVisibility(View.GONE);
    }

    public void onClickD1(View view) {
        String string = "q";
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");
        Botones_esconder();
        Observa.setVisibility(View.VISIBLE);
        //countDownTimer.cancel();
        //startActivity(new Intent(getApplicationContext(), Deporte1.class));

    }

    public void onClickD2(View view) {
        String string = "w";
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");
        Botones_esconder();
        Observa.setVisibility(View.VISIBLE);
        //countDownTimer.cancel();
        //startActivity(new Intent(getApplicationContext(), Deporte2.class));

    }

    public void onClickD3(View view) {
        String string = "e";
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");
        Botones_esconder();
        Observa.setVisibility(View.VISIBLE);
        //countDownTimer.cancel();
        //startActivity(new Intent(getApplicationContext(), Deporte3.class));

    }

    public void onClickD4(View view) {
        String string = "r";
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");
        Botones_esconder();
        Observa.setVisibility(View.VISIBLE);
        //countDownTimer.cancel();
        // startActivity(new Intent(getApplicationContext(), Deporte4.class));

    }

    public void onClickD5(View view) {
        String string = "t";
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");
        Botones_esconder();
        Observa.setVisibility(View.VISIBLE);
        //countDownTimer.cancel();
        //startActivity(new Intent(getApplicationContext(), Deporte5.class));
    }

    public void onClickAtras(View view) {
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
