package th.co.infinitecorp.www.qcontroller.Service;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;

import org.greenrobot.eventbus.EventBus;

import th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.QDisplayInfo;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.Utils.GData;

public class RealtimeService extends Service {
    private Thread  callRealtimeThread=null;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        callRealtimeThread=new Thread(CallRealtimeThread);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        callRealtimeThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    Thread CallRealtimeThread=new Thread(new Runnable() {
        @Override
        public void run() {
            Integer runIndex=0;
            Integer ProcessIndex=0;
            while (true)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(runIndex++>=5) {
                    runIndex=0;
                    EventBus.getDefault().post(new DebugMessageEvent("RealtimeService Thread Run..."+ProcessIndex));
                    if(ProcessIndex++>100)
                        ProcessIndex=0;
                }
            }
        }
    });

}
