package th.co.infinitecorp.www.qcontroller.Management;

import android.content.Context;

import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CounterlogInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentDivisionInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentGroupInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentStationInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.QLaunchingInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.QueueInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.DivisionInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.UserlogInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;
import th.co.infinitecorp.www.qcontroller.Utils.Convert;
import th.co.infinitecorp.www.qcontroller.Utils.DateTime;
import th.co.infinitecorp.www.qcontroller.Utils.GData;
import th.co.infinitecorp.www.qcontroller.Utils.Protocol;
import th.co.infinitecorp.www.qcontroller.Utils.constant;

public class QueueMgr {

    /*Status of Queue*/
    public static class qstatus {
        final public static byte cancelbyError = -8;
        final public static byte cancelbySWcleardata = -7;
        final public static byte cancelbyMobileApp = -6;
        final public static byte cancelbyHWcleardata = -5;
        final public static byte cancelbyOverQno = -4;
        final public static byte cancelbychangeDate = -3;
        final public static byte cancelbyturnoff = -2;
        final public static byte cancelQ = -1;

        final public static byte qwaiting = 1;
        final public static byte qcalling_serving = 2;
        final public static byte qwaiting_by_station = 18;

        final public static byte qhold = 10;

        final public static byte finish_by_transec = 4;
        final public static byte finish_by_next = 5;
        final public static byte finish_by_logoff = 6;
        final public static byte finish_by_transfer = 12;

        final public static byte qinit =  0;
        final public static byte unused_qserving_again = 3;
        final public static byte unused_qcall_hold = 11;
        final public static byte unuses_by_setsubdiv = 17;

        final public static byte unused_finish_by_otherdiv = 7;
        final public static byte unused_finish_by_8 = 8;
        final public static byte unused_finish_by_sta = 9;

        final public static byte unused_qwaiting_from_other_div = 13;
        final public static byte unused_qwaiting_by_new_trans = 14;
        final public static byte unused_qserving_by_direct = 15;
        final public static byte unused_serving_by_16 = 16;
        final public static byte finish_by_CallDiv = 19;

    }
    public static class TransferToStatus {
        final public static byte UnStatus=-1;
        final public static byte RealQueue = 0;
        final public static byte NextQueue = 1;
        final public static byte NewQueue = 2;
    }
    public static class TransferToTarget {
        final public static byte UnTarget = 0;
        final public static byte TRANSFER_To_Employee = 0x22;
        final public static byte TRANSFER_To_Station = 0x23;
        final public static byte TRANSFER_To_DIV = 0x24;
    }

