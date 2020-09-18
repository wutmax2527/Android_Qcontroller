package th.co.infinitecorp.www.qcontroller.DataInfo.MASTER;

public class UserInfo {
     Integer Branch_ID;
     Integer ID;
     String Name;
     String Code;
     String Address1;
     String Address2;
     String Picture;
     String employee_id;

     public UserInfo()
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

     public String getCode() {
          return Code;
     }

     public void setCode(String code) {
          Code = code;
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

     public String getPicture() {
          return Picture;
     }

     public void setPicture(String picture) {
          Picture = picture;
     }

     public String getEmployee_id() {
          return employee_id;
     }

     public void setEmployee_id(String employee_id) {
          this.employee_id = employee_id;
     }
}
