package th.co.infinitecorp.www.qcontroller.QTouchWeb.Models;

import java.util.HashMap;
import java.util.Map;

public class QTouchWebInfo {
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
    public static class RequestQ{
        public int ProtocolID;
        public String CardID;
        public int DivID;
        public boolean IsPrint;
        public boolean IsVDOcall;
        public int LangID;
        public String PicData;
        public String Telno;
        public int getProtocolID() {
            return Protocal.RequestQ.ordinal();
        }

        public String getCardID() {
            return CardID;
        }

        public void setCardID(String cardID) {
            CardID = cardID;
        }

        public int getDivID() {
            return DivID;
        }

        public void setDivID(int divID) {
            DivID = divID;
        }

        public boolean isPrint() {
            return IsPrint;
        }

        public void setPrint(boolean print) {
            IsPrint = print;
        }

        public boolean isVDOcall() {
            return IsVDOcall;
        }

        public void setVDOcall(boolean VDOcall) {
            IsVDOcall = VDOcall;
        }

        public int getLangID() {
            return LangID;
        }

        public void setLangID(int langID) {
            LangID = langID;
        }

        public String getPicData() {
            return PicData;
        }

        public void setPicData(String picData) {
            PicData = picData;
        }

        public String getTelno() {
            return Telno;
        }

        public void setTelno(String telno) {
            Telno = telno;
        }
    }
}
