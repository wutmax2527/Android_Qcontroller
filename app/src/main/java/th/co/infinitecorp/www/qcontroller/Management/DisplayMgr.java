package th.co.infinitecorp.www.qcontroller.Management;

import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.DisplayInfo;
import th.co.infinitecorp.www.qcontroller.Service.DisplayService;

public class DisplayMgr {
    public static void UpdateQueueOnDisplay(QInfo qStart,QInfo qEnd,byte station_id) {
        /*Main Display*/
        //DisplayService.UpdateQueueOnDisplay((byte) 105,qStart,qEnd,station_id);

        /*Counter Display*/
        DisplayService.UpdateCallingQueueOnDisplay(station_id,qStart,qEnd,station_id,(byte) 5,(byte) 0);
    }
    public static void UpdateWaitQOnDisplay() {
        /*Main Display*/

        /*Counter Display*/
    }
    public static void UpdateEstWaitTimeQOnDisplay() {
        /*Counter Display*/
    }
}
