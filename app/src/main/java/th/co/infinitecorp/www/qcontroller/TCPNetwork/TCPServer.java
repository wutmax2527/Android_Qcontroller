package th.co.infinitecorp.www.qcontroller.TCPNetwork;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;

public class TCPServer implements TCPServerService {
    private static final String TAG = TCPServer.class.getSimpleName();
    private ServerSocket serverSocket;
    Handler updateConversationHandler;
    Thread serverThread = null;
    int listenerPort;
    private boolean isConnected = false;
    private InetAddress inetAddress;

    /*Interface*/
    private OnDataReceivedListener dataReceivedListener;
    public void setOnDataReceivedListener(OnDataReceivedListener listener) {
        this.dataReceivedListener = listener;
    }
    public interface OnDataReceivedListener {
        void onConnect(Socket socket,String ip);
        void onDataReceived(Socket socket,String ip, String message,byte[] bytes);
        void onDisconnect(Socket socket,String ip);
    }
    @Override
    public OnDataReceivedListener getDataReceivedListener() {
        return dataReceivedListener;
    }
    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    @Override
    public InetAddress getInetAddress() {
        return inetAddress;
    }

    @Override
    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public void start() {

    }
    public void stop() {

    }

    public TCPServer(int listenerPort) {
        this.listenerPort=listenerPort;
        updateConversationHandler = new Handler();
        this.serverThread = new Thread(new ServerThread(listenerPort,this));
        this.serverThread.start();
    }

    public class ServerThread implements Runnable {

        private TCPServerService tcpServerService;
        private final int listenerPort;
        public ServerThread(int listenerPort, TCPServerService tcpServerService) {
            this.listenerPort = listenerPort;
            this.tcpServerService=tcpServerService;
        }
        public void run() {
            Socket socket = null;
            try {
                serverSocket = new ServerSocket(listenerPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    socket = serverSocket.accept();
                   CommunicationThread commThread = new CommunicationThread(socket,tcpServerService);
                    new Thread(commThread).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    protected void Sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
    class CommunicationThread implements Runnable {

        private Socket clientSocket;
        private BufferedReader input;
        private TCPServerService tcpServerService;

        public CommunicationThread(Socket clientSocket, TCPServerService tcpServerService) {
            this.clientSocket = clientSocket;
            this.tcpServerService=tcpServerService;
            try {
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            //EventBus.getDefault().post(new DebugMessageEvent("****Connect****"));
            /*Socket is Connect*/
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    tcpServerService.getDataReceivedListener().onConnect(clientSocket, clientSocket.getInetAddress().getHostAddress());
                }
            });
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    final String incomingMsg="";
                    if(input.ready()) {
                        InputStream inputStream  = this.clientSocket.getInputStream();
                        byte[] bytes = new byte[inputStream.available()];
                        int len = 0;
                        len=inputStream.read(bytes);
                        if (len > 0) {
                            final byte[] rBuf = bytes;
                            final String ip = clientSocket.getInetAddress().getHostAddress();
                            final Socket socket = clientSocket;

                            if (tcpServerService.getDataReceivedListener() != null) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    public void run() {
                                        tcpServerService.getDataReceivedListener().onDataReceived(socket, ip, incomingMsg, rBuf);
                                    }
                                });
                            }
                        } else {
                            Thread.currentThread().interrupt();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            /*Socket is disconnect*/
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    tcpServerService.getDataReceivedListener().onDisconnect(clientSocket, clientSocket.getInetAddress().getHostAddress());
                }
            });

        }
    }
    class updateUIThread implements Runnable {
        private String msg;

        public updateUIThread(String str) {
            this.msg = str;
        }

        @Override
        public void run() {
            //EventBus.getDefault().post(new DebugMessageEvent("Client Says: "+ msg + new Date()));

        }
    }
    public void Send(Socket socket,String message) {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write(message);
            out.flush();
        }catch (Exception e){}
    }
    public void Send(Socket socket,byte[] bytes) {
        try {
            OutputStream outputStream=socket.getOutputStream();
            outputStream.write(bytes);
            PrintWriter printWriter=new PrintWriter(outputStream);
            printWriter.flush();
            //printWriter.close();

        }catch (Exception e){}
    }
    /*
    public void SendReceive(final Socket socket,byte[] bytes) {
        Send(socket,bytes);
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            while (socket!=null) {
                try {
                    final String incomingMsg="";
                    if(bufferedReader.ready()) {
                        InputStream inputStream  = socket.getInputStream();
                        byte[] bytes = new byte[inputStream.available()];
                        int len = 0;
                        len=inputStream.read(bytes);
                        if (len > 0) {
                            final byte[] rBuf = bytes;
                            final String ip = socket.getInetAddress().getHostAddress();

                            if (this.tcpServerService.getDataReceivedListener() != null) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    public void run() {
                                        tcpServerService.getDataReceivedListener().onDataReceived(socket, ip, incomingMsg, rBuf);
                                    }
                                });
                            }
                        } else {
                            Thread.currentThread().interrupt();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    */
}