    /*Create New Queue*/
    public static QueueInfo NEW_QUEUE() {
        QueueInfo q=new QueueInfo();
        byte div=0;
        short waitQ = 0;
        byte staOpen=0;
        byte stationId=0;
        byte groupId=0;
        byte subdivId=0;
        byte qAlp=0;
        byte qType=0;
        byte qNum=0;
        byte qStatus=(byte) qstatus.qinit;


        q.setServid(0);
        q.setTranno(0);
        q.setDivisionId(div);
        q.setWaitq(waitQ);
        q.setStaOpen(staOpen);
        q.setStationId(stationId);
        q.setGroupId(groupId);
        q.setUserId(0);
        q.setSubdivId(subdivId);
        q.setqAlp(qAlp);
        q.setqType(qType);
        q.setqNum(qNum);
        q.setStatus(qStatus);
        q.setQueueNo("");
        return  q;
    }
    public static QueueInfo GetNewQueue(Context context, byte div) {
        QueueInfo q = NEW_QUEUE();

        byte div_alp=0, div_qType=1;
        short div_qBegin=1, div_qEnd=999, qNum = 0;

        if(GData.Division!=null) {
            for (DivisionInfo division : GData.Division) {
                if (division.getId().byteValue()==div) {
                    div_alp = division.getAlphabet();
                    div_qType = division.getqType();
                    div_qBegin = division.getqBegin();
                    div_qEnd = division.getqEnd();

                    qNum = (short) (division.getqLaunching() + 1);
                    break;
                }
            }
        }
        if ((qNum > div_qEnd) || (qNum < div_qBegin))
            qNum = div_qBegin;

        //increment queue all same range queue
        if(GData.Division!=null) {
            for (DivisionInfo division : GData.Division) {
                if ((division.getAlphabet() == div_alp) && (division.getqBegin() <= qNum) && (qNum <= division.getqEnd())) {
                    DivisionInfo divisionInfo = new DivisionInfo();
                    divisionInfo = division;
                    divisionInfo.setqLaunching(qNum);
                    GData.Division.set(divisionInfo.getId() - 1, divisionInfo);
                    QLaunchingInfo qLaunchingInfo=new QLaunchingInfo ();
                    qLaunchingInfo.setqLaunching(qNum);
                    GData.QLaunching.set(divisionInfo.getId() - 1,qLaunchingInfo);
                }
            }
        }

        if (qNum > 0) {
            q = CreateNewWaitQueue(context,div_qType,div_alp,qNum, div,(byte) qstatus.qwaiting);
        }

        return q;
    }
    public static QueueInfo CreateNewWaitQueue(Context context,byte qType,byte qAlp,Short qNum,byte div,byte qStatus) {
        QueueInfo q=NEW_QUEUE();

        short waitQ = 0;
        byte staOpen=0;
        byte stationId=0;
        byte groupId=0;
        byte subdivId=0;
        if (qStatus == qstatus.qwaiting) {
            //waitQ = CalMinWaitQ(div,ref grpId);
        }
        if (waitQ > 0)
        {

        }
        q.setServid(GData.SERVE_ID++);
        q.setTranno(0);
        q.setDivisionId(div);
        q.setWaitq(waitQ);
        q.setStaOpen(staOpen);
        q.setStationId(stationId);
        q.setGroupId(groupId);
        q.setUserId(0);
        q.setSubdivId(subdivId);
        q.setqAlp(qAlp);
        q.setqType(qType);
        q.setqNum(qNum);
        q.setStatus(qStatus);
        q.setQueueNo(Convert.QNumberToString(qType, qAlp,qNum));
        q.setReqDateTime(DateTime.GetDateTimeNow());

        AddNewWaitQueue(context,q);
        return  q;
    }

    /*Add Queue Waiting*/
    private static boolean AddNewWaitQueue(Context context,QueueInfo q) {
        cal_Qstat();
        LogMgr.add_qwaitng(context,q, q.getStatus());
        return true;
    }
    public static boolean Check_SameQueue(QueueInfo q,QueueInfo qCheck) {
        if((q.getqType()==qCheck.getqType())&&(q.getqAlp()==qCheck.getqAlp())&&(q.getqNum()==qCheck.getqNum())){
          return true;
        }
        return false;
    }

