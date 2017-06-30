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

public class MujerAtras extends Activity {
    public final String ACTION_USB_PERMISSION = "com.hariharan.arduinousb.USB_PERMISSION";
    Button  AtrasButton,clearButton, MostrarButton;
    ImageButton EspaldaButton, GluteosButton, GirarButton;
    TextView textView;
    UsbManager usbManager;
    UsbDevice device;
    ImageView Observa;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;
    String string;
    boolean etapa= true;

    CountDownTimer countDownTimer2;

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
        setContentView(R.layout.activity_mujer_atras);
        usbManager = (UsbManager) getSystemService(this.USB_SERVICE);
        EspaldaButton = (ImageButton) findViewById(R.id.buttonEspalda);
        GluteosButton = (ImageButton) findViewById(R.id.buttonGluteo);
        Transapernte();
        GirarButton = (ImageButton) findViewById(R.id.buttonGirar);
        Observa = (ImageView) findViewById(R.id.Observa);
        Observa.setVisibility(View.GONE);

        MostrarButton = (Button) findViewById(R.id.buttonMostrar);
        MostrarButton.setEnabled(false);
        AtrasButton = (Button) findViewById(R.id.buttonAtras);
        clearButton = (Button) findViewById(R.id.buttonClear);
        textView = (TextView) findViewById(R.id.textView);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(broadcastReceiver, filter);
        //tvAppend(textView,"Antes de Start");
        Start();

        // tvAppend(textView,"Despues de Start");


    }

    public void Transapernte(){
        EspaldaButton.setAlpha(0f);
        GluteosButton.setAlpha(0f);
    }

    public void Botones_esconder(){
        EspaldaButton.setVisibility(View.GONE);
        GluteosButton.setVisibility(View.GONE);
        GirarButton.setVisibility(View.GONE);
        MostrarButton.setVisibility(View.GONE);

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


    public void click_musculo(){
        countDownTimer.cancel();
        countDownTimer.start();
        MostrarButton.setEnabled(true);
    }



    public void onClickEspalda(View view) {
        etapa=false;
        EspaldaButton.setAlpha(1f);
        string = "v";
        click_musculo();



    }
    public void onClickGluteo(View view) {
        etapa=false;
        GluteosButton.setAlpha(1f);
        string = "b";
        click_musculo();
    }

    public void onClickMostrar(View view){
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");
        Botones_esconder();
        Observa.setVisibility(View.VISIBLE);}

    public void onClickGirar(View view) {
        countDownTimer.cancel();
        startActivity(new Intent(getApplicationContext(), Mujer.class));
        // TODO: pasar a la actividad de espalda
    }


    public void onClickAtras(View view) {
        if (etapa){
            countDownTimer.cancel();
            startActivity(new Intent(getApplicationContext(), Genero.class));
        }
        else {
            etapa=true;
            Transapernte();
        }

    }

    public void Stop() {
        serialPort.close();
        tvAppend(textView,"\nSerial Connection Closed! \n");

    }

    public void onClickClear(View view) {
        textView.setText(" ");
    }

    private void tvAppend(TextView tv, CharSequence text) {
        /*final TextView ftv = tv;
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

