package th.co.infinitecorp.www.qcontroller.API;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

public class QcontrollerApi {
    // BranchInfo
    public interface BranchInfoDetailListener {
        void onBranchInfoResult(List<BranchInfo> info, Integer http_code);
    }
    BranchInfoDetailListener branchInfoDetailListener;
    public void RequestBranchInfoDetail(String search, final BranchInfoDetailListener listener){
        this.branchInfoDetailListener = listener;
        RestManager rest = new RestManager();
        Call<List<BranchInfo>> call;
        call =  rest.getService().requestBranchInfoDetail(search);
        call.enqueue(new Callback<List<BranchInfo>>() {
            @Override
            public void onResponse(Call<List<BranchInfo>> call, Response<List<BranchInfo>> response) {
                if(response.code() == 200){
                    List<BranchInfo> info = response.body();
                    listener.onBranchInfoResult(info,response.code());
                    Log.d("debug","BranchInfo [API]: " + response.code());
                }else{
                    Log.d("debug","BranchInfo [API]: " + response.code());
                    listener.onBranchInfoResult(null,response.code());
                }
            }
            @Override
            public void onFailure(Call<List<BranchInfo>> call, Throwable t) {
                Log.d("debug","BranchInfo [API]: fail "+t.getLocalizedMessage());
                listener.onBranchInfoResult(null,500);
            }
        });
        Log.d("debug_URL","URL="+call.request().url().toString());
    }
    //DivInfo
    public interface DivInfoDetailListener {
        void onDivInfoResult(List<DivInfo> info, Integer http_code);
    }
    DivInfoDetailListener divInfoDetailListener;
    public void RequestDivInfoDetail(String search, final DivInfoDetailListener listener){
        this.divInfoDetailListener = listener;
        RestManager rest = new RestManager();
        Call<List<DivInfo>> call;
        call =  rest.getService().requestDivInfoDetail(search);
        call.enqueue(new Callback<List<DivInfo>>() {
            @Override
            public void onResponse(Call<List<DivInfo>> call, Response<List<DivInfo>> response) {
                if(response.code() == 200){
                    List<DivInfo> info = response.body();
                    listener.onDivInfoResult(info,response.code());
                    Log.d("debug","DivInfo [API]: " + response.code());
                }else{
                    Log.d("debug","DivInfo [API]: " + response.code());
                    listener.onDivInfoResult(null,response.code());
                }
            }
            @Override
            public void onFailure(Call<List<DivInfo>> call, Throwable t) {
                Log.d("debug","DivInfo [API]: fail "+t.getLocalizedMessage());
                listener.onDivInfoResult(null,500);
            }
        });
        Log.d("debug_URL","URL="+call.request().url().toString());
    }
    //LangDivInfo
    public interface LangDivInfoDetailListener {
        void onLangDivInfoResult(List<LangDivInfo> info, Integer http_code);
    }
    LangDivInfoDetailListener langDivInfoDetailListener;
    public void RequestLangDivInfoDetail(String search, final LangDivInfoDetailListener listener){
        this.langDivInfoDetailListener = listener;
        RestManager rest = new RestManager();
        Call<List<LangDivInfo>> call;
        call =  rest.getService().requestLangDivInfoDetail(search);
        call.enqueue(new Callback<List<LangDivInfo>>() {
            @Override
            public void onResponse(Call<List<LangDivInfo>> call, Response<List<LangDivInfo>> response) {
                if(response.code() == 200){
                    List<LangDivInfo> info = response.body();
                    listener.onLangDivInfoResult(info,response.code());
                    Log.d("debug","LangDivInfo [API]: " + response.code());
                }else{
                    Log.d("debug","LangDivInfo [API]: " + response.code());
                    listener.onLangDivInfoResult(null,response.code());
                }
            }
            @Override
            public void onFailure(Call<List<LangDivInfo>> call, Throwable t) {
                Log.d("debug","LangDivInfo [API]: fail "+t.getLocalizedMessage());
                listener.onLangDivInfoResult(null,500);
            }
        });
        Log.d("debug_URL","URL="+call.request().url().toString());
    }
    //StaInfo
    public interface StaInfoDetailListener {
        void onStaInfoResult(List<StaInfo> info, Integer http_code);
    }
    StaInfoDetailListener staInfoDetailListener;
    public void RequestStaInfoDetail(String search, final StaInfoDetailListener listener){
        this.staInfoDetailListener = listener;
        RestManager rest = new RestManager();
        Call<List<StaInfo>> call;
        call =  rest.getService().requestStaInfoDetail(search);
        call.enqueue(new Callback<List<StaInfo>>() {
            @Override
            public void onResponse(Call<List<StaInfo>> call, Response<List<StaInfo>> response) {
                if(response.code() == 200){
                    List<StaInfo> info = response.body();
                    listener.onStaInfoResult(info,response.code());
                    Log.d("debug","StaInfo [API]: " + response.code());
                }else{
                    Log.d("debug","StaInfo [API]: " + response.code());
                    listener.onStaInfoResult(null,response.code());
                }
            }
            @Override
            public void onFailure(Call<List<StaInfo>> call, Throwable t) {
                Log.d("debug","StaInfo [API]: fail "+t.getLocalizedMessage());
                listener.onStaInfoResult(null,500);
            }
        });
        Log.d("debug_URL","URL="+call.request().url().toString());
    }
    //BreakInfo
    public interface BreakInfoDetailListener {
        void onBreakInfoResult(List<BreakInfo> info, Integer http_code);
    }
    BreakInfoDetailListener breakInfoDetailListener;
    public void RequestBreakInfoDetail(String search, final BreakInfoDetailListener listener){
        this.breakInfoDetailListener = listener;
        RestManager rest = new RestManager();
        Call<List<BreakInfo>> call;
        call =  rest.getService().requestBreakInfoDetail(search);
        call.enqueue(new Callback<List<BreakInfo>>() {
            @Override
            public void onResponse(Call<List<BreakInfo>> call, Response<List<BreakInfo>> response) {
                if(response.code() == 200){
                    List<BreakInfo> info = response.body();
                    listener.onBreakInfoResult(info,response.code());
                    Log.d("debug","BreakInfo [API]: " + response.code());
                }else{
                    Log.d("debug","BreakInfo [API]: " + response.code());
                    listener.onBreakInfoResult(null,response.code());
                }
            }
            @Override
            public void onFailure(Call<List<BreakInfo>> call, Throwable t) {
                Log.d("debug","BreakInfo [API]: fail "+t.getLocalizedMessage());
                listener.onBreakInfoResult(null,500);
            }
        });
        Log.d("debug_URL","URL="+call.request().url().toString());
    }
    //GrpInfo
    public interface GrpInfoDetailListener {
        void onGrpInfoResult(List<GrpInfo> info, Integer http_code);
    }
    GrpInfoDetailListener grpInfoDetailListener;
    public void RequestGrpInfoDetail(String search, final GrpInfoDetailListener listener){
        this.grpInfoDetailListener = listener;
        RestManager rest = new RestManager();
        Call<List<GrpInfo>> call;
        call =  rest.getService().requestGrpInfoDetail(search);
        call.enqueue(new Callback<List<GrpInfo>>() {
            @Override
            public void onResponse(Call<List<GrpInfo>> call, Response<List<GrpInfo>> response) {
                if(response.code() == 200){
                    List<GrpInfo> info = response.body();
                    listener.onGrpInfoResult(info,response.code());
                    Log.d("debug","GrpInfo [API]: " + response.code());
                }else{
                    Log.d("debug","GrpInfo [API]: " + response.code());
                    listener.onGrpInfoResult(null,response.code());
                }
            }
            @Override
            public void onFailure(Call<List<GrpInfo>> call, Throwable t) {
                Log.d("debug","GrpInfo [API]: fail "+t.getLocalizedMessage());
                listener.onGrpInfoResult(null,500);
            }
        });

        Log.d("debug_URL","URL="+call.request().url().toString());
    }
    //UserInfo
    public interface UserInfoDetailListener {
        void onUserInfoResult(List<UserInfo> info, Integer http_code);
    }
    UserInfoDetailListener userInfoDetailListener;
    public void RequestUserInfoDetail(String search, final UserInfoDetailListener listener){
        this.userInfoDetailListener = listener;
        RestManager rest = new RestManager();
        Call<List<UserInfo>> call;
        call =  rest.getService().requestUserInfoDetail(search);
        call.enqueue(new Callback<List<UserInfo>>() {
            @Override
            public void onResponse(Call<List<UserInfo>> call, Response<List<UserInfo>> response) {
                if(response.code() == 200){
                    List<UserInfo> info = response.body();
                    listener.onUserInfoResult(info,response.code());
                    Log.d("debug","UserInfo[API]: " + response.code());
                }else{
                    Log.d("debug","UserInfo[API]: " + response.code());
                    listener.onUserInfoResult(null,response.code());
                }
            }
            @Override
            public void onFailure(Call<List<UserInfo>> call, Throwable t) {
                Log.d("debug","UserInfo [API]: fail "+t.getLocalizedMessage());
                listener.onUserInfoResult(null,500);
            }
        });
        Log.d("debug_URL","URL="+call.request().url().toString());
    }
    //EmployeeInfo
    public interface EmployeeInfoDetailListener {
        void onEmployeeInfoResult(List<EmployeeInfo> info, Integer http_code);
    }
    EmployeeInfoDetailListener employeeInfoDetailListener;
    public void RequestEmployeeInfoDetail(String search, final EmployeeInfoDetailListener listener){
        this.employeeInfoDetailListener = listener;
        RestManager rest = new RestManager();
        Call<List<EmployeeInfo>> call;
        call =  rest.getService().requestEmployeeInfoDetail(search);
        call.enqueue(new Callback<List<EmployeeInfo>>() {
            @Override
            public void onResponse(Call<List<EmployeeInfo>> call, Response<List<EmployeeInfo>> response) {
                if(response.code() == 200){
                    List<EmployeeInfo> info = response.body();
                    listener.onEmployeeInfoResult(info,response.code());
                    Log.d("debug","EmployeeInfo [API]: " + response.code());
                }else{
                    Log.d("debug","EmployeeInfo [API]: " + response.code());
                    listener.onEmployeeInfoResult(null,response.code());
                }
            }
            @Override
            public void onFailure(Call<List<EmployeeInfo>> call, Throwable t) {
                Log.d("debug","EmployeeInfo [API]: fail "+t.getLocalizedMessage());
                listener.onEmployeeInfoResult(null,500);
            }
        });
        Log.d("debug_URL","URL="+call.request().url().toString());
    }
    //DivisionAdvInfo
    public interface DivisionAdvInfoDetailListener {
        void onDivisionAdvInfoResult(List<DivisionAdvInfo> info, Integer http_code);
    }
    DivisionAdvInfoDetailListener divisionAdvInfoDetailListener;
    public void RequestDivisionAdvInfoDetail(String search, final DivisionAdvInfoDetailListener listener){
        this.divisionAdvInfoDetailListener = listener;
        RestManager rest = new RestManager();
        Call<List<DivisionAdvInfo>> call;
        call =  rest.getService().requestDivisionAdvInfoDetail(search);
        call.enqueue(new Callback<List<DivisionAdvInfo>>() {
            @Override
            public void onResponse(Call<List<DivisionAdvInfo>> call, Response<List<DivisionAdvInfo>> response) {
                if(response.code() == 200){
                    List<DivisionAdvInfo> info = response.body();
                    listener.onDivisionAdvInfoResult(info,response.code());
                    Log.d("debug","DivisionAdvInfo [API]: " + response.code());
                }else{
                    Log.d("debug","DivisionAdvInfo [API]: " + response.code());
                    listener.onDivisionAdvInfoResult(null,response.code());
                }
            }
            @Override
            public void onFailure(Call<List<DivisionAdvInfo>> call, Throwable t) {
                Log.d("debug","DivisionAdvInfo [API]: fail "+t.getLocalizedMessage());
                listener.onDivisionAdvInfoResult(null,500);
            }
        });
        Log.d("debug_URL","URL="+call.request().url().toString());
    }
    //PF_OTHER
    public interface PF_OtherInfoDetailListener {
        void onPF_OtherInfoResult(List<PF_OTHER> info, Integer http_code);
    }
    PF_OtherInfoDetailListener pf_otherInfoDetailListener;
    public void RequestPF_OtherInfoDetail(String search, final PF_OtherInfoDetailListener listener){
        this.pf_otherInfoDetailListener = listener;
        RestManager rest = new RestManager();
        Call<List<PF_OTHER>> call;
        call =  rest.getService().requestPF_OTHERInfoDetail(search);
        call.enqueue(new Callback<List<PF_OTHER>>() {
            @Override
            public void onResponse(Call<List<PF_OTHER>> call, Response<List<PF_OTHER>> response) {
                if(response.code() == 200){
                    List<PF_OTHER> info = response.body();
                    listener.onPF_OtherInfoResult(info,response.code());
                    Log.d("debug","PF_Other [API]: " + response.code());
                }else{
                    Log.d("debug","PF_Other [API]: " + response.code());
                    listener.onPF_OtherInfoResult(null,response.code());
                }
            }
            @Override
            public void onFailure(Call<List<PF_OTHER>> call, Throwable t) {
                Log.d("debug","PF_Other [API]: fail "+t.getLocalizedMessage());
                listener.onPF_OtherInfoResult(null,500);
            }
        });
        Log.d("debug_URL","URL="+call.request().url().toString());
    }
    //PF_DIVISION
    public interface PF_DivisionInfoDetailListener {
        void onPF_DivisionInfoResult(List<PF_DIVISION> info, Integer http_code);
    }
    PF_DivisionInfoDetailListener pf_divisionInfoDetailListener;
    public void RequestPF_DIVISIONInfoDetail(String search, final PF_DivisionInfoDetailListener listener){
        this.pf_divisionInfoDetailListener = listener;
        RestManager rest = new RestManager();
        Call<List<PF_DIVISION>> call;
        call =  rest.getService().requestPF_DIVISIONInfoDetail(search);
        call.enqueue(new Callback<List<PF_DIVISION>>() {
            @Override
            public void onResponse(Call<List<PF_DIVISION>> call, Response<List<PF_DIVISION>> response) {
                if(response.code() == 200){
                    List<PF_DIVISION> info = response.body();
                    listener.onPF_DivisionInfoResult(info,response.code());
                    Log.d("debug","PF_DIVISION [API]: " + response.code());
                }else{
                    Log.d("debug","PF_DIVISION [API]: " + response.code());
                    listener.onPF_DivisionInfoResult(null,response.code());
                }
            }
            @Override
            public void onFailure(Call<List<PF_DIVISION>> call, Throwable t) {
                Log.d("debug","PF_DIVISION [API]: fail "+t.getLocalizedMessage());
                listener.onPF_DivisionInfoResult(null,500);
            }
        });
        Log.d("debug_URL","URL="+call.request().url().toString());
    }
    //PF_AUTOTRANSFER
    public interface PF_AutoTransferInfoDetailListener {
        void onPF_AutoTransferInfoResult(List<PF_AUTOTRANSFER> info, Integer http_code);
    }
    PF_AutoTransferInfoDetailListener pf_autotransferInfoDetailListener;
    public void RequestPF_AUTOTRANSFERInfoDetail(String search, final PF_AutoTransferInfoDetailListener listener){
        this.pf_autotransferInfoDetailListener = listener;
        RestManager rest = new RestManager();
        Call<List<PF_AUTOTRANSFER>> call;
        call =  rest.getService().requestPF_AUTOTRANSFERInfoDetail(search);
        call.enqueue(new Callback<List<PF_AUTOTRANSFER>>() {
            @Override
            public void onResponse(Call<List<PF_AUTOTRANSFER>> call, Response<List<PF_AUTOTRANSFER>> response) {
                if(response.code() == 200){
                    List<PF_AUTOTRANSFER> info = response.body();
                    listener.onPF_AutoTransferInfoResult(info,response.code());
                    Log.d("debug","PF_AUTOTRANSFER [API]: " + response.code());
                }else{
                    Log.d("debug","PF_AUTOTRANSFER [API]: " + response.code());
                    listener.onPF_AutoTransferInfoResult(null,response.code());
                }
            }
            @Override
            public void onFailure(Call<List<PF_AUTOTRANSFER>> call, Throwable t) {
                Log.d("debug","PF_AUTOTRANSFER [API]: fail "+t.getLocalizedMessage());
                listener.onPF_AutoTransferInfoResult(null,500);
            }
        });
        Log.d("debug_URL","URL="+call.request().url().toString());
    }
    //PF_DIVMAP
    public interface PF_DivMapInfoDetailListener {
        void onPF_DivMapInfoResult(List<PF_DIVMAP> info, Integer http_code);
    }
    PF_DivMapInfoDetailListener pf_divmapInfoDetailListener;
    public void RequestPF_DIVMAPInfoDetail(String search, final PF_DivMapInfoDetailListener listener){
        this.pf_divmapInfoDetailListener = listener;
        RestManager rest = new RestManager();
        Call<List<PF_DIVMAP>> call;
        call =  rest.getService().requestPF_DIVMAPInfoDetail(search);
        call.enqueue(new Callback<List<PF_DIVMAP>>() {
            @Override
            public void onResponse(Call<List<PF_DIVMAP>> call, Response<List<PF_DIVMAP>> response) {
                if(response.code() == 200){
                    List<PF_DIVMAP> info = response.body();
                    listener.onPF_DivMapInfoResult(info,response.code());
                    Log.d("debug","PF_DIVMAP [API]: " + response.code());
                }else{
                    Log.d("debug","PF_DIVMAP [API]: " + response.code());
                    listener.onPF_DivMapInfoResult(null,response.code());
                }
            }
            @Override
            public void onFailure(Call<List<PF_DIVMAP>> call, Throwable t) {
                Log.d("debug","PF_DIVMAP [API]: fail "+t.getLocalizedMessage());
                listener.onPF_DivMapInfoResult(null,500);
            }
        });
        Log.d("debug_URL","URL="+call.request().url().toString());
    }
    //PF_STAMAP
    public interface PF_StaMapInfoDetailListener {
        void onPF_StaMapInfoResult(List<PF_STAMAP> info, Integer http_code);
    }
    PF_StaMapInfoDetailListener pf_stamapInfoDetailListener;
    public void RequestPF_STAMAPInfoDetail(String search, final PF_StaMapInfoDetailListener listener){
        this.pf_stamapInfoDetailListener= listener;
        RestManager rest = new RestManager();
        Call<List<PF_STAMAP>> call;
        call =  rest.getService().requestPF_STAMAPInfoDetail(search);
        call.enqueue(new Callback<List<PF_STAMAP>>() {
            @Override
            public void onResponse(Call<List<PF_STAMAP>> call, Response<List<PF_STAMAP>> response) {
                if(response.code() == 200){
                    List<PF_STAMAP> info = response.body();
                    listener.onPF_StaMapInfoResult(info,response.code());
                    Log.d("debug","PF_STAMAP [API]: " + response.code());
                }else{
                    Log.d("debug","PF_STAMAP [API]: " + response.code());
                    listener.onPF_StaMapInfoResult(null,response.code());
                }
            }
            @Override
            public void onFailure(Call<List<PF_STAMAP>> call, Throwable t) {
                Log.d("debug","[API]: fail "+t.getLocalizedMessage());
                listener.onPF_StaMapInfoResult(null,500);
            }
        });
        Log.d("debug_URL","URL="+call.request().url().toString());
    }
    //PF_ALARMGROUP
    public interface PF_AlarmGroupInfoDetailListener {
        void onPF_AlarmGroupInfoResult(List<PF_ALARMGROUP> info, Integer http_code);
    }
    PF_AlarmGroupInfoDetailListener pf_alarmgroupInfoDetailListener;
    public void RequestPF_AlarmGroupInfoDetail(String search, final PF_AlarmGroupInfoDetailListener listener){
        this.pf_alarmgroupInfoDetailListener= listener;
        RestManager rest = new RestManager();
        Call<List<PF_ALARMGROUP>> call;
        call =  rest.getService().requestPF_ALARMGROUPInfoDetail(search);
        call.enqueue(new Callback<List<PF_ALARMGROUP>>() {
            @Override
            public void onResponse(Call<List<PF_ALARMGROUP>> call, Response<List<PF_ALARMGROUP>> response) {
                if(response.code() == 200){
                    List<PF_ALARMGROUP> info = response.body();
                    listener.onPF_AlarmGroupInfoResult(info,response.code());
                    Log.d("debug","PF_ALARMGROUP [API]: " + response.code());
                }else{
                    Log.d("debug","PF_ALARMGROUP [API]: " + response.code());
                    listener.onPF_AlarmGroupInfoResult(null,response.code());
                }
            }
            @Override
            public void onFailure(Call<List<PF_ALARMGROUP>> call, Throwable t) {
                Log.d("debug","PF_ALARMGROUP [API]: fail "+t.getLocalizedMessage());
                listener.onPF_AlarmGroupInfoResult(null,500);
            }
        });
        Log.d("debug_URL","URL="+call.request().url().toString());
    }
}