    /*Update RealTime & Data Change*/
    public static void cal_Qstat() {


        int[] waitQ=new int[constant.nDiv];
        int[] holdQ=new int[constant.nDiv];

        for (Integer i = 0; i < constant.nDiv; i++) {
            waitQ[i]=0x00;
            holdQ[i]=0x00;
        }
        int[] staWaitQ=new int[constant.nStation];
        for (Integer i = 0; i < constant.nStation; i++) {
            staWaitQ[i]=0x00;
        }

        for (QueueInfo nextQ:GData.Queue) {
            if(nextQ.getStatus()==qstatus.qwaiting)
            {
                int divId=nextQ.getDivisionId();
                if(divId>0)
                    waitQ[divId-1]+=1;
            }
            if(nextQ.getStatus()==qstatus.qhold)
            {
                int divId=nextQ.getDivisionId();
                if(divId>0)
                    holdQ[divId-1]+=1;
            }
            if(nextQ.getStatus()==qstatus.qwaiting_by_station)
            {
                int staId=nextQ.getStationId();
                if(staId>0)
                    staWaitQ[staId-1]+=1;
            }
        }
        for (Integer i = 1; i <= constant.nDiv; i++) {
            CurrentDivisionInfo curDiv = new CurrentDivisionInfo();
            curDiv=GData.CurDiv.get(i-1);
            curDiv.setWaitQ((short) waitQ[i-1]);
            curDiv.setHoldQ((short) holdQ[i-1]);
            GData.CurDiv.set(i-1, curDiv);
        }
        int[] waitQGrp=new int[constant.nGroup];
        int[] holdQGrp=new int[constant.nGroup];
        for (Integer i = 0; i < constant.nGroup; i++) {
            waitQGrp[i]=0x00;
            holdQGrp[i]=0x00;
        }

        waitQGrp[0]=GData.CurDiv.get(0).getWaitQ();
        holdQGrp[0]=GData.CurDiv.get(0).getHoldQ();
        waitQGrp[1]=GData.CurDiv.get(0).getWaitQ();
        holdQGrp[1]=GData.CurDiv.get(0).getHoldQ();
        waitQGrp[2]=GData.CurDiv.get(0).getWaitQ();
        holdQGrp[2]=GData.CurDiv.get(0).getHoldQ();

        for (Integer i = 1; i <= constant.nGroup; i++) {
            CurrentGroupInfo curGrp = new CurrentGroupInfo();
            byte grpId=i.byteValue();

            curGrp=GData.CurGrp.get(grpId-1);
            curGrp.setWaitQ((short) waitQGrp[grpId-1]);
            curGrp.setHoldQ((short) holdQGrp[grpId-1]);
            GData.CurGrp.set(grpId-1, curGrp);

        }
        for (Integer i = 1; i <= constant.nStation; i++) {
            CurrentStationInfo curSta = new CurrentStationInfo();
            curSta=GData.CurStation.get(i-1);
            byte staId=i.byteValue();
            byte grpId=curSta.getGroupId();
            if(grpId>0) {
                curSta.setWaitQ(GData.CurGrp.get(grpId - 1).getWaitQ());
                curSta.setHoldQ(GData.CurGrp.get(grpId - 1).getHoldQ());
            }
            curSta.setStaWaitQ((short)staWaitQ[i-1]);

            GData.CurStation.set(i-1, curSta);
        }
    }
    public static void UpdateQueueStatChange() {
        cal_Qstat();

    }

    /*Search Queue*/
    public static QueueInfo  Search_Next_Queue(byte sta) {
        QueueInfo q=NEW_QUEUE();
        //Fix Test
        /*
        q.setqType(QInfo.QType.Type_3DG);
        q.setqAlp((byte)0x41);
        q.setqNum((short) 3);
        */
        for (QueueInfo nextQ:GData.Queue) {
            if(nextQ.getStatus()==qstatus.qwaiting)
            {
                q=nextQ;
                break;
            }
        }

      return q;
    }
    public static QueueInfo  Search_Hold_Queue(byte sta) {
        QueueInfo q=NEW_QUEUE();
        for (QueueInfo nextQ:GData.Queue) {
            if(nextQ.getStatus()==qstatus.qhold)
            {
                q=nextQ;
                break;
            }
        }

        return q;
    }
    public static QueueInfo  Search_Queue(QInfo qInfo) {
        QueueInfo q=NEW_QUEUE();

        for (QueueInfo fQ:GData.Queue) {
            if((fQ.getqType()==qInfo.getqType())&&(fQ.getqAlp()==qInfo.getqAlp())&&(fQ.getqNum()==qInfo.getqNum()))
            {
                q=fQ;
                break;
            }
        }
        return q;
    }
    public static QueueInfo  Search_Queue(QueueInfo qInfo) {
        QueueInfo q=NEW_QUEUE();

        for (QueueInfo fQ:GData.Queue) {
            if((fQ.getqType()==qInfo.getqType())&&(fQ.getqAlp()==qInfo.getqAlp())&&(fQ.getqNum()==qInfo.getqNum()))
            {
                q=fQ;
                break;
            }
        }
        return q;
    }
    public static QueueInfo  Search_Queue_ByStatus(QInfo qInfo,byte qStatus) {
        QueueInfo q=NEW_QUEUE();

        for (QueueInfo fQ:GData.Queue) {
            if((fQ.getqType()==qInfo.getqType())&&(fQ.getqAlp()==qInfo.getqAlp())&&(fQ.getqNum()==qInfo.getqNum())&&(fQ.getStatus()==qStatus))
            {
                q=fQ;
                break;
            }
        }
        return q;
    }
    public static QueueInfo  Search_Queue_ByCounter(byte sta) {
        QueueInfo q=NEW_QUEUE();

        for (QueueInfo fQ:GData.Queue) {
            if(fQ.getStationId()==sta)
            {
                q=fQ;
            }
        }
        return q;
    }

