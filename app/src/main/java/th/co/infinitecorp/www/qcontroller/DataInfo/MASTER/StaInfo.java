package th.co.infinitecorp.www.qcontroller.DataInfo.MASTER;

public class StaInfo {
    Integer Branch_ID;
    Integer ID;
    String Name;
    Integer Group_ID;

    public StaInfo()
    {

    }

    public StaInfo(Integer branch_ID, Integer ID, String name, Integer group_ID) {
        Branch_ID = branch_ID;
        this.ID = ID;
        Name = name;
        Group_ID = group_ID;
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

    public Integer getGroup_ID() {
        return Group_ID;
    }

    public void setGroup_ID(Integer group_ID) {
        Group_ID = group_ID;
    }
}
