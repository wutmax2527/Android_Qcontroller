package th.co.infinitecorp.www.qcontroller.Service;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.net.Socket;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.QueueInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DateTimeInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.QTicketInfo;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.MainActivity;
import th.co.infinitecorp.www.qcontroller.Management.QueueMgr;
import th.co.infinitecorp.www.qcontroller.Management.SystemMgr;
import th.co.infinitecorp.www.qcontroller.Management.ViewLogMgr;
import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCP;
import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCPClient;
import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCPServer;
import th.co.infinitecorp.www.qcontroller.Utils.Convert;
import th.co.infinitecorp.www.qcontroller.Utils.DateTime;
import th.co.infinitecorp.www.qcontroller.Utils.GData;
import th.co.infinitecorp.www.qcontroller.Utils.Protocol;

public class QTouchService  implements IQTouchService  {

    private static final String TAG = QTouchService.class.getSimpleName();
    Context context;
    public  QTouchService(Context context)
    {
        this.context=context;
    }

    /*QTouch Server Service*/
    public  boolean Handle_Server_QTOUCH(Context context, TCPServer tcpServer, Socket socket, byte[] bytes) {
        this.context=context;
        boolean resp=false;
        int idx=0;
        if(!Protocol.Verify_DataFrame(bytes)) {
            return false;
        }

        int len= Protocol.GetLength(bytes[0],bytes[1]);
        byte cmd=bytes[3];
        byte frameID=bytes[4];
        byte deviceType=bytes[5];
        byte deviceID=bytes[6];

        switch (cmd)
        {
            case Protocol.QTOUCH_CMD.SYNC:
                answer(tcpServer,socket,cmd,deviceType,deviceID,Protocol.ACK);
                break;
            case Protocol.QTOUCH_CMD.REQUEST_Q:
                Response_REQUEST_QUEUE(context,tcpServer,socket,cmd,deviceType,deviceID,bytes);
                QueueMgr.UpdateQueueStatChange();
                break;
            case Protocol.QTOUCH_CMD.RESET_Q:
                answer(tcpServer,socket,cmd,deviceType,deviceID,Protocol.ACK);
                SystemMgr.ResetQ_All(context);
                QueueMgr.UpdateQueueStatChange();
                break;
            case Protocol.QTOUCH_CMD.RESET_SYSTEM:
                answer(tcpServer,socket,cmd,deviceType,deviceID,Protocol.ACK);
                SystemMgr.Reset_System(context);
                QueueMgr.UpdateQueueStatChange();
                break;
            default:
                 answer(tcpServer,socket,cmd,deviceType,deviceID,Protocol.NACK);
                 break;
        }
        return resp;
    }
    private static void answer(TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte ans) {
        byte[] b=Protocol.Answer_Protocol_V2(cmd, Protocol.FrameID.ID2,deviceType,deviceId,ans);
        tcpServer.Send(socket,b);
    }
    public QueueInfo Get_NewQueue(Context context,byte div) {
        QueueInfo queueInfo= QueueMgr.GetNewQueue(context,div);
        EventBus.getDefault().post(new DebugMessageEvent("GetQ div="+div));
        EventBus.getDefault().post(new DebugMessageEvent("Q.Size="+GData.Queue.size()+" div="+div+" Q="+queueInfo.getQueueNo()));
        return  queueInfo;
    }
    private  boolean Response_REQUEST_QUEUE(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {
        /*
        QTOUCH->STX-LEN[2]-CMD-FrameID2-DeviceType-DeviceID-SVer[4]-HVer[4]-NONE-STATUS-DivID-TEXT_LEN[xx]-TEXT_DATA-SUM-EOT
        Server->STX-LEN[2]-CMD-FrameID1-DeviceType-DeviceID-SVer[4]-HVer[4]-ACK-STATUS-DATETIME[7]-QueueNo[4]-Copy-
       WaitQ[2]-AprxWaitTime[2]-NumPrint[2]-TEXT_LEN[2]-TEXT_DATA-SUM-EOT
Remark: DATETIME[7]=YYYY-MM-DD-hh-mm-ss
        */

        /*Prepare QTicket Info*/
        int idx=17;
        byte divId=bytes[idx++];
        QueueInfo q = Get_NewQueue(context,divId);
        DateTimeInfo dateTimeInfo = DateTime.ConvertStringeDateTimeToModel(DateTime.GetCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

        int yyyy= dateTimeInfo.getYear();
        byte month=dateTimeInfo.getMonth();
        byte day=dateTimeInfo.getDay();
        byte hour=dateTimeInfo.getHour();
        byte minute=dateTimeInfo.getMinute();
        byte second=dateTimeInfo.getSec();
        QInfo qInfo =new QInfo();
        qInfo.setqType(q.getqType());
        qInfo.setqAlp((byte)q.getqAlp());
        qInfo.setqNum((byte)q.getqNum());
        byte copy=0x01;
        short waitQ=q.getWaitq();
        short waitTime=q.getEstWaitSec();
        short numPrint=0;

        byte[] sbytes=new byte[1024*2];
        idx=0;
        //---DATETIME(YYYY-MM-DD-HH-mm-ss)
        sbytes[idx++]=Convert.GetByteHigh(yyyy);
        sbytes[idx++]=Convert.GetByteLow(yyyy);

        sbytes[idx++]=month;
        sbytes[idx++]=day;
        sbytes[idx++]=hour;
        sbytes[idx++]=minute;
        sbytes[idx++]=second;
        //---Queue
        sbytes[idx++]=qInfo.getqType();
        sbytes[idx++]=qInfo.getqAlp();
        sbytes[idx++]=Convert.GetByteHigh(qInfo.getqNum());
        sbytes[idx++]=Convert.GetByteLow(qInfo.getqNum());
        //---Copy
        sbytes[idx++]=copy;
        //---WaitQ
        sbytes[idx++]=Convert.GetByteHigh(waitQ);
        sbytes[idx++]=Convert.GetByteLow(waitQ);
        //---WaitTime
        sbytes[idx++]=Convert.GetByteHigh(waitTime);
        sbytes[idx++]=Convert.GetByteLow(waitTime);

        //---NumPrint
        sbytes[idx++]=Convert.GetByteHigh(numPrint);
        sbytes[idx++]=Convert.GetByteLow(numPrint);

        //---TEXT_LEN
        sbytes[idx++]=0x00;
        sbytes[idx++]=0x00;

        byte[] b=Protocol.prepareData_Protocol_V2(cmd, Protocol.FrameID.ID2,Protocol.DeviceType.QTOUCH,deviceId,Protocol.ACK,Protocol.RESPONSE_STATUS.SUCCESS,Arrays.copyOf(sbytes, idx));
        tcpServer.Send(socket,b);
        ViewLogMgr.ShowQueue();
        return true;
    }

    /*QTouch Client Service*/
    private static byte[] prepareData_REQUEST_QUEUE(byte divId) {
        byte[] bytes=new byte[1024*2];
        int idx=0;

        /*
        //Prototol
        QTOUCH->STX-LEN[2]-CMD-FrameID2-DeviceType-DeviceID-SVer[4]-HVer[4]-NONE-STATUS-DivID-TEXT_LEN[2]-TEXT_DATA-SUM-EOT
        Server->STX-LEN[2]-CMD-FrameID1-DeviceType-DeviceID-SVer[4]-HVer[4]-ACK-STATUS-DATETIME[7]-QueueNo[4]-Copy-
                 WaitQ[2]-AprxWaitTime[2]-NumPrint[2]-TEXT_LEN[2]-TEXT_DATA-SUM-EOT
                 Remark: DATETIME[7]=YYYY-MM-DD-hh-mm-ss
        */

        /*Data*/
        bytes[idx++] = divId;

        bytes[idx++] = 0x00;
        bytes[idx++] = 0x00;

        byte[] b = Arrays.copyOf(bytes, idx);
        byte[] sBytes=Protocol.prepareData_Protocol_V2(Protocol.QTOUCH_CMD.REQUEST_Q,Protocol.FrameID.ID1,Protocol.DeviceType.QTOUCH,(byte) 0x01,Protocol.NONE,Protocol.RESPONSE_STATUS.SUCCESS,b);
        return sBytes;
    }
    public static void Call_REQUEST_QUEUE(final byte divId){
        if(!GData.initialFinished)
            return;

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes=prepareData_REQUEST_QUEUE(divId);

                final TCPClient tcpClient_QTOUCH=new TCPClient("127.0.0.1", TCP.Server.ListenPort.Periperal);
                tcpClient_QTOUCH.SendReceive_Hex(tcpClient_QTOUCH.getSocket(),2000,bytes);
                tcpClient_QTOUCH.setOnDataReceivedListener(new TCPClient.OnDataReceivedListener() {
                    @Override
                    public void onDataReceived(Socket socket, String message,byte[] rBytes) {
                        EventBus.getDefault().post(new DebugMessageEvent("tcpClient_QTOUCH Rev data="+message+" len="+rBytes.length));

                            EventBus.getDefault().post(new DebugMessageEvent("tcpClient_QTOUCH Rev...OK"));
                            byte cmd=rBytes[3];
                            switch (cmd) {
                                case Protocol.QTOUCH_CMD.REQUEST_Q:
                                //---Print Queue
                                QTicketInfo info = new QTicketInfo();
                                int idx = 17;
                                short year = Convert.GetShort((byte) rBytes[idx++], (byte) rBytes[idx++]);
                                byte yy = 0;
                                Log.d(TAG, "year="+year);
                                //yy = (byte)(year%2000);
                                if (year > 2000) {
                                    yy = (byte) (year - 2000);
                                }

                                info.setDivId(divId);
                                info.setYear(yy);
                                info.setMonth(rBytes[idx++]);
                                info.setDate(rBytes[idx++]);
                                info.setHour(rBytes[idx++]);
                                info.setMinute(rBytes[idx++]);
                                info.setSecond((byte) rBytes[idx++]);
                                info.setqType((byte) rBytes[idx++]);
                                info.setqAlp((byte) rBytes[idx++]);
                                info.setqNum(Convert.GetQNo(rBytes[idx++], rBytes[idx++]));
                                info.setCopy((byte) rBytes[idx++]);
                                info.setWaitQ(Convert.GetShort(rBytes[idx++], rBytes[idx++]));
                                info.setWaitTime(Convert.GetShort(rBytes[idx++], rBytes[idx++]));
                                info.setNumPrint(Convert.GetShort(rBytes[idx++], rBytes[idx++]));
                                //GData.qTicketInfo=new QTicketInfo();
                                GData.qTicketInfo=info;
                                GData.qTicketInfo.setActivePrint(true);
                                /*
                                if(info.getCopy()>0) {
                                    PrinterService printerService = new PrinterService();
                                    printerService.PrintQTicketOnQPrint(GData.uart, divId, info);
                                    printerService.setOnDataReceivedListener(new PrinterService.OnDataReceivedListener() {
                                        @Override
                                        public void onDataReceived(boolean status) {
                                            EventBus.getDefault().post(new DebugMessageEvent("Get Ticket status=" + status));
                                        }
                                        @Override
                                        public void onReceiveFail() {

                                        }
                                    });
                                }
                                */
                                break;

                                default: break;
                            }

                    }
                });

            }
        });
        thread.start();

    }
}
