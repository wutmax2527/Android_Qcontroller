package th.co.infinitecorp.www.qcontroller.DataInfo.MASTER;

public class PF_ALARMGROUP {
    Integer BRANCH_ID;
    Integer PROFILE_ID;
    Integer GROUP_ID;
    Integer WAITQALARM;
    Integer WAITTIMEALARM;

    public  PF_ALARMGROUP()
    {

    }

    public Integer getBRANCH_ID() {
        return BRANCH_ID;
    }

    public void setBRANCH_ID(Integer BRANCH_ID) {
        this.BRANCH_ID = BRANCH_ID;
    }

    public Integer getPROFILE_ID() {
        return PROFILE_ID;
    }

    public void setPROFILE_ID(Integer PROFILE_ID) {
        this.PROFILE_ID = PROFILE_ID;
    }

    public Integer getGROUP_ID() {
        return GROUP_ID;
    }

    public void setGROUP_ID(Integer GROUP_ID) {
        this.GROUP_ID = GROUP_ID;
    }

    public Integer getWAITQALARM() {
        return WAITQALARM;
    }

    public void setWAITQALARM(Integer WAITQALARM) {
        this.WAITQALARM = WAITQALARM;
    }

    public Integer getWAITTIMEALARM() {
        return WAITTIMEALARM;
    }

    public void setWAITTIMEALARM(Integer WAITTIMEALARM) {
        this.WAITTIMEALARM = WAITTIMEALARM;
    }
}
