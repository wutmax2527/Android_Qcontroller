package th.co.infinitecorp.www.qcontroller.Management;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import th.co.infinitecorp.www.qcontroller.DataInfo.BranchStatusInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentDivisionInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentGroupInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentStationInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.DivisionInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.QLaunchingInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.QueueInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.DivInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.Mapping.DivMapGroupInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.Mapping.StaMapGroupInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.PeriperalInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.TcpSocketInfo;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.Utils.GData;
import th.co.infinitecorp.www.qcontroller.Utils.Protocol;
import th.co.infinitecorp.www.qcontroller.Utils.constant;

public class InitializeMgr {
    public static void Start(Context context) {
        Init_Profile(context);
        Init_Profile_Mapping(context);
        Init_CurrentLog(context);
        Init_Division(context);
        Init_CurrentData(context);
        Init_HardwareData(context);
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
    public static void Init_Profile_Mapping(Context context){
        GData.StaMapGroupInfos=LogMgr.Load_StaMapGroupInfo(context);
        if(GData.StaMapGroupInfos.size()==0){
           for(Integer i=1;i<=constant.nStation;i++){
               StaMapGroupInfo s=new StaMapGroupInfo();
               s.setSTATION_ID(i);
               s.setGROUP_ID(i);
               GData.StaMapGroupInfos.add(s);
           }
        }
        GData.DivMapGroupInfos=LogMgr.Load_DivMapGroupInfo(context);
        if(GData.DivMapGroupInfos.size()==0){
            for (Integer i=1;i<=constant.nGroup;i++){
                DivMapGroupInfo d=new DivMapGroupInfo();
                d.setGROUP_ID(i);
                d.setDIVISION_ID(i);
                d.setLEVEL_SERVICE(1);
                GData.DivMapGroupInfos.add(d);
            }
        }

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
                CurrentStationInfo curSta=CounterMgr.NEW_CurrentStationInfo();
                curSta.setId(i);
                GData.CurStation.add(curSta);
            }
        }
        //--Update StaMapGroup to Current Station
        for (Integer i = 1; i <= constant.nStation; i++) {
            CurrentStationInfo curSta=GData.CurStation.get(i-1);
            byte grpId=1;

            if(i<=GData.StaMapGroupInfos.size()) {
                StaMapGroupInfo staMapGrp = new StaMapGroupInfo();
                staMapGrp = GData.StaMapGroupInfos.get(i - 1);
                grpId = staMapGrp.getGROUP_ID().byteValue();
            }
            curSta.setGroupId(grpId);
            List<DivMapGroupInfo> divMapGroup=new ArrayList<>();
            for(Integer j=0;j<GData.DivMapGroupInfos.size();j++){
                DivMapGroupInfo divMapGrp=new DivMapGroupInfo();
                divMapGrp=GData.DivMapGroupInfos.get(j);
                if(grpId==divMapGrp.getGROUP_ID()){
                    divMapGroup.add(divMapGrp);
                }
            }
            curSta.setDivMapGroup(divMapGroup);
            GData.CurStation.set(i-1,curSta);
        }

        GData.CurDiv=LogMgr.Load_CurrentDivisionInfo(context);
        if(GData.CurDiv.size()==0) {
            for (Integer i = 1; i <= constant.nDiv; i++) {
                CurrentDivisionInfo curDiv=new CurrentDivisionInfo();
                curDiv.setId(i);
                curDiv.setWaitQ((short) 0);
                curDiv.setHoldQ((short)0);
                curDiv.setName("Division"+i);
                curDiv.setNextQ(new QueueInfo());
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
                curGrp.setAvailableCounter((short)0);
                curGrp.setLastQ(new QueueInfo());
                curGrp.setName("G"+i);
                GData.CurGrp.add(curGrp);
            }
        }
        //---Update DivMapGroup to Current Group
        for (Integer i = 1; i <= constant.nGroup; i++) {
            CurrentGroupInfo curGrp=GData.CurGrp.get(i-1);
            List<DivMapGroupInfo> divMapGroup=new ArrayList<>();
            for(Integer j=0;j<GData.DivMapGroupInfos.size();j++){
                DivMapGroupInfo divMapGrp=new DivMapGroupInfo();
                divMapGrp=GData.DivMapGroupInfos.get(j);
                if(i==divMapGrp.getGROUP_ID()){
                    divMapGroup.add(divMapGrp);
                }
            }
            curGrp.setDivMapGroup(divMapGroup);
            GData.CurGrp.set(i-1,curGrp);
        }
    }
    public static void Init_HardwareData(Context context) {
        GData.PeriperalInfos.clear();
        //GData.PeriperalInfos=LogMgr.Load_PeriperalInfo(context);
        if(GData.PeriperalInfos.size()==0){
            Integer id=1;
            //---HardKey
            for(Integer i=1;i<=constant.nStation;i++)
            {
                PeriperalInfo p=new PeriperalInfo();
                p.setId(id);
                p.setDeviceId(i.byteValue());
                p.setDeviceType(Protocol.DeviceType.HW_KEYPAD);
                p.setIp("");
                p.setSocket(null);
                p.setStatus((byte)PeriperalMgr.status.InActive);
                GData.PeriperalInfos.add(p);
                id++;
            }
            //---Softkey
            for(Integer i=1;i<=constant.nStation;i++)
            {
                PeriperalInfo p=new PeriperalInfo();
                p.setId(id);
                p.setDeviceId(i.byteValue());
                p.setDeviceType(Protocol.DeviceType.SW_KEYPAD);
                p.setIp("");
                p.setSocket(null);
                p.setStatus((byte)PeriperalMgr.status.InActive);
                GData.PeriperalInfos.add(p);
                id++;
            }
            //---Led Display
            for(Integer i=1;i<=constant.nLedDisplay;i++)
            {
                PeriperalInfo p=new PeriperalInfo();
                p.setId(id);
                p.setDeviceId(i.byteValue());
                p.setDeviceType(Protocol.DeviceType.DISPLAY);
                p.setIp("");
                p.setSocket(null);
                p.setStatus((byte)PeriperalMgr.status.InActive);
                GData.PeriperalInfos.add(p);
                id++;
            }
        }
        Integer id=1;
        //---Softkey
        GData.TcpSocketInfos.clear();;
        for(Integer i=1;i<=constant.nStation;i++)
        {
            TcpSocketInfo t=new TcpSocketInfo();
            t.setId(id);
            t.setDeviceId(i.byteValue());
            t.setDeviceType(Protocol.DeviceType.SW_KEYPAD);
            t.setIp("");
            t.setSocket(null);
            t.setStatus((byte)PeriperalMgr.status.InActive);
            GData.TcpSocketInfos.add(t);
            id++;
        }
    }
}
