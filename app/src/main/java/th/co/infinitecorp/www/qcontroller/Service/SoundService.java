package th.co.infinitecorp.www.qcontroller.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

import th.co.infinitecorp.www.qcontroller.DataInfo.QInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.QSoundInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.QTicketInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.DisplayInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.PlaylistInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.SoundInfo;
import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;
import th.co.infinitecorp.www.qcontroller.Service.InternalSound.PlaySound;
import th.co.infinitecorp.www.qcontroller.Uart.Uart;
import th.co.infinitecorp.www.qcontroller.Utils.Convert;
import th.co.infinitecorp.www.qcontroller.Utils.GData;
import th.co.infinitecorp.www.qcontroller.Utils.Protocol;

import static th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.SoundInfo.SoundType.WhoType.sound_EN_noi;
import static th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.SoundInfo.SoundType.WhoType.sound_TH_noi;

public class SoundService extends Service implements ISoundService{
    private Thread callSoundThread=null;
    PlaySound playsound = new PlaySound(SoundService.this);
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        callSoundThread=new Thread(CallSoundThread);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(callSoundThread!=null)
          callSoundThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    Thread CallSoundThread =new Thread(new Runnable() {
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
                   EventBus.getDefault().post(new DebugMessageEvent("SoundService Thread Run..."+ProcessIndex));
                   if(ProcessIndex++>100)
                       ProcessIndex=0;
               }
               /*Play from Playlist*/
               if(GData.playlistInfos!=null) {
                   if (GData.playlistInfos.size() > 0) {
                       PlaylistInfo p = new PlaylistInfo();
                       p = GData.playlistInfos.get(0);

                       /*play Sound on MediaPlayer*/
                       QInfo qStart=p.getqStart();
                       QInfo qEnd=p.getqEnd();
                       Character alp=' ';
                       alp=(char)qStart.getqAlp();

                       playsound.Sound(Integer.valueOf(p.getSoundType()),  true, (char) (qStart.getqType()), alp, Integer.valueOf(qStart.getqNum()), Integer.valueOf(qEnd.getqNum()), (char) p.getStaNo());

                       EventBus.getDefault().post(new DebugMessageEvent("PlaySound from Playlist..." + p.getSoundId()));
                       GData.playlistInfos.remove(0);
                       try {
                           Thread.sleep(1000);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
               }
           }
        }
    });
    /*OnInternalSound*/
    public static void AddToPlayList(PlaylistInfo playlistInfo) {
        GData.playlistInfos.add(playlistInfo);
    }
    public static void ClearPlayList()
    {
        GData.playlistInfos.clear();
    }
    private static PlaylistInfo NEW_QueueOnInternalSound() {
        PlaylistInfo d=new PlaylistInfo();
        d.setSoundId(0x00);
        d.setqStart(new QInfo());
        d.setqEnd(new QInfo());
        d.setStaNo((byte)0x00);
        d.setSoundType((byte)0x00);
        d.setTimes((byte) 0x00);
        return  d;
    }
    public static void UpdateCallingQueueOnInternalSound(Integer id, QInfo qStart, QInfo qEnd, byte station_id,byte soundType,byte times) {
        PlaylistInfo d=NEW_QueueOnInternalSound();
        d.setSoundId(id);
        d.setqStart(qStart);
        d.setqEnd(qEnd);
        d.setStaNo(station_id);
        d.setSoundType(soundType);
        d.setTimes(times);
        for(int i=0;i<times;i++)
            AddToPlayList(d);
    }
    /*Command To QSound*/
    public  void CommunicateQSound(Uart uart) {
        /*Play from Playlist*/
        if(GData.qSoundInfos.size()>0)
        {
            QSoundInfo q=new QSoundInfo();
            q=GData.qSoundInfos.get(0);
            /*play Sound on QmsSound Device*/
            CallingQOnQmsSound(uart,q);
            GData.qSoundInfos.remove(0);

        }
    }
    public static void AddCallingQueueToList(QSoundInfo qSoundInfo) {
        GData.qSoundInfos.add(qSoundInfo);
    }
    public static void ClearQSoundInfo() {
        GData.qSoundInfos.clear();
    }

    private static QSoundInfo NEW_QueueOnQSound() {
        QSoundInfo d=new QSoundInfo();
        d.setId((byte) 0x00);
        d.setqStart(new QInfo());
        d.setqEnd(new QInfo());
        d.setStaNo((byte)0x00);
        d.setSoundType((byte)0x00);
        d.setTimes((byte) 0x00);
        return  d;
    }
    public static void UpdateCallingQueueOnQSound(byte id, QInfo qStart, QInfo qEnd, byte station_id,byte soundType,byte times) {
        QSoundInfo d=NEW_QueueOnQSound();
        d.setId(id);
        d.setqStart(qStart);
        d.setqEnd(qEnd);
        d.setStaNo(station_id);
        d.setSoundType(soundType);
        d.setTimes(times);
        AddCallingQueueToList(d);
    }
    /*CALLING_SPAN_Q*/
    public void CallingQOnQmsSound(Uart uart,QSoundInfo info){
        callingQOnQmsSound(uart,info,this);
    }
    private  boolean callingQOnQmsSound(final Uart uart, final QSoundInfo info, final ISoundService iSoundService) {

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes= prepareCallingQOnQSound(info);
                String str= Convert.ByteArrayToHexStringWithSpace(bytes);
                EventBus.getDefault().post(new DebugMessageEvent("QSound_Serial Send Hex:"+str));
                uart.SendReceive_Byte(500,bytes,true);
                uart.setOnDataReceivedListener(new Uart.OnDataReceivedListener() {
                    @Override
                    public boolean onDataReceived(byte[] bytes) {

                        String str=Convert.ByteArrayToHexStringWithSpace(bytes);
                        EventBus.getDefault().post(new DebugMessageEvent("QSound_Serial Rev Len="+bytes.length+" Hex="+str));
                        byte resp= Protocol.Verifed_DataFrame(bytes);
                        if(resp==Protocol.FRAME_STATUS.CORRECT) {
                            byte cmd=bytes[4];
                            if(cmd==Protocol.QSOUND_CMD.CALLING_SPAN_Q)
                                EventBus.getDefault().post(new DebugMessageEvent("***QSound_Serial Rev OK***"));
                            else
                                EventBus.getDefault().post(new DebugMessageEvent("***QSound_Serial Rev CMD Fail***"));
                        }else {
                            EventBus.getDefault().post(new DebugMessageEvent("***QSound_Serial Rev Fail="+resp));
                        }
                        return true;
                    }
                    @Override
                    public boolean onReceiveFail(byte[] bytes) {
                        EventBus.getDefault().post(new DebugMessageEvent("***QSound_Serial Rev TimeOut***"));
                        return false;
                    }
                });
            }
        });
        thread.start();
        return false;
    }
    private static   byte[] prepareCallingQOnQSound(QSoundInfo info) {
        byte[] bytes=new byte[1024*2];
        int idx=0;
        //Server ->STX-ADDR-LEN[2]-CMD-QStart[4]-QEnd[4]-StaNo-SoundType-Times-SUM-EOT
        QInfo qStart=info.getqStart();
        QInfo qEnd=info.getqEnd();
        //QStart
        bytes[idx++]=qStart.getqType();
        bytes[idx++]=qStart.getqAlp();
        bytes[idx++]=Convert.GetByteHigh(qStart.getqNum());
        bytes[idx++]=Convert.GetByteLow(qStart.getqNum());
        //QEnd
        bytes[idx++]=qEnd.getqType();
        bytes[idx++]=qEnd.getqAlp();
        bytes[idx++]=Convert.GetByteHigh(qEnd.getqNum());
        bytes[idx++]=Convert.GetByteLow(qEnd.getqNum());
        //StaNo
        bytes[idx++]=info.getStaNo();
        //SoundType
        bytes[idx++]=info.getSoundType();
        //Times
        bytes[idx++]=info.getTimes();

        byte[] b = Arrays.copyOf(bytes, idx);
        return Protocol.prepareData_Protocol_V1(Protocol.QSOUND_CMD.CALLING_SPAN_Q,Protocol.Uart_Address.Sound1,b);
    }


}
