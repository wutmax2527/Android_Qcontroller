package th.co.infinitecorp.www.qcontroller.Management;

import org.greenrobot.eventbus.EventBus;

import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.Utils.GData;

public class ViewLogMgr {

    public static  void ShowQueue() {
        if (GData.Queue != null)
            EventBus.getDefault().post(new DebugMessageEvent("Queue.Size=" + GData.Queue.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("Queue is null"));

    }
    public static  void ShowUserlog() {
        if (GData.Userlog != null)
            EventBus.getDefault().post(new DebugMessageEvent("Userlog.Size=" + GData.Userlog.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("Userlog is null"));
    }
    public static  void ShowCounterlog() {

        if (GData.Counterlog != null)
            EventBus.getDefault().post(new DebugMessageEvent("Counterlog.Size=" + GData.Counterlog.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("Counterlog is null"));
    }

    public static  void ShowPeriperal() {

        if (GData.PeriperalInfos != null)
            EventBus.getDefault().post(new DebugMessageEvent("Periperal.Size=" + GData.PeriperalInfos.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("Periperal is null"));
    }
    public static  void ShowStaMapGroup() {

        if (GData.StaMapGroupInfos!= null)
            EventBus.getDefault().post(new DebugMessageEvent("StaMapGroupInfos.Size=" + GData.StaMapGroupInfos.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("StaMapGroupInfos is null"));
    }
    public static  void ShowDivMapGroup() {

        if (GData.DivMapGroupInfos!= null)
            EventBus.getDefault().post(new DebugMessageEvent("DivMapGroupInfos.Size=" + GData.DivMapGroupInfos.size()));
        else
            EventBus.getDefault().post(new DebugMessageEvent("DivMapGroupInfos is null"));
    }
}
