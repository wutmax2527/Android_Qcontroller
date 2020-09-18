package th.co.infinitecorp.www.qcontroller.Management;

import android.content.Context;

import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentGroupInfo;
import th.co.infinitecorp.www.qcontroller.Utils.GData;

public class GroupMgr {

    public static CurrentGroupInfo NEW_CurrentGroupInfo(){
        CurrentGroupInfo  d=new CurrentGroupInfo();
        d.setId(0);
        d.setWaitQ((short) 0);
        d.setHoldQ((short)0);
        d.setName("");
        return  d;
    }
    public static CurrentGroupInfo Find_CurrentByGroup(Context context, Integer grpId) {
        CurrentGroupInfo updateC=NEW_CurrentGroupInfo();

        if(GData.CurGrp!=null) {
            if (GData.CurGrp.size() > 0) {
                for (CurrentGroupInfo c : GData.CurGrp) {
                    if (c.getId() == grpId) {
                        updateC = c;
                        break;
                    }
                }
            }
        }
        return updateC;
    }

}
