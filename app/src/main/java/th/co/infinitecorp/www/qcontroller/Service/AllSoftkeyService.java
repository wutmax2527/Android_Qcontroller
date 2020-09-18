package th.co.infinitecorp.www.qcontroller.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CounterlogInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentGroupInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentStationInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.QueueInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.UserlogInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.PeriperalInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.StationReserveInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.TcpSocketInfo;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.MainActivity;
import th.co.infinitecorp.www.qcontroller.Management.CounterMgr;
import th.co.infinitecorp.www.qcontroller.Management.GroupMgr;
import th.co.infinitecorp.www.qcontroller.Management.PeriperalMgr;
import th.co.infinitecorp.www.qcontroller.Management.QueueMgr;
import th.co.infinitecorp.www.qcontroller.Management.UserMgr;
import th.co.infinitecorp.www.qcontroller.Management.ViewLogMgr;
import th.co.infinitecorp.www.qcontroller.QClientWeb.Models.QClientWebInfo;
import th.co.infinitecorp.www.qcontroller.QClientWeb.QClientOnWebService;
import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCPServer;
import th.co.infinitecorp.www.qcontroller.Utils.Convert;
import th.co.infinitecorp.www.qcontroller.Utils.DateTime;
import th.co.infinitecorp.www.qcontroller.Utils.GData;
import th.co.infinitecorp.www.qcontroller.Utils.Protocol;
import th.co.infinitecorp.www.qcontroller.Utils.constant;

import static th.co.infinitecorp.www.qcontroller.Utils.Protocol.PrepareData_Protocol_V6;

