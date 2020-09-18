package th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW;

import java.util.List;

import th.co.infinitecorp.www.qcontroller.DataInfo.Mapping.DivMapGroupInfo;

public class CurrentStationInfo {
    Integer id;
    Integer userId;
    String name;
    short staWaitQ;
    short waitQ;
    short holdQ;
    short transferQ;
    short staGangOpen;
    byte groupId;
    byte status;

    QueueInfo queueInfo;
    List<DivMapGroupInfo> divMapGroup;

    public CurrentStationInfo(){
        groupId=1;
        transferQ=0;
        staGangOpen=0;
        name="";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public short getTransferQ() {
        return transferQ;
    }

    public void setTransferQ(short transferQ) {
        this.transferQ = transferQ;
    }

    public short getStaGangOpen() {
        return staGangOpen;
    }

    public void setStaGangOpen(short staGangOpen) {
        this.staGangOpen = staGangOpen;
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

    public List<DivMapGroupInfo> getDivMapGroup() {
        return divMapGroup;
    }

    public void setDivMapGroup(List<DivMapGroupInfo> divMapGroup) {
        this.divMapGroup = divMapGroup;
    }
}
