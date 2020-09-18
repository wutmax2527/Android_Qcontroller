package th.co.infinitecorp.www.qcontroller.UDPNetwork;

import java.net.InetAddress;
import java.net.Socket;

public interface UDPClientService {
    boolean isConnected();
    void setConnected(boolean isConnected);
    InetAddress getInetAddress();
    void setInetAddress(InetAddress inetAddress);
    public void Send(Socket socket, byte[] bytes);
}
