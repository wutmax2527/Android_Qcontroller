package th.co.infinitecorp.www.qcontroller.TCPNetwork;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import th.co.infinitecorp.www.qcontroller.Utils.Convert;

public class TCPClient implements TCPClientService {
    private static final String TAG = TCPClient.class.getSimpleName();
    /*parameter*/
    private TcpService tcpService;
    private Socket socket;
    private String ip;
    private int port;
    private String message;
    private byte[] bytes;
    private int timeOutMs=3000;
    private boolean isRunning = false;
    private boolean isConnected = false;

    public Socket getSocket() {
        return socket;
    }

    /*Interface*/
    private OnDataReceivedListener dataReceivedListener;
    public void setOnDataReceivedListener(OnDataReceivedListener listener) {
        this.dataReceivedListener = listener;
    }
    public interface OnDataReceivedListener {
        void onDataReceived(Socket socket,String message,byte[] bytes);
    }
    @Override
    public OnDataReceivedListener getDataReceivedListener() {
        return dataReceivedListener;
    }



    public TCPClient(String ip,int port){
        this.ip=ip;
        this.port = port;
        try {
            InetAddress serverAddress = InetAddress.getByName(ip);
            socket = new Socket(serverAddress, port);
            boolean createSocket=true;
        }catch (Exception e){
            Log.d(TAG, "Error1="+e.getLocalizedMessage());}
    }

    public void stop(){
        if (isRunning) {
            if(tcpService!=null){
                tcpService.killTask();
            }
            isRunning = false;
        }
    }
    public boolean CloseSocket()
    {
        try {
            this.socket.close();
            return  true;
        }catch (IOException e){ Log.d(TAG, "Close Socket Error="+e.getLocalizedMessage());}
        return  false;
    }

       public boolean isRunning() {
        return isRunning;
    }


        public static class TcpService extends AsyncTask<Void,Void,Void>
        {
            private Socket socket;
            private String ip;
            private int port;
            private int timeOut;
            private TCPClientService tcpClientService;
            private Boolean taskState = true;

            BufferedReader in;
        PrintWriter out;
        String incomingMsg;

