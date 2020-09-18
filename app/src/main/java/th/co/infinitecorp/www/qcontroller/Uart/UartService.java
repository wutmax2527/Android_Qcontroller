package th.co.infinitecorp.www.qcontroller.Uart;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.MainActivity;
import th.co.infinitecorp.www.qcontroller.R;
import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCPClient;

public interface UartService  {
    Uart.OnDataReceivedListener getDataReceivedListener();
    public  void SendReceive_Byte(int timeOutMs,byte[] bytes,boolean forceSend);

}
