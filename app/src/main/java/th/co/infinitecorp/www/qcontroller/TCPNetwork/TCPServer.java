package th.co.infinitecorp.www.qcontroller.TCPNetwork;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

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
        void onDataReceived(Socket socket,String message, String ip,byte[] bytes);
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


    public TCPServer(int listenerPort)
    {
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

    class CommunicationThread implements Runnable {

        private Socket clientSocket;
        private BufferedReader input;
        private TCPServerService tcpServerService;

        public CommunicationThread(Socket clientSocket, TCPServerService tcpServerService) {
            this.clientSocket = clientSocket;
            this.tcpServerService=tcpServerService;
            try {
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    final String incomingMsg;
                    int charsRead = 0;
                    char[] buffer = new char[2024];
                    charsRead =input.read(buffer);
                    if(charsRead>0) {
                        incomingMsg = new String(buffer).substring(0, charsRead);
                        byte[] bytes= incomingMsg.getBytes();
                        Log.w(TAG, "incomingMsg=" + incomingMsg);
                        final String ip=clientSocket.getInetAddress().getHostAddress();
                        updateConversationHandler.post(new updateUIThread(" ip="+ip+" size="+bytes.length+"  data="+incomingMsg));
                        final Socket socket=clientSocket;
                        final  byte[] rBuf=bytes;
                        if (tcpServerService.getDataReceivedListener() != null && incomingMsg.length() > 0) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                public void run() {
                                    tcpServerService.getDataReceivedListener().onDataReceived(socket, ip,incomingMsg, rBuf);
                                }
                            });
                        }

                    }else {
                        Thread.currentThread().interrupt();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
    public void Send(Socket socket,String message)
    {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write(message);
            out.flush();
        }catch (Exception e){}
    }
    public void Send(Socket socket,byte[] bytes)
    {
        try {
            OutputStream outputStream=socket.getOutputStream();
            outputStream.write(bytes);
            PrintWriter printWriter=new PrintWriter(outputStream);
            printWriter.flush();
            //printWriter.close();

        }catch (Exception e){}
    }
}
