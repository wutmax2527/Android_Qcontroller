package th.co.infinitecorp.www.qcontroller.Management;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import th.co.infinitecorp.www.qcontroller.DataInfo.BranchStatusInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CounterlogInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentDivisionInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentGroupInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentStationInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.QLaunchingInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.QueueInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.QueuelogInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.UserlogInfo;
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
import th.co.infinitecorp.www.qcontroller.Utils.DateTime;
import th.co.infinitecorp.www.qcontroller.Utils.ExternalStorage;
import th.co.infinitecorp.www.qcontroller.Utils.FileName;
import th.co.infinitecorp.www.qcontroller.Utils.FolderPath;
import th.co.infinitecorp.www.qcontroller.Utils.GData;

public class LogMgr {

    //---Queue
    public static void add_qwaitng(Context context,QueueInfo ALLQ, int qstatus) {
        GData.Queue.add(ALLQ);
        LogMgr.Save_QueueInfo(context,GData.Queue);
        LogMgr.Save_QLaunchingInfo(context,GData.QLaunching);
    }
    public static boolean Update_Queue_Status(Context context,QueueInfo ALLQ, int oldQstatus,byte newQstatus) {
        boolean found=false;
        QueueInfo updateQ=QueueMgr.NEW_QUEUE();
        Integer index=0;
        for (QueueInfo q:GData.Queue) {
            if(QueueMgr.Check_SameQueue(q,ALLQ)&& ALLQ.getStatus()==oldQstatus) {
                updateQ=ALLQ;
               found=true;
              break;
            }
            index++;
        }
        if(found) {
            if (GData.Queue.size() > 0 && (index < GData.Queue.size())) {
                updateQ.setStatus(newQstatus);
                GData.Queue.set(index, updateQ);
            }
            return LogMgr.Save_QueueInfo(context, GData.Queue);
        }
        return false;
    }

    //---Userlog
    public static void add_userlog(Context context,UserlogInfo ulogInfo) {
        GData.Userlog.add(ulogInfo);
        LogMgr.Save_UserlogInfo(context,GData.Userlog);
    }
    public static boolean Update_Userlog_Status(Context context,byte sta,UserlogInfo ulog, int oldStatus,byte newStatus) {
        UserlogInfo updateU=UserMgr.NEW_Userlog();
        boolean found=false;
        Integer index=0;
        for (UserlogInfo u:GData.Userlog) {
            if((u.getStationId()==sta)&&(u.getUserId()==ulog.getUserId())&&((u.getStatus()==UserMgr.uStatus.Login)||(u.getStatus()==UserMgr.uStatus.Breaking))){
                updateU=u;

                found=true;
                break;
            }
            index++;
        }
        if(found){
            if (GData.Userlog.size() > 0 && (index < GData.Userlog.size())) {
                updateU.setStopDateTime(DateTime.GetDateTimeNow());
                updateU.setStatus(newStatus);
                GData.Userlog.set(index, updateU);
            }
            return LogMgr.Save_UserlogInfo(context, GData.Userlog);
        }

        return false;
    }

    //---Counterlog
    public static void add_counterlog(Context context,CounterlogInfo clogInfo) {
        GData.Counterlog.add(clogInfo);
        LogMgr.Save_CounterlogInfo(context,GData.Counterlog);
    }
    public static boolean Update_Counterlog_Status(Context context,byte sta, CounterlogInfo clog, byte newStatus) {
        CounterlogInfo updateC=CounterMgr.NEW_Counterlog();
        boolean found=false;
        Integer index=0;
        for (CounterlogInfo c:GData.Counterlog) {
            if((c.getStationId()==sta)&&((c.getStatus()==UserMgr.uStatus.Login)||(c.getStatus()==UserMgr.uStatus.Breaking))){
                updateC=c;

                found=true;
                break;
            }
            index++;
        }
        if(found){
            if (GData.Counterlog.size() > 0 && (index < GData.Counterlog.size())) {
                updateC.setStopDateTime(DateTime.GetDateTimeNow());
                updateC.setStatus(newStatus);
                GData.Counterlog.set(index, updateC);
            }
            return LogMgr.Save_CounterlogInfo(context, GData.Counterlog);
        }

        return false;
    }