public class AllSoftkeyService extends Service {
    private static final String TAG = AllSoftkeyService.class.getSimpleName();
    private Thread callAllSoftkeyThread = null;
    private static TCPServer tcpServer_SoftKeyUpdate;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.tcpServer_SoftKeyUpdate=MainActivity.tcpServer_SoftKeyUpdate;
        callAllSoftkeyThread = new Thread(CallAllSoftkeyThread);
        callAllSoftkeyThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    Thread CallAllSoftkeyThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Integer runIndex = 0;
            Integer ProcessIndex = 0;
            QueueInfo qStart;
            QueueInfo qEnd;
            while (true) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (runIndex++ >= 25) {
                    runIndex = 0;
                    EventBus.getDefault().post(new DebugMessageEvent(TAG + " Thread Run..." + ProcessIndex));
                    if (ProcessIndex++ > 100)
                        ProcessIndex = 0;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                /*Update Data To AllSoftkey*/
                UpdateQueueStatusToSoftkey(AllSoftkeyService.this,tcpServer_SoftKeyUpdate);

                //---Handle Call Reserve Q
                try
                {
                    for (int i = 0; i < GData.stationReserveInfos.size(); i++)
                    {
                        StationReserveInfo m = new StationReserveInfo();
                        m = GData.stationReserveInfos.get(i);
                        final byte sta=(byte) m.getId();
                        qStart = QueueMgr.Search_Next_Queue((byte) sta);

                        if(qStart.getqNum()>0) {
                            if (m.getKeyType() == (byte) CounterMgr.KEYTYPE.softkey.getValue()) {
                                EventBus.getDefault().post(new DebugMessageEvent(TAG + " station Call Reserve(Win)=" + m.getId()));

                                Socket socket=null;
                                TcpSocketInfo tcpSocketInfo=new TcpSocketInfo();
                                for (TcpSocketInfo t : GData.TcpSocketInfos) {
                                    if(t.getDeviceId()==sta) {
                                        socket = t.getSocket();
                                        tcpSocketInfo = t;
                                        break;
                                    }
                                }
                                if(socket!=null&&tcpSocketInfo.getStatus() == PeriperalMgr.status.ACTIVE) {
                                    int idx = 0;
                                    byte[] b;

                                    byte[] sbytes = new byte[1024 * 2];
                                    sbytes[idx++] = qStart.getqType();
                                    sbytes[idx++] = qStart.getqAlp();
                                    sbytes[idx++] = Convert.GetByteHigh(qStart.getqNum());
                                    sbytes[idx++] = Convert.GetByteLow(qStart.getqNum());
                                    sbytes[idx++] = qStart.getDivisionId();
                                    sbytes[idx++] = 0x00;
                                    sbytes[idx++] = 0x00;

                                    b = new byte[idx];
                                    b = Arrays.copyOf(sbytes, idx);
                                    byte cmd = Protocol.Softkey_UPDATEDATA_CMD.RESERVEQ;
                                    qEnd=new QueueInfo();
                                    final QueueInfo qS=qStart;
                                    final QueueInfo qE=qEnd;
                                    if(tcpServer_SoftKeyUpdate!=null) {
                                        tcpServer_SoftKeyUpdate.Send(socket, PrepareData_Protocol_V6(cmd, sta, b));
                                        EventBus.getDefault().post(new DebugMessageEvent("send ReservQ Len=" + b.length));
                                        if (qS.getqNum() > 0) {
                                            QueueMgr.CallingQueue(AllSoftkeyService.this, qS, qE, (byte) sta, false, true);
                                            CounterMgr.Remove_reserve_station(sta);
                                        }
                                        try {
                                            Thread.sleep(5000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                              break;
                            } else {
                                boolean rt=QClientOnWebService.Send(qStart,sta);
                                if(rt) {
                                    if (qStart.getqNum() > 0) {
                                        qEnd = new QueueInfo();
                                        QueueMgr.CallingQueue(AllSoftkeyService.this, qStart, qEnd, (byte) sta, false, true);
                                        CounterMgr.Remove_reserve_station(sta);
                                    }
                                }

                                EventBus.getDefault().post(new DebugMessageEvent(TAG + " station Call Reserve(Web)=" + m.getId()));

                            }
                        }
                    }
                }catch(Exception ex) {
                }
            }
        }
    });

    public static void Handle_ALLSoftkeyCmd(Context context, TCPServer tcpServer, Socket socket, byte[] bytes) {
        byte deviceType = Protocol.DeviceType.SW_KEYPAD;
        byte sta = bytes[3];
        byte cmd=bytes[4];
        EventBus.getDefault().post(new DebugMessageEvent("Handle_ALLSoftkeyCmd cmd="+cmd));

        switch (cmd) {

            case Protocol.Softkey_CMD.STARTUP:
                STARTUP(context,tcpServer,socket,cmd,sta,bytes);
                break;
            case Protocol.Softkey_CMD.LOGON:
                Logon(context,tcpServer,socket,cmd,sta,bytes);
                break;
            case Protocol.Softkey_CMD.PAUSE:
                Pause(context,tcpServer,socket,cmd,sta,bytes);
                break;
            case Protocol.Softkey_CMD.NEXT:
                Next(context,tcpServer,socket,cmd,sta,bytes);
                break;
            case Protocol.Softkey_CMD.RECALL:
                Recall(context,tcpServer,socket,cmd,sta,bytes);
                break;
            case Protocol.Softkey_CMD.DIRECT_CALL:
                Direct_Call(context,tcpServer,socket,cmd,sta,bytes);
                break;
            case Protocol.Softkey_CMD.ENDTRANS:
                Entrans(context,tcpServer,socket,cmd,sta,bytes);
                break;
            case Protocol.Softkey_CMD.CANCEL:
                Cancel(context,tcpServer,socket,cmd,sta,bytes);
                break;
            case Protocol.Softkey_CMD.HOLD:
                Hold(context,tcpServer,socket,cmd,sta,bytes);
                break;
            case Protocol.Softkey_CMD.CALLHOLD:
                Call_Hold(context,tcpServer,socket,cmd,sta,bytes);
                break;
            case Protocol.Softkey_CMD.BREAK:
                BREAK(context,tcpServer,socket,cmd,sta,bytes);
                break;
            case Protocol.Softkey_CMD.CHANGEGROUP:
                ChangeGroup(context,tcpServer,socket,cmd,sta,bytes);
                break;
            case Protocol.Softkey_CMD.WALKDIRECT:
                WalkDirect(context,tcpServer,socket,cmd,sta,bytes);
                break;
            case Protocol.Softkey_CMD.TRANSFER:
                TRANSFER(context,tcpServer,socket,cmd,sta,bytes);
                break;
            case Protocol.Softkey_CMD.TRANSFER_STA:
                TRANSFER_STA(context,tcpServer,socket,cmd,sta,bytes);
                break;
            case Protocol.Softkey_CMD.TRANSFER_To_Employee:
                TRANSFER_To_Employee(context,tcpServer,socket,cmd,sta,bytes);
                break;
            case Protocol.Softkey_CMD.TRANSFER_To_Station:
                TRANSFER_To_Station(context,tcpServer,socket,cmd,sta,bytes);
                break;
            case Protocol.Softkey_CMD.TRANSFER_To_DIV:
                TRANSFER_To_DIV(context,tcpServer,socket,cmd,sta,bytes);
                break;

        }

    }

    public static void Handle_ALLSoftkeyUpdate(Context context, TCPServer tcpServer, Socket socket, byte[] bytes) {

        byte deviceType = Protocol.DeviceType.SW_KEYPAD;
        byte deviceId = bytes[3];
        byte cmd=bytes[4];
        EventBus.getDefault().post(new DebugMessageEvent("Handle_ALLSoftkeyUpdate cmd="+cmd));

    }

    private static byte[] prepareUpdateData(byte cmd, byte[] bytes) {
        byte[] bytes1 = new byte[1024 * 10];
        int idx = 0;
        if (bytes != null) {
            for (Integer i = 0; i < bytes.length; i++) {
                bytes1[idx++] = bytes[i];
            }
        }
        byte[] b = null;
        if (idx > 0)
            b = Arrays.copyOf(bytes1, idx);
        return Protocol.PrepareData_Protocol_V5(cmd, b);
    }

    private static byte[] prepareData(byte cmd, byte deviceId, byte[] bytes) {
        byte[] bytes1 = new byte[1024 * 2];
        int idx = 0;
        if (bytes != null) {
            for (Integer i = 0; i < bytes.length; i++) {
                bytes1[idx++] = bytes[i];
            }
        }
        byte[] b = null;
        if (idx > 0)
            b = Arrays.copyOf(bytes1, idx);
        return Protocol.PrepareData_Protocol_V4(cmd, deviceId, b);
    }

    /*Function Handle */
    private static void UpdateQueueStatusToSoftkey(Context context,TCPServer tcpServer) {

        if(tcpServer==null) return;

        byte[] sbytes = new byte[1024 * 2];
        byte[] b;
        int idx = 0;
        byte cmd;
        String strData = "";
        byte[] byteArrray;
        String inputString;
        Charset charset = StandardCharsets.UTF_16LE;
        //stx-bh-bl-cmd-data-chkSum-eot

        /*GROUP status info*/
        idx=0;

        //inputString = "1|2|3|4|5|6|7;";
        inputString="";
        for(Integer i=0;i<constant.nGroup;i++) {
            int grpId=i+1;
            CurrentGroupInfo curS = GroupMgr.Find_CurrentByGroup(context, grpId);
            if(curS.getId()==grpId) {
                int wQ = curS.getWaitQ();
                int openCounter = 1;
                int waitQ = wQ;
                int maxWaitSec = 0;
                int avgWaitSec = 0;
                int holdQ = curS.getHoldQ();
                String lastQ = "";
                inputString += grpId + "|";
                inputString += openCounter + "|";
                inputString += waitQ + "|";
                inputString += maxWaitSec + "|";
                inputString += avgWaitSec + "|";
                inputString += lastQ + "|";
                inputString += holdQ;
                inputString += ";";
            }
        }

        charset = StandardCharsets.UTF_16LE;
        byteArrray = inputString.getBytes(charset);
        cmd=Protocol.Softkey_UPDATEDATA_CMD.STATUS_INFO;
        for (int i = 0; i < byteArrray.length; i++) {
            sbytes[idx++] = (byte) byteArrray[i];
        }
        b=new byte[idx];
        b = Arrays.copyOf(sbytes, idx);

        EventBus.getDefault().post(new DebugMessageEvent("Will_sendUpdate_STATUS_INFO Len=" + b.length));

        for (TcpSocketInfo t : GData.TcpSocketInfos) {

            Socket socket=t.getSocket();
            if ((t.getSocket()!=null)&& (t.getDeviceType()==Protocol.DeviceType.SW_KEYPAD)&& (t.getStatus() == PeriperalMgr.status.ACTIVE)) {
                tcpServer.Send(socket, prepareUpdateData(cmd, b));
                EventBus.getDefault().post(new DebugMessageEvent("sendUpdate_STATUS_INFO Len=" + b.length));
            }
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*STATION_WAIT*/
        /*
        idx=0;
        cmd=Protocol.Softkey_UPDATEDATA_CMD.STATION_WAIT;
        for (int i = 0; i < constant.nStation; i++)
        {
            sbytes[idx++] = (byte) 0x03;
        }

        b = Arrays.copyOf(sbytes, idx);

        EventBus.getDefault().post(new DebugMessageEvent("Will_sendUpdate_STATION_WAIT Len=" + b.length));

        for (TcpSocketInfo t : GData.TcpSocketInfos) {
            byte deviceId=t.getDeviceId();
            byte sta= t.getDeviceId();
            Socket socket=t.getSocket();
            if ((t.getSocket()!=null)&& (t.getDeviceType()==Protocol.DeviceType.SW_KEYPAD)&& (t.getStatus() == PeriperalMgr.status.ACTIVE)) {
                tcpServer.Send(socket, prepareUpdateData(cmd, b));
                EventBus.getDefault().post(new DebugMessageEvent("sendUpdate_STATION_WAIT Len=" + b.length));
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        /*COUNTER_STATUS_INFO*/
        //CurrentGroupInfo curS = GroupMgr.Find_CurrentByGroup(context, grpId);
        /*

          strData += id.ToString() + "|";
                                strData += QueMgr.counter_stat[i].userID.ToString() + "|";
                                strData += staffName + "|";
                                strData += QueMgr.counter_stat[i].groupID.ToString() + "|";
                                strData += groupName + "|";
                                strData += QueMgr.counter_stat[i].qWait.ToString() + "|";
                                strData += QueMgr.counter_stat[i].qWaitTransfer.ToString() + "|";
                                strData += QueMgr.counter_stat[i].qHold.ToString() + "|";
                                strData += QueMgr.counter_stat[i].servedQ.ToString() + "|";
                                strData += QueMgr.counter_stat[i].abandonedQ.ToString() + "|";
                                strData += QueMgr.counter_stat[i].nStaOpen.ToString() + "|";
                                strData += QueMgr.counter_stat[i].waitingTimeMode.ToString() + "|";
                                strData += QueMgr.counter_stat[i].userProfile.ToString() + "|";
                                strData += QueMgr.counter_stat[i].printerOnLine.ToString() + "|";
                                strData += QueMgr.counter_stat[i].paperAlarm.ToString() + "|";
                                strData += QueMgr.counter_stat[i].waitQAlarm.ToString() + "|";
                                strData += QueMgr.counter_stat[i].waitTimeAlarm.ToString() + ",";
         */

        /*
        idx=0;
        sbytes = new byte[1024 * 10];
        //inputString = "1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17,";
        inputString="";
        for(Integer i=0;i<constant.nStation;i++) {
            Integer ctId=i+1;
            byte ct=ctId.byteValue();
            CurrentStationInfo curS=CounterMgr.Find_CurrentUserByStation(context,ct);
            if(curS.getId()==ctId) {
                String userID="1234";
                String staffName="staffName1";
                int groupID=curS.getGroupId();
                String groupName="groupName1";
                int wQ = curS.getWaitQ();
                int waitQ = wQ;
                int waitT = 2;
                int holdQ = 2;
                int servedQ= 0;
                int abandonedQ= 0;
                int nStaOpen= 1;
                int waitingTimeMode=0;
                int userProfile=0;
                int printerOnLine=0;
                int paperAlarm=0;
                int waitQAlarm=0;
                int waitTimeAlarm=0;

                inputString += ctId + "|";
                inputString += userID + "|";
                inputString += staffName + "|";
                inputString += groupID + "|";
                inputString += groupName + "|";
                inputString += waitQ + "|";
                inputString += waitT + "|";
                inputString += holdQ + "|";
                inputString += servedQ + "|";
                inputString += abandonedQ + "|";
                inputString += nStaOpen+ "|";
                inputString += waitingTimeMode+ "|";
                inputString += userProfile+ "|";
                inputString += printerOnLine+ "|";
                inputString += paperAlarm+ "|";
                inputString += waitQAlarm+ "|";
                inputString += waitTimeAlarm;
                inputString += ",";
            }
        }
        byteArrray = inputString.getBytes(StandardCharsets.UTF_16LE);
        cmd=Protocol.Softkey_UPDATEDATA_CMD.COUNTER_STATUS_INFO;
        for (int i = 0; i < byteArrray.length; i++) {
            sbytes[idx++] = (byte) byteArrray[i];
        }
        b = Arrays.copyOf(sbytes, idx);
        EventBus.getDefault().post(new DebugMessageEvent("Will_sendUpdate_COUNTER_STATUS_INFO Len=" + b.length));
        for (TcpSocketInfo t : GData.TcpSocketInfos) {
            Socket socket=t.getSocket();
            if ((t.getSocket()!=null)&& (t.getDeviceType()==Protocol.DeviceType.SW_KEYPAD)&& (t.getStatus() == PeriperalMgr.status.ACTIVE)) {
                tcpServer.Send(socket, prepareUpdateData(cmd, b));
                EventBus.getDefault().post(new DebugMessageEvent("sendUpdate_COUNTER_STATUS_INFO Len=" + b.length));
            }
        }
        */

    }

    private static boolean STARTUP(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {

        byte[] sbytes = new byte[1024 * 1];
        byte[] byteArrray=null;
        String inputString="";
        byte[] b;
        int idx = 0;

        byte subCmd = bytes[5];
        EventBus.getDefault().post(new DebugMessageEvent("Handle_ALLSoftkeyUpdate subCmd="+subCmd));
        switch (subCmd) {
            case Protocol.Softkey_STARTUP_SUBCMD.groupinfo:
                CurrentStationInfo curS=new CurrentStationInfo();
                curS=CounterMgr.Find_CurrentUserByStation(context,sta);
                sbytes[idx++] = subCmd;
                sbytes[idx++]=curS.getGroupId();
                inputString="1|G1|0|0;2|G2|0|0;3|G3|0|0;";
                byteArrray = inputString.getBytes(StandardCharsets.UTF_16LE);
                for (int i = 0; i < byteArrray.length; i++) {
                    sbytes[idx++] = (byte) byteArrray[i];
                }
                b = Arrays.copyOf(sbytes, idx);
                tcpServer.Send(socket, prepareData(cmd, sta, b));
                break;
            case Protocol.Softkey_STARTUP_SUBCMD.divinfo:
                sbytes[idx++] = subCmd;
                inputString="1|Div1|0;2|Div2|0;3|Div3|0;";
                byteArrray = inputString.getBytes(StandardCharsets.UTF_16LE);
                for (int i = 0; i < byteArrray.length; i++) {
                    sbytes[idx++] = (byte) byteArrray[i];
                }
                b = Arrays.copyOf(sbytes, idx);
                tcpServer.Send(socket, prepareData(cmd, sta, b));
                break;
            case Protocol.Softkey_STARTUP_SUBCMD.breakinfo:
                sbytes[idx++] = subCmd;
                inputString="1|BreakReason1|1;2|BreakReason2|2;";
                byteArrray = inputString.getBytes(StandardCharsets.UTF_16LE);
                for (int i = 0; i < byteArrray.length; i++) {
                    sbytes[idx++] = (byte) byteArrray[i];
                }
                b = Arrays.copyOf(sbytes, idx);
                tcpServer.Send(socket, prepareData(cmd, sta, b));
                break;
            case Protocol.Softkey_STARTUP_SUBCMD.holdreasoninfo:
                sbytes[idx++] = subCmd;
                inputString="1|HoldReason1|1;2|HoldReason2|2;";
                byteArrray = inputString.getBytes(StandardCharsets.UTF_16LE);
                for (int i = 0; i < byteArrray.length; i++) {
                    sbytes[idx++] = (byte) byteArrray[i];
                }
                b = Arrays.copyOf(sbytes, idx);
                tcpServer.Send(socket, prepareData(cmd, sta, b));
                break;
            case Protocol.Softkey_STARTUP_SUBCMD.userSettingInfo:
                sbytes[idx++] = subCmd;
                b = Arrays.copyOf(sbytes, idx);
                tcpServer.Send(socket, prepareData(cmd, sta, b));
                break;
            case Protocol.Softkey_STARTUP_SUBCMD.counterinfo:
                sbytes[idx++] = subCmd;
                b = Arrays.copyOf(sbytes, idx);
                tcpServer.Send(socket, prepareData(cmd, sta, b));
                break;
            case Protocol.Softkey_STARTUP_SUBCMD.divList:
                sbytes[idx++] = subCmd;
                b = Arrays.copyOf(sbytes, idx);;
                tcpServer.Send(socket, prepareData(cmd, sta, b));
                break;
        }



        return true;
    }

    private static boolean Logon(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {
        byte[] sbytes = new byte[1024 * 2];
        int idx = 0;


        sbytes[idx++] = Protocol.ACK;
        String inputString="12345";


        byte[] byteArrray;
        byteArrray = inputString.getBytes(StandardCharsets.UTF_16LE);
        for (int i = 0; i < byteArrray.length; i++)
        {
            sbytes[idx++] = (byte) byteArrray[i];
        }
        byte[] b = Arrays.copyOf(sbytes, idx);
        tcpServer.Send(socket, prepareData(cmd, sta, b));

        return true;

    }

    private static boolean Pause(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {
        byte[] sbytes = new byte[1024 * 1];
        int idx = 0;

        QueueInfo qStart = QueueMgr.Search_Serving_Queue(sta);
        qStart.setEndDateTime(DateTime.GetDateTimeNow());
        QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.finish_by_logoff);

        sbytes[idx++] = Protocol.ACK;
        byte[] b = Arrays.copyOf(sbytes, idx);
        tcpServer.Send(socket, prepareData(cmd, sta, b));

        return true;

    }

    private static boolean Next(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {


        byte[] sbytes = new byte[1024 * 2];
        int idx = 0;
        Integer tranno=0;
        UserlogInfo ulog =new UserlogInfo();
        UserlogInfo u=new UserlogInfo();
        CounterlogInfo clog=new CounterlogInfo();
        CurrentStationInfo curS=new CurrentStationInfo();

        if(bytes.length==7) {
            //[Qcontroller] -> stx-bh-bl-sta-NEXT-QType-Qnum3byte-Qdiv-prevDiv-waitSec[2]-sum-eot
            QueueMgr.Finish_Queue(context, sta);
            QueueInfo q = QueueMgr.Search_Next_Queue(sta);
            sbytes[idx++] = q.getqType();
            sbytes[idx++] = q.getqAlp();
            sbytes[idx++] = Convert.GetByteHigh(q.getqNum());
            sbytes[idx++] = Convert.GetByteLow(q.getqNum());
            sbytes[idx++] = 0x01;
            sbytes[idx++] = 0x01;
            sbytes[idx++] = 0x00;
            sbytes[idx++] = 0x00;
            byte[] b = Arrays.copyOf(sbytes, idx);
            tcpServer.Send(socket, prepareData(cmd, sta, b));

        }else if(bytes.length==8){

            QueueInfo qStart = QueueMgr.Search_Next_Queue(sta);
            if(qStart.getqNum()>0) {
                qStart.setUserId(UserMgr.Find_CurrentUserByCounterActive(context, sta).getUserId());
                qStart.setStationId(sta);
                qStart.setStDateTime(DateTime.GetDateTimeNow());
                QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.qcalling_serving);

                curS = CounterMgr.Find_CurrentUserByStation(context, sta);
                curS.setQueueInfo(qStart);
                CounterMgr.UpdateCurrentStationStatus(context, sta, curS);
                QueueMgr.UpdateQueueStatChange();
                QInfo qS = new QInfo();
                QInfo qE = new QInfo();

                qS.setqType(qStart.getqType());
                qS.setqAlp(qStart.getqAlp());
                qS.setqNum(qStart.getqNum());
                QueueMgr.UpdateCallingQueue(qS, qE, sta);
                CounterMgr.Remove_reserve_station(sta);
            }else
            {
                CounterMgr.Add_reserve_station(sta, (byte) CounterMgr.KEYTYPE.softkey.getValue());
            }

            ViewLogMgr.ShowQueue();
        }
        return true;
    }

    private static boolean Recall(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {

        QueueInfo qStart = QueueMgr.Search_Serving_Queue(sta);

        QInfo qS = new QInfo();
        QInfo qE = new QInfo();

        qS.setqType(qStart.getqType());
        qS.setqAlp(qStart.getqAlp());
        qS.setqNum(qStart.getqNum());
        QueueMgr.UpdateCallingQueue(qS, qE, sta);

        byte[] sbytes = new byte[1024 * 1];
        int idx = 0;
        sbytes[idx++] = Protocol.ACK;
        byte[] b = Arrays.copyOf(sbytes, idx);
        tcpServer.Send(socket, prepareData(cmd, sta, b));

        return true;
    }

    private static boolean Direct_Call(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {

        //   [Sofkey]      -> stx-bh-bl-sta-DIRECT-Qtype-Qnum3byte-sum-eot
        //    [Qcontroller] -> stx-bh-bl-sta-DIRECT-Qtype-Qdiv-Ack-sum-eot


        int idx = 0;
        idx = 5;
        byte qType = bytes[idx++];
        byte qAlp = bytes[idx++];
        byte qH = bytes[idx++];
        byte qL = bytes[idx++];
        //---Rev Confirm Ack Queue
        QInfo qIn = new QInfo();
        qIn.setqType(qType);
        qIn.setqAlp(qAlp);
        qIn.setqNum(Convert.GetQNo(qH, qL));
        QueueInfo qStart = QueueMgr.Search_Queue(qIn);

        QInfo qS = new QInfo();
        QInfo qE = new QInfo();
        qS.setqType(qStart.getqType());
        qS.setqAlp(qStart.getqAlp());
        qS.setqNum(qStart.getqNum());

        qStart.setUserId(UserMgr.Find_CurrentUserByCounterActive(context,sta).getUserId());
        qStart.setStationId(sta);
        qStart.setStDateTime(DateTime.GetDateTimeNow());
        QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.qcalling_serving);

        CurrentStationInfo curS=new CurrentStationInfo();
        curS=CounterMgr.Find_CurrentUserByStation(context,sta);
        curS.setQueueInfo(qStart);
        CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
        QueueMgr.UpdateQueueStatChange();

        QueueMgr.UpdateCallingQueue(qS, qE, sta);

        byte[] sbytes = new byte[1024 * 1];
        idx = 0;
        sbytes[idx++] = qType;
        sbytes[idx++] = 0x01;
        sbytes[idx++] = Protocol.ACK;
        byte[] b = Arrays.copyOf(sbytes, idx);
        tcpServer.Send(socket, prepareData(cmd, sta, b));

        return true;
    }

    private static boolean Entrans(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {

        QueueInfo qStart = QueueMgr.Search_Serving_Queue(sta);
        qStart.setEndDateTime(DateTime.GetDateTimeNow());
        QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.finish_by_next);

        byte[] sbytes = new byte[1024 * 1];
        int idx = 0;
        sbytes[idx++] = Protocol.ACK;
        byte[] b = Arrays.copyOf(sbytes, idx);
        tcpServer.Send(socket, prepareData(cmd, sta, b));
        return true;
    }

    private static boolean Cancel(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {

        QueueInfo qStart = QueueMgr.Search_Serving_Queue(sta);
        qStart.setEndDateTime(DateTime.GetDateTimeNow());
        QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.cancelQ);

        byte[] sbytes = new byte[1024 * 1];
        int idx = 0;
        sbytes[idx++] = Protocol.ACK;
        byte[] b = Arrays.copyOf(sbytes, idx);
        tcpServer.Send(socket, prepareData(cmd, sta, b));
        return true;
    }

    private static boolean Hold(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {

        QueueInfo qStart = QueueMgr.Search_Serving_Queue(sta);
        qStart.setEndDateTime(DateTime.GetDateTimeNow());
        QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.qhold);

        byte[] sbytes = new byte[1024 * 1];
        int idx = 0;
        sbytes[idx++] = Protocol.ACK;
        byte[] b = Arrays.copyOf(sbytes, idx);
        tcpServer.Send(socket, prepareData(cmd, sta, b));
        return true;
    }

    private static boolean Call_Hold(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {

        byte[] sbytes = new byte[1024 * 2];
        int idx = 0;
        Integer tranno=0;
        UserlogInfo ulog =new UserlogInfo();
        UserlogInfo u=new UserlogInfo();
        CounterlogInfo clog=new CounterlogInfo();
        CurrentStationInfo curS=new CurrentStationInfo();

        if(bytes.length==7) {
            //[Qcontroller] ->stx-bh-bl-sta-CallHold-QType-Qnum3byte-Qdiv-pvDiv-holdSec2byte-sum-eot
            QueueMgr.Finish_Queue(context, sta);
            QueueInfo q = QueueMgr.Search_Hold_Queue(sta);
            sbytes[idx++] = q.getqType();
            sbytes[idx++] = q.getqAlp();
            sbytes[idx++] = Convert.GetByteHigh(q.getqNum());
            sbytes[idx++] = Convert.GetByteLow(q.getqNum());
            sbytes[idx++] = 0x01;
            sbytes[idx++] = 0x01;
            sbytes[idx++] = 0x00;
            sbytes[idx++] = 0x00;
            byte[] b = Arrays.copyOf(sbytes, idx);
            tcpServer.Send(socket, prepareData(cmd, sta, b));
        }else if(bytes.length==8){

            QueueInfo qStart = QueueMgr.Search_Hold_Queue(sta);
            qStart.setUserId(UserMgr.Find_CurrentUserByCounterActive(context,sta).getUserId());
            qStart.setStationId(sta);
            qStart.setStDateTime(DateTime.GetDateTimeNow());
            QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.qcalling_serving);

            curS=CounterMgr.Find_CurrentUserByStation(context,sta);
            curS.setQueueInfo(qStart);
            CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
            QueueMgr.UpdateQueueStatChange();
            QInfo qS = new QInfo();
            QInfo qE = new QInfo();

            qS.setqType(qStart.getqType());
            qS.setqAlp(qStart.getqAlp());
            qS.setqNum(qStart.getqNum());
            QueueMgr.UpdateCallingQueue(qS, qE, sta);

            ViewLogMgr.ShowQueue();
        }
        return true;
    }

    private static boolean BREAK(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {
        byte[] sbytes = new byte[1024 * 1];
        int idx = 0;

        QueueInfo qStart = QueueMgr.Search_Serving_Queue(sta);
        qStart.setEndDateTime(DateTime.GetDateTimeNow());
        QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.finish_by_logoff);
        idx = 5;

        idx = 0;
        sbytes[idx++] = Protocol.ACK;
        byte[] b = Arrays.copyOf(sbytes, idx);
        tcpServer.Send(socket, prepareData(cmd, sta, b));

        return true;
    }

    private static boolean ChangeGroup(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {
        byte[] sbytes = new byte[1024 * 1];
        int idx = 0;

        QueueInfo qStart = QueueMgr.Search_Serving_Queue(sta);
        qStart.setEndDateTime(DateTime.GetDateTimeNow());
        QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.finish_by_next);
        idx=5;
        byte newGrpId=bytes[idx];

        CurrentStationInfo curS=new CurrentStationInfo();
        curS=CounterMgr.Find_CurrentUserByStation(context,sta);
        curS.setGroupId(newGrpId);
        CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
        curS=CounterMgr.Find_CurrentUserByStation(context,sta);

        byte ans=Protocol.ACK;
        if(CounterMgr.ChangeGroup(sta,curS.getGroupId()))
            ans=Protocol.ACK;
        else
            ans=Protocol.NACK;

        idx = 0;
        sbytes[idx++] = ans;
        byte[] b = Arrays.copyOf(sbytes, idx);
        tcpServer.Send(socket, prepareData(cmd, sta, b));

        return true;
    }
    private static boolean WalkDirect(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {


        /*
         [Sofkey]      -> stx-bh-bl-sta-WALKDIRECT-Qdiv-sum-eot
         [Qcontroller] -> stx-bh-bl-sta-WALKDIRECT-QType-Qnum3byte-sum-eot
         [Sofkey]      -> stx-bh-bl-sta-WALKDIRECT-ACK-0x00-sum-eot
         */

        QueueMgr.Finish_Queue(context, sta);

        byte[] sbytes = new byte[1024 * 2];
        int idx = 0;
        Integer tranno=0;
        UserlogInfo ulog =new UserlogInfo();
        UserlogInfo u=new UserlogInfo();
        CounterlogInfo clog=new CounterlogInfo();
        CurrentStationInfo curS=new CurrentStationInfo();

        if(bytes.length==8) {
            //[Qcontroller] -> stx-bh-bl-sta-WALKDIRECT-Qdiv-sum-eot
            idx=5;
            byte divId=bytes[idx];
            QueueMgr.Finish_Queue(context, sta);
            QueueInfo q = QueueMgr.GetNewQueue(context, divId);
            EventBus.getDefault().post(new DebugMessageEvent("GetQ div=" + divId));

            sbytes[idx++] = q.getqType();
            sbytes[idx++] = q.getqAlp();
            sbytes[idx++] = Convert.GetByteHigh(q.getqNum());
            sbytes[idx++] = Convert.GetByteLow(q.getqNum());
            sbytes[idx++] = 0x01;
            sbytes[idx++] = 0x01;
            sbytes[idx++] = 0x00;
            sbytes[idx++] = 0x00;
            byte[] b = Arrays.copyOf(sbytes, idx);
            tcpServer.Send(socket, prepareData(cmd, sta, b));
        }else if(bytes.length==9){

            QueueInfo qStart = QueueMgr.Search_Next_Queue(sta);
            qStart.setUserId(UserMgr.Find_CurrentUserByCounterActive(context,sta).getUserId());
            qStart.setStationId(sta);
            qStart.setStDateTime(DateTime.GetDateTimeNow());
            QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.qcalling_serving);

            curS=CounterMgr.Find_CurrentUserByStation(context,sta);
            curS.setQueueInfo(qStart);
            CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
            QueueMgr.UpdateQueueStatChange();
            QInfo qS = new QInfo();
            QInfo qE = new QInfo();

            qS.setqType(qStart.getqType());
            qS.setqAlp(qStart.getqAlp());
            qS.setqNum(qStart.getqNum());
            QueueMgr.UpdateCallingQueue(qS, qE, sta);

            ViewLogMgr.ShowQueue();
        }

        return true;
    }

    private static boolean TRANSFER(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {
        byte[] sbytes = new byte[1024 * 1];
        int idx = 0;

        QueueInfo qStart = QueueMgr.Search_Serving_Queue(sta);
        qStart.setEndDateTime(DateTime.GetDateTimeNow());
        QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.finish_by_transec);
        idx=5;
        byte newGrpId=bytes[idx];

        CurrentStationInfo curS=new CurrentStationInfo();
        curS=CounterMgr.Find_CurrentUserByStation(context,sta);
        curS.setGroupId(newGrpId);
        CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
        curS=CounterMgr.Find_CurrentUserByStation(context,sta);

        byte ans=Protocol.ACK;
        if(curS.getGroupId()==newGrpId)
            ans=Protocol.ACK;
        else
            ans=Protocol.NACK;

        idx = 0;
        sbytes[idx++] = ans;
        byte[] b = Arrays.copyOf(sbytes, idx);
        tcpServer.Send(socket, prepareData(cmd, sta, b));

        return true;
    }

    private static boolean TRANSFER_STA(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {
        byte[] sbytes = new byte[1024 * 1];
        int idx = 0;

        QueueInfo qStart = QueueMgr.Search_Serving_Queue(sta);
        qStart.setEndDateTime(DateTime.GetDateTimeNow());
        QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.finish_by_transec);
        idx=5;
        byte newGrpId=bytes[idx];

        CurrentStationInfo curS=new CurrentStationInfo();
        curS=CounterMgr.Find_CurrentUserByStation(context,sta);
        curS.setGroupId(newGrpId);
        CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
        curS=CounterMgr.Find_CurrentUserByStation(context,sta);

        byte ans=Protocol.ACK;
        if(curS.getGroupId()==newGrpId)
            ans=Protocol.ACK;
        else
            ans=Protocol.NACK;

        idx = 0;
        sbytes[idx++] = ans;
        byte[] b = Arrays.copyOf(sbytes, idx);
        tcpServer.Send(socket, prepareData(cmd, sta, b));

        return true;
    }

    private static boolean TRANSFER_To_Employee(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {
        byte[] sbytes = new byte[1024 * 1];
        int idx = 0;

        QueueInfo qStart = QueueMgr.Search_Serving_Queue(sta);
        qStart.setEndDateTime(DateTime.GetDateTimeNow());
        QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.finish_by_transec);
        idx=5;
        byte newGrpId=bytes[idx];

        CurrentStationInfo curS=new CurrentStationInfo();
        curS=CounterMgr.Find_CurrentUserByStation(context,sta);
        curS.setGroupId(newGrpId);
        CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
        curS=CounterMgr.Find_CurrentUserByStation(context,sta);

        byte ans=Protocol.ACK;
        if(curS.getGroupId()==newGrpId)
            ans=Protocol.ACK;
        else
            ans=Protocol.NACK;

        idx = 0;
        sbytes[idx++] = ans;
        byte[] b = Arrays.copyOf(sbytes, idx);
        tcpServer.Send(socket, prepareData(cmd, sta, b));

        return true;
    }

    private static boolean TRANSFER_To_Station(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {
        byte[] sbytes = new byte[1024 * 1];
        int idx = 0;

        QueueInfo qStart = QueueMgr.Search_Serving_Queue(sta);
        qStart.setEndDateTime(DateTime.GetDateTimeNow());
        QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.finish_by_transec);
        idx=5;
        byte newGrpId=bytes[idx];

        CurrentStationInfo curS=new CurrentStationInfo();
        curS=CounterMgr.Find_CurrentUserByStation(context,sta);
        curS.setGroupId(newGrpId);
        CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
        curS=CounterMgr.Find_CurrentUserByStation(context,sta);

        byte ans=Protocol.ACK;
        if(curS.getGroupId()==newGrpId)
            ans=Protocol.ACK;
        else
            ans=Protocol.NACK;

        idx = 0;
        sbytes[idx++] = ans;
        byte[] b = Arrays.copyOf(sbytes, idx);
        tcpServer.Send(socket, prepareData(cmd, sta, b));

        return true;
    }

    private static boolean TRANSFER_To_DIV(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte sta, byte[] bytes) {
        byte[] sbytes = new byte[1024 * 1];
        int idx = 0;

        QueueInfo qStart = QueueMgr.Search_Serving_Queue(sta);
        qStart.setEndDateTime(DateTime.GetDateTimeNow());
        QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.finish_by_transec);
        idx=5;
        byte newGrpId=bytes[idx];

        CurrentStationInfo curS=new CurrentStationInfo();
        curS=CounterMgr.Find_CurrentUserByStation(context,sta);
        curS.setGroupId(newGrpId);
        CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
        curS=CounterMgr.Find_CurrentUserByStation(context,sta);

        byte ans=Protocol.ACK;
        if(curS.getGroupId()==newGrpId)
            ans=Protocol.ACK;
        else
            ans=Protocol.NACK;

        idx = 0;
        sbytes[idx++] = ans;
        byte[] b = Arrays.copyOf(sbytes, idx);
        tcpServer.Send(socket, prepareData(cmd, sta, b));

        return true;
    }

}
