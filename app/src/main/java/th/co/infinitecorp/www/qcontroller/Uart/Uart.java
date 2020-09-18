package th.co.infinitecorp.www.qcontroller.Uart;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Arrays;

import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.GPIO.GPIOService;
import th.co.infinitecorp.www.qcontroller.R;
import th.co.infinitecorp.www.qcontroller.Utils.Convert;

public class Uart implements UartService {
    private static final String TAG = Uart.class.getSimpleName();
    /*Parameter*/
    private static android_serialport_api.SerialPort mSerialPortData = null;
    protected static android_serialport_api.SerialPort mSerialPort;
    protected static OutputStream mOutputStream;
    private static InputStream mInputStream;
    private static ReadThread mReadThread;
    private static boolean isOpen=false;
    private boolean isRunning = false;
    private boolean forceSend=false;


    /*Interface*/
    private OnDataReceivedListener dataReceivedListener;
    public void setOnDataReceivedListener(OnDataReceivedListener listener) {
        this.dataReceivedListener = listener;
    }
    public interface OnDataReceivedListener {
        boolean onDataReceived(byte[] bytes);
        boolean onReceiveFail(byte[] bytes);
    }
    @Override
    public OnDataReceivedListener getDataReceivedListener() {
        return dataReceivedListener;
    }

