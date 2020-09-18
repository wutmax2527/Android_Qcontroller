package th.co.infinitecorp.www.qcontroller.Management;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import th.co.infinitecorp.www.qcontroller.DataInfo.BranchStatusInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.DivisionInfo;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.MainActivity;
import th.co.infinitecorp.www.qcontroller.Utils.Cookie;
import th.co.infinitecorp.www.qcontroller.Utils.DateTime;
import th.co.infinitecorp.www.qcontroller.Utils.GData;

public class SystemMgr {
    public static boolean ResetQ_All(Context context)
    {
        return Reset_Queue(context);
    }
    public static boolean Reset_System(Context context) {
        boolean res=Reset_Queue(context);
        LogMgr.Delete_UserlogInfo(context);
        GData.Userlog=LogMgr.Load_UserlogInfo(context);
        LogMgr.Delete_CounterlogInfo(context);
        GData.Counterlog=LogMgr.Load_CounterlogInfo(context);

        LogMgr.Delete_CurrentStationInfo(context);
        GData.CurStation=LogMgr.Load_CurrentStationInfo(context);
        LogMgr.Delete_CurrentDivisionInfo(context);
        GData.CurDiv=LogMgr.Load_CurrentDivisionInfo(context);
        LogMgr.Delete_CurrentGroupInfo(context);
        GData.CurGrp=LogMgr.Load_CurrentGroupInfo(context);
        InitializeMgr.Init_CurrentData(context);

        ViewLogMgr.ShowQueue();
        ViewLogMgr.ShowUserlog();
        ViewLogMgr.ShowCounterlog();
        return res;
    }
    public static boolean Reset_Queue(Context context) {
        LogMgr.Delete_QLaunchingInfo(context);
        GData.QLaunching=LogMgr.Load_QLaunchingInfo(context);
        LogMgr.Delete_QueueInfo(context);
        GData.Queue=LogMgr.Load_QueueInfo(context);

        InitializeMgr.Init_CurrentQ(context);
        if(GData.Division.size()>0) {
            for (DivisionInfo divsion : GData.Division) {
                DivisionInfo divisionInfo = new DivisionInfo();
                divisionInfo = divsion;
                Short qLaunching = 0;
                divisionInfo.setqLaunching(qLaunching);
                GData.Division.set(divisionInfo.getId() - 1, divisionInfo);
            }
        }
        EventBus.getDefault().post(new DebugMessageEvent("***Reset Queue***"));
        return true;
    }
    public static boolean CheckResetQueue(Context context) {
        if(GData.branchStatusInfos.size()>0)
        {
            BranchStatusInfo branchStatus=new BranchStatusInfo();
            branchStatus= GData.branchStatusInfos.get(0);
            String curDate= DateTime.GetCurrentDateTime("yyyy-MM-dd");
            String lastDate=branchStatus.getLastDate();
            if(!curDate.equals(lastDate))
            {
                EventBus.getDefault().post(new DebugMessageEvent("curDate="+curDate));
                EventBus.getDefault().post(new DebugMessageEvent("lastDate="+lastDate));

                branchStatus.setLastDate(curDate);
                GData.branchStatusInfos.set(0,branchStatus);
                //Cookie.SaveBranchStatusDataInfo(MainActivity.this, GData.branchStatusInfos);
                LogMgr.Save_BranchStatusInfo(context,GData.branchStatusInfos);
                //---Reset Q
                boolean res=SystemMgr.ResetQ_All(context);
                return res;

            }
        }
        return false;

    }
    public static void WaitTimeOut_Sec(Integer timeOutSec) {
        long start_Sec = System.currentTimeMillis() / 1000;
        while (true)
        {
            long cur_Sec = System.currentTimeMillis() / 1000;
            if((cur_Sec-start_Sec)>timeOutSec)
            {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
