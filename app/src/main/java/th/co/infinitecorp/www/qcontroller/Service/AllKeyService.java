package th.co.infinitecorp.www.qcontroller.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import th.co.infinitecorp.www.qcontroller.DataInfo.CounterStatusInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CounterlogInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentStationInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.QueueInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.UserlogInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.PeriperalInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.DisplayInfo;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.Management.CounterMgr;
import th.co.infinitecorp.www.qcontroller.Management.PeriperalMgr;
import th.co.infinitecorp.www.qcontroller.Management.QueueMgr;
import th.co.infinitecorp.www.qcontroller.Management.UserMgr;
import th.co.infinitecorp.www.qcontroller.Management.ViewLogMgr;
import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCP;
import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCPClient;
import th.co.infinitecorp.www.qcontroller.TCPNetwork.TCPServer;
import th.co.infinitecorp.www.qcontroller.Utils.Convert;
import th.co.infinitecorp.www.qcontroller.Utils.GData;
import th.co.infinitecorp.www.qcontroller.Utils.Protocol;

public class AllKeyService extends Service {
    private static final String TAG = AllKeyService.class.getSimpleName();
    private Thread callAllKeyThread = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        callAllKeyThread = new Thread(CallAllKeyThread);
        callAllKeyThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    Thread CallAllKeyThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Integer runIndex = 0;
            Integer ProcessIndex = 0;
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
                    Thread.sleep(4800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                /*Communicate AllKey*/
                //--Handle Call Reserve Q(QclientWin)
                for (PeriperalInfo p : GData.PeriperalInfos) {
                    String ip = p.getIp();
                    byte deviceId=p.getDeviceId();
                    byte sta=p.getDeviceId();
                    if (ip != "" && p.getStatus() == PeriperalMgr.status.ACTIVE) {
                        //---Update Key Queue Status
                        byte[] bytes = prepareDataCallReserveQ(deviceId,sta);
                        Log.w(TAG, "sendUpdatKeyStatus Len=" + bytes.length);
                        EventBus.getDefault().post(new DebugMessageEvent("C_tcpClient_KEY Send ip:" + ip + " Len=" + bytes.length));
                        final TCPClient tcpClient_KEY = new TCPClient(ip, TCP.Client.TargetPort.KEYPAD);
                        tcpClient_KEY.SendReceive_Hex(tcpClient_KEY.getSocket(), 1000, bytes);
                        tcpClient_KEY.setOnDataReceivedListener(new TCPClient.OnDataReceivedListener() {
                            @Override
                            public void onDataReceived(Socket socket, String message, byte[] rBytes) {

                                EventBus.getDefault().post(new DebugMessageEvent("C_tcpClient_KEY Rev data=" + message + " len=" + rBytes.length));
                                if (tcpClient_KEY.getSocket() == socket) {
                                    EventBus.getDefault().post(new DebugMessageEvent("C_tcpClient_KEY Remove List"));
                                }
                            }
                        });
                    }
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //---Handle Key QStatus
                for (PeriperalInfo p : GData.PeriperalInfos) {
                    String ip = p.getIp();
                    byte sta=p.getDeviceId();
                    byte deviceId=p.getDeviceId();

                    if (ip != "" && p.getStatus() == PeriperalMgr.status.ACTIVE) {
                        //---Update Key Queue Status
                        byte[] bytes = prepareDataUpdateQueueStatus(deviceId,sta);
                        Log.w(TAG, "sendUpdatKeyStatus Len=" + bytes.length);
                        EventBus.getDefault().post(new DebugMessageEvent("U_tcpClient_KEY Send ip:" + ip + " Len=" + bytes.length));
                        final TCPClient tcpClient_KEY = new TCPClient(ip, TCP.Client.TargetPort.KEYPAD);
                        tcpClient_KEY.SendReceive_Hex(tcpClient_KEY.getSocket(), 1000, bytes);
                        tcpClient_KEY.setOnDataReceivedListener(new TCPClient.OnDataReceivedListener() {
                            @Override
                            public void onDataReceived(Socket socket, String message, byte[] rBytes) {

                                EventBus.getDefault().post(new DebugMessageEvent("U_tcpClient_KEY Rev data=" + message + " len=" + rBytes.length));
                                if (tcpClient_KEY.getSocket() == socket) {
                                    EventBus.getDefault().post(new DebugMessageEvent("U_tcpClient_KEY Remove List"));
                                }
                            }
                        });
                    }
                }


            }
        }
    });

    private byte[] prepareDataCallReserveQ(byte deviceId,byte sta) {
        byte[] bytes = new byte[1024 * 2];
        int idx = 0;

        /*Data*/
        bytes[idx++] = sta;

        byte[] b = Arrays.copyOf(bytes, idx);
        byte[] sBytes = prepareDataForSend(Protocol.KEYPAD_CMD.CallReserveQ, Protocol.FrameID.ID1, Protocol.DeviceType.HW_KEYPAD, deviceId, b);
        return sBytes;

    }

    private byte[] prepareDataUpdateQueueStatus(byte deviceId,byte sta) {
        byte[] bytes = new byte[1024 * 2];
        int idx = 0;
        /*Data*/
        bytes[idx++] = sta;

        byte[] b = Arrays.copyOf(bytes, idx);
        byte[] sBytes = prepareDataForSend(Protocol.KEYPAD_CMD.RealTimeData, Protocol.FrameID.ID1, Protocol.DeviceType.HW_KEYPAD, deviceId, b);
        return sBytes;

    }

    private byte[] prepareDataForSend(byte cmd, byte frameId, byte deviceType, byte deviceId, byte[] bytes) {
        byte[] sBytes = Protocol.prepareData_Protocol_V2(cmd, frameId, deviceType, deviceId, Protocol.NONE, Protocol.RESPONSE_STATUS.SUCCESS, bytes);
        return sBytes;
    }

    public static void Handle_ALLKey(Context context, TCPServer tcpServer, Socket socket, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;
        byte qType = 0;
        ;
        byte qAlp = 0;
        byte qH = 0;
        byte qL = 0;
        int len = 0;
        QInfo qStart = new QInfo();
        QInfo qEnd = new QInfo();
        byte stx = bytes[0];
        short lenData = (short) Convert.GetShort(bytes[1], bytes[2]);
        idx = 3;
        byte cmd = bytes[idx++];
        byte frameID = bytes[idx++];
        byte deviceType = bytes[idx++];
        byte deviceID = bytes[idx++];
        byte[] bData = null;

        switch (cmd) {

            case Protocol.KEYPAD_CMD.SYNC:
                Sync(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.STARTUP:
                Startup(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.LOGIN:
                Login(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.LOGOUT:
                Logout(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.NEXT:
                Next(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.RECALL:
                Recall(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.DIRECT_CALL:
                DirectCall(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.SPAN_CALL:
                SpanCall(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.HOLD:
                Hold(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.CALLHOLD:
                CallHold(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.CANCEL:
                Cancel(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.ENDTRANS:
                EndTrans(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.WALKDIRECT:
                WalkDirect(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.TRANSFER:
                Transfer(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.TRANSACTION:
                Transaction(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.SUBDIV:
                SubDiv(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.CHANGEGROUP:
                ChangeGroup(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.HELP_ME:
                Help(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.BREAK:
                Break(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.REQUEST_CURRENT_STATUS:
                REQUEST_CURRENT_STATUS(context, tcpServer, socket, cmd, deviceType, deviceID, bytes);
                break;
            case Protocol.KEYPAD_CMD.CHECK_CONNECTION:
                Response_NormalAnswer(context, tcpServer, socket, cmd, deviceType, deviceType, Protocol.ACK, Protocol.RESPONSE_STATUS.SUCCESS);
                break;
            default:
                Response_NormalAnswer(context, tcpServer, socket, cmd, deviceType, deviceType, Protocol.NACK, Protocol.RESPONSE_STATUS.FAIL);
                break;

        }
    }

    private static byte[] prepareData(byte cmd, byte deviceType, byte deviceId, byte answer, byte status, byte[] bytes) {
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
        return Protocol.prepareData_Protocol_V2(cmd, (byte) 0x02, deviceType, deviceId, answer, status, b);
    }

    /*Function Handle */
    private static boolean Response_NormalAnswer(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte answer, byte status) {
        byte[] b = prepareData(cmd, deviceType, deviceId, answer, status, null);
        tcpServer.Send(socket, b);
        return true;
    }

    private static boolean Sync(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;
        QInfo qStart = new QInfo();
        idx = 15;
        ack = bytes[idx++];
        status = bytes[idx++];
        sta = bytes[idx++];

        //----Answer
        CounterlogInfo clog = CounterMgr.Find_CurrentUserByCounter(context, sta);
        CurrentStationInfo curS = CounterMgr.Find_CurrentUserByStation(context, sta);
        byte grpID = curS.getGroupId();
        byte counterStatus = curS.getStatus();
        int userID = clog.getUserId();
        int wQ = curS.getWaitQ();
        int hQ = curS.getHoldQ();
        int swQ = curS.getStaWaitQ();
        QueueInfo q = QueueMgr.Search_Queue_ByCounter(sta);

        byte[] sbytes = new byte[1024 * 2];
        idx = 0;
        sbytes[idx++] = sta;
        sbytes[idx++] = counterStatus;
        sbytes[idx++] = (byte) (userID >> 24);
        sbytes[idx++] = (byte) (userID >> 16);
        sbytes[idx++] = (byte) (userID >> 8);
        sbytes[idx++] = (byte) (userID & 0xFF);
        sbytes[idx++] = grpID;
        sbytes[idx++] = Convert.GetByteHigh(wQ);
        sbytes[idx++] = Convert.GetByteLow(wQ);
        sbytes[idx++] = Convert.GetByteHigh(hQ);
        sbytes[idx++] = Convert.GetByteLow(hQ);
        sbytes[idx++] = Convert.GetByteHigh(swQ);
        sbytes[idx++] = Convert.GetByteLow(swQ);
        sbytes[idx++] = curS.getQueueInfo().getqType();
        sbytes[idx++] = curS.getQueueInfo().getqAlp();
        sbytes[idx++] = Convert.GetByteHigh(curS.getQueueInfo().getqNum());
        sbytes[idx++] = Convert.GetByteLow(curS.getQueueInfo().getqNum());
        sbytes[idx++] = curS.getQueueInfo().getStatus();
        sbytes[idx++] = curS.getQueueInfo().getDivisionId();
        sbytes[idx++] = 0x00;
        sbytes[idx++] = 0x00;
        byte[] b = Arrays.copyOf(sbytes, idx);

        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, Protocol.RESPONSE_STATUS.SUCCESS, b));

        return true;
    }

    private static boolean Startup(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;

        idx = 15;
        ack = bytes[idx++];
        status = bytes[idx++];
        sta = bytes[idx++];

        //---Logout Handler
        UserlogInfo ulog = UserMgr.Find_CurrentUserByCounterActive(context, sta);
        boolean resU = UserMgr.UpdateUserlogStatus(context, sta, ulog, UserMgr.uStatus.Logout);

        CounterlogInfo clog = CounterMgr.Find_CurrentUserByCounterActive(context, sta);
        boolean resC = CounterMgr.UpdateCounterlogStatus(context, sta, clog, CounterMgr.cStatus.Logout);

        CurrentStationInfo curS = CounterMgr.Find_CurrentUserByStation(context, sta);
        curS.setStatus(CounterMgr.cStatus.Logout);
        boolean resS = CounterMgr.UpdateCurrentStationStatus(context, sta, curS);

        //----Answer
        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, Protocol.RESPONSE_STATUS.SUCCESS, null));
        return true;
    }

    private static boolean Login(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        Integer userId = 0;
        String name = "";
        boolean verify = false;
        int idx = 15;
        int len = 0;
        byte sta;
        byte[] bData = null;

        byte ack = bytes[idx++];
        byte status = bytes[idx++];
        sta = bytes[idx++];
        len = (bytes[idx++] << 8);
        len |= bytes[idx++];

        bData = new byte[len];
        for (int i = 0; i < len; i++) {
            bData[i] = bytes[idx++];
        }
        String userCode = "";
        String username = "";
        String password = "";
        String strData = new String(bData, StandardCharsets.US_ASCII);
        String[] strAr = strData.split("\\|");

        userCode = strAr[0];

        if (strAr.length > 1) {
            if (strAr[1] != null) {
                if (strAr[1].length() > 0)
                    username = strAr[1];
            }
        }
        if (strAr.length > 2) {
            if (strAr[2] != null) {
                if (strAr[2].length() > 0)
                    password = strAr[2];
            }
        }
        //---Login Handler
        if (username.equals("") && password.equals("")) {
            //---HardKey
            if (userCode.equals("999999") || userCode.equals("000000")) //fix UserCode
            {
                if (userCode.equals("999999"))
                    userId = 999999;
                name = "User Innet";
                verify = true;
            } else {

                verify = false;
            }

        } else {
            //---Softkey
            verify = false;
        }

        boolean newUser = true;
        UserlogInfo ulog = UserMgr.Find_CurrentUserByCounterActive(context, sta);
        if (ulog != null) {
            if (ulog.getUserId() != null) {
                if ((ulog.getUserId().equals(userId))) {
                    newUser = false;
                }
            }
        }

        if (verify) {

            if (newUser) {
                UserMgr.CreateNewUserlog(context, userId, sta);
                CounterMgr.CreateNewCounterlog(context, userId, sta);
            } else {
                boolean resU = UserMgr.UpdateUserlogStatus(context, sta, ulog, UserMgr.uStatus.Login);
                CounterlogInfo clog = CounterMgr.Find_CurrentUserByCounterActive(context, sta);
                boolean resC = CounterMgr.UpdateCounterlogStatus(context, sta, clog, CounterMgr.cStatus.Login);
            }
            CurrentStationInfo resS = CounterMgr.Find_CurrentUserByStation(context, sta);
            resS.setUserId(userId);
            resS.setStatus(CounterMgr.cStatus.Login);
            CounterMgr.UpdateCurrentStationStatus(context, sta, resS);

        }
        //----Answer
        byte[] sbytes = new byte[1024 * 2];
        idx = 0;
        sbytes[idx++] = sta;
        sbytes[idx++] = 0x00;
        sbytes[idx++] = 0x00;

        byte[] b = Arrays.copyOf(sbytes, idx);
        byte respStatus = Protocol.RESPONSE_STATUS.SUCCESS;
        if (!verify)
            respStatus = Protocol.RESPONSE_STATUS.FAIL;

        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, respStatus, b));
        ViewLogMgr.ShowUserlog();
        ViewLogMgr.ShowCounterlog();

        return true;
    }

    private static boolean Logout(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        boolean verify = false;
        int idx = 15;
        int len = 0;
        byte sta;
        byte ack = bytes[idx++];
        byte status = bytes[idx++];
        sta = bytes[idx++];
        //---Logout Handler
        boolean res = QueueMgr.HandleEventOnQueueStatus(context, sta, cmd, null, null, (byte) 0x00);
        byte respStatus = Protocol.RESPONSE_STATUS.SUCCESS;
        if (!res)
            respStatus = Protocol.RESPONSE_STATUS.FAIL;
        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, respStatus, null));

        ViewLogMgr.ShowUserlog();
        ViewLogMgr.ShowCounterlog();

        return true;
    }

    private static boolean Next(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;
        byte qType = 0;
        byte qAlp = 0;
        byte qH = 0;
        byte qL = 0;
        int len = 0;
        QInfo qStart = new QInfo();
        QInfo qEnd = new QInfo();
        byte frameID = bytes[4];
        byte[] bData = null;
        if (frameID == 1) {

            idx = 15;
            ack = bytes[idx++];
            status = bytes[idx++];
            sta = bytes[idx++];

            //----Answer
            byte[] sbytes = new byte[1024 * 2];
            idx = 0;
            QueueMgr.Finish_Queue(context, sta);
            QueueInfo q = QueueMgr.Search_Next_Queue(sta);
            sbytes[idx++] = sta;
            sbytes[idx++] = q.getqType();
            sbytes[idx++] = q.getqAlp();
            sbytes[idx++] = Convert.GetByteHigh(q.getqNum());
            sbytes[idx++] = Convert.GetByteLow(q.getqNum());
            byte[] b = Arrays.copyOf(sbytes, idx);
            tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, Protocol.RESPONSE_STATUS.SUCCESS, b));

            QueueMgr.HandleEventOnQueueStatus(context, sta, cmd, q, null, (byte) 0x00);

        } else if (frameID == 3) {
            idx = 15;
            ack = bytes[idx++];
            status = bytes[idx++];
            sta = bytes[idx++];
            qType = bytes[idx++];
            qAlp = bytes[idx++];
            qH = bytes[idx++];
            qL = bytes[idx++];
            //Rev Confirm Ack Queue
            qStart.setqType(qType);
            qStart.setqAlp(qAlp);
            qStart.setqNum(Convert.GetQNo(qH, qL));

            QueueMgr.UpdateCallingQueue(qStart, qEnd, sta);

            ViewLogMgr.ShowQueue();
        } else {
            Response_NormalAnswer(context, tcpServer, socket, cmd, deviceType, deviceId, Protocol.NACK, Protocol.RESPONSE_STATUS.FAIL);
        }

        return true;
    }

    private static boolean Recall(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;
        byte qType = 0;
        byte qAlp = 0;
        byte qH = 0;
        byte qL = 0;

        byte qType_end = 0;
        byte qAlp_end = 0;
        byte qH_end = 0;
        byte qL_end = 0;

        int len = 0;
        QInfo qStart = new QInfo();
        QInfo qEnd = new QInfo();
        idx = 15;
        ack = bytes[idx++];
        status = bytes[idx++];
        sta = bytes[idx++];
        qType = bytes[idx++];
        qAlp = bytes[idx++];
        qH = bytes[idx++];
        qL = bytes[idx++];

        qType_end = bytes[idx++];
        qAlp_end = bytes[idx++];
        qH_end = bytes[idx++];
        qL_end = bytes[idx++];

        //Rev Confirm Ack Queue
        qStart.setqType(qType);
        qStart.setqAlp(qAlp);
        qStart.setqNum(Convert.GetQNo(qH, qL));

        qEnd.setqType(qType_end);
        qEnd.setqAlp(qAlp_end);
        qEnd.setqNum(Convert.GetQNo(qH_end, qL_end));

        //---Action
        QueueInfo q = QueueMgr.Search_Queue(qStart);
        boolean res = QueueMgr.HandleEventOnQueueStatus(context, sta, cmd, q, null, (byte) 0x00);
        //----Answer
        byte[] sbytes = new byte[1024 * 2];
        idx = 0;
        sbytes[idx++] = sta;
        sbytes[idx++] = qStart.getqType();
        sbytes[idx++] = qStart.getqAlp();
        sbytes[idx++] = Convert.GetByteHigh(qStart.getqNum());
        sbytes[idx++] = Convert.GetByteLow(qStart.getqNum());
        sbytes[idx++] = qEnd.getqType();
        sbytes[idx++] = qEnd.getqAlp();
        sbytes[idx++] = Convert.GetByteHigh(qEnd.getqNum());
        sbytes[idx++] = Convert.GetByteLow(qEnd.getqNum());
        byte[] b = Arrays.copyOf(sbytes, idx);
        byte respStatus = Protocol.RESPONSE_STATUS.SUCCESS;
        if (!res)
            respStatus = Protocol.RESPONSE_STATUS.FAIL;
        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, respStatus, b));

        //if(res)
        QueueMgr.UpdateCallingQueue(qStart, qEnd, deviceId);
        ViewLogMgr.ShowQueue();
        return true;
    }

    private static boolean DirectCall(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;
        byte qType = 0;
        ;
        byte qAlp = 0;
        byte qH = 0;
        byte qL = 0;
        int len = 0;
        QInfo qStart = new QInfo();
        QInfo qEnd = new QInfo();

        idx = 15;
        ack = bytes[idx++];
        status = bytes[idx++];
        sta = bytes[idx++];
        qType = bytes[idx++];
        qAlp = bytes[idx++];
        qH = bytes[idx++];
        qL = bytes[idx++];
        //---Rev Confirm Ack Queue
        qStart.setqType(qType);
        qStart.setqAlp(qAlp);
        qStart.setqNum(Convert.GetQNo(qH, qL));
        //---Action
        QueueInfo q = QueueMgr.Search_Queue(qStart);
        QueueMgr.HandleEventOnQueueStatus(context, sta, cmd, q, null, (byte) 0x00);
        //---Answer
        byte[] sbytes = new byte[1024 * 2];
        idx = 0;
        sbytes[idx++] = sta;
        sbytes[idx++] = qStart.getqType();
        sbytes[idx++] = qStart.getqAlp();
        sbytes[idx++] = Convert.GetByteHigh(qStart.getqNum());
        sbytes[idx++] = Convert.GetByteLow(qStart.getqNum());
        byte[] b = Arrays.copyOf(sbytes, idx);
        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, Protocol.RESPONSE_STATUS.SUCCESS, b));
        QueueMgr.UpdateCallingQueue(qStart, qEnd, deviceId);
        ViewLogMgr.ShowQueue();
        return true;
    }

    private static boolean SpanCall(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;
        byte qType = 0;
        byte qAlp = 0;
        byte qH = 0;
        byte qL = 0;

        byte qType_end = 0;
        byte qAlp_end = 0;
        byte qH_end = 0;
        byte qL_end = 0;

        int len = 0;
        QInfo qStart = new QInfo();
        QInfo qEnd = new QInfo();

        idx = 15;
        ack = bytes[idx++];
        status = bytes[idx++];
        sta = bytes[idx++];
        qType = bytes[idx++];
        qAlp = bytes[idx++];
        qH = bytes[idx++];
        qL = bytes[idx++];

        qType_end = bytes[idx++];
        qAlp_end = bytes[idx++];
        qH_end = bytes[idx++];
        qL_end = bytes[idx++];

        //---Rev Confirm Ack Queue
        qStart.setqType(qType);
        qStart.setqAlp(qAlp);
        qStart.setqNum(Convert.GetQNo(qH, qL));

        qEnd.setqType(qType_end);
        qEnd.setqAlp(qAlp_end);
        qEnd.setqNum(Convert.GetQNo(qH_end, qL_end));

        //---Action
        QueueInfo qSt = QueueMgr.Search_Queue(qStart);
        QueueInfo qEn = QueueMgr.Search_Queue(qEnd);
        boolean res = QueueMgr.HandleEventOnQueueStatus(context, sta, cmd, qSt, qEn, (byte) 0x00);

        //---Answer
        byte[] sbytes = new byte[1024 * 2];
        idx = 0;
        sbytes[idx++] = sta;
        sbytes[idx++] = qStart.getqType();
        sbytes[idx++] = qStart.getqAlp();
        sbytes[idx++] = Convert.GetByteHigh(qStart.getqNum());
        sbytes[idx++] = Convert.GetByteLow(qStart.getqNum());
        sbytes[idx++] = qEnd.getqType();
        sbytes[idx++] = qEnd.getqAlp();
        sbytes[idx++] = Convert.GetByteHigh(qEnd.getqNum());
        sbytes[idx++] = Convert.GetByteLow(qEnd.getqNum());
        byte[] b = Arrays.copyOf(sbytes, idx);
        byte respStatus = Protocol.RESPONSE_STATUS.SUCCESS;
        if (!res)
            respStatus = Protocol.RESPONSE_STATUS.FAIL;

        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, respStatus, b));

        //if(res)
        QueueMgr.UpdateCallingQueue(qStart, qEnd, deviceId);
        ViewLogMgr.ShowQueue();
        return true;
    }

    private static boolean Hold(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;
        byte qType = 0;
        ;
        byte qAlp = 0;
        byte qH = 0;
        byte qL = 0;
        int len = 0;
        QInfo qStart = new QInfo();
        QInfo qEnd = new QInfo();
        idx = 15;
        ack = bytes[idx++];
        status = bytes[idx++];
        sta = bytes[idx++];
        qType = bytes[idx++];
        qAlp = bytes[idx++];
        qH = bytes[idx++];
        qL = bytes[idx++];
        //---Rev Confirm Ack Queue
        qStart.setqType(qType);
        qStart.setqAlp(qAlp);
        qStart.setqNum(Convert.GetQNo(qH, qL));
        //---Action
        QueueInfo q = QueueMgr.Search_Queue_ByStatus(qStart, QueueMgr.qstatus.qcalling_serving);
        boolean res = QueueMgr.HandleEventOnQueueStatus(context, sta, cmd, q, null, (byte) 0x00);
        //----Answer
        byte[] sbytes = new byte[1024 * 2];
        idx = 0;
        sbytes[idx++] = sta;
        sbytes[idx++] = q.getqType();
        sbytes[idx++] = q.getqAlp();
        sbytes[idx++] = Convert.GetByteHigh(q.getqNum());
        sbytes[idx++] = Convert.GetByteLow(q.getqNum());
        byte[] b = Arrays.copyOf(sbytes, idx);
        byte respStatus = Protocol.RESPONSE_STATUS.SUCCESS;
        if (!res)
            respStatus = Protocol.RESPONSE_STATUS.FAIL;
        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, respStatus, b));
        ViewLogMgr.ShowQueue();
        return true;
    }

    private static boolean CallHold(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;
        byte qType = 0;
        ;
        byte qAlp = 0;
        byte qH = 0;
        byte qL = 0;
        int len = 0;
        QInfo qStart = new QInfo();
        QInfo qEnd = new QInfo();
        byte frameID = bytes[4];
        byte[] bData = null;
        if (frameID == 1) {
            byte[] sbytes = new byte[1024 * 2];
            idx = 0;
            //---Check Hold Queue
            QueueMgr.Finish_Queue(context, deviceId);
            QueueInfo q = QueueMgr.Search_Hold_Queue(deviceId); //Find Hold Queue
            sbytes[idx++] = deviceId;
            sbytes[idx++] = q.getqType();
            sbytes[idx++] = q.getqAlp();
            sbytes[idx++] = Convert.GetByteHigh(q.getqNum());
            sbytes[idx++] = Convert.GetByteLow(q.getqNum());
            byte[] b = Arrays.copyOf(sbytes, idx);
            tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, Protocol.RESPONSE_STATUS.SUCCESS, b));
            //---Action
            QueueMgr.HandleEventOnQueueStatus(context, sta, cmd, q, null, (byte) 0x00);
            //QueueMgr.UpdateQueueStatus(context, q, QueueMgr.qstatus.qcalling_serving);
        } else if (frameID == 3) {
            idx = 15;
            ack = bytes[idx++];
            status = bytes[idx++];
            sta = bytes[idx++];
            qType = bytes[idx++];
            qAlp = bytes[idx++];
            qH = bytes[idx++];
            qL = bytes[idx++];
            //Rev Confirm Ack Queue

            qStart.setqType(qType);
            qStart.setqAlp(qAlp);
            qStart.setqNum(Convert.GetQNo(qH, qL));

            QueueMgr.UpdateCallingQueue(qStart, qEnd, sta);
            ViewLogMgr.ShowQueue();
        } else {
            Response_NormalAnswer(context, tcpServer, socket, cmd, deviceType, deviceId, Protocol.NACK, Protocol.RESPONSE_STATUS.FAIL);
        }

        return true;
    }

    private static boolean Cancel(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;
        byte qType = 0;
        ;
        byte qAlp = 0;
        byte qH = 0;
        byte qL = 0;
        int len = 0;
        QInfo qStart = new QInfo();
        QInfo qEnd = new QInfo();
        idx = 15;
        ack = bytes[idx++];
        status = bytes[idx++];
        sta = bytes[idx++];
        qType = bytes[idx++];
        qAlp = bytes[idx++];
        qH = bytes[idx++];
        qL = bytes[idx++];
        //Rev Confirm Ack Queue
        qStart.setqType(qType);
        qStart.setqAlp(qAlp);
        qStart.setqNum(Convert.GetQNo(qH, qL));
        //---Action
        QueueInfo q = QueueMgr.Search_Queue_ByStatus(qStart, QueueMgr.qstatus.qcalling_serving);
        boolean res = QueueMgr.HandleEventOnQueueStatus(context, sta, cmd, q, null, (byte) 0x00);
        //----Answer
        byte[] sbytes = new byte[1024 * 2];
        idx = 0;
        sbytes[idx++] = sta;
        sbytes[idx++] = q.getqType();
        sbytes[idx++] = q.getqAlp();
        sbytes[idx++] = Convert.GetByteHigh(q.getqNum());
        sbytes[idx++] = Convert.GetByteLow(q.getqNum());
        byte[] b = Arrays.copyOf(sbytes, idx);
        byte respStatus = Protocol.RESPONSE_STATUS.SUCCESS;
        if (!res)
            respStatus = Protocol.RESPONSE_STATUS.FAIL;
        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, respStatus, b));
        ViewLogMgr.ShowQueue();
        return true;
    }

    private static boolean EndTrans(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;
        byte qType = 0;
        ;
        byte qAlp = 0;
        byte qH = 0;
        byte qL = 0;
        int len = 0;
        QInfo qStart = new QInfo();
        QInfo qEnd = new QInfo();
        idx = 15;
        ack = bytes[idx++];
        status = bytes[idx++];
        sta = bytes[idx++];
        qType = bytes[idx++];
        qAlp = bytes[idx++];
        qH = bytes[idx++];
        qL = bytes[idx++];
        //Rev Confirm Ack Queue
        qStart.setqType(qType);
        qStart.setqAlp(qAlp);
        qStart.setqNum(Convert.GetQNo(qH, qL));
        //---Action
        QueueInfo q = QueueMgr.Search_Queue_ByStatus(qStart, QueueMgr.qstatus.qcalling_serving);
        boolean res = QueueMgr.HandleEventOnQueueStatus(context, sta, cmd, q, null, (byte) 0x00);
        //----Answer
        byte[] sbytes = new byte[1024 * 2];
        idx = 0;
        sbytes[idx++] = sta;
        sbytes[idx++] = q.getqType();
        sbytes[idx++] = q.getqAlp();
        sbytes[idx++] = Convert.GetByteHigh(q.getqNum());
        sbytes[idx++] = Convert.GetByteLow(q.getqNum());
        byte[] b = Arrays.copyOf(sbytes, idx);
        byte respStatus = Protocol.RESPONSE_STATUS.SUCCESS;
        if (!res)
            respStatus = Protocol.RESPONSE_STATUS.FAIL;
        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, respStatus, b));
        ViewLogMgr.ShowQueue();
        return true;
    }

    private static boolean WalkDirect(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;
        byte qType = 0;
        byte qAlp = 0;
        byte qH = 0;
        byte qL = 0;
        int len = 0;
        byte divId = 0;
        QInfo qStart = new QInfo();
        QInfo qEnd = new QInfo();
        idx = 15;
        ack = bytes[idx++];
        status = bytes[idx++];
        sta = bytes[idx++];
        divId = bytes[idx++];

        //---Generate Queue
        QueueInfo queueInfo = QueueMgr.GetNewQueue(context, divId);
        EventBus.getDefault().post(new DebugMessageEvent("GetQ div=" + divId));
        //Rev Confirm Ack Queue
        qStart.setqType(queueInfo.getqType());
        qStart.setqAlp(queueInfo.getqAlp());
        qStart.setqNum(queueInfo.getqNum());

        //---Action
        QueueInfo q = QueueMgr.Search_Queue(qStart);
        boolean res = QueueMgr.HandleEventOnQueueStatus(context, sta, cmd, q, null, (byte) 0x00);
        //----Answer
        byte[] sbytes = new byte[1024 * 2];
        idx = 0;
        sbytes[idx++] = sta;
        sbytes[idx++] = qStart.getqType();
        sbytes[idx++] = qStart.getqAlp();
        sbytes[idx++] = Convert.GetByteHigh(qStart.getqNum());
        sbytes[idx++] = Convert.GetByteLow(qStart.getqNum());
        byte[] b = Arrays.copyOf(sbytes, idx);
        byte respStatus = Protocol.RESPONSE_STATUS.SUCCESS;
        if (!res)
            respStatus = Protocol.RESPONSE_STATUS.FAIL;
        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, respStatus, b));
        ViewLogMgr.ShowQueue();
        return true;
    }

    private static boolean Transfer(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        //-TYPE_TRANSFER-Target-Transfer_Status
        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;
        byte qType = 0;
        ;
        byte qAlp = 0;
        byte qH = 0;
        byte qL = 0;
        int len = 0;
        byte TYPE_TRANSFER = 0;
        byte Target = 0;
        byte Transfer_Status = 0;
        QInfo qStart = new QInfo();
        QInfo qEnd = new QInfo();
        idx = 15;
        ack = bytes[idx++];
        status = bytes[idx++];
        sta = bytes[idx++];
        qType = bytes[idx++];
        qAlp = bytes[idx++];
        qH = bytes[idx++];
        qL = bytes[idx++];
        TYPE_TRANSFER = bytes[idx++];
        Target = bytes[idx++];
        Transfer_Status = bytes[idx++];
        //Rev Confirm Ack Queue
        qStart.setqType(qType);
        qStart.setqAlp(qAlp);
        qStart.setqNum(Convert.GetQNo(qH, qL));
        //---Action
        QueueInfo q = QueueMgr.Search_Queue_ByStatus(qStart, QueueMgr.qstatus.qcalling_serving);
        boolean res = QueueMgr.HandleEventOnQueueStatus(context, sta, cmd, q, null, (byte) Target);
        //----Answer
        byte[] sbytes = new byte[1024 * 2];
        idx = 0;
        sbytes[idx++] = sta;
        sbytes[idx++] = q.getqType();
        sbytes[idx++] = q.getqAlp();
        sbytes[idx++] = Convert.GetByteHigh(q.getqNum());
        sbytes[idx++] = Convert.GetByteLow(q.getqNum());
        byte[] b = Arrays.copyOf(sbytes, idx);
        byte respStatus = Protocol.RESPONSE_STATUS.SUCCESS;
        if (!res)
            respStatus = Protocol.RESPONSE_STATUS.FAIL;
        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, respStatus, b));
        ViewLogMgr.ShowQueue();
        return true;
    }

    private static boolean Transaction(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;
        byte qType = 0;
        byte qAlp = 0;
        byte qH = 0;
        byte qL = 0;
        int len = 0;
        QInfo qStart = new QInfo();
        QInfo qEnd = new QInfo();
        idx = 15;
        ack = bytes[idx++];
        status = bytes[idx++];
        sta = bytes[idx++];
        qType = bytes[idx++];
        qAlp = bytes[idx++];
        qH = bytes[idx++];
        qL = bytes[idx++];
        //Rev Confirm Ack Queue
        qStart.setqType(qType);
        qStart.setqAlp(qAlp);
        qStart.setqNum(Convert.GetQNo(qH, qL));
        //---Action
        QueueInfo q = QueueMgr.Search_Queue(qStart);
        boolean res = QueueMgr.HandleEventOnQueueStatus(context, sta, cmd, q, null, (byte) 0x00);
        //----Answer
        byte[] sbytes = new byte[1024 * 2];
        idx = 0;
        sbytes[idx++] = sta;
        sbytes[idx++] = q.getqType();
        sbytes[idx++] = q.getqAlp();
        sbytes[idx++] = Convert.GetByteHigh(q.getqNum());
        sbytes[idx++] = Convert.GetByteLow(q.getqNum());
        byte[] b = Arrays.copyOf(sbytes, idx);
        byte respStatus = Protocol.RESPONSE_STATUS.SUCCESS;
        if (!res)
            respStatus = Protocol.RESPONSE_STATUS.FAIL;

        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, respStatus, b));
        ViewLogMgr.ShowQueue();
        return true;
    }

    private static boolean SubDiv(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;
        byte subDivID = 0;
        byte qType = 0;
        byte qAlp = 0;
        byte qH = 0;
        byte qL = 0;
        int len = 0;
        QInfo qStart = new QInfo();
        QInfo qEnd = new QInfo();
        idx = 15;
        ack = bytes[idx++];
        status = bytes[idx++];
        sta = bytes[idx++];
        qType = bytes[idx++];
        qAlp = bytes[idx++];
        qH = bytes[idx++];
        qL = bytes[idx++];
        subDivID = bytes[idx++];
        //Rev Confirm Ack Queue
        qStart.setqType(qType);
        qStart.setqAlp(qAlp);
        qStart.setqNum(Convert.GetQNo(qH, qL));
        //---Action
        QueueInfo q = QueueMgr.Search_Queue(qStart);
        boolean res = QueueMgr.HandleEventOnQueueStatus(context, sta, cmd, q, null, (byte) subDivID);
        //----Answer
        byte[] sbytes = new byte[1024 * 2];
        idx = 0;
        sbytes[idx++] = sta;
        sbytes[idx++] = q.getqType();
        sbytes[idx++] = q.getqAlp();
        sbytes[idx++] = Convert.GetByteHigh(q.getqNum());
        sbytes[idx++] = Convert.GetByteLow(q.getqNum());
        byte[] b = Arrays.copyOf(sbytes, idx);
        byte respStatus = Protocol.RESPONSE_STATUS.SUCCESS;
        if (!res)
            respStatus = Protocol.RESPONSE_STATUS.FAIL;
        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, respStatus, b));
        ViewLogMgr.ShowQueue();
        return true;
    }

    private static boolean ChangeGroup(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;
        byte newGrpID = 0;

        idx = 15;
        ack = bytes[idx++];
        status = bytes[idx++];
        sta = bytes[idx++];
        newGrpID = bytes[idx++];
        //---Action
        boolean res = QueueMgr.HandleEventOnQueueStatus(context, sta, cmd, null, null, (byte) newGrpID);

        //----Answer
        byte respStatus = Protocol.RESPONSE_STATUS.SUCCESS;
        if (!res)
            respStatus = Protocol.RESPONSE_STATUS.FAIL;

        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, respStatus, null));

        return true;
    }

    private static boolean Help(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;
        byte qType = 0;
        byte qAlp = 0;
        byte qH = 0;
        byte qL = 0;
        QInfo qStart = new QInfo();
        idx = 15;
        ack = bytes[idx++];
        status = bytes[idx++];
        sta = bytes[idx++];
        qType = bytes[idx++];
        qAlp = bytes[idx++];
        qH = bytes[idx++];
        qL = bytes[idx++];
        //Rev Confirm Ack Queue
        qStart.setqType(qType);
        qStart.setqAlp(qAlp);
        qStart.setqNum(Convert.GetQNo(qH, qL));
        //---Action
        QueueInfo q = QueueMgr.Search_Queue(qStart);
        QueueMgr.HandleEventOnQueueStatus(context, sta, cmd, q, null, (byte) 0x00);
        //----Answer
        byte[] sbytes = new byte[1024 * 2];
        idx = 0;
        sbytes[idx++] = sta;
        sbytes[idx++] = q.getqType();
        sbytes[idx++] = q.getqAlp();
        sbytes[idx++] = Convert.GetByteHigh(q.getqNum());
        sbytes[idx++] = Convert.GetByteLow(q.getqNum());
        byte[] b = Arrays.copyOf(sbytes, idx);
        byte respStatus = Protocol.RESPONSE_STATUS.SUCCESS;
        if (q.getqNum() == 0x0000)
            respStatus = Protocol.RESPONSE_STATUS.FAIL;
        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, respStatus, b));

        return true;
    }

    private static boolean Break(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;

        idx = 15;
        ack = bytes[idx++];
        status = bytes[idx++];
        sta = bytes[idx++];
        boolean res = QueueMgr.HandleEventOnQueueStatus(context, sta, cmd, null, null, (byte) 0x00);
        //----Answer
        byte respStatus = Protocol.RESPONSE_STATUS.SUCCESS;
        if (!res)
            respStatus = Protocol.RESPONSE_STATUS.FAIL;
        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, respStatus, null));
        ViewLogMgr.ShowUserlog();
        ViewLogMgr.ShowCounterlog();
        return true;
    }

    private static boolean REQUEST_CURRENT_STATUS(Context context, TCPServer tcpServer, Socket socket, byte cmd, byte deviceType, byte deviceId, byte[] bytes) {

        int idx = 0;
        byte ack = 0;
        byte status = 0;
        byte sta = 0;
        byte qType = 0;
        byte qAlp = 0;
        byte qH = 0;
        byte qL = 0;
        QInfo qStart = new QInfo();
        idx = 15;
        ack = bytes[idx++];
        status = bytes[idx++];
        sta = bytes[idx++];

        //Rev Confirm Ack Queue
        qStart.setqType(qType);
        qStart.setqAlp(qAlp);
        qStart.setqNum(Convert.GetQNo(qH, qL));

        //---Action

        //----Answer
        CounterlogInfo clog = CounterMgr.Find_CurrentUserByCounter(context, sta);
        CurrentStationInfo curS = CounterMgr.Find_CurrentUserByStation(context, sta);
        byte grpID = curS.getGroupId();
        byte counterStatus = curS.getStatus();
        int userID = clog.getUserId();
        int wQ = curS.getWaitQ();
        int hQ = curS.getHoldQ();
        int swQ = curS.getStaWaitQ();

        QueueInfo q = QueueMgr.Search_Queue_ByCounter(sta);

        byte[] sbytes = new byte[1024 * 2];
        idx = 0;
        sbytes[idx++] = sta;
        sbytes[idx++] = counterStatus;
        sbytes[idx++] = (byte) (userID >> 24);
        sbytes[idx++] = (byte) (userID >> 16);
        sbytes[idx++] = (byte) (userID >> 8);
        sbytes[idx++] = (byte) (userID & 0xFF);
        sbytes[idx++] = grpID;
        sbytes[idx++] = Convert.GetByteHigh(wQ);
        sbytes[idx++] = Convert.GetByteLow(wQ);
        sbytes[idx++] = Convert.GetByteHigh(hQ);
        sbytes[idx++] = Convert.GetByteLow(hQ);
        sbytes[idx++] = Convert.GetByteHigh(swQ);
        sbytes[idx++] = Convert.GetByteLow(swQ);
        sbytes[idx++] = curS.getQueueInfo().getqType();
        sbytes[idx++] = curS.getQueueInfo().getqAlp();
        sbytes[idx++] = Convert.GetByteHigh(curS.getQueueInfo().getqNum());
        sbytes[idx++] = Convert.GetByteLow(curS.getQueueInfo().getqNum());
        sbytes[idx++] = curS.getQueueInfo().getStatus();
        sbytes[idx++] = curS.getQueueInfo().getDivisionId();
        sbytes[idx++] = 0x00;
        sbytes[idx++] = 0x00;
        byte[] b = Arrays.copyOf(sbytes, idx);

        tcpServer.Send(socket, prepareData(cmd, deviceType, deviceId, Protocol.ACK, Protocol.RESPONSE_STATUS.SUCCESS, b));

        return true;
    }


}
