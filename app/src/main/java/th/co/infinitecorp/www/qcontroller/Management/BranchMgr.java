package th.co.infinitecorp.www.qcontroller.Management;

import th.co.infinitecorp.www.qcontroller.DataInfo.BranchStatusInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.MASTER.BranchInfo;

public class BranchMgr {

    public static BranchInfo  NEW_BranchInfo()
    {
        BranchInfo d=new BranchInfo();
        return  d;
    }
    public static BranchStatusInfo NEW_BranchStatusInfo()
    {
        BranchStatusInfo d=new BranchStatusInfo();
        return  d;
    }
}