    public static boolean UpdateQueueStatus(Context context, QueueInfo q,byte newQstatus) {
        return LogMgr.Update_Queue_Status(context,q,q.getStatus(),newQstatus);
    }
    /*Finish Queue*/
    public static QueueInfo  Finish_Queue(Context context,byte sta) {
        QueueInfo q=NEW_QUEUE();
        for (QueueInfo lastQ:GData.Queue) {
            if((lastQ.getStatus()==qstatus.qcalling_serving)&&(sta==lastQ.getStationId()))
            {
                q=lastQ;
                break;
            }
        }
        q.setStationId(sta);
        q.setEndDateTime(DateTime.GetDateTimeNow());
        QueueMgr.UpdateQueueStatus(context,q, qstatus.finish_by_next);
        return q;
    }
    public static void UpdateCallingQueue(QInfo qStart,QInfo qEnd,byte sta) {
        if(qStart.getqNum()!=0) {
            SoundMgr.UpdateCallingQueue(qStart, qEnd, sta);
            DisplayMgr.UpdateQueueOnDisplay(qStart, qEnd, sta);
        }
    }

    /*handle Queue Status*/
    public static boolean HandleEventOnQueueStatus(Context context,byte sta,byte cmd, QueueInfo qStart, QueueInfo qEnd,byte targetId) {
        Integer tranno=0;
        UserlogInfo ulog =new UserlogInfo();
        UserlogInfo u=new UserlogInfo();
        CounterlogInfo clog=new CounterlogInfo();
        CurrentStationInfo curS=new CurrentStationInfo();
        switch (cmd) {

            case Protocol.KEYPAD_CMD.SYNC:

                return true;
            case Protocol.KEYPAD_CMD.STARTUP:

                return true;
            case Protocol.KEYPAD_CMD.LOGIN:
                u=UserMgr.Find_CurrentUserByCounterActive(context,sta);
                if(u.getStatus()==UserMgr.uStatus.Login){
                    return true;
                }
                return false;
            case Protocol.KEYPAD_CMD.LOGOUT:
                ulog=UserMgr.Find_CurrentUserByCounterActive(context,sta);
                UserMgr.UpdateUserlogStatus(context,sta,ulog,UserMgr.uStatus.Logout);

                clog=CounterMgr.Find_CurrentUserByCounterActive(context,sta);
                CounterMgr.UpdateCounterlogStatus(context,sta,clog,CounterMgr.cStatus.Logout);

                curS=CounterMgr.Find_CurrentUserByStation(context,sta);
                curS.setStatus(CounterMgr.cStatus.Logout);
                CounterMgr.UpdateCurrentStationStatus(context,sta,curS);

                u=UserMgr.Find_CurrentUserByCounter(context,sta);
                if(u.getStatus()==UserMgr.uStatus.Logout){
                    return true;
                }
                return false;
            case Protocol.KEYPAD_CMD.BREAK:

                ulog=UserMgr.Find_CurrentUserByCounterActive(context,sta);
                UserMgr.UpdateUserlogStatus(context,sta,ulog,UserMgr.uStatus.Breaking);
                clog=CounterMgr.Find_CurrentUserByCounterActive(context,sta);
                curS=CounterMgr.Find_CurrentUserByStation(context,sta);
                curS.setStatus(CounterMgr.cStatus.Breaking);
                CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
                CounterMgr.UpdateCounterlogStatus(context,sta,clog,CounterMgr.cStatus.Breaking);
                u=UserMgr.Find_CurrentUserByCounterActive(context,sta);
                if(u.getStatus()==UserMgr.uStatus.Breaking){
                    return true;
                }
                return false;
            case Protocol.KEYPAD_CMD.NEXT:
                qStart.setUserId(UserMgr.Find_CurrentUserByCounterActive(context,sta).getUserId());
                qStart.setStationId(sta);
                qStart.setStDateTime(DateTime.GetDateTimeNow());
                QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.qcalling_serving);

                curS=CounterMgr.Find_CurrentUserByStation(context,sta);
                curS.setQueueInfo(qStart);
                CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
                QueueMgr.UpdateQueueStatChange();
                return true;
            case Protocol.KEYPAD_CMD.RECALL:
                if(qStart.getqNum()==0x0000){
                    return false;
                }
                return true;
            case Protocol.KEYPAD_CMD.DIRECT_CALL:
                qStart.setUserId(UserMgr.Find_CurrentUserByCounterActive(context,sta).getUserId());
                qStart.setStationId(sta);
                qStart.setStDateTime(DateTime.GetDateTimeNow());
                QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.qcalling_serving);

