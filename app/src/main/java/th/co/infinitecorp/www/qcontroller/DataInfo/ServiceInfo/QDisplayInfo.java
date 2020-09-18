package th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo;

import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;

public class QDisplayInfo {
    byte id;
    String  ip;
    QInfo qStart;
    QInfo  qEnd;
    byte station_id;

    public QDisplayInfo() {
    }

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

    public byte getStation_id() {
        return station_id;
    }

    public void setStation_id(byte station_id) {
        this.station_id = station_id;
    }
}
