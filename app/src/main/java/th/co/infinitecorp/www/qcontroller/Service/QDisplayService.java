package th.co.infinitecorp.www.qcontroller.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.greenrobot.eventbus.EventBus;

import th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.DisplayInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.QDisplayInfo;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.Utils.GData;

public class QDisplayService extends Service {
    private Thread  callQDisplayThread=null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        callQDisplayThread=new Thread(CallQDisplayThread);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        callQDisplayThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    Thread CallQDisplayThread=new Thread(new Runnable() {
        @Override
        public void run() {
            Integer runIndex=0;
            Integer ProcessIndex=0;
            while (true)
            {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(runIndex++>=25) {
                    runIndex=0;
                    EventBus.getDefault().post(new DebugMessageEvent("QDisplayService Thread Run..."+ProcessIndex));
                    if(ProcessIndex++>100)
                        ProcessIndex=0;
                }
                /*Show Display*/
                if(GData.qDisplayInfos.size()>0)
                {
                    QDisplayInfo qDisplayInfo=new QDisplayInfo();
                    qDisplayInfo=GData.qDisplayInfos.get(0);
                    /*Communicate QDisplay*/


                    GData.qDisplayInfos.remove(0);
                }
            }
        }
    });
    public static void AddToList(QDisplayInfo qDisplayInfo)
    {
        GData.qDisplayInfos.add(qDisplayInfo);
    }
    public static void ClearList()
    {
        GData.qDisplayInfos.clear();
    }
}
