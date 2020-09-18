package th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW;

public class CurrentStationInfo {
    Integer id;
    Integer userId;
    short staWaitQ;
    short waitQ;
    short holdQ;

    byte groupId;
    byte status;

    QueueInfo queueInfo;

    public CurrentStationInfo(){

    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public short getStaWaitQ() {
        return staWaitQ;
    }

    public void setStaWaitQ(short staWaitQ) {
        this.staWaitQ = staWaitQ;
    }

    public short getWaitQ() {
        return waitQ;
    }
    public void setWaitQ(short waitQ) {
        this.waitQ = waitQ;
    }

    public short getHoldQ() {
        return holdQ;
    }

    public void setHoldQ(short holdQ) {
        this.holdQ = holdQ;
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



    public QueueInfo getQueueInfo() {
        return queueInfo;
    }

    public void setQueueInfo(QueueInfo queueInfo) {
        this.queueInfo = queueInfo;
    }
}
