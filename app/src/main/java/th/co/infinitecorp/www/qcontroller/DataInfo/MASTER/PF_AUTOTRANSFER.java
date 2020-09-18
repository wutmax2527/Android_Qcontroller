package th.co.infinitecorp.www.qcontroller.DataInfo.MASTER;

public class PF_AUTOTRANSFER {
    Integer BRANCH_ID;
    Integer PROFILE_ID;
    Integer DIVISION_ID;
    String TRANSFER_DIVISION_ID;

    public PF_AUTOTRANSFER()
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

    public Integer getDIVISION_ID() {
        return DIVISION_ID;
    }

    public void setDIVISION_ID(Integer DIVISION_ID) {
        this.DIVISION_ID = DIVISION_ID;
    }

    public String getTRANSFER_DIVISION_ID() {
        return TRANSFER_DIVISION_ID;
    }

    public void setTRANSFER_DIVISION_ID(String TRANSFER_DIVISION_ID) {
        this.TRANSFER_DIVISION_ID = TRANSFER_DIVISION_ID;
    }
}
