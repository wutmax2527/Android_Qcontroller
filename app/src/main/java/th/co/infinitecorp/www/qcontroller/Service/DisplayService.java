package th.co.infinitecorp.www.qcontroller.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.net.Socket;
import java.util.Arrays;

import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.DisplayInfo;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCP;
import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCPClient;
import th.co.infinitecorp.www.qcontroller.Utils.Convert;
import th.co.infinitecorp.www.qcontroller.Utils.GData;
import th.co.infinitecorp.www.qcontroller.Utils.Protocol;

public class DisplayService extends Service {
    private static final String TAG = DisplayService.class.getSimpleName();
    private Thread  callDisplayThread=null;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        callDisplayThread=new Thread(CallDisplayThread);
        callDisplayThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    Thread CallDisplayThread=new Thread(new Runnable() {
        @Override
        public void run() {
            Integer runIndex=0;
            Integer ProcessIndex=0;
            while (true) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(runIndex++>=25) {
                    runIndex=0;
                    EventBus.getDefault().post(new DebugMessageEvent("DisplayService Thread Run..."+ProcessIndex));
                    if(ProcessIndex++>100)
                        ProcessIndex=0;
                }
                /*Show Display*/
                if(GData.displayInfos.size()>0) {
                    DisplayInfo d=new DisplayInfo();
                    d=GData.displayInfos.get(0);
                    /*Communicate Display*/
                    byte[] bytes=prepareDataUpdateQueue(d);
                    Log.w(TAG, "sendUpdateQueue Len="+bytes.length);
                    EventBus.getDefault().post(new DebugMessageEvent("tcpClient_DISPLAY Send data Len="+bytes.length));
                    final TCPClient tcpClient_DISPLAY=new TCPClient(d.getIp(), TCP.Client.TargetPort.DISPLAY);
                    tcpClient_DISPLAY.SendReceive_Hex(tcpClient_DISPLAY.getSocket(),2000,bytes);
                    tcpClient_DISPLAY.setOnDataReceivedListener(new TCPClient.OnDataReceivedListener() {
                        @Override
                        public void onDataReceived(Socket socket, String message,byte[] rBytes) {
                            EventBus.getDefault().post(new DebugMessageEvent("tcpClient_DISPLAY Rev len="+rBytes.length));
                            if(tcpClient_DISPLAY.getSocket()==socket)
                            {
                                EventBus.getDefault().post(new DebugMessageEvent("tcpClient_DISPLAY Remove List"));

                            }
                        }
                    });

                    GData.displayInfos.remove(0);
                    Log.w(TAG, "Remove List");
                }
            }
        }
    });
    /*Update Calling Queue*/
    private static void UpdateCallingQueueToList(DisplayInfo displayInfo) {

        boolean found=false;
        Integer index=0;
        for(DisplayInfo info : GData.displayInfos){
           if(displayInfo.getId()==info.getId())
           {
               found=true;
               GData.displayInfos.add(index,info);
               Log.w(TAG, "UpdateToList");
               break;
           }
            index++;
        }
        if(!found) {
            GData.displayInfos.add(displayInfo);
            Log.w(TAG, "AddToList");
        }
    }
    private static void ClearCallingQueueList() {
        GData.displayInfos.clear();
    }
    private static DisplayInfo NEW_QueueOnDisplay() {
        DisplayInfo d=new DisplayInfo();
        d.setId((byte) 0x00);
        d.setIp("10.172.103.117");
        d.setqStart(new QInfo());
        d.setqEnd(new QInfo());
        d.setStation_id((byte) 0);
        d.setArrowLeft((byte)0);
        d.setArrowRight((byte)0);
        d.setnBlink((byte) 0);
        d.setSoundType((byte) 0x00);
        return  d;
    }
    public static void UpdateCallingQueueOnDisplay(byte id, QInfo qStart, QInfo qEnd, byte station_id,byte nBlink,byte soundType) {
        DisplayInfo d=NEW_QueueOnDisplay();
        d.setId(id);
        d.setqStart(qStart);
        d.setqEnd(qEnd);
        d.setStation_id(station_id);
        d.setnBlink(nBlink);
        d.setSoundType(soundType);
        UpdateCallingQueueToList(d);
    }
    private byte[] prepareDataUpdateQueue(DisplayInfo d) {
        byte[] bytes=new byte[1024*2];
        int idx=0;

        byte deviceId =d.getId();
        QInfo qStart=d.getqStart();
        QInfo qEnd=d.getqEnd();
        /*Data*/
        //---Queue Start
        bytes[idx++] = qStart.getqType();
        bytes[idx++] = qStart.getqAlp();
        bytes[idx++] = Convert.GetByteHigh(qStart.getqNum());
        bytes[idx++] = Convert.GetByteLow(qStart.getqNum());
        //---Queue Start
        bytes[idx++] = qEnd.getqType();
        bytes[idx++] = qEnd.getqAlp();
        bytes[idx++] = Convert.GetByteHigh(qEnd.getqNum());
        bytes[idx++] = Convert.GetByteLow(qEnd.getqNum());

        bytes[idx++]=d.getStation_id();
        bytes[idx++]=d.getArrowLeft();
        bytes[idx++]=d.getArrowRight();
        bytes[idx++]=d.getShow();
        bytes[idx++]=d.getnBlink();
        bytes[idx++]=d.getSoundType();
        byte[] b = Arrays.copyOf(bytes, idx);
        byte[] sBytes=prepareDataForSend(Protocol.DISPLAY_CMD.SHOW_Q, Protocol.FrameID.ID1,Protocol.DeviceType.DISPLAY,deviceId,b);
        return sBytes;
    }
    private byte[] prepareDataForSend(byte cmd,byte frameId,byte deviceType,byte deviceId,byte[] bytes) {
        byte[] sBytes=Protocol.prepareData_Protocol_V2(cmd,frameId,deviceType,deviceId,Protocol.NONE,Protocol.RESPONSE_STATUS.SUCCESS,bytes);
        return sBytes;
    }
    /*Update Waiting Queue*/

    /*Update Waiting Time*/
}
