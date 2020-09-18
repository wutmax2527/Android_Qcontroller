package th.co.infinitecorp.www.qcontroller.QClientWeb.Models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QClientWebInfo {
    public enum Protocal{
        none(0),
        NextQ(1),
        ReCallQ(2),
        HoldQ(3),
        CallHoldQ(4),
        EndQ(5),
        TranferQStation(6),
        ReservQ(7),
        ServQ(8),
        Login(9),
        Logoff(10),
        Break(11),
        ChangGroup(12),
        DirectQ(13),
        WalkInQ(14),
        Help(15),
        Login_response(16),
        CancelQ(17),
        ServQ_Ack(18),
        StationProfile(19),
        QueueStatus(20),
        DivisionInfos(21),
        GroupInfo(22),
        TranferQDivision(23),
        BreakingInfo(24),
        HoldReasonInfo(25),
        GroupInfos(26),
        BreakingInfos(27),
        HoldReasonInfos(28),
        StationInfo(29),
        StationInfos(30),
        AddTransaction(31),
        QueueInfo(32),//32
        QueueInfos(33),//33
        HoldQueueInfos(34),//34
        Setting(35),//35
        RequestQ(36),//36
        RequestQ_response(37);//37

        private int value;
        private static Map map = new HashMap<>();

        private Protocal(int value) {
            this.value = value;
        }

        static {
            for (Protocal p : Protocal.values()) {
                map.put(p.value, p);
            }
        }

        public static Protocal valueOf(int protocal) {
            return (Protocal) map.get(protocal);
        }

        public int getValue() {
            return value;
        }

    }
    public enum ePriority {
        none,
        First,
        Realtime,
        Last,
    }
    public static class NextQ {
        public int ProtocolID;
        public int StationID;

        public int getProtocolID() {
            return Protocal.NextQ.ordinal();
        }

        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }
    }
    public static class ReCallQ {
        public int ProtocolID;
        public int StationID;

        public ReCallQ() {
            ProtocolID=Protocal.ReCallQ.ordinal();
        }

        public int getProtocolID() {
            return Protocal.ReCallQ.ordinal();
        }

        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }
    }
    public static class HoldQ {
        public int ProtocolID;
        public int StationID;

        public HoldQ() {
            ProtocolID=Protocal.HoldQ.ordinal();
        }

        public int getProtocolID() {
            return Protocal.HoldQ.ordinal();
        }

        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }
    }
    public static class CallHoldQ {
        public int ProtocolID;
        public int StationID;

        public CallHoldQ() {
            ProtocolID=Protocal.CallHoldQ.ordinal();
        }

        public int getProtocolID() {
        return Protocal.CallHoldQ.ordinal();
        }

        public int getStationID() {
        return StationID;
        }

        public void setStationID(int stationID) {
        StationID = stationID;
        }
    }
    public static class EndQ {
        public int ProtocolID;
        public int StationID;

        public EndQ() {
            ProtocolID=Protocal.EndQ.ordinal();
        }

        public int getProtocolID() {
            return Protocal.EndQ.ordinal();
        }

        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }
    }
    public static class TranferQStation {
        public int ProtocolID;
        public int StationID;
        public int ToStationID;
        public ePriority PriorityQ;

        public TranferQStation() {
            ProtocolID=Protocal.TranferQStation.ordinal();
        }

        public int getProtocolID() {
            return Protocal.TranferQStation.ordinal();
        }


        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }

        public int getToStationID() {
            return ToStationID;
        }

        public void setToStationID(int toStationID) {
            ToStationID = toStationID;
        }

        public ePriority getPriorityQ() {
            return PriorityQ;
        }

        public void setPriorityQ(ePriority priorityQ) {
            PriorityQ = priorityQ;
        }
    }
    public static class TranferQDivision {
        public int ProtocolID;
        public int StationID;
        public int ToDivID;
        public ePriority PriorityQ;

        public TranferQDivision() {
            ProtocolID=Protocal.TranferQDivision.ordinal();
        }

        public int getProtocolID() {
            return Protocal.TranferQDivision.ordinal();
        }

        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }

        public int getToDivID() {
            return ToDivID;
        }

        public void setToDivID(int toDivID) {
            ToDivID = toDivID;
        }

        public ePriority getPriorityQ() {
            return PriorityQ;
        }

        public void setPriorityQ(ePriority priorityQ) {
            PriorityQ = priorityQ;
        }
    }
    public static class ReservQ {
        public int ProtocolID;
        public int StationID;
        public boolean Reserv;

        public ReservQ() {
            ProtocolID=Protocal.ReservQ.ordinal();
        }

        public int getProtocolID() {
            return Protocal.ReservQ.ordinal();
        }
        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }

        public boolean isReserv() {
            return Reserv;
        }

        public void setReserv(boolean reserv) {
            Reserv = reserv;
        }
    }
    public static class ServQ {
        public int ProtocolID;
        public int StationID;
        public String QueueNo;
        public String Tel;
        public String Ref1;
        public String Ref2;
        public String Ref3;
        public ServQ() {
            ProtocolID=Protocal.ServQ.ordinal();
            StationID=0;
            QueueNo="";
            Tel="";
            Ref1="";
            Ref2="";
            Ref3="";
        }
        public int getProtocolID() {
            return Protocal.ServQ.ordinal();
        }

        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }

        public String getQueueNo() {
            return QueueNo;
        }

        public void setQueueNo(String queueNo) {
            QueueNo = queueNo;
        }

        public String getTel() {
            return Tel;
        }

        public void setTel(String tel) {
            Tel = tel;
        }

        public String getRef1() {
            return Ref1;
        }

        public void setRef1(String ref1) {
            Ref1 = ref1;
        }

        public String getRef2() {
            return Ref2;
        }

        public void setRef2(String ref2) {
            Ref2 = ref2;
        }

        public String getRef3() {
            return Ref3;
        }

        public void setRef3(String ref3) {
            Ref3 = ref3;
        }
    }
    public static class ServQ_Ack{
        public int ProtocolID;
        public int StationID;
        public String QueueNo;

        public ServQ_Ack() {
            ProtocolID=Protocal.ServQ_Ack.ordinal();
        }

        public int getProtocolID() {
            return Protocal.ServQ_Ack.ordinal();
        }


        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }

        public String getQueueNo() {
            return QueueNo;
        }

        public void setQueueNo(String queueNo) {
            QueueNo = queueNo;
        }
    }
    public static class Login {
        public int ProtocolID;
        public int StationID;
        public String UserLogin;
        public String PasswordLogin;

        public Login() {
            ProtocolID=Protocal.Login.ordinal();
        }

        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }

        public String getUserLogin() {
            return UserLogin;
        }

        public void setUserLogin(String userLogin) {
            UserLogin = userLogin;
        }

        public String getPasswordLogin() {
            return PasswordLogin;
        }

        public void setPasswordLogin(String passwordLogin) {
            PasswordLogin = passwordLogin;
        }
    }
    public static class Login_response {
        public int ProtocolID;
        public int StationID;
        public boolean Status;
        public String UserName;
        public String UserID;
        public String Description;

        public Login_response() {
            ProtocolID=Protocal.Login_response.ordinal();
            Status=false;
            UserName="";
            UserID="";
            Description="";
        }

        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }

        public boolean isStatus() {
            return Status;
        }

        public void setStatus(boolean status) {
            Status = status;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public String getUserID() {
            return UserID;
        }

        public void setUserID(String userID) {
            UserID = userID;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }
    }
    public static class Logoff {
        public int ProtocolID;
        public int StationID;

        public Logoff() {
            ProtocolID=Protocal.Logoff.ordinal();
            StationID=0;
        }

        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }
    }
    public static class Break {
        public int ProtocolID;
        public int StationID;

        public Break() {
            ProtocolID=Protocal.Break.ordinal();
        }

        public int getProtocolID() {
        return Protocal.Break.ordinal();
        }

        public int getStationID() {
        return StationID;
        }

        public void setStationID(int stationID) {
        StationID = stationID;
        }
    }
    public static class ChangGroup {
        public int ProtocolID;
        public int StationID;
        public int ToGroupID;

        public ChangGroup() {
            ProtocolID=Protocal.ChangGroup.ordinal();
        }

        public int getProtocolID() {
            return Protocal.ChangGroup.ordinal();
        }

        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }

        public int getToGroupID() {
            return ToGroupID;
        }

        public void setToGroupID(int toGroupID) {
            ToGroupID = toGroupID;
        }
    }
    public static class WalkInQ {
        public int ProtocolID;
        public int StationID;
        public int DivID;

        public WalkInQ() {
            ProtocolID=Protocal.WalkInQ.ordinal();
        }

        public int getProtocolID() {
            return Protocal.WalkInQ.ordinal();
        }

        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }

        public int getDivID() {
            return DivID;
        }

        public void setDivID(int divID) {
            DivID = divID;
        }
    }
    public static class DirectQ {
        public int ProtocolID;
        public int StationID;
        public int ServID;

        public DirectQ() {
            ProtocolID=Protocal.DirectQ.ordinal();
        }

        public int getProtocolID() {
            return Protocal.DirectQ.ordinal();
        }

        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }

        public int getServID() {
            return ServID;
        }

        public void setServID(int servID) {
            ServID = servID;
        }
    }
    public static class CancelQ {
        public int ProtocolID;
        public int StationID;
        public int ServID;

        public CancelQ() {
            ProtocolID=Protocal.CancelQ.ordinal();
        }

        public int getProtocolID() {
            return Protocal.CancelQ.ordinal();
        }


        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }

        public int getServID() {
            return ServID;
        }

        public void setServID(int servID) {
            ServID = servID;
        }
    }
    public static class StationProfile {
        public int ProtocolID;
        public int GroupID;
        public String GroupName;

        public StationProfile() {
            ProtocolID=Protocal.StationProfile.ordinal();
            GroupID=0;
            GroupName="";
        }

        public int getGroupID() {
            return GroupID;
        }

        public void setGroupID(int groupID) {
            GroupID = groupID;
        }

        public String getGroupName() {
            return GroupName;
        }

        public void setGroupName(String groupName) {
            GroupName = groupName;
        }
    }
    public static class QueueStatus {
        public int ProtocolID;
        public int WaitingQForGroup;
        public int HoldQForGroup;
        public int TranferQ;
        public int StationGangOpen;
        public String AlertPaper;
        public String AlertQ;
        public String AlertOther;

        public QueueStatus() {
            ProtocolID=Protocal.QueueStatus.ordinal();
            WaitingQForGroup=0;
            HoldQForGroup=0;
            int TranferQ=0;
            int StationGangOpen=0;
            String AlertPaper="";
            String AlertQ="";
            String AlertOther="";
        }

        public int getWaitingQForGroup() {
            return WaitingQForGroup;
        }

        public void setWaitingQForGroup(int waitingQForGroup) {
            WaitingQForGroup = waitingQForGroup;
        }

        public int getHoldQForGroup() {
            return HoldQForGroup;
        }

        public void setHoldQForGroup(int holdQForGroup) {
            HoldQForGroup = holdQForGroup;
        }

        public int getTranferQ() {
            return TranferQ;
        }

        public void setTranferQ(int tranferQ) {
            TranferQ = tranferQ;
        }

        public int getStationGangOpen() {
            return StationGangOpen;
        }

        public void setStationGangOpen(int stationGangOpen) {
            StationGangOpen = stationGangOpen;
        }

        public String getAlertPaper() {
            return AlertPaper;
        }

        public void setAlertPaper(String alertPaper) {
            AlertPaper = alertPaper;
        }

        public String getAlertQ() {
            return AlertQ;
        }

        public void setAlertQ(String alertQ) {
            AlertQ = alertQ;
        }

        public String getAlertOther() {
            return AlertOther;
        }

        public void setAlertOther(String alertOther) {
            AlertOther = alertOther;
        }
    }
    public static class DivisionInfos {
        public int ProtocolID;
        public List<DivisionInfo> Divisions;

        public DivisionInfos() {
            ProtocolID=Protocal.DivisionInfos.ordinal();
        }

        public List<DivisionInfo> getDivisions() {
            return Divisions;
        }

        public void setDivisions(List<DivisionInfo> divisions) {
            Divisions = divisions;
        }
    }
    public static class DivisionInfo{
        public int ID;
        public String Name;
        public int ServQ;
        public int WaitQ;
        public String MaxWaitTime;
        public String NaxtQ;

        public DivisionInfo() {
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public int getServQ() {
            return ServQ;
        }

        public void setServQ(int servQ) {
            ServQ = servQ;
        }

        public int getWaitQ() {
            return WaitQ;
        }

        public void setWaitQ(int waitQ) {
            WaitQ = waitQ;
        }

        public String getMaxWaitTime() {
            return MaxWaitTime;
        }

        public void setMaxWaitTime(String maxWaitTime) {
            MaxWaitTime = maxWaitTime;
        }

        public String getNaxtQ() {
            return NaxtQ;
        }

        public void setNaxtQ(String naxtQ) {
            NaxtQ = naxtQ;
        }
    }
    public static class GroupInfos {
        public int ProtocolID;
        public List<GroupInfo> Groups;

        public GroupInfos() {
            ProtocolID=Protocal.GroupInfos.ordinal();
        }

        public List<GroupInfo> getGroups() {
            return Groups;
        }

        public void setGroups(List<GroupInfo> groups) {
            Groups = groups;
        }
    }
    public static class GroupInfo {
        public int ProtocolID;
        public int ID;
        public String Name;
        public int AvailableCount;
        public int WaitQ;
        public String MaxWaitTime;
        public String LastQ;

        public GroupInfo() {
            ProtocolID=Protocal.GroupInfo.ordinal();
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public int getAvailableCount() {
            return AvailableCount;
        }

        public void setAvailableCount(int availableCount) {
            AvailableCount = availableCount;
        }

        public int getWaitQ() {
            return WaitQ;
        }

        public void setWaitQ(int waitQ) {
            WaitQ = waitQ;
        }

        public String getMaxWaitTime() {
            return MaxWaitTime;
        }

        public void setMaxWaitTime(String maxWaitTime) {
            MaxWaitTime = maxWaitTime;
        }

        public String getLastQ() {
            return LastQ;
        }

        public void setLastQ(String lastQ) {
            LastQ = lastQ;
        }
    }
    public static class BreakingInfos {
        public int ProtocolID;
        public List<BreakingInfo> Breakings;

        public BreakingInfos() {
            ProtocolID=Protocal.BreakingInfos.ordinal();
        }

        public List<BreakingInfo> getBreakings() {
            return Breakings;
        }

        public void setBreakings(List<BreakingInfo> breakings) {
            Breakings = breakings;
        }
    }
    public static class BreakingInfo {
        public int ProtocolID;
        public int ID;
        public String Name;

        public BreakingInfo() {
            ProtocolID=Protocal.BreakingInfo.ordinal();
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }
    }
    public static class HoldReasonInfos {
        public int ProtocolID;
        public List<HoldReasonInfo> HoldReasons;

        public HoldReasonInfos() {
            ProtocolID=Protocal.HoldReasonInfos.ordinal();
        }

        public List<HoldReasonInfo> getHoldReasons() {
            return HoldReasons;
        }

        public void setHoldReasons(List<HoldReasonInfo> holdReasons) {
            HoldReasons = holdReasons;
        }
    }
    public static class HoldReasonInfo {
        public int ProtocolID;
        public int ID;
        public String Name;

        public HoldReasonInfo() {
            ProtocolID=Protocal.HoldReasonInfo.ordinal();
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }
    }
    public static class StationInfos {
        public int ProtocolID;
        public List<StationInfo> Stations;

        public StationInfos() {
            ProtocolID=Protocal.StationInfos.ordinal();
        }

        public List<StationInfo> getStations() {
            return Stations;
        }

        public void setStations(List<StationInfo> stations) {
            Stations = stations;
        }
    }
    public static class StationInfo {
        public int ProtocolID;
        public int ID;
        public String Name;

        public StationInfo() {
            ProtocolID=Protocal.StationInfo.ordinal();
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }
    }
    public static class WaitQueueInfos {
        public int ProtocolID;
        public List<QueueInfo> Queues;

        public WaitQueueInfos() {
            ProtocolID = Protocal.QueueInfos.ordinal();
        }

        public int getProtocolID() {
            return ProtocolID;
        }

        public List<QueueInfo> getQueues() {
            return Queues;
        }

        public void setQueues(List<QueueInfo> queues) {
            Queues = queues;
        }
    }
    public static class HoldQueueInfos {
        public int ProtocolID;
        public List<QueueInfo> Queues;

        public HoldQueueInfos() {
            ProtocolID=Protocal.HoldQueueInfos.ordinal();
        }

        public int getProtocolID() {
            return Protocal.HoldQueueInfos.ordinal();
        }

        public List<QueueInfo> getQueues() {
            return Queues;
        }

        public void setQueues(List<QueueInfo> queues) {
            Queues = queues;
        }
    }
    public static class QueueInfo {
        public int ProtocolID;
        public String QNo;
        public int DivID;
        public String WaitTime;
        public int SevID;
        public String urlPicCustomer;
        public String CustomerName;
        public String Ref1;
        public String Ref2;

        public QueueInfo() {
            ProtocolID=Protocal.QueueInfo.ordinal();
            QNo="0";
            DivID=0;
            WaitTime="00:00:00";
            SevID=0;
            urlPicCustomer="";
            CustomerName="";
            Ref1="";
            Ref2="";
        }

        public String getQNo() {
            return QNo;
        }

        public void setQNo(String QNo) {
            this.QNo = QNo;
        }

        public int getDivID() {
            return DivID;
        }

        public void setDivID(int divID) {
            DivID = divID;
        }

        public String getWaitTime() {
            return WaitTime;
        }

        public void setWaitTime(String waitTime) {
            WaitTime = waitTime;
        }

        public int getSevID() {
            return SevID;
        }

        public void setSevID(int sevID) {
            SevID = sevID;
        }

        public String getUrlPicCustomer() {
            return urlPicCustomer;
        }

        public void setUrlPicCustomer(String urlPicCustomer) {
            this.urlPicCustomer = urlPicCustomer;
        }

        public String getCustomerName() {
            return CustomerName;
        }

        public void setCustomerName(String customerName) {
            CustomerName = customerName;
        }

        public String getRef1() {
            return Ref1;
        }

        public void setRef1(String ref1) {
            Ref1 = ref1;
        }

        public String getRef2() {
            return Ref2;
        }

        public void setRef2(String ref2) {
            Ref2 = ref2;
        }
    }
    public static class AddTransaction {
        public int ProtocolID;
        public int StationID;
        public int DivID ;

        public AddTransaction() {
            ProtocolID=Protocal.AddTransaction.ordinal();
        }

        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }

        public int getDivID() {
            return DivID;
        }

        public void setDivID(int divID) {
            DivID = divID;
        }
    }
    public static class Setting {
        public int ProtocolID;
        public int StationID;
        public boolean IsAutoReserv;

        public Setting() {
            ProtocolID=Protocal.Setting.ordinal();
            IsAutoReserv=false;
        }

        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }

        public boolean isAutoReserv() {
            return IsAutoReserv;
        }

        public void setAutoReserv(boolean autoReserv) {
            IsAutoReserv = autoReserv;
        }
    }
    public static class Help {
        public int ProtocolID;
        public int StationID;
        public String Description;

        public Help() {
            ProtocolID=Protocal.Help.ordinal();
        }

        public int getStationID() {
            return StationID;
        }

        public void setStationID(int stationID) {
            StationID = stationID;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }
    }
    public class TimeSpan {
        private int hours;
        private int minutes;

        // Constructs a time span with the given interval.
        // pre: hours >= 0 && minutes >= 0
        public TimeSpan(int hours, int minutes) {
            this.hours = 0;
            this.minutes = 0;
            add(hours, minutes);
        }

        // Adds the given interval to this time span.
        // pre: hours >= 0 && minutes >= 0
        public void add(int hours, int minutes) {
            this.hours += hours;
            this.minutes += minutes;

            // convert each 60 minutes into one hour
            this.hours += this.minutes / 60;
            this.minutes = this.minutes % 60;
        }

        // Returns whether o is a TimeSpan representing the same
        // number of hours and minutes as this TimeSpan object.
        public boolean equals(Object o) {
            if (o instanceof TimeSpan) {
                TimeSpan other = (TimeSpan) o;
                return hours == other.hours &&
                        minutes == other.minutes;
            } else {   // not a TimeSpan object
                return false;
            }
        }

        // Returns a String for this time span such as "6h15m".
        public String toString() {
            return hours + "h" + minutes + "m";
        }
    }
}
