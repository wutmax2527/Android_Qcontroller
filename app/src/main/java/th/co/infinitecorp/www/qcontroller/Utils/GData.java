package th.co.infinitecorp.www.qcontroller.Utils;

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
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.DivisionInfo;
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
import th.co.infinitecorp.www.qcontroller.DataInfo.Mapping.DivMapGroupInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.Mapping.StaMapGroupInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.PeriperalInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.QSoundInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.QTicketInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.DisplayInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.PlaylistInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.QDisplayInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.StationReserveInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.TcpSocketInfo;

public class GData {

    public static Integer SERVE_ID=0;
    public static Integer Branch_ID=99997;
    public static String TARGET_SERVER="10.172.100.107";//"10.172.100.51";

    public static boolean IsShowThredRun=false;

    /*Profile Info*/
    public static List<BranchInfo> branchInfos;
    public static List<DivInfo> divInfos;
    public static List<LangDivInfo> langDivInfos;
    public static List<StaInfo> staInfos;
    public static List<BreakInfo> breakInfos;
    public static List<GrpInfo> grpInfos;
    public static List<UserInfo> userInfos;
    public static List<EmployeeInfo> employeeInfos;
    public static List<DivisionAdvInfo> divisionAdvInfos;
    public static List<PF_OTHER> pf_others;
    public static List<PF_DIVISION> pf_divisions;
    public static List<PF_AUTOTRANSFER> pf_autotransfers;
    public static List<PF_DIVMAP> pf_divmaps;
    public static List<PF_STAMAP> pf_stamaps;
    public static List<PF_ALARMGROUP> pf_alarmgroups;

    /*Mapping Info*/
    public static List<StaMapGroupInfo> StaMapGroupInfos;
    public static List<DivMapGroupInfo> DivMapGroupInfos;

    /*Current Log Info*/
    public static List<BranchStatusInfo> branchStatusInfos;
    public static List<DivisionInfo> Division;
    public static List<QLaunchingInfo> QLaunching;
    public static List<QueueInfo> Queue;
    public static List<QueuelogInfo> Queuelog;
    public static List<UserlogInfo> Userlog;
    public static List<CounterlogInfo> Counterlog;
    public static List<PeriperalInfo> PeriperalInfos=new ArrayList<>();
    public static List<TcpSocketInfo> TcpSocketInfos=new ArrayList<>();
    public static List<CurrentDivisionInfo> CurDiv;
    public static List<CurrentGroupInfo> CurGrp;
    public static List<CurrentStationInfo> CurStation;

    /*Sound Playlist*/
    public static List<PlaylistInfo> playlistInfos=new ArrayList<>();
    public static List<QSoundInfo> qSoundInfos=new ArrayList<>();
    /*Display*/
    public static  List<DisplayInfo> displayInfos=new ArrayList<>();
    /*QDisplay*/
    public static  List<QDisplayInfo> qDisplayInfos=new ArrayList<>();

    /*global variable*/
    public static Integer callAPI_Index=0;
    public static boolean initialFinished=false;

    /*Uart Q-Print&QSound*/
    public static QTicketInfo qTicketInfo=new QTicketInfo();

    /*Reserve Counter fo Calling Q*/
    public static List<StationReserveInfo> stationReserveInfos=new ArrayList<>();
}
