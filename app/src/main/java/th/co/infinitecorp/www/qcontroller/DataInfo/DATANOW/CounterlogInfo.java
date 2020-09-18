package th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW;

import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;

public class CounterlogInfo {

    String startDateTime;
    String stopDateTime;
    Integer userId;
    byte stationId;
    byte groupId;
    byte status;

    QInfo qInfo;
    byte qStatus;

    public CounterlogInfo(){

    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getStopDateTime() {
        return stopDateTime;
    }

    public void setStopDateTime(String stopDateTime) {
        this.stopDateTime = stopDateTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public byte getStationId() {
        return stationId;
    }

    public void setStationId(byte stationId) {
        this.stationId = stationId;
    }

    public byte getGroupId() {
        return groupId;
    }

    public void setGroupId(byte groupId) {
        this.groupId = groupId;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public QInfo getqInfo() {
        return qInfo;
    }

    public void setqInfo(QInfo qInfo) {
        this.qInfo = qInfo;
    }

    public byte getqStatus() {
        return qStatus;
    }

    public void setqStatus(byte qStatus) {
        this.qStatus = qStatus;
    }
}
