package th.co.infinitecorp.www.qcontroller.Management;
import android.content.Context;

import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.QueueInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.UserlogInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.UserInfo;
import th.co.infinitecorp.www.qcontroller.Utils.Convert;
import th.co.infinitecorp.www.qcontroller.Utils.DateTime;
import th.co.infinitecorp.www.qcontroller.Utils.GData;

public class UserMgr {

    /*Status of User*/
    public static class uStatus {

        final public static byte NONE = 0;
        final public static byte Login = 1;
        final public static byte Logout = 2;
        final public static byte Breaking = 3;

    }
    public static UserInfo NEW_User() {
        UserInfo d=new UserInfo();
        return  d;
    }
    public static UserlogInfo NEW_Userlog() {
        UserlogInfo d=new UserlogInfo();
        d.setGroupId((byte) 0x00);
        d.setUserId(0);
        d.setStatus(UserMgr.uStatus.NONE);
        return  d;
    }

    /*Add Userlog*/
    private static boolean AddNewUserlog(Context context, UserlogInfo u) {
        LogMgr.add_userlog(context,u);
        return true;
    }

    /*Create New Userlog*/
    public static UserlogInfo CreateNewUserlog(Context context,Integer userId,byte stationId) {

        byte groupId=0x01;
        byte status= uStatus.Login;

        return  CreateNewUserlog(context,userId,stationId,groupId,status);

    }
    public static UserlogInfo CreateNewUserlog(Context context,Integer userId,byte stationId,byte groupId,byte status) {
        UserlogInfo u=NEW_Userlog();

        u.setStartDateTime(DateTime.GetDateTimeNow());
        u.setUserId(userId);
        u.setStationId(stationId);
        u.setGroupId(groupId);
        u.setStatus(status);

        AddNewUserlog(context,u);
        return  u;
    }

    /*Update Userlog*/
    public static boolean UpdateUserlogStatus(Context context,byte sta, UserlogInfo u,byte newQstatus) {

        return LogMgr.Update_Userlog_Status(context,sta,u,u.getStatus(),newQstatus);
    }

    /*Find CurrentUser*/
    public static UserlogInfo Find_CurrentUserByCounter(Context context,byte sta) {
        UserlogInfo updateU=UserMgr.NEW_Userlog();
        boolean found=false;
        Integer index=0;
        for (UserlogInfo u:GData.Userlog) {
            if((u.getStationId()==sta)&&(u.getStatus()!= uStatus.NONE)){
                updateU=u;
                found=true;
            }
            index++;
        }
        return updateU;
    }
    public static UserlogInfo Find_CurrentUserByCounterActive(Context context,byte sta) {
        UserlogInfo updateU=UserMgr.NEW_Userlog();
        boolean found=false;
        Integer index=0;
        for (UserlogInfo u:GData.Userlog) {
            if((u.getStationId()==sta)&&((u.getStatus()==UserMgr.uStatus.Login)||(u.getStatus()==UserMgr.uStatus.Breaking))){
                updateU=u;
                found=true;
                return updateU;
            }
            index++;
        }
        return updateU;
    }

}