    public Uart (int Baudrate) {
        try {
            if(!isOpen) {
                if (openSerialPort(Baudrate)) {
                    this.isOpen = true;
                    Log.d(TAG, "Open Port OK");
                }else {
                    Log.d(TAG, "Port is Open ");
                }
            }
        } catch (SecurityException e) {
            Log.d(TAG, "ex1="+e.getLocalizedMessage());
        } catch (InvalidParameterException e) {
            Log.d(TAG, "ex2="+e.getLocalizedMessage());
        }
    }
    public static boolean Init(Context context, int Baudrate) {
        try {
            if(openSerialPort(context,Baudrate)) {

                mReadThread = new ReadThread();
                mReadThread.start();

                Toast.makeText(context, "Open Serial Port Success", Toast.LENGTH_SHORT).show();
                return true;
            }
        } catch (SecurityException e) {
            Toast.makeText(context, R.string.error_security, Toast.LENGTH_SHORT).show();
        } catch (InvalidParameterException e) {
            Toast.makeText(context, R.string.error_configuration, Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    public static android_serialport_api.SerialPort getSerialPort(int Baudrate) throws SecurityException, IOException, InvalidParameterException {

        if (mSerialPortData == null) {
            /* Read serial port parameters */
            String path = "/dev/ttyHSL1";
            int baudrate = Baudrate;

            /*
            if (uart_num.getSelectedItemPosition() == 0) {
                path = "/dev/ttyHSL1";
            } else {
                path = "/dev/ttyHSL0";
            }
            */
            //baudrate = Integer.parseInt(baudCollection[baud_rate.getSelectedItemPosition()]);
            /* Check parameters */
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }

            /* Open the serial port */
            mSerialPortData = new android_serialport_api.SerialPort(new File(path), baudrate, 0);
        }
        return mSerialPortData;
    }

    public static boolean openSerialPort(Context context,int Baudrate) {
        try {

            mSerialPort = getSerialPort(Baudrate);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
            return  true;
        } catch (SecurityException e) {
            //Toast.makeText(context, R.string.error_security, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "ex6="+e.getLocalizedMessage());
        } catch (IOException e) {
            //Toast.makeText(context, R.string.error_unknown, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "ex7="+e.getLocalizedMessage());
        } catch (InvalidParameterException e) {
            //Toast.makeText(context, R.string.error_configuration, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "ex8="+e.getLocalizedMessage());
        }
        return  false;
    }
    public static boolean openSerialPort(int Baudrate) {
        try {

            mSerialPort = getSerialPort(Baudrate);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
            return  true;
        } catch (SecurityException e) {
            Log.d(TAG, "ex3="+e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d(TAG, "ex4="+e.getLocalizedMessage());
        } catch (InvalidParameterException e) {
            Log.d(TAG, "ex5="+e.getLocalizedMessage());
        }
        return  false;
    }
    public static void closeSerialPort() {
        try {
            if (mSerialPortData != null) {
                mSerialPortData.close();
                mSerialPortData = null;
            }
        }catch (Exception e){
            Log.d(TAG, "ex10="+e.getLocalizedMessage());
        }
    }
    private static class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        onDataReceived(buffer, size);
                    }
                } catch (IOException e) {
                    Log.d(TAG, "ex9="+e.getLocalizedMessage());
                    e.printStackTrace();
                    return;
                }
            }
        }

        protected void onDataReceived(final byte[] buffer, final int size) {
            String mEditStr=new String(buffer, 0, size);
            EventBus.getDefault().post(new DebugMessageEvent("Serial Rev:"+mEditStr));
        }

    }
    public static boolean  WriteText(Context context,String text){
        return writeText(context,text);
    }
    private static boolean  writeText(Context context,String text) {

        if (text.isEmpty()) {
            Toast.makeText(context, "Data is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        EventBus.getDefault().post(new DebugMessageEvent("Serial Send:"+text));
        try {
            mOutputStream.write(text.getBytes(), 0, text.length());
            return true;
        } catch (SecurityException e) {
            //Toast.makeText(context, R.string.error_security, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            //Toast.makeText(context, R.string.error_unknown, Toast.LENGTH_SHORT).show();
            Log.e("Write serial port", "Error: " + e.toString());
        } catch (InvalidParameterException e) {
            //Toast.makeText(context, R.string.error_configuration, Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    public static boolean  WriteByte(Context context, byte[] bytes){
        return writeByte(context,bytes);
    }
    private static boolean  writeByte(Context context,byte[] bytes) {

        if (bytes.length<=0) {
            //Toast.makeText(context, "Data is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        String text= new String(bytes);
        EventBus.getDefault().post(new DebugMessageEvent("Serial Send:"+text));
        try {
            mOutputStream.write(bytes, 0, bytes.length);
            mOutputStream.flush();
            return true;
        } catch (SecurityException e) {
            //Toast.makeText(context, R.string.error_security, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error: " + e.toString());
        } catch (IOException e) {
            //Toast.makeText(context, R.string.error_unknown, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error: " + e.toString());
        } catch (InvalidParameterException e) {
            //Toast.makeText(context, R.string.error_configuration, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public  void SendReceive_Byte(int timeOutMs,byte[] bytes,boolean forceSend) {
        this.forceSend=forceSend;
        if(!isRunning||forceSend) {
            String str= Convert.ByteArrayToHexStringWithSpace(bytes);
            EventBus.getDefault().post(new DebugMessageEvent("Serial Send Hex:"+str));
            sendDataAndReceive_Byte(timeOutMs, bytes,forceSend, this);
            isRunning=true;
        }
    }
    private void sendDataAndReceive_Byte(final int timeOutMs, final byte[] bytes,final boolean forceSend, final UartService uartService){

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (bytes.length<=0) {
                    return;
                }
                try {

                    if(forceSend) {

                        GPIOService.HIGH_SEND_RS485();
                        try {
                            Thread.sleep(10);
                        }catch (Exception e){}
                        try {
                            mOutputStream.write(bytes, 0, bytes.length);
                            mOutputStream.flush();
                        }catch (Exception ex){}

                    }else {
                        GPIOService.HIGH_SEND_RS485();
                        try {
                            Thread.sleep(10);
                        }catch (Exception e){}
                        try {
                            mOutputStream.write(bytes, 0, bytes.length);
                            mOutputStream.flush();
                        }catch (Exception ex){}

                    }
                    try {
                        Thread.sleep(10);
                    }catch (Exception e){}
                    GPIOService.LOW_RECEIVE_RS485();
                    try {
                        Thread.sleep(10);
                    }catch (Exception e){}

                } catch (SecurityException e) {

                } /*catch (IOException e) {
                    Log.e(TAG, "Error: " + e.toString());
                } */
                catch (InvalidParameterException e) {
                }
                Log.d(TAG, "Start Data Rev...");
                long start_ms = System.currentTimeMillis();
                String incomingMsg=null;
                int size=0;
                while (true)
                {
                    try {
                        Thread.sleep(10);
                    }catch (Exception e){}

                    long cur_ms = System.currentTimeMillis();
                    if(cur_ms-start_ms>=timeOutMs){

                        break;
                    }
                    try {
                        byte[] buffer = new byte[1024*2];
                        if (mInputStream == null) return;
                        size = mInputStream.read(buffer);
                        if (size > 0) {
                            final byte[] rBytes=Arrays.copyOf(buffer,size);
                            Log.d(TAG, "Rev Len="+size);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                public void run() {
                                    if(uartService!=null)
                                        uartService.getDataReceivedListener().onDataReceived(rBytes);
                                }
                            });
                            return;
                            //break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }

                }
                if(size==0){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            uartService.getDataReceivedListener().onReceiveFail(null);
                        }
                    });
                }
                isRunning=false;

            }
        }).start();
    }

}
