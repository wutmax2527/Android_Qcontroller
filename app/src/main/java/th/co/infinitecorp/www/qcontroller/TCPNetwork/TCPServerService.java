package th.co.infinitecorp.www.qcontroller.TCPNetwork;

import java.net.InetAddress;
import java.net.Socket;

public interface TCPServerService {
    boolean isConnected();
    void setConnected(boolean isConnected);
    TCPServer.OnDataReceivedListener getDataReceivedListener();

    InetAddress getInetAddress();

    void setInetAddress(InetAddress inetAddress);

    public void Send(Socket socket, byte[] bytes);


}
