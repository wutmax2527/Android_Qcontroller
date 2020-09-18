package th.co.infinitecorp.www.qcontroller.Database.Model;

public class QMSDB {
    public static class TB_Test{
        public long ID;
        public String SUBJECT;
        public String DESC;

        public TB_Test() {
        }

        public long getID() {
            return ID;
        }

        public void setID(long ID) {
            this.ID = ID;
        }

        public String getSUBJECT() {
            return SUBJECT;
        }

        public void setSUBJECT(String SUBJECT) {
            this.SUBJECT = SUBJECT;
        }

        public String getDESC() {
            return DESC;
        }

        public void setDESC(String DESC) {
            this.DESC = DESC;
        }
    }
}
