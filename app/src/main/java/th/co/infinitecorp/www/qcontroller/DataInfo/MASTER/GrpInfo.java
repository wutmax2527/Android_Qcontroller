package th.co.infinitecorp.www.qcontroller.DataInfo.MASTER;

import com.google.gson.annotations.Expose;

public class GrpInfo {
     @Expose
     Integer Branch_ID;
     @Expose
     Integer  ID;
     @Expose
     String Name;
     @Expose
     String Desc;
     @Expose
     String WaitTime_Alert;
     @Expose
     Integer WaitQ_Alert;
     @Expose
     Integer Show;
     @Expose
     Integer Seq;

    public GrpInfo()
    {

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

     public String getWaitTime_Alert() {
          return WaitTime_Alert;
     }

     public void setWaitTime_Alert(String waitTime_Alert) {
          WaitTime_Alert = waitTime_Alert;
     }

     public Integer getWaitQ_Alert() {
          return WaitQ_Alert;
     }

     public void setWaitQ_Alert(Integer waitQ_Alert) {
          WaitQ_Alert = waitQ_Alert;
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
}
