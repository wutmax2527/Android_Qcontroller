package th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW;

public class CurrentDivisionInfo {
    Integer id;
    String name;
    short waitQ;
    short holdQ;
    short servQ;
    public String maxWaitTime;
    public QueueInfo nextQ;

    public CurrentDivisionInfo(){
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

    public short getServQ() {
        return servQ;
    }

    public void setServQ(short servQ) {
        this.servQ = servQ;
    }

    public String getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(String maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public QueueInfo getNextQ() {
        return nextQ;
    }

    public void setNextQ(QueueInfo nextQ) {
        this.nextQ = nextQ;
    }
}
