package th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW;

public class UserlogInfo {
    String startDateTime;
    String stopDateTime;
    Integer userId;
    byte stationId;
    byte groupId;
    byte status;

    public UserlogInfo()
    {

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
}