    //---CurrentStation
    public static boolean Update_CurrentStation_Status(Context context,byte sta, CurrentStationInfo clog) {
        CurrentStationInfo updateC=CounterMgr.NEW_CurrentStationInfo();
        boolean found=false;
        Integer index=0;
        for (CurrentStationInfo c:GData.CurStation) {
            if((c.getId()==sta)){
                updateC=clog;
                found=true;
                break;
            }
            index++;
        }
        if(found){
            if (GData.CurStation.size() > 0 && (index < GData.CurStation.size())) {
                GData.CurStation.set(index, updateC);
            }
            return LogMgr.Save_CurrentStationInfo(context, GData.CurStation);
        }

        return false;
    }

    //---Branch info
    public  static boolean Save_BranchInfo(Context context, List<BranchInfo> infos) {
        Type type=new TypeToken<List<BranchInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.BranchInfo_JSON),json);
    }
    public static List<BranchInfo> Load_BranchInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.BranchInfo_JSON));
        Type type=new TypeToken<List<BranchInfo>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<BranchInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<BranchInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_BranchInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.BranchInfo_JSON));
    }
    //---Div info
    public  static boolean Save_DivInfo(Context context, List<DivInfo> infos) {
        Type type=new TypeToken<List<DivInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.DivInfo_JSON),json);
    }
    public static List<DivInfo> Load_DivInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.DivInfo_JSON));
        Type type=new TypeToken<List<DivInfo>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<DivInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<DivInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_DivInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.DivInfo_JSON));
    }
    //---LangDiv info
    public  static boolean Save_LangDivInfo(Context context, List<LangDivInfo> infos) {
        Type type=new TypeToken<List<LangDivInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.LangDivInfo_JSON),json);
    }
    public static List<LangDivInfo> Load_LangDivInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.LangDivInfo_JSON));
        Type type=new TypeToken<List<LangDivInfo>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<LangDivInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<LangDivInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_LangDivInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.LangDivInfo_JSON));
    }
    //---Sta info
    public  static boolean Save_StaInfo(Context context, List<StaInfo> infos) {
        Type type=new TypeToken<List<StaInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.StaInfo_JSON),json);
    }
    public static List<StaInfo> Load_StaInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.StaInfo_JSON));
        Type type=new TypeToken<List<StaInfo>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<StaInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<StaInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_StaInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.StaInfo_JSON));
    }
    //---Break info
    public  static boolean Save_BreakInfo(Context context, List<BreakInfo> infos) {
        Type type=new TypeToken<List<BreakInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.BreakInfo_JSON),json);
    }
    public static List<BreakInfo> Load_BreakInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.BreakInfo_JSON));
        Type type=new TypeToken<List<BreakInfo>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<BreakInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<BreakInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_BreakInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.BreakInfo_JSON));
    }
    //---Grp info
    public  static boolean Save_GrpInfo(Context context, List<GrpInfo> infos) {
        Type type=new TypeToken<List<GrpInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.GrpInfo_JSON),json);
    }
    public static List<GrpInfo> Load_GrpInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.GrpInfo_JSON));
        Type type=new TypeToken<List<GrpInfo>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<GrpInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<GrpInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_GrpInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.GrpInfo_JSON));
    }
    //---User info
    public  static boolean Save_UserInfo(Context context, List<UserInfo> infos) {
        Type type=new TypeToken<List<UserInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.UserInfo_JSON),json);
    }
    public static List<UserInfo> Load_UserInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.UserInfo_JSON));
        Type type=new TypeToken<List<UserInfo>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<UserInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<UserInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_UserInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.UserInfo_JSON));
    }
    //---Employee info
    public  static boolean Save_EmployeeInfo(Context context, List<EmployeeInfo> infos) {
        Type type=new TypeToken<List<EmployeeInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.EmployeeInfo_JSON),json);
    }
    public static List<EmployeeInfo> Load_EmployeeInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.EmployeeInfo_JSON));
        Type type=new TypeToken<List<EmployeeInfo>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<EmployeeInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<EmployeeInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_EmployeeInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.EmployeeInfo_JSON));
    }
    //---DivisionAdv info
    public  static boolean Save_DivisionAdvInfo(Context context, List<DivisionAdvInfo> infos) {
        Type type=new TypeToken<List<DivisionAdvInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.DivisionAdvInfo_JSON),json);
    }
    public static List<DivisionAdvInfo> Load_DivisionAdvInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.DivisionAdvInfo_JSON));
        Type type=new TypeToken<List<DivisionAdvInfo>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<DivisionAdvInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<DivisionAdvInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_DivisionAdvInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.DivisionAdvInfo_JSON));
    }
    //---PF_OTHER info
    public  static boolean Save_PF_OTHERInfo(Context context, List<PF_OTHER> infos) {
        Type type=new TypeToken<List<PF_OTHER>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_OTHER_JSON),json);
    }
    public static List<PF_OTHER> Load_PF_OTHERInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_OTHER_JSON));
        Type type=new TypeToken<List<PF_OTHER>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<PF_OTHER> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<PF_OTHER> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_PF_OTHERInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_OTHER_JSON));
    }
    //---PF_DIVISION info
    public  static boolean Save_PF_DIVISIONInfo(Context context, List<PF_DIVISION> infos) {
        Type type=new TypeToken<List<PF_DIVISION>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_DIVISION_JSON),json);
    }
    public static List<PF_DIVISION> Load_PF_DIVISIONInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_DIVISION_JSON));
        Type type=new TypeToken<List<PF_DIVISION>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<PF_DIVISION> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<PF_DIVISION> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_PF_DIVISIONInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_DIVISION_JSON));
    }
    //---PF_AUTOTRANSFER info
    public  static boolean Save_PF_AUTOTRANSFERInfo(Context context, List<PF_AUTOTRANSFER> infos) {
        Type type=new TypeToken<List<PF_AUTOTRANSFER>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_AUTOTRANSFER_JSON),json);
    }
    public static List<PF_AUTOTRANSFER> Load_PF_AUTOTRANSFERInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_AUTOTRANSFER_JSON));
        Type type=new TypeToken<List<PF_AUTOTRANSFER>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<PF_AUTOTRANSFER> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<PF_AUTOTRANSFER> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_PF_AUTOTRANSFERInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_AUTOTRANSFER_JSON));
    }
    //---PF_DIVMAP info
    public  static boolean Save_PF_DIVMAPInfo(Context context, List<PF_DIVMAP> infos) {
        Type type=new TypeToken<List<PF_DIVMAP>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_DIVMAP_JSON),json);
    }
    public static List<PF_DIVMAP> Load_PF_DIVMAPInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_DIVMAP_JSON));
        Type type=new TypeToken<List<PF_DIVMAP>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<PF_DIVMAP> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<PF_DIVMAP> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_PF_DIVMAPInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_DIVMAP_JSON));
    }
    //---PF_STAMAP info
    public  static boolean Save_PF_STAMAPInfo(Context context, List<PF_STAMAP> infos) {
        Type type=new TypeToken<List<PF_STAMAP>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_STAMAP_JSON),json);
    }
    public static List<PF_STAMAP> Load_PF_STAMAPInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_STAMAP_JSON));
        Type type=new TypeToken<List<PF_STAMAP>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<PF_STAMAP> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<PF_STAMAP> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_PF_STAMAPInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_STAMAP_JSON));
    }

    //---PF_ALARMGROUP info
    public  static boolean Save_PF_ALARMGROUPInfo(Context context, List<PF_ALARMGROUP> infos) {
        Type type=new TypeToken<List<PF_ALARMGROUP>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_ALARMGROUP_JSON),json);
    }
    public static List<PF_ALARMGROUP> Load_PF_ALARMGROUPInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_ALARMGROUP_JSON));
        Type type=new TypeToken<List<PF_ALARMGROUP>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<PF_ALARMGROUP> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<PF_ALARMGROUP> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_PF_ALARMGROUPInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.PF_ALARMGROUP_JSON));
    }

    //---BranchStatus info
    public  static boolean Save_BranchStatusInfo(Context context, List<BranchStatusInfo> infos) {
        Type type=new TypeToken<List<BranchStatusInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.DataInfo), FileName.BranchStatus_JSON),json);
    }
    public static List<BranchStatusInfo> Load_BranchStatusInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.DataInfo), FileName.BranchStatus_JSON));
        Type type=new TypeToken<List<BranchStatusInfo>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<BranchStatusInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<BranchStatusInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_BranchStatusInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.DataInfo), FileName.BranchStatus_JSON));
    }
    //---QLaunchingInfo
    public  static boolean Save_QLaunchingInfo(Context context, List<QLaunchingInfo> infos) {
        Type type=new TypeToken<List<QLaunchingInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.QLaunchingInfo_JSON),json);
    }
    public static List<QLaunchingInfo> Load_QLaunchingInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.QLaunchingInfo_JSON));
        Type type=new TypeToken<List<QLaunchingInfo>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<QLaunchingInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<QLaunchingInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_QLaunchingInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.QLaunchingInfo_JSON));
    }

    //---Queue info
    public  static boolean Save_QueueInfo(Context context, List<QueueInfo> infos) {
        Type type=new TypeToken<List<QueueInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.Queue_JSON),json);
    }
    public static List<QueueInfo> Load_QueueInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.Queue_JSON));
        Type type=new TypeToken<List<QueueInfo>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<QueueInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<QueueInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_QueueInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.Queue_JSON));
    }

    //---Queuelog info
    public  static boolean Save_QueuelogInfo(Context context, List<QueuelogInfo> infos) {
        Type type=new TypeToken<List<QueuelogInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.Queuelog_JSON),json);
    }
    public static List<QueuelogInfo> Load_QueuelogInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.Queuelog_JSON));
        Type type=new TypeToken<List<QueuelogInfo>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<QueuelogInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<QueuelogInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_QueuelogInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.Queuelog_JSON));
    }

    //---Userlog info
    public  static boolean Save_UserlogInfo(Context context, List<UserlogInfo> infos) {
        Type type=new TypeToken<List<UserlogInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.Userlog_JSON),json);
    }
    public static List<UserlogInfo> Load_UserlogInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.Userlog_JSON));
        Type type=new TypeToken<List<UserlogInfo>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<UserlogInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<UserlogInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_UserlogInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.Userlog_JSON));
    }

    //---Counterlog info
    public  static boolean Save_CounterlogInfo(Context context, List<CounterlogInfo> infos) {
        Type type=new TypeToken<List<CounterlogInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.Counterlog_JSON),json);
    }
    public static List<CounterlogInfo> Load_CounterlogInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.Counterlog_JSON));
        Type type=new TypeToken<List<CounterlogInfo>>(){}.getType();
        if(!strData.equals(""))
        {
            Gson gson=new Gson();
            List<CounterlogInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<CounterlogInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_CounterlogInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.Counterlog_JSON));
    }
    //---CurrentStationInfo
    public  static boolean Save_CurrentStationInfo(Context context, List<CurrentStationInfo> infos) {
        Type type=new TypeToken<List<CurrentStationInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.CurrentStationInfo_JSON),json);
    }
    public static List<CurrentStationInfo> Load_CurrentStationInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.CurrentStationInfo_JSON));
        Type type=new TypeToken<List<CurrentStationInfo>>(){}.getType();
        if(!strData.equals("")) {
            Gson gson=new Gson();
            List<CurrentStationInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<CurrentStationInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_CurrentStationInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.CurrentStationInfo_JSON));
    }
    //---CurrentDivisionInfo
    public  static boolean Save_CurrentDivisionInfo(Context context, List<CurrentDivisionInfo> infos) {
        Type type=new TypeToken<List<CurrentDivisionInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.CurrentDivisionInfo_JSON),json);
    }
    public static List<CurrentDivisionInfo> Load_CurrentDivisionInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.CurrentDivisionInfo_JSON));
        Type type=new TypeToken<List<CurrentDivisionInfo>>(){}.getType();
        if(!strData.equals("")) {
            Gson gson=new Gson();
            List<CurrentDivisionInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<CurrentDivisionInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_CurrentDivisionInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.CurrentDivisionInfo_JSON));
    }
    //---CurrentGroupInfo
    public  static boolean Save_CurrentGroupInfo(Context context, List<CurrentGroupInfo> infos) {
        Type type=new TypeToken<List<CurrentGroupInfo>>(){}.getType();
        Gson gson=new Gson();
        String json=gson.toJson(infos,type);
        return ExternalStorage.writeFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.CurrentGroupInfo_JSON),json);
    }
    public static List<CurrentGroupInfo> Load_CurrentGroupInfo(Context context) {
        String strData=ExternalStorage.readFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.CurrentGroupInfo_JSON));
        Type type=new TypeToken<List<CounterlogInfo>>(){}.getType();
        if(!strData.equals("")) {
            Gson gson=new Gson();
            List<CurrentGroupInfo> listLog=gson.fromJson(strData,type);
            return  listLog;
        }
        List<CurrentGroupInfo> list=new ArrayList<>();
        return list;
    }
    public static boolean Delete_CurrentGroupInfo(Context context){
        return ExternalStorage.deleteFile(new File(context.getExternalFilesDir(FolderPath.MASTER), FileName.CurrentGroupInfo_JSON));
    }
}
