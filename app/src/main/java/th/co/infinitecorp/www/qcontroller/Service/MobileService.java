package th.co.infinitecorp.www.qcontroller.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.net.Socket;

import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCPServer;

public class MobileService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void Handle_Mobile(TCPServer tcpServer, Socket socket, byte[] bytes)
    {
        byte[] b=new byte[3];
        b[0]=0x02;
        b[1]=0x01;
        b[2]=0x03;
        tcpServer.Send(socket,b);
    }
}
