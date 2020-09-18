package th.co.infinitecorp.www.qcontroller.API;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
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

public interface QcontrollerService {
    @GET("branchinfoApi")
    Call<List<BranchInfo>> requestBranchInfoDetail(@Query("search") String search);
    @GET("divinfoApi")
    Call<List<DivInfo>> requestDivInfoDetail(@Query("search") String search);
    @GET("lang_divApi")
    Call<List<LangDivInfo>> requestLangDivInfoDetail(@Query("search") String search);
    @GET("stainfoApi")
    Call<List<StaInfo>> requestStaInfoDetail(@Query("search") String search);
    @GET("breakinfoApi")
    Call<List<BreakInfo>> requestBreakInfoDetail(@Query("search") String search);
    @GET("grpinfoApi")
    Call<List<GrpInfo>> requestGrpInfoDetail(@Query("search") String search);
    @GET("userinfoApi")
    Call<List<UserInfo>> requestUserInfoDetail(@Query("search") String search);
    @GET("employeeinfoApi")
    Call<List<EmployeeInfo>> requestEmployeeInfoDetail(@Query("search") String search);
    @GET("division_advApi")
    Call<List<DivisionAdvInfo>> requestDivisionAdvInfoDetail(@Query("search") String search);
    @GET("PF_OTHERApi")
    Call<List<PF_OTHER>> requestPF_OTHERInfoDetail(@Query("search") String search);
    @GET("PF_DIVISIONApi")
    Call<List<PF_DIVISION>> requestPF_DIVISIONInfoDetail(@Query("search") String search);
    @GET("PF_AUTOTRANSFERApi")
    Call<List<PF_AUTOTRANSFER>> requestPF_AUTOTRANSFERInfoDetail(@Query("search") String search);
    @GET("PF_DIVMAPApi")
    Call<List<PF_DIVMAP>> requestPF_DIVMAPInfoDetail(@Query("search") String search);
    @GET("PF_STAMAPApi")
    Call<List<PF_STAMAP>> requestPF_STAMAPInfoDetail(@Query("search") String search);
    @GET("PF_ALARMGROUPApi")
    Call<List<PF_ALARMGROUP>> requestPF_ALARMGROUPInfoDetail(@Query("search") String search);
}
