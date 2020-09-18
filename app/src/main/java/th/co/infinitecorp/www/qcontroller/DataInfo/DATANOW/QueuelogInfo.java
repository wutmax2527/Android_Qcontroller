package th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW;

public class QueuelogInfo {
    String inDateTime;
    String stDateTime;
    String endDateTime;
    String holdDateTime;
    String queueNo;
    Integer userId;

    Integer  estWaitSec;
    Integer  servesec;
    Integer  waitSec;
    Integer holdSec;

    Short waitq;

    byte lang;
    byte tranno;
    byte qDiv;
    byte stationId;
    byte groupId;
    byte pvDiv;
    byte subdivId;
    byte score;
    char status;

    public  QueuelogInfo()
    {

    }

    public String getInDateTime() {
        return inDateTime;
    }

    public void setInDateTime(String inDateTime) {
        this.inDateTime = inDateTime;
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

    public String getHoldDateTime() {
        return holdDateTime;
    }

    public void setHoldDateTime(String holdDateTime) {
        this.holdDateTime = holdDateTime;
    }

    public String getQueueNo() {
        return queueNo;
    }

    public void setQueueNo(String queueNo) {
        this.queueNo = queueNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEstWaitSec() {
        return estWaitSec;
    }

    public void setEstWaitSec(Integer estWaitSec) {
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

    public Short getWaitq() {
        return waitq;
    }

    public void setWaitq(Short waitq) {
        this.waitq = waitq;
    }

    public byte getLang() {
        return lang;
    }

    public void setLang(byte lang) {
        this.lang = lang;
    }

    public byte getTranno() {
        return tranno;
    }

    public void setTranno(byte tranno) {
        this.tranno = tranno;
    }

    public byte getqDiv() {
        return qDiv;
    }

    public void setqDiv(byte qDiv) {
        this.qDiv = qDiv;
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

    public byte getSubdivId() {
        return subdivId;
    }

    public void setSubdivId(byte subdivId) {
        this.subdivId = subdivId;
    }

    public byte getScore() {
        return score;
    }

    public void setScore(byte score) {
        this.score = score;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }
}
