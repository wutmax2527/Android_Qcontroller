package th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo;

import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;

public class DisplayInfo {
    byte id;
    String  ip;
    QInfo qStart;
    QInfo  qEnd;
    byte nBlink;
    byte show;
    byte station_id;
    byte arrowLeft;
    byte arrowRight;
    byte soundType;

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public QInfo getqStart() {
        return qStart;
    }

    public void setqStart(QInfo qStart) {
        this.qStart = qStart;
    }

    public QInfo getqEnd() {
        return qEnd;
    }

    public void setqEnd(QInfo qEnd) {
        this.qEnd = qEnd;
    }

    public byte getnBlink() {
        return nBlink;
    }

    public void setnBlink(byte nBlink) {
        this.nBlink = nBlink;
    }

    public byte getShow() {
        return show;
    }

    public void setShow(byte show) {
        this.show = show;
    }

    public byte getStation_id() {
        return station_id;
    }

    public void setStation_id(byte station_id) {
        this.station_id = station_id;
    }

    public byte getArrowLeft() {
        return arrowLeft;
    }

    public void setArrowLeft(byte arrowLeft) {
        this.arrowLeft = arrowLeft;
    }

    public byte getArrowRight() {
        return arrowRight;
    }

    public void setArrowRight(byte arrowRight) {
        this.arrowRight = arrowRight;
    }

    public byte getSoundType() {
        return soundType;
    }

    public void setSoundType(byte soundType) {
        this.soundType = soundType;
    }
}
