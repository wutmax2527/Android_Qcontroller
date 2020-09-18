package th.co.infinitecorp.www.qcontroller.UDPNetwork;

import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

public class UDPServer implements UDPServerService {

    private DatagramSocket serverSocket;
    private DatagramPacket datagramPacket;
    private boolean isConnected = false;
    private InetAddress inetAddress;
    int listenerPort;
    Handler updateConversationHandler;
    Thread serverThread;
    private OnDataReceivedListener udpServerReceivedListener;

    public void setOnDataReceivedListener(OnDataReceivedListener listener) {
        this.udpServerReceivedListener = listener;
    }
    public interface OnDataReceivedListener {
        void onDataReceived(DatagramSocket socket,DatagramPacket packet, String message, byte[] bytes,String ip,int port);
    }

    @Override
    public OnDataReceivedListener getDataReceivedListener() {
        return udpServerReceivedListener;
    }

    public void Send(DatagramSocket socket,DatagramPacket packet,byte[] bytes)
    {
        if(bytes!=null) {
            InetAddress ipaddress = packet.getAddress();
            //final String ip = ipaddress.getHostAddress();
            final int port = packet.getPort();
            DatagramPacket packetForSend = new DatagramPacket(bytes, bytes.length, ipaddress, port);
            try {
                socket.send(packetForSend);
            } catch (IOException ex) {
            }
        }
    }
    public void Send(DatagramSocket socket,DatagramPacket packet,String message)
    {
        if(message!="") {
            InetAddress ipaddress = packet.getAddress();
            final String ip = ipaddress.getHostAddress();
            final int port = packet.getPort();

            byte[] buf = message.getBytes();
            DatagramPacket packetForSend = new DatagramPacket(buf, buf.length, ipaddress, port);
            try {
                socket.send(packetForSend);
            } catch (IOException ex) {
            }
        }
    }

    public UDPServer(int listenerPort){
        this.listenerPort = listenerPort;
        updateConversationHandler = new Handler(  );
        this.serverThread = new Thread( new ServerThread( listenerPort,this ) );
        this.serverThread.start();
    }

    public class ServerThread implements Runnable{
        private UDPServerService udpServerService;
        private final int listenerPort;
        private byte[] buffer = new byte[1024];

        public ServerThread(int listenerPort, UDPServerService udpServerService){
            this.listenerPort = listenerPort;
            this.udpServerService = udpServerService;
        }

        public void run(){
            Socket socket;
            try {
                serverSocket = new DatagramSocket(listenerPort);
                serverSocket.setBroadcast(true);

            }catch (IOException e){
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    datagramPacket = new DatagramPacket( buffer,buffer.length );
                    serverSocket.receive( datagramPacket );
                    InetAddress ipaddress = datagramPacket.getAddress();
                    final String ip=ipaddress.getHostAddress();
                    final int port = datagramPacket.getPort();
                    final byte[] bytes = datagramPacket.getData();
                    if (udpServerService.getDataReceivedListener() != null && datagramPacket.getData() != null) {
                        new Handler( Looper.getMainLooper()).post( new Runnable() {
                            public void run() {
                                udpServerService.getDataReceivedListener().onDataReceived(serverSocket,datagramPacket,bytes.toString(), bytes,ip,port);
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    serverSocket.close();
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                //EventBus.getDefault().post(new DebugMessageEvent("udpServer Running"));

            }
            //EventBus.getDefault().post(new DebugMessageEvent("udpServer Close"));
            //serverSocket.close();
        }
    }
    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

}
