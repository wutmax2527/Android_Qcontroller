package th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW;

public class DivisionInfo {
    Integer id;
    short qLaunching;
    byte  alphabet;
    byte  qType;
    short qBegin;
    short qEnd;
    short alarmWaitQ;
    short alarmWaitTi;
    short avgServTime;
    short waitQ;
    short servedQ;
    String divName;
    byte nCopies;
    byte transferTo;
    byte soundSource;

    public DivisionInfo()
    {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getqLaunching() {
        return qLaunching;
    }

    public void setqLaunching(short qLaunching) {
        this.qLaunching = qLaunching;
    }

    public byte getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(byte alphabet) {
        this.alphabet = alphabet;
    }

    public byte getqType() {
        return qType;
    }

    public void setqType(byte qType) {
        this.qType = qType;
    }

    public short getqBegin() {
        return qBegin;
    }

    public void setqBegin(short qBegin) {
        this.qBegin = qBegin;
    }

    public short getqEnd() {
        return qEnd;
    }

    public void setqEnd(short qEnd) {
        this.qEnd = qEnd;
    }

    public short getAlarmWaitQ() {
        return alarmWaitQ;
    }

    public void setAlarmWaitQ(short alarmWaitQ) {
        this.alarmWaitQ = alarmWaitQ;
    }

    public short getAlarmWaitTi() {
        return alarmWaitTi;
    }

    public void setAlarmWaitTi(short alarmWaitTi) {
        this.alarmWaitTi = alarmWaitTi;
    }

    public short getAvgServTime() {
        return avgServTime;
    }

    public void setAvgServTime(short avgServTime) {
        this.avgServTime = avgServTime;
    }

    public short getWaitQ() {
        return waitQ;
    }

    public void setWaitQ(short waitQ) {
        this.waitQ = waitQ;
    }

    public short getServedQ() {
        return servedQ;
    }

    public void setServedQ(short servedQ) {
        this.servedQ = servedQ;
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }

    public byte getnCopies() {
        return nCopies;
    }

    public void setnCopies(byte nCopies) {
        this.nCopies = nCopies;
    }

    public byte getTransferTo() {
        return transferTo;
    }

    public void setTransferTo(byte transferTo) {
        this.transferTo = transferTo;
    }

    public byte getSoundSource() {
        return soundSource;
    }

    public void setSoundSource(byte soundSource) {
        this.soundSource = soundSource;
    }
}
