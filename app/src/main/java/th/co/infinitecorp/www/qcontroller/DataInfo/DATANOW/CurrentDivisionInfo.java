package th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW;

public class CurrentDivisionInfo {
    Integer id;
    short waitQ;
    short holdQ;

    public CurrentDivisionInfo(){

    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
