package th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW;

import java.util.List;
import java.util.Queue;

import th.co.infinitecorp.www.qcontroller.DataInfo.Mapping.DivMapGroupInfo;

public class CurrentGroupInfo {
    Integer id;
    String name;
    short waitQ;
    short holdQ;
    short availableCounter;
    public String maxWaitTime;
    public QueueInfo lastQ;
    List<DivMapGroupInfo> divMapGroup;

    public CurrentGroupInfo(){
        availableCounter=0;
        maxWaitTime="00:00:00";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public short getAvailableCounter() {
        return availableCounter;
    }

    public void setAvailableCounter(short availableCounter) {
        this.availableCounter = availableCounter;
    }

    public String getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(String maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public QueueInfo getLastQ() {
        return lastQ;
    }

    public void setLastQ(QueueInfo lastQ) {
        this.lastQ = lastQ;
    }

    public List<DivMapGroupInfo> getDivMapGroup() {
        return divMapGroup;
    }

    public void setDivMapGroup(List<DivMapGroupInfo> divMapGroup) {
        this.divMapGroup = divMapGroup;
    }
}
