package th.co.infinitecorp.www.qcontroller.DataInfo.MASTER;

public class DivInfo {
    Integer Branch_ID;
    Integer ID;
    String Name;
    String Desc;
    Integer QStart;
    Integer QStop;
    Integer Print_Coppies;
    String servtime_alert;
    Integer Show;
    Integer Seq;
    Integer ShowMobile;

    public DivInfo()
    {

    }

    public DivInfo(Integer branch_ID, Integer ID, String name, String desc, Integer QStart, Integer QStop, Integer print_Coppies, String servtime_alert, Integer seq,Integer show,  Integer showMobile) {
        this.Branch_ID = branch_ID;
        this.ID = ID;
        this.Name = name;
        this.Desc = desc;
        this.QStart = QStart;
        this.QStop = QStop;
        this.Print_Coppies = print_Coppies;
        this.servtime_alert = servtime_alert;
        this.Show = show;
        this.Seq = seq;
        this.ShowMobile = showMobile;
    }

    public Integer getBranch_ID() {
        return Branch_ID;
    }

    public void setBranch_ID(Integer branch_ID) {
        Branch_ID = branch_ID;
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

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public Integer getQStart() {
        return QStart;
    }

    public void setQStart(Integer QStart) {
        this.QStart = QStart;
    }

    public Integer getQStop() {
        return QStop;
    }

    public void setQStop(Integer QStop) {
        this.QStop = QStop;
    }

    public Integer getPrint_Coppies() {
        return Print_Coppies;
    }

    public void setPrint_Coppies(Integer print_Coppies) {
        Print_Coppies = print_Coppies;
    }

    public String getServtime_alert() {
        return servtime_alert;
    }

    public void setServtime_alert(String servtime_alert) {
        this.servtime_alert = servtime_alert;
    }

    public Integer getShow() {
        return Show;
    }

    public void setShow(Integer show) {
        Show = show;
    }

    public Integer getSeq() {
        return Seq;
    }

    public void setSeq(Integer seq) {
        Seq = seq;
    }

    public Integer getShowMobile() {
        return ShowMobile;
    }

    public void setShowMobile(Integer showMobile) {
        ShowMobile = showMobile;
    }
}
