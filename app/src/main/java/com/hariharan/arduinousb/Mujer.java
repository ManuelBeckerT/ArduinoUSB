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
import android.widget.TextView;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Mujer extends Activity {
    public final String ACTION_USB_PERMISSION = "com.hariharan.arduinousb.USB_PERMISSION";
    //Button BrazosButton, AbdomenButton, PiernasButton, EspaldaButton, GluteoButton;
    Button GirarButton, AtrasButton,clearButton;
    Image
    TextView textView;
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;
    boolean posicion= true;

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
                            setUiEnabled(true);
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
        setContentView(R.layout.activity_mujer);
        usbManager = (UsbManager) getSystemService(this.USB_SERVICE);
        BrazosButton = (Button) findViewById(R.id.buttonBrazos);
        AbdomenButton = (Button) findViewById(R.id.buttonAbdomen);
        PiernasButton = (Button) findViewById(R.id.buttonPiernas);
        EspaldaButton = (Button) findViewById(R.id.buttonEspalda);
        GluteoButton = (Button) findViewById(R.id.buttonGluteo);
        GirarButton = (Button) findViewById(R.id.buttonGirar);
        AtrasButton = (Button) findViewById(R.id.buttonAtras);
        clearButton = (Button) findViewById(R.id.buttonClear);
        textView = (TextView) findViewById(R.id.textView);
        setUiEnabled(false); //TODO: CAMBIAR A FALSE
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(broadcastReceiver, filter);
        //tvAppend(textView,"Antes de Start");
        Start();
        Adelante();

        // tvAppend(textView,"Despues de Start");


    }


    public void Adelante() {
        BrazosButton.setVisibility(View.VISIBLE);
        AbdomenButton.setVisibility(View.VISIBLE);
        PiernasButton.setVisibility(View.VISIBLE);
        EspaldaButton.setVisibility(View.GONE);
        GluteoButton.setVisibility(View.GONE);
    }


    public void Atras(){
        BrazosButton.setVisibility(View.GONE);
        AbdomenButton.setVisibility(View.GONE);
        PiernasButton.setVisibility(View.GONE   );
        EspaldaButton.setVisibility(View.VISIBLE);
        GluteoButton.setVisibility(View.VISIBLE);

    }

    public void setUiEnabled(boolean bool) {
        BrazosButton.setEnabled(bool);
        AbdomenButton.setEnabled(bool);
        PiernasButton.setEnabled(bool);
        EspaldaButton.setEnabled(bool);
        GluteoButton.setEnabled(bool);
        textView.setEnabled(bool);

    }

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

    public void onClickBrazos(View view) {
        String string = "z";
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");

    }

    public void onClickAbdomen(View view) {
        String string = "x";
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");

    }
    public void onClickPiernas(View view) {
        String string = "c";
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");

    }
    public void onClickEspalda(View view) {
        String string = "v";
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");

    }
    public void onClickGluteo(View view) {
        String string = "b";
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");
    }

    public void onClickGirar(View view) {
        if (posicion){
            Atras();
            posicion= false;
        }
        else {
            Adelante();
            posicion=true;
        }

    }
    public void onClickAtras(View view) {
        countDownTimer.cancel();
        startActivity(new Intent(getApplicationContext(), Genero.class));

    }

    public void Stop() {
        setUiEnabled(false);
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

