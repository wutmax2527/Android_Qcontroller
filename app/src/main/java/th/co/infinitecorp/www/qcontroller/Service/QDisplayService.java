package th.co.infinitecorp.www.qcontroller.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.greenrobot.eventbus.EventBus;

import java.net.Socket;
import java.util.Arrays;

import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.DisplayInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.QDisplayInfo;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCP;
import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCPClient;
import th.co.infinitecorp.www.qcontroller.Utils.Convert;
import th.co.infinitecorp.www.qcontroller.Utils.GData;
import th.co.infinitecorp.www.qcontroller.Utils.Protocol;

public class QDisplayService extends Service {
    private Thread  callQDisplayThread=null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        callQDisplayThread=new Thread(CallQDisplayThread);
        callQDisplayThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    Thread CallQDisplayThread=new Thread(new Runnable() {
        @Override
        public void run() {
            Integer runIndex=0;
            Integer ProcessIndex=0;
            while (true)
            {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(runIndex++>=25) {
                    runIndex=0;
                    EventBus.getDefault().post(new DebugMessageEvent("QDisplayService Thread Run..."+ProcessIndex+" List_Size="+GData.qDisplayInfos.size()));
                    if(ProcessIndex++>100)
                        ProcessIndex=0;
                }
                /*Show Display*/
                if(GData.qDisplayInfos.size()>0)
                {
                    //for(Integer i=0;i<GData.qDisplayInfos.size();i++) {
                        QDisplayInfo d = new QDisplayInfo();
                        d = GData.qDisplayInfos.get(0);
                        /*Communicate QDisplay*/
                        if(d.getId()>0) {

                            byte[] bytes = prepareDataUpdateQueue(d);
                            EventBus.getDefault().post(new DebugMessageEvent("Update Data To QDisplay Len="+bytes.length));
                            final TCPClient tcpClient_QDisplay = new TCPClient(d.getIp(), TCP.Client.TargetPort.QDISPLAY);
                            tcpClient_QDisplay.SendReceive_Hex(tcpClient_QDisplay.getSocket(), 3000, bytes);
                            tcpClient_QDisplay.setOnDataReceivedListener(new TCPClient.OnDataReceivedListener() {
                                @Override
                                public void onDataReceived(Socket socket, String message, byte[] rBytes) {
                                    EventBus.getDefault().post(new DebugMessageEvent("***QDisplay Rev Ack Len="+rBytes.length));

                                    if(tcpClient_QDisplay.getSocket()==socket) {
                                        EventBus.getDefault().post(new DebugMessageEvent("***QDisplay Remove List"));
                                    }

                                }
                            });
                        }
                        GData.qDisplayInfos.remove(0);
                    //}

                }
            }
        }
    });
    public static void AddToList(QDisplayInfo qDisplayInfo)
    {
        GData.qDisplayInfos.add(qDisplayInfo);
    }
    public static void ClearList()
    {
        GData.qDisplayInfos.clear();
    }


    private static QDisplayInfo NEW_QueueOnQDisplay() {
        QDisplayInfo d=new QDisplayInfo();
        d.setId((byte) 0x00);
        d.setIp("127.0.0.1");
        d.setqStart(new QInfo());
        d.setqEnd(new QInfo());
        d.setStation_id((byte) 0);

        return  d;
    }
    public static void UpdateCallingQueueOnQDisplay(byte id,String ip, QInfo qStart, QInfo qEnd, byte station_id) {
        QDisplayInfo d=NEW_QueueOnQDisplay();
        d.setId(id);
        d.setIp(ip);
        d.setqStart(qStart);
        d.setqEnd(qEnd);
        d.setStation_id(station_id);
        AddToList(d);
    }
    private byte[] prepareDataUpdateQueue(QDisplayInfo d) {
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

        byte[] b = Arrays.copyOf(bytes, idx);
        byte[] sBytes=prepareDataForSend(Protocol.DISPLAY_CMD.SHOW_Q, Protocol.FrameID.ID1,Protocol.DeviceType.QDISPLAY,deviceId,b);
        return sBytes;
    }
    private byte[] prepareDataForSend(byte cmd,byte frameId,byte deviceType,byte deviceId,byte[] bytes) {
        byte[] sBytes= Protocol.prepareData_Protocol_V2(cmd,frameId,deviceType,deviceId,Protocol.NONE,Protocol.RESPONSE_STATUS.SUCCESS,bytes);
        return sBytes;
    }

}
