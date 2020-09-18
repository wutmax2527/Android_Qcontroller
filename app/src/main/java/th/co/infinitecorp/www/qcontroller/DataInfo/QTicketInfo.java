package th.co.infinitecorp.www.qcontroller.DataInfo;

import th.co.infinitecorp.www.qcontroller.Utils.DateTime;

public class QTicketInfo {
    byte divId=0x00;
    byte qType=0x00;
    byte qAlp=0x00;
    short qNum=0x00;
    byte copy=0x00;
    short waitQ=0x00;
    short waitTime=0x00;
    short numPrint=0x00;
    byte date;
    byte month;
    byte year;
    byte hour;
    byte  minute;
    byte second;
    byte enableText;
    String text;
    String dateTime;   //yyyy-MM-dd HH:mm:ss
    boolean activePrint=false;
    public byte getDivId() {
        return divId;
    }

    public void setDivId(byte divId) {
        this.divId = divId;
    }

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

    public byte getCopy() {
        return copy;
    }

    public void setCopy(byte copy) {
        this.copy = copy;
    }

    public short getWaitQ() {
        return waitQ;
    }

    public void setWaitQ(short waitQ) {
        this.waitQ = waitQ;
    }

    public short getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(short waitTime) {
        this.waitTime = waitTime;
    }

    public short getNumPrint() {
        return numPrint;
    }

    public void setNumPrint(short numPrint) {
        this.numPrint = numPrint;
    }

    public byte getDate() {
        return date;
    }

    public void setDate(byte date) {
        this.date = date;
    }

    public byte getMonth() {
        return month;
    }

    public void setMonth(byte month) {
        this.month = month;
    }

    public byte getYear() {
        return year;
    }

    public void setYear(byte year) {
        this.year = year;
    }

    public byte getHour() {
        return hour;
    }

    public void setHour(byte hour) {
        this.hour = hour;
    }

    public byte getMinute() {
        return minute;
    }

    public void setMinute(byte minute) {
        this.minute = minute;
    }

    public byte getSecond() {
        return second;
    }

    public void setSecond(byte second) {
        this.second = second;
    }

    public byte getEnableText() {
        return enableText;
    }

    public void setEnableText(byte enableText) {
        this.enableText = enableText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isActivePrint() {
        return activePrint;
    }

    public void setActivePrint(boolean activePrint) {
        this.activePrint = activePrint;
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