                curS=CounterMgr.Find_CurrentUserByStation(context,sta);
                curS.setQueueInfo(qStart);
                CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
                QueueMgr.UpdateQueueStatChange();
                return true;
            case Protocol.KEYPAD_CMD.SPAN_CALL:
                QueueInfo qSt=qStart;
                QueueInfo qEn=qEnd;
                Integer userId=UserMgr.Find_CurrentUserByCounterActive(context,sta).getUserId();
                if((qStart.getqNum()==0x0000)||(qEnd.getqNum()==0x0000)){
                    return false;
                }
                for(short qNum=qStart.getqNum();qNum<=qEnd.getqNum();qNum++) {
                    QueueInfo qInfo=new QueueInfo();
                    qInfo=qStart;
                    qInfo.setqNum(qNum);
                    QueueInfo qInfo2=Search_Queue(qInfo);
                    if(qInfo2.getServid()>0) {
                        qStart.setUserId(userId);
                        qInfo.setStationId(sta);
                        qInfo.setStDateTime(DateTime.GetDateTimeNow());
                        QueueMgr.UpdateQueueStatus(context, qInfo, QueueMgr.qstatus.qcalling_serving);
                    }
                }
                QueueMgr.UpdateQueueStatChange();
                return true;
            case Protocol.KEYPAD_CMD.HOLD:
                if(qStart.getqNum()==0x0000){
                    return false;
                }
                qStart.setStationId(sta);
                qStart.setEndDateTime(DateTime.GetDateTimeNow());
                QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.qhold);

                curS=CounterMgr.Find_CurrentUserByStation(context,sta);
                curS.setQueueInfo(qStart);
                CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
                QueueMgr.UpdateQueueStatChange();
                return true;
            case Protocol.KEYPAD_CMD.CALLHOLD:
                qStart.setUserId(UserMgr.Find_CurrentUserByCounterActive(context,sta).getUserId());
                qStart.setStationId(sta);
                qStart.setStDateTime(DateTime.GetDateTimeNow());
                QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.qcalling_serving);

                curS=CounterMgr.Find_CurrentUserByStation(context,sta);
                curS.setQueueInfo(qStart);
                CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
                QueueMgr.UpdateQueueStatChange();
                return true;
            case Protocol.KEYPAD_CMD.CANCEL:
                if(qStart.getqNum()==0x0000){
                    return false;
                }
                qStart.setStationId(sta);
                qStart.setEndDateTime(DateTime.GetDateTimeNow());
                QueueMgr.UpdateQueueStatus(context, qStart, qstatus.cancelQ);

                curS=CounterMgr.Find_CurrentUserByStation(context,sta);
                curS.setQueueInfo(qStart);
                CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
                QueueMgr.UpdateQueueStatChange();
                return true;
            case Protocol.KEYPAD_CMD.ENDTRANS:
                if(qStart.getqNum()==0x0000){
                    return false;
                }
                qStart.setStationId(sta);
                qStart.setEndDateTime(DateTime.GetDateTimeNow());
                QueueMgr.UpdateQueueStatus(context, qStart, qstatus.finish_by_transec);

                curS=CounterMgr.Find_CurrentUserByStation(context,sta);
                curS.setQueueInfo(qStart);
                CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
                QueueMgr.UpdateQueueStatChange();
                return true;
            case Protocol.KEYPAD_CMD.WALKDIRECT:
                if(qStart.getqNum()==0x0000){
                    return false;
                }
                qStart.setUserId(UserMgr.Find_CurrentUserByCounterActive(context,sta).getUserId());
                qStart.setStationId(sta);
                qStart.setStDateTime(DateTime.GetDateTimeNow());
                QueueMgr.UpdateQueueStatus(context, qStart, QueueMgr.qstatus.qcalling_serving);

                curS=CounterMgr.Find_CurrentUserByStation(context,sta);
                curS.setQueueInfo(qStart);
                CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
                QueueMgr.UpdateQueueStatChange();
                return true;
            case Protocol.KEYPAD_CMD.TRANSFER:
                if(qStart.getqNum()==0x0000){
                    return false;
                }

                curS=CounterMgr.Find_CurrentUserByStation(context,sta);
                curS.setQueueInfo(qStart);
                CounterMgr.UpdateCurrentStationStatus(context,sta,curS);

                QueueMgr.Finish_Queue(context, sta);
                qStart.setStationId((byte) 0x00);
                qStart.setDivisionId(targetId);
                AddNewWaitQueue(context,qStart);
                QueueMgr.UpdateQueueStatChange();

                return true;
            case Protocol.KEYPAD_CMD.TRANSACTION:
                if(qStart.getqNum()==0x0000){
                    return false;
                }
                qStart.setEndDateTime(DateTime.GetDateTimeNow());
                QueueMgr.UpdateQueueStatus(context, qStart, qstatus.finish_by_transec);
                AddNewWaitQueue(context,qStart);
                tranno=qStart.getTranno();
                qStart.setTranno(++tranno);
                qStart.setReqDateTime(DateTime.GetDateTimeNow());
                qStart.setStDateTime(DateTime.GetDateTimeNow());
                QueueMgr.UpdateQueueStatus(context, qStart, qstatus.qcalling_serving);
                return true;
            case Protocol.KEYPAD_CMD.SUBDIV:
                if(qStart.getqNum()==0x0000){
                    return false;
                }
                qStart.setSubdivId(targetId);
                qStart.setEndDateTime(DateTime.GetDateTimeNow());
                QueueMgr.UpdateQueueStatus(context, qStart, qstatus.finish_by_transec);
                AddNewWaitQueue(context,qStart);
                tranno=qStart.getTranno();
                qStart.setTranno(++tranno);
                qStart.setReqDateTime(DateTime.GetDateTimeNow());
                qStart.setStDateTime(DateTime.GetDateTimeNow());
                QueueMgr.UpdateQueueStatus(context, qStart, qstatus.qcalling_serving);
                return true;
            case Protocol.KEYPAD_CMD.CHANGEGROUP:
                curS=CounterMgr.Find_CurrentUserByStation(context,sta);
                curS.setGroupId(targetId);
                CounterMgr.UpdateCurrentStationStatus(context,sta,curS);
                curS=CounterMgr.Find_CurrentUserByStation(context,sta);
                if(curS.getGroupId()==targetId)
                    return true;
                else
                    return false;
            case Protocol.KEYPAD_CMD.HELP_ME:

                return true;

            default:
                return false;

        }
    }
}
