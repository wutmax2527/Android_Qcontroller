package th.co.infinitecorp.www.qcontroller.Management;

import android.content.Context;

import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.CurrentDivisionInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.DivisionInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.QLaunchingInfo;
import th.co.infinitecorp.www.qcontroller.Utils.GData;

public class DivisionMgr {
    public static DivisionInfo NEW_DIVISION() {
        DivisionInfo d=new DivisionInfo();
        byte qType=2;  //0=None,1=4Digit,2=3Digit,3=NoZero ,4=5Digit
        byte alphabet=65;//"A"
        short qBegin=1;
        short qEnd=999;
        short qLaunching=0;

        d.setqType(qType);
        d.setAlphabet(alphabet);
        d.setqBegin(qBegin);
        d.setqEnd(qEnd);
        d.setqLaunching(qLaunching);
        return  d;
    }
    public static QLaunchingInfo NEW_CURRNETQ() {
        QLaunchingInfo d=new QLaunchingInfo();
        short qLaunching=0;
        d.setqLaunching(qLaunching);
        return  d;

    }
    public static CurrentDivisionInfo NEW_CurrentDivisionInfo(){
        CurrentDivisionInfo d=new CurrentDivisionInfo();
        d.setId(0);
        d.setWaitQ((short) 0);
        d.setHoldQ((short)0);
        return  d;
    }
    public static CurrentDivisionInfo GetCurrentDivisionInfo(Context context, int divId) {
        if(divId<0)
            return  null;
        CurrentDivisionInfo curDiv=DivisionMgr.NEW_CurrentDivisionInfo();
        if(GData.CurDiv!=null) {
            for (CurrentDivisionInfo c : GData.CurDiv) {
                if (c.getId() == divId) {
                    curDiv = c;
                    return curDiv;
                }
            }
        }
        return null;
    }
}
