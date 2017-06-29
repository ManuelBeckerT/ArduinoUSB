/*TODO:
Hacer que cuando vuelva se vuelva a aconectar
despues de x segundo que se reset app

 */

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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.media.MediaPlayer;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;



public class StartActivity extends Activity {
    public final String ACTION_USB_PERMISSION = "com.hariharan.arduinousb.USB_PERMISSION";
    TextView textView;
    EditText editText;
    //ImageView imagen;
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;
    MediaPlayer sonido1;
    MediaPlayer sonido2;
    MediaPlayer sonido3;
    boolean tiempo_sonido;

    public void Start() {
        tvAppend(textView, "Conectando\n");
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



    UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() { //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(byte[] arg0) {
            String data = null;
            try {
                data = new String(arg0, "UTF-8");
                data.concat("/n");

                if (data.contains("P")) {
                    //if (data.contains("50") || data.contains("P") || data==" P" || data=="P " || data==" P/n" || data=="50/n" || data=="0050" ||data=="0050/n") {

                    tvAppend(textView, "Sonido\n");
                    star_sonido();

                }

                else {
                    //tvAppend(textView, "2\n");
                    //star_sonido();
                    tvAppend(textView, "data:"+data+"-\n");
                }
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
        setContentView(R.layout.activity_start);
        usbManager = (UsbManager) getSystemService(this.USB_SERVICE);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        //imagen = (ImageView) findViewById(R.id.imageView);
        setUiEnabled(true);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(broadcastReceiver, filter);
        sonido1 = MediaPlayer.create(this, R.raw.audio_dilab);
        sonido2 = MediaPlayer.create(this, R.raw.auido_2);
        sonido3 = MediaPlayer.create(this, R.raw.audio_3);
        tiempo_sonido= true;
        Start();


    }


    public void star_sonido(){
        Random r = new Random();
        int i = r.nextInt(3);
        if (i==1) {
            sonido1.start();
        }
        else if(i==2){
            sonido2.start();
        }
        else{
            sonido3.start();
        }
    }

    public void setUiEnabled(boolean bool) {
        textView.setEnabled(bool);

    }

    /*public void onClickD1(View view) {
        String string = "p";
        serialPort.write(string.getBytes());
    }*/

    /*public void Start() {

        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            boolean keep = true;
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                int deviceVID = device.getVendorId();
                if (deviceVID == 10755)//Arduino Vendor ID
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


*/

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //String string = "0";
            //serialPort.write(string.getBytes());
            //Stop();
            startActivity(new Intent(getApplicationContext(), FiltroActivity.class));
        }
        return super.onTouchEvent(event);
    }

    public void onClickStart(View view) {
        //String string = "0";
        //serialPort.write(string.getBytes());
        //Stop();
        startActivity(new Intent(getApplicationContext(), FiltroActivity.class));
    }

    public void Stop() {
        setUiEnabled(false);
        serialPort.close();
        tvAppend(textView,"\nSerial Connection Closed! \n");

    }

    public void Star_sonido(){
        String string = "f1";
        serialPort.write(string.getBytes());
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

