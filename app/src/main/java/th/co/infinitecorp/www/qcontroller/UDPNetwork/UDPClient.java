package th.co.infinitecorp.www.qcontroller.UDPNetwork;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class UDPClient extends AsyncTask<Void,Void,String>{
    private DatagramPacket packet;
    private OnMessageReceived onMessageReceived;
    private boolean isOpenSocket = false;

    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

    public UDPClient(DatagramPacket packet) {
        this.packet = packet;
    }

    @Override
    protected String doInBackground(Void... voids) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(  );
            isOpenSocket = true;
            socket.send( packet );

            while (isOpenSocket){
                try {
                    byte[] dataReceive = new byte[256];
                    DatagramPacket packet = new DatagramPacket( dataReceive,dataReceive.length );
                    socket.setSoTimeout( 10000 );
                    socket.receive( packet );
                    String receive = new String( packet.getData() );
                    onMessageReceived.messageReceived( receive );
                } catch (SocketTimeoutException e){
                    e.printStackTrace();
                    socket.close();
                    isOpenSocket = false;
                } catch (IOException e){
                    e.printStackTrace();
                    socket.close();
                    isOpenSocket = false;
                } finally {
                    socket.close();
                    isOpenSocket = false;
                }

            }
        } catch (SocketException e) {
            e.printStackTrace();
            socket.close();
            isOpenSocket = false;
        } catch (IOException e) {
            e.printStackTrace();
            socket.close();
            isOpenSocket = false;
        }
        return null;
    }

}
