package th.co.infinitecorp.www.qcontroller.Management;

import android.content.Context;

import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CounterlogInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentStationInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.UserlogInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.StaInfo;
import th.co.infinitecorp.www.qcontroller.Utils.DateTime;
import th.co.infinitecorp.www.qcontroller.Utils.GData;

public class CounterMgr {
    /*Status of User*/
    public static class cStatus {

        final public static byte NONE = 0;
        final public static byte Login = 1;
        final public static byte Logout = 2;
        final public static byte Breaking = 3;
    }
    public static StaInfo NEW_User() {
        StaInfo d=new StaInfo();
        return  d;
    }
    public static CounterlogInfo NEW_Counterlog(){
        CounterlogInfo d=new CounterlogInfo();
        d.setGroupId((byte) 0x00);
        d.setUserId(0);
        d.setStatus(cStatus.NONE);
        return  d;
    }

    public static CurrentStationInfo NEW_CurrentStationInfo(){
        CurrentStationInfo d=new CurrentStationInfo();
        d.setId(0);
        d.setUserId(0);
        d.setWaitQ((short) 0);
        d.setHoldQ((short)0);
        d.setGroupId((byte) 0x01);
        d.setStatus(cStatus.NONE);
        d.setQueueInfo(QueueMgr.NEW_QUEUE());
        return  d;
    }

    /*Add Counterlog*/
    private static boolean AddNewCounterlog(Context context, CounterlogInfo c) {
        LogMgr.add_counterlog(context,c);
        return true;
    }

    /*Create New Counterlog*/
    public static CounterlogInfo CreateNewCounterlog(Context context, Integer userId, byte stationId) {

        byte groupId=0x01;
        byte status= CounterMgr.cStatus.Login;

        return  CreateNewCounterlog(context,userId,stationId,groupId,status);

    }
    public static CounterlogInfo CreateNewCounterlog(Context context,Integer userId,byte stationId,byte groupId,byte status) {
        CounterlogInfo c=NEW_Counterlog();

        c.setStartDateTime(DateTime.GetDateTimeNow());
        c.setUserId(userId);
        c.setStationId(stationId);
        c.setGroupId(groupId);
        c.setStatus(status);

        AddNewCounterlog(context,c);
        return  c;
    }

    /*Update Userlog*/
    public static boolean UpdateCounterlogStatus(Context context,byte sta, CounterlogInfo c,byte newQstatus) {

        return LogMgr.Update_Counterlog_Status(context,sta,c,newQstatus);
    }
    /*Update UpdateCurrentStationStatus*/
    public static boolean UpdateCurrentStationStatus(Context context,byte sta, CurrentStationInfo c) {

        return LogMgr.Update_CurrentStation_Status(context,sta,c);
    }
    /*Find CurrentUser*/
    public static CounterlogInfo Find_CurrentUserByCounter(Context context,byte sta) {
        CounterlogInfo updateC=CounterMgr.NEW_Counterlog();
        boolean found=false;
        Integer index=0;
        for (CounterlogInfo c:GData.Counterlog) {
            if((c.getStationId()==sta)&&(c.getStatus()!=UserMgr.uStatus.NONE)){
                updateC=c;
                found=true;
            }
            index++;
        }
        return updateC;
    }
    public static CounterlogInfo Find_CurrentUserByCounterActive(Context context,byte sta) {
        CounterlogInfo updateC=CounterMgr.NEW_Counterlog();
        boolean found=false;
        Integer index=0;
        for (CounterlogInfo c:GData.Counterlog) {
            if((c.getStationId()==sta)&&((c.getStatus()==UserMgr.uStatus.Login)||(c.getStatus()==UserMgr.uStatus.Breaking))){
                updateC=c;
                found=true;
                return updateC;
            }
            index++;
        }
        return updateC;
    }
    public static CurrentStationInfo Find_CurrentUserByStation(Context context,byte sta) {
        CurrentStationInfo updateC=CounterMgr.NEW_CurrentStationInfo();
        boolean found=false;
        Integer index=0;
        for (CurrentStationInfo c:GData.CurStation) {
            if(c.getId()==sta){
                updateC=c;
                found=true;
            }
            index++;
        }
        return updateC;
    }

}
