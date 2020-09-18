package th.co.infinitecorp.www.qcontroller.DataInfo;

public class QInfo {
    byte qType=0x00;
    byte qAlp=0x00;
    short qNum=0x00;

    public byte getqType() {
        return qType;
    }

    public void setqType(byte qType) {
        this.qType = qType;
    }

    public byte getqAlp() {
        return qAlp;
    }

    public void setqAlp(byte qAlp) {
        this.qAlp = qAlp;
    }

    public short getqNum() {
        return qNum;
    }

    public void setqNum(short qNum) {
        this.qNum = qNum;
    }

    /*Queue Type*/
    public static class QType
    {
        final public static byte NONE = 0x00;
        final public static byte Type_4DG = 0x01;
        final public static byte Type_3DG = 0x02;
        final public static byte Type_NoZero = 0x03;
    }


}
