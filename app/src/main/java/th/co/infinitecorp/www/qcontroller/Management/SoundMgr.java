package th.co.infinitecorp.www.qcontroller.Management;

import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;
import th.co.infinitecorp.www.qcontroller.Service.DisplayService;
import th.co.infinitecorp.www.qcontroller.Service.SoundService;

public class SoundMgr {

    public static void UpdateCallingQueue(QInfo qStart, QInfo qEnd, byte station_id) {
        /*Sound Built-in*/
        SoundService.UpdateCallingQueueOnInternalSound(0x00,qStart,qEnd, station_id,(byte)0x06,(byte) 1);
        /*QSound-V3R01*/
        //SoundService.UpdateCallingQueueOnQSound((byte) 0x00,qStart,qEnd, station_id,(byte)0x02,(byte) 1);
    }
}
