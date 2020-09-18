package th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW;

public class QueueInfo {
    String reqDateTime;
    String stDateTime;
    String endDateTime;
    String queueNo;
    byte qType;
    byte qAlp;
    short qNum;
    short estWaitSec;
    Integer servid;
    Integer tranno;
    Integer userId;
    Integer  servesec;
    Integer  waitSec;
    Integer holdSec;

    short waitq;


    byte lang;
    byte divisionId;
    byte stationId;
    byte groupId;
    byte pvDiv;
    byte score;
    byte status;
    byte subdivId;
    byte staOpen;
    public QueueInfo()
    {

    }

    public String getReqDateTime() {
        return reqDateTime;
    }

    public void setReqDateTime(String reqDateTime) {
        this.reqDateTime = reqDateTime;
    }

    public String getStDateTime() {
        return stDateTime;
    }

    public void setStDateTime(String stDateTime) {
        this.stDateTime = stDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getQueueNo() {
        return queueNo;
    }

    public void setQueueNo(String queueNo) {
        this.queueNo = queueNo;
    }

    public byte getqType() {
        return qType;
    }

    public void setqType(byte qType) {
        this.qType = qType;
    }

    public byte getqAlp() {
        return qAlp;
    }

    public void setqAlp(byte qAlp) {
        this.qAlp = qAlp;
    }

    public short getqNum() {
        return qNum;
    }

    public void setqNum(short qNum) {
        this.qNum = qNum;
    }

    public void setEstWaitSec(short estWaitSec) {
        this.estWaitSec = estWaitSec;
    }

    public Integer getServid() {
        return servid;
    }

    public void setServid(Integer servid) {
        this.servid = servid;
    }

    public Integer getTranno() {
        return tranno;
    }

    public void setTranno(Integer tranno) {
        this.tranno = tranno;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Short getEstWaitSec() {
        return estWaitSec;
    }

    public void setEstWaitSec(Short estWaitSec) {
        this.estWaitSec = estWaitSec;
    }

    public Integer getServesec() {
        return servesec;
    }

    public void setServesec(Integer servesec) {
        this.servesec = servesec;
    }

    public Integer getWaitSec() {
        return waitSec;
    }

    public void setWaitSec(Integer waitSec) {
        this.waitSec = waitSec;
    }

    public Integer getHoldSec() {
        return holdSec;
    }

    public void setHoldSec(Integer holdSec) {
        this.holdSec = holdSec;
    }

    public short getWaitq() {
        return waitq;
    }

    public void setWaitq(short waitq) {
        this.waitq = waitq;
    }

    public byte getLang() {
        return lang;
    }

    public void setLang(byte lang) {
        this.lang = lang;
    }

    public byte getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(byte divisionId) {
        this.divisionId = divisionId;
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

    public byte getPvDiv() {
        return pvDiv;
    }

    public void setPvDiv(byte pvDiv) {
        this.pvDiv = pvDiv;
    }

    public byte getScore() {
        return score;
    }

    public void setScore(byte score) {
        this.score = score;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getSubdivId() {
        return subdivId;
    }

    public void setSubdivId(byte subdivId) {
        this.subdivId = subdivId;
    }

    public byte getStaOpen() {
        return staOpen;
    }

    public void setStaOpen(byte staOpen) {
        this.staOpen = staOpen;
    }
}
