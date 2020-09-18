package th.co.infinitecorp.www.qcontroller.UDPNetwork;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public interface UDPServerService {

    public void Send(DatagramSocket socket, DatagramPacket packet, byte[] bytes);
    public void Send(DatagramSocket socket, DatagramPacket packet, String message);
    UDPServer.OnDataReceivedListener getDataReceivedListener();
}
