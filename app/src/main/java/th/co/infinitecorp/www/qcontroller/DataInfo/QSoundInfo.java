package th.co.infinitecorp.www.qcontroller.DataInfo;

public class QSoundInfo {
    byte id;
    QInfo qStart;
    QInfo  qEnd;
    byte staNo=0x00;
    byte soundType=0x00;
    byte times=0x00;

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public QInfo getqStart() {
        return qStart;
    }

    public void setqStart(QInfo qStart) {
        this.qStart = qStart;
    }

    public QInfo getqEnd() {
        return qEnd;
    }

    public void setqEnd(QInfo qEnd) {
        this.qEnd = qEnd;
    }

    public byte getStaNo() {
        return staNo;
    }

    public void setStaNo(byte staNo) {
        this.staNo = staNo;
    }

    public byte getSoundType() {
        return soundType;
    }

    public void setSoundType(byte soundType) {
        this.soundType = soundType;
    }

    public byte getTimes() {
        return times;
    }

    public void setTimes(byte times) {
        this.times = times;
    }
}
