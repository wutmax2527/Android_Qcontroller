package th.co.infinitecorp.www.qcontroller.DataInfo;

public class DateTimeInfo {
    int year;
    byte month;
    byte day;
    byte hour;
    byte minute;
    byte sec;

    public DateTimeInfo(int year, byte month, byte day, byte hour, byte minute, byte sec) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.sec = sec;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public byte getMonth() {
        return month;
    }

    public void setMonth(byte month) {
        this.month = month;
    }

    public byte getDay() {
        return day;
    }

    public void setDay(byte day) {
        this.day = day;
    }

    public byte getHour() {
        return hour;
    }

    public void setHour(byte hour) {
        this.hour = hour;
    }

    public byte getMinute() {
        return minute;
    }

    public void setMinute(byte minute) {
        this.minute = minute;
    }

    public byte getSec() {
        return sec;
    }

    public void setSec(byte sec) {
        this.sec = sec;
    }
}