        public void killTask() {
            taskState = false;
        }
        public TcpService(Socket socket,String ip,int port,int timeOut,String message, TCPClientService tcpClientService) {
            this.socket=socket;
            this.ip=ip;
            this.port = port;
            this.timeOut=timeOut;
            this.tcpClientService = tcpClientService;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                long start_sec = System.currentTimeMillis() / 1000;
                while (taskState&socket.isConnected()) {
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    long cur_sec = System.currentTimeMillis() / 1000;
                    if(cur_sec-start_sec>=timeOut){
                        break;
                    }
                    if(in.ready()) {
                        int charsRead = 0; final char[] buffer = new char[2024]; //choose your buffer size if you need other than 1024
                        charsRead =in.read(buffer);
                        incomingMsg= new String(buffer).substring(0, charsRead);
                        if (tcpClientService.getDataReceivedListener() != null && incomingMsg.length() > 0) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                public void run() {
                                    tcpClientService.getDataReceivedListener().onDataReceived(socket,incomingMsg,Convert.CharToByteArray(buffer));
                                }
                            });
                        }
                    }
                    if (incomingMsg != null) {
                        taskState=false;

                        break;
                    }
                    try {
                        Thread.sleep(100);
                    }catch (Exception e){}
                }
            } catch (Exception e) {
                Log.d(TAG, "Error2", e);
            }
            finally {

                out.flush();
                out.close();
                try {
                    in.close();
                }catch (Exception e){
                }

                try {
                    this.socket.close();
                }catch (Exception ex){}

            }
            return null;
        }
    }
    /*Send Data*/
    public void Send_Message(Socket socket,String message)
    {
        if (!this.isRunning) {
            Log.i(TAG, "Will Send...");
            this.message=message;
            if(socket.isClosed()){
                Log.i(TAG, "Socket is Closed");
            }else {
                Log.i(TAG, "Socket is Opened");
                send(this.socket,ip,port,message,this);
            }
            isRunning = true;
        }
    }
    private void send(final Socket socket,final String ip, final int port, final String Message,final TCPClientService tcpClientService){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "Start Data Sending2...");
                    if(socket.isConnected()) {
                        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                        printWriter.write(Message);
                        Log.d(TAG, "Start Data Sent2");
                        printWriter.flush();
                        printWriter.close();
                    }else {
                        Log.d(TAG, "isConnected is false");
                    }
                } catch(UnknownHostException e){
                    Log.d(TAG, "ex1="+e.getLocalizedMessage());
                } catch (IOException e){
                    Log.d(TAG, "ex2="+e.getLocalizedMessage());
                }
            }
        }).start();
    }
    /*Send & Receive*/
    public  void SendReceive_Message(Socket socket,int timeOutMs,String message){
        if (!this.isRunning) {
            //this.socket=socket;
            this.timeOutMs=timeOutMs;
            this.message=message;
            sendDataAndReceive_Message(this.socket,ip,port,timeOutMs,message,this);
            isRunning = true;
        }
    }
    private void sendDataAndReceive_Message(final Socket socket,final String ip, final int port,final int timeOut, final String Message,final TCPClientService tcpClientService){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //final Socket socket = new Socket(ip, port);
                    if(socket==null)
                        return;

                    Log.i(TAG, "Start Data Sending...");
                    PrintWriter printWriter=new PrintWriter(socket.getOutputStream());
                    printWriter.write(Message);
                    Log.d(TAG, "Start Data Sent");

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Log.d(TAG, "Start Data Rev...");

                    long start_Ms = System.currentTimeMillis();
                    String incomingMsg=null;
                    while (socket.isConnected())
                    {
                        long cur_Ms = System.currentTimeMillis();
                        if(cur_Ms-start_Ms>=timeOut){

                            break;
                        }

                        if(in.ready()) {
                            int charsRead = 0;
                            char[] buffer = new char[2024]; //choose your buffer size if you need other than 1024
                            charsRead = in.read(buffer);
                            incomingMsg = new String(buffer).substring(0, charsRead);
                           if(charsRead>0) {
                               Log.i(TAG, "Rev Success");
                               char[] chars=new char[charsRead];
                               chars=Arrays.copyOf(buffer,charsRead);
                               final byte[] bytes=Convert.CharToByteArray(chars);
                               final String msg = incomingMsg;
                               new Handler(Looper.getMainLooper()).post(new Runnable() {
                                   public void run() {
                                       tcpClientService.getDataReceivedListener().onDataReceived(socket, msg,bytes);
                                   }
                               });
                               break;
                           }
                        }
                        try {
                            Thread.sleep(100);
                        }catch (Exception e){}
                    }
                    printWriter.flush();
                    printWriter.close();
                    in.close();
                    isRunning = false;
                } catch(UnknownHostException e1){

                } catch (IOException e1){

                }
            }
        }).start();

    }
    public  void SendReceive_Hex(Socket socket,int timeOutMs,byte[] bytes){
        if (!this.isRunning) {
            //this.socket=socket;
            this.timeOutMs=timeOutMs;
            this.bytes=bytes;
            sendDataAndReceive_Hex(this.socket,ip,port,timeOutMs,bytes,this);
            isRunning = true;
        }
    }
    private void sendDataAndReceive_Hex(final Socket socket,final String ip, final int port,final int timeOutMs, final byte[] bytes,final TCPClientService tcpClientService){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(socket==null)
                        return;

                    Log.i(TAG, "Start Data Sending...");
                    OutputStream outputStream=socket.getOutputStream();
                    outputStream.write(bytes);
                    PrintWriter printWriter=new PrintWriter(outputStream);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Log.d(TAG, "Start Data Rev...");
                    long start_Ms = System.currentTimeMillis();
                    String incomingMsg=null;
                    while (socket.isConnected())
                    {
                        if(in.ready()) {
                            int charsRead = 0;
                            char[] buffer = new char[2024]; //choose your buffer size if you need other than 1024
                            charsRead = in.read(buffer);
                            incomingMsg = new String(buffer).substring(0, charsRead);
                            if (charsRead>0) {
                                Log.i(TAG, "Rev Success");
                                char[] chars=new char[charsRead];
                                chars=Arrays.copyOf(buffer,charsRead);
                                final byte[] bytes=Convert.CharToByteArray(chars);
                                final String msg = incomingMsg;
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    public void run() {
                                        tcpClientService.getDataReceivedListener().onDataReceived(socket, msg, bytes);
                                    }
                                });
                                isRunning = false;
                                break;
                            }
                        }
                        long cur_Ms = System.currentTimeMillis();
                        if(cur_Ms-start_Ms>=timeOutMs){

                            isRunning = false;
                            break;
                        }

                        try {
                            Thread.sleep(100);
                        }catch (Exception e){}
                    }
                    printWriter.flush();
                    printWriter.close();
                    in.close();
                    try {
                        socket.close();
                    }catch (Exception ex){}
                    isRunning = false;
                } catch(UnknownHostException e1){

                } catch (IOException e1){

                }
            }
        }).start();
        isRunning = false;
    }
}
