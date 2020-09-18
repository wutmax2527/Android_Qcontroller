package th.co.infinitecorp.www.qcontroller.Management;

import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.DisplayInfo;
import th.co.infinitecorp.www.qcontroller.Service.DisplayService;
import th.co.infinitecorp.www.qcontroller.Service.QDisplayService;

public class DisplayMgr {
    public static void UpdateQueueOnDisplay(QInfo qStart,QInfo qEnd,byte station_id) {
        /*Main Display*/
        //DisplayService.UpdateQueueOnDisplay((byte) 105,qStart,qEnd,station_id);

        /*Counter Display*/
        DisplayService.UpdateCallingQueueOnDisplay(station_id,qStart,qEnd,station_id,(byte) 5,(byte) 0);

        /*QDisplay*/
        //PC=10.172.103.117
        //Mobile=10.172.102.42
        QDisplayService.UpdateCallingQueueOnQDisplay((byte) 0x01,"10.172.102.42",qStart,qEnd,station_id);
    }
    public static void UpdateWaitQOnDisplay() {
        /*Main Display*/

        /*Counter Display*/
    }
    public static void UpdateEstWaitTimeQOnDisplay() {
        /*Counter Display*/
    }
}
