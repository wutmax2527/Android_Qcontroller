package th.co.infinitecorp.www.qcontroller.Management;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import th.co.infinitecorp.www.qcontroller.API.QcontrollerApi;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.BranchInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.BreakInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.DivInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.DivisionAdvInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.EmployeeInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.GrpInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.LangDivInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.PF_ALARMGROUP;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.PF_AUTOTRANSFER;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.PF_DIVISION;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.PF_DIVMAP;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.PF_OTHER;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.PF_STAMAP;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.StaInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.UserInfo;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.MainActivity;
import th.co.infinitecorp.www.qcontroller.Utils.GData;

public class APIMgr {

    public static void Call_API_Profile(final Context context)
    {
        GData.callAPI_Index=0;
        EventBus.getDefault().post(new DebugMessageEvent("branch_id="+ GData.Branch_ID));
        String search="{branch_id:" + GData.Branch_ID.toString() + "}";
        String search_ProfileID0="{branch_id:" + GData.Branch_ID.toString() + ",PROFILE_ID:0}";
        String search_ProfileID1="{branch_id:" + GData.Branch_ID.toString() + ",PROFILE_ID:1}";

        Log.d("debug_URL","search->"+search);

        new QcontrollerApi().RequestBranchInfoDetail("{id:"+GData.Branch_ID.toString()+"}", new QcontrollerApi.BranchInfoDetailListener() {
            @Override
            public void onBranchInfoResult(List<BranchInfo> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("BranchInfo http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_BranchInfo(context,info);
                }
                GData.callAPI_Index++;
            }
        });

        new  QcontrollerApi().RequestDivInfoDetail("{BRANCH_ID:" + GData.Branch_ID.toString() + "}", new QcontrollerApi.DivInfoDetailListener() {
            @Override
            public void onDivInfoResult(List<DivInfo> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("DivInfo http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_DivInfo(context,info);
                }
                GData.callAPI_Index++;
            }
        });

        new QcontrollerApi().RequestLangDivInfoDetail("{branch_id:" + GData.Branch_ID.toString() + "}", new QcontrollerApi.LangDivInfoDetailListener() {
            @Override
            public void onLangDivInfoResult(List<LangDivInfo> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("LangDivInfo http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_LangDivInfo(context,info);
                }
                GData.callAPI_Index++;
            }
        });

        new QcontrollerApi().RequestStaInfoDetail("{branch_id:" + GData.Branch_ID.toString() + "}", new QcontrollerApi.StaInfoDetailListener() {
            @Override
            public void onStaInfoResult(List<StaInfo> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("StaInfo http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_StaInfo(context,info);
                }
                GData.callAPI_Index++;
            }
        });

        new QcontrollerApi().RequestBreakInfoDetail("{branch_id:" + GData.Branch_ID.toString() + "}", new QcontrollerApi.BreakInfoDetailListener() {
            @Override
            public void onBreakInfoResult(List<BreakInfo> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("BreakInfo http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_BreakInfo(context,info);
                }
                GData.callAPI_Index++;
            }
        });

        new  QcontrollerApi().RequestGrpInfoDetail("{branch_id:" + GData.Branch_ID.toString() + "}", new QcontrollerApi.GrpInfoDetailListener() {
            @Override
            public void onGrpInfoResult(List<GrpInfo> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("GrpInfo http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_GrpInfo(context,info);
                }
                GData.callAPI_Index++;
            }
        });

        new QcontrollerApi().RequestUserInfoDetail("{branch_id:" + GData.Branch_ID.toString() + "}", new QcontrollerApi.UserInfoDetailListener() {
            @Override
            public void onUserInfoResult(List<UserInfo> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("UserInfo http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_UserInfo(context,info);
                }
                GData.callAPI_Index++;
            }
        });

        new QcontrollerApi().RequestEmployeeInfoDetail("{branch_id:" + GData.Branch_ID.toString() + "}", new QcontrollerApi.EmployeeInfoDetailListener() {
            @Override
            public void onEmployeeInfoResult(List<EmployeeInfo> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("EmployeeInfo http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_EmployeeInfo(context,info);
                }
                GData.callAPI_Index++;
            }
        });

        new QcontrollerApi().RequestDivisionAdvInfoDetail("{branch_id:" + GData.Branch_ID.toString() + "}", new QcontrollerApi.DivisionAdvInfoDetailListener() {
            @Override
            public void onDivisionAdvInfoResult(List<DivisionAdvInfo> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("DivisionAdv http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_DivisionAdvInfo(context,info);
                }
                GData.callAPI_Index++;
            }
        });

        new QcontrollerApi().RequestPF_AlarmGroupInfoDetail(search_ProfileID1, new QcontrollerApi.PF_AlarmGroupInfoDetailListener() {
            @Override
            public void onPF_AlarmGroupInfoResult(List<PF_ALARMGROUP> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("PF_AlarmGroup http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_PF_ALARMGROUPInfo(context,info);
                }
                GData.callAPI_Index++;
            }
        });

        new QcontrollerApi().RequestPF_AUTOTRANSFERInfoDetail(search_ProfileID1, new QcontrollerApi.PF_AutoTransferInfoDetailListener() {
            @Override
            public void onPF_AutoTransferInfoResult(List<PF_AUTOTRANSFER> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("PF_AUTOTRANSFER http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_PF_AUTOTRANSFERInfo(context,info);
                }
                GData.callAPI_Index++;
            }
        });

        new QcontrollerApi().RequestPF_DIVISIONInfoDetail(search_ProfileID1, new QcontrollerApi.PF_DivisionInfoDetailListener() {
            @Override
            public void onPF_DivisionInfoResult(List<PF_DIVISION> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("PF_DIVISION http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_PF_DIVISIONInfo(context,info);
                }
                GData.callAPI_Index++;
            }
        });

        new QcontrollerApi().RequestPF_DIVMAPInfoDetail(search_ProfileID1, new QcontrollerApi.PF_DivMapInfoDetailListener() {
            @Override
            public void onPF_DivMapInfoResult(List<PF_DIVMAP> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("PF_DIVMAP http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_PF_DIVMAPInfo(context,info);
                }
                GData.callAPI_Index++;
            }
        });

        new QcontrollerApi().RequestPF_STAMAPInfoDetail(search_ProfileID1, new QcontrollerApi.PF_StaMapInfoDetailListener() {
            @Override
            public void onPF_StaMapInfoResult(List<PF_STAMAP> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("PF_STAMAP http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_PF_STAMAPInfo(context,info);
                }
                GData.callAPI_Index++;
            }
        });

        new QcontrollerApi().RequestPF_OtherInfoDetail(search_ProfileID0, new QcontrollerApi.PF_OtherInfoDetailListener() {
            @Override
            public void onPF_OtherInfoResult(List<PF_OTHER> info, Integer http_code) {
                EventBus.getDefault().post(new DebugMessageEvent("PF_Other http_code="+http_code));
                if(info!=null) {
                    LogMgr.Save_PF_OTHERInfo(context,info);
                }
                GData.callAPI_Index++;
            }
        });


    }
}
