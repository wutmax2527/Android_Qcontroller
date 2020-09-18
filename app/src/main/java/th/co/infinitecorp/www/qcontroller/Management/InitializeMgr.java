package th.co.infinitecorp.www.qcontroller.Management;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import th.co.infinitecorp.www.qcontroller.DataInfo.BranchStatusInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentDivisionInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentGroupInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentStationInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.DivisionInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.QLaunchingInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.DivInfo;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.Utils.GData;
import th.co.infinitecorp.www.qcontroller.Utils.constant;

public class InitializeMgr {
    public static void Start(Context context) {
        Init_Profile(context);
        Init_CurrentLog(context);
        Init_Division(context);
        Init_CurrentData(context);
    }
    public static void Init_BranchStatus(Context contex) {
        GData.branchStatusInfos=LogMgr.Load_BranchStatusInfo(contex);
        EventBus.getDefault().post(new DebugMessageEvent("GData.branchStatusInfos.size="+GData.branchStatusInfos.size()));
        if(GData.branchStatusInfos.size()==0) {
            for (Integer i = 1; i <= 1; i++) {
                BranchStatusInfo branchStatusInfo= BranchMgr.NEW_BranchStatusInfo();
                branchStatusInfo.setId(99999);
                GData.branchStatusInfos.add(branchStatusInfo);
            }
        }
        else
        {
        }
        BranchStatusInfo branchStatus=new BranchStatusInfo();
        branchStatus= GData.branchStatusInfos.get(0);
        GData.Branch_ID= branchStatus.getId();

        String lastDate=branchStatus.getLastDate();
        EventBus.getDefault().post(new DebugMessageEvent("load lastDate="+lastDate));
    }
    public static void Init_CurrentQ(Context context) {
        GData.QLaunching=LogMgr.Load_QLaunchingInfo(context);
        if(GData.QLaunching.size()==0) {
            for (Integer i = 1; i <= constant.nDiv; i++) {
                QLaunchingInfo qLaunchingInfo= DivisionMgr.NEW_CURRNETQ();
                qLaunchingInfo.setId(i);
                GData.QLaunching.add(qLaunchingInfo);
            }
        }
    }
    public static void Init_CurrentLog(Context context) {
        //---Current Info
        GData.Queue = LogMgr.Load_QueueInfo(context);
        GData.Queuelog=LogMgr.Load_QueuelogInfo(context);
        GData.Userlog=LogMgr.Load_UserlogInfo(context);
        GData.Counterlog=LogMgr.Load_CounterlogInfo(context);
    }
    public static void Init_Profile(Context context) {
        GData.branchInfos=LogMgr.Load_BranchInfo(context);
        GData.divInfos=LogMgr.Load_DivInfo(context);
        GData.langDivInfos=LogMgr.Load_LangDivInfo(context);
        GData.staInfos=LogMgr.Load_StaInfo(context);
        GData.breakInfos=LogMgr.Load_BreakInfo(context);
        GData.grpInfos=LogMgr.Load_GrpInfo(context);
        GData.userInfos=LogMgr.Load_UserInfo(context);
        GData.employeeInfos=LogMgr.Load_EmployeeInfo(context);
        GData.divisionAdvInfos=LogMgr.Load_DivisionAdvInfo(context);
        GData.pf_others=LogMgr.Load_PF_OTHERInfo(context);
        GData.pf_divisions=LogMgr.Load_PF_DIVISIONInfo(context);
        GData.pf_autotransfers=LogMgr.Load_PF_AUTOTRANSFERInfo(context);
        GData.pf_divmaps=LogMgr.Load_PF_DIVMAPInfo(context);
        GData.pf_stamaps=LogMgr.Load_PF_STAMAPInfo(context);
        GData.pf_alarmgroups=LogMgr.Load_PF_ALARMGROUPInfo(context);


    }
    public static void Init_Division(Context context) {
        //---Current Q
        Init_CurrentQ(context);
        GData.Division=new ArrayList<>();
        if(GData.Division.size()==0) {
            for (Integer i = 1; i <= constant.nDiv; i++) {
                DivisionInfo d = DivisionMgr.NEW_DIVISION();
                d.setId(i);
                d.setAlphabet((byte)(0x41+((i-1))));
                d.setqLaunching(GData.QLaunching.get(i-1).getqLaunching());
                GData.Division.add(d);
            }
        }
        GData.divInfos=LogMgr.Load_DivInfo(context);
        //---Div info
        if(GData.divInfos.size()>0)
        {
            for (DivisionInfo div : GData.Division) {
                for (DivInfo d: GData.divInfos) {
                    if(div.getId().equals(d.getID())) {
                        DivisionInfo divs = div;
                        divs.setDivName(d.getName());
                        divs.setqBegin(d.getQStart().shortValue());
                        divs.setqEnd(d.getQStop().shortValue());
                        divs.setnCopies(d.getPrint_Coppies().byteValue());
                        GData.Division.set(divs.getId()-1, divs);
                        break;
                    }
                }
            }
        }
        //---PF_DIVISION

    }
    public static void Init_CurrentData(Context context) {

        GData.CurStation=LogMgr.Load_CurrentStationInfo(context);
        if(GData.CurStation.size()==0) {
            for (Integer i = 1; i <= constant.nStation; i++) {
                CurrentStationInfo curDiv=CounterMgr.NEW_CurrentStationInfo();
                curDiv.setId(i);
                GData.CurStation.add(curDiv);
            }
        }

        GData.CurDiv=LogMgr.Load_CurrentDivisionInfo(context);
        if(GData.CurDiv.size()==0) {
            for (Integer i = 1; i <= constant.nDiv; i++) {
                CurrentDivisionInfo curDiv=new CurrentDivisionInfo();
                curDiv.setId(i);
                curDiv.setWaitQ((short) 0);
                curDiv.setHoldQ((short)0);
                GData.CurDiv.add(curDiv);
            }
        }

        GData.CurGrp=LogMgr.Load_CurrentGroupInfo(context);
        if(GData.CurGrp.size()==0) {
            for (Integer i = 1; i <= constant.nGroup; i++) {
                CurrentGroupInfo curGrp=new CurrentGroupInfo();
                curGrp.setId(i);
                curGrp.setWaitQ((short) 0);
                curGrp.setHoldQ((short) 0);
                GData.CurGrp.add(curGrp);
            }
        }
    }
}
