package th.co.infinitecorp.www.qcontroller.DataInfo.MASTER;

public class BreakInfo {
     Integer branch_id;
     Integer id;
     String reason;
     Integer have_desc;
     Integer seq;

     public BreakInfo()
     {

     }

     public Integer getBranch_id() {
          return branch_id;
     }

     public void setBranch_id(Integer branch_id) {
          this.branch_id = branch_id;
     }

     public Integer getId() {
          return id;
     }

     public void setId(Integer id) {
          this.id = id;
     }

     public String getReason() {
          return reason;
     }

     public void setReason(String reason) {
          this.reason = reason;
     }

     public Integer getHave_desc() {
          return have_desc;
     }

     public void setHave_desc(Integer have_desc) {
          this.have_desc = have_desc;
     }

     public Integer getSeq() {
          return seq;
     }

     public void setSeq(Integer seq) {
          this.seq = seq;
     }
}
