package th.co.infinitecorp.www.qcontroller.DataInfo.MASTER;

import com.google.gson.annotations.Expose;

public class BranchInfo {

    @Expose
    Integer ID;
    @Expose
    String Name;
    @Expose
    String Code;
    @Expose
    Integer workday_group_id;
    @Expose
    String Open;
    @Expose
    String Close;
    @Expose
    String DayBegin;
    @Expose
    String DayEnd;
    @Expose
    String Time;
    @Expose
    String ServerTime;
    @Expose
    String UploadTime;
    @Expose
    String IPAddress;
    @Expose
    String Address1;
    @Expose
    String Address2;
    @Expose
    String LastSync;
    @Expose
    Integer BranchStatus;
    @Expose
    Integer DIVALT_PF_ID;
    @Expose
    Integer zone_ID;
    @Expose
    Integer appointmentGroupId;
    @Expose
    String Email;
    @Expose
    String Telephone;
    @Expose
    String Webpage;
    @Expose
    Float longitude;
    @Expose
    Float latitude;

    public  BranchInfo()
    {

    }

    public BranchInfo(Integer ID, String name, String code, String open, String close, String IPAddress, String dayBegin, String dayEnd) {
        this.ID = ID;
        this.Name = name;
        this.Code = code;
        this.Open = open;
        this.Close = close;
        this.DayBegin = dayBegin;
        this.DayEnd = dayEnd;
        this.IPAddress = IPAddress;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public Integer getWorkday_group_id() {
        return workday_group_id;
    }

    public void setWorkday_group_id(Integer workday_group_id) {
        this.workday_group_id = workday_group_id;
    }

    public String getOpen() {
        return Open;
    }

    public void setOpen(String open) {
        Open = open;
    }

    public String getClose() {
        return Close;
    }

    public void setClose(String close) {
        Close = close;
    }

    public String getDayBegin() {
        return DayBegin;
    }

    public void setDayBegin(String dayBegin) {
        DayBegin = dayBegin;
    }

    public String getDayEnd() {
        return DayEnd;
    }

    public void setDayEnd(String dayEnd) {
        DayEnd = dayEnd;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getServerTime() {
        return ServerTime;
    }

    public void setServerTime(String serverTime) {
        ServerTime = serverTime;
    }

    public String getUploadTime() {
        return UploadTime;
    }

    public void setUploadTime(String uploadTime) {
        UploadTime = uploadTime;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getLastSync() {
        return LastSync;
    }

    public void setLastSync(String lastSync) {
        LastSync = lastSync;
    }

    public Integer getBranchStatus() {
        return BranchStatus;
    }

    public void setBranchStatus(Integer branchStatus) {
        BranchStatus = branchStatus;
    }

    public Integer getDIVALT_PF_ID() {
        return DIVALT_PF_ID;
    }

    public void setDIVALT_PF_ID(Integer DIVALT_PF_ID) {
        this.DIVALT_PF_ID = DIVALT_PF_ID;
    }

    public Integer getZone_ID() {
        return zone_ID;
    }

    public void setZone_ID(Integer zone_ID) {
        this.zone_ID = zone_ID;
    }

    public Integer getAppointmentGroupId() {
        return appointmentGroupId;
    }

    public void setAppointmentGroupId(Integer appointmentGroupId) {
        this.appointmentGroupId = appointmentGroupId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getWebpage() {
        return Webpage;
    }

    public void setWebpage(String webpage) {
        Webpage = webpage;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }
}
