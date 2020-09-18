package th.co.infinitecorp.www.qcontroller.DataInfo;

public class BranchStatusInfo {
    Integer id;
    String lastDate;
    String closeTime;
    String lastDateTimeResetQ;
    String lastDateTimeExistApp;

    public  BranchStatusInfo()
    {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getLastDateTimeResetQ() {
        return lastDateTimeResetQ;
    }

    public void setLastDateTimeResetQ(String lastDateTimeResetQ) {
        this.lastDateTimeResetQ = lastDateTimeResetQ;
    }

    public String getLastDateTimeExistApp() {
        return lastDateTimeExistApp;
    }

    public void setLastDateTimeExistApp(String lastDateTimeExistApp) {
        this.lastDateTimeExistApp = lastDateTimeExistApp;
    }
}
