package th.co.infinitecorp.www.qcontroller.DataInfo;

import java.util.List;

import th.co.infinitecorp.www.qcontroller.DataInfo.Mapping.DivMapGroupInfo;

public class CounterStatusInfo {
    byte group_id;
    byte counterStatus;
    byte qStatus;
    int user_id;
    short waitQ;
    short holdQ;
    String groupName;
    String nameUser;


    public CounterStatusInfo() {
    }
    public CounterStatusInfo(byte group_id, byte counterStatus, byte qStatus, int user_id, short waitQ, short holdQ, String groupName, String nameUser) {
        this.group_id = group_id;
        this.counterStatus = counterStatus;
        this.qStatus = qStatus;
        this.user_id = user_id;
        this.waitQ = waitQ;
        this.holdQ = holdQ;
        this.groupName = groupName;
        this.nameUser = nameUser;
    }

    public byte getGroup_id() {
        return group_id;
    }

    public void setGroup_id(byte group_id) {
        this.group_id = group_id;
    }

    public byte getCounterStatus() {
        return counterStatus;
    }

    public void setCounterStatus(byte counterStatus) {
        this.counterStatus = counterStatus;
    }

    public byte getqStatus() {
        return qStatus;
    }

    public void setqStatus(byte qStatus) {
        this.qStatus = qStatus;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }


}
