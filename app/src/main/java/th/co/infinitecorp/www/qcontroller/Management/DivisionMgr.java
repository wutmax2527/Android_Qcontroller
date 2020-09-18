package th.co.infinitecorp.www.qcontroller.Management;

import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.DivisionInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW.QLaunchingInfo;

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
}
