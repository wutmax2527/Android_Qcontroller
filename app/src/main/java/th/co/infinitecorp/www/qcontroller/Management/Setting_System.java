package th.co.infinitecorp.www.qcontroller.Management;

public class Setting_System {
    public Setting_System() {
    }

    public boolean getModeOnline() {
        return ModeOnline;
    }

    public void setModeOnline(boolean modeOnline) {
        ModeOnline = modeOnline;
    }

    public String getServer() {
        return Server;
    }

    public void setServer(String server) {
        Server = server;
    }

    public int getBranchID() {
        return BranchID;
    }

    public void setBranchID(int branchID) {
        BranchID = branchID;
    }

    public Setting_System(boolean modeOnline, String server, int branchID) {
        ModeOnline = modeOnline;
        Server = server;
        BranchID = branchID;
    }

    boolean ModeOnline;
    String Server;
    int BranchID;
}
