package th.co.infinitecorp.www.qcontroller.TCPNetwork;

import java.net.Socket;

public interface TCPClientService {
    TCPClient.OnDataReceivedListener getDataReceivedListener();
    public void Send_Message(Socket socket,String message);
    public  void SendReceive_Message(Socket socket, int timeOutMs, String message);
    public  void SendReceive_Hex(Socket socket, int timeOutMs, byte[] bytes);
}
