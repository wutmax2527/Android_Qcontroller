package th.co.infinitecorp.www.qcontroller.Service.InternalSound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.PlaylistInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.SoundInfo;
import th.co.infinitecorp.www.qcontroller.R;

import static th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.SoundInfo.SoundType.*;
import static th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.SoundInfo.SoundType.Conjuction;
import static th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.SoundInfo.SoundType.Conjuction.at_couter;
import static th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.SoundInfo.SoundType.Conjuction.number;
import static th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo.SoundInfo.SoundType.Conjuction.teen;

public class PlaySound {
    List<PlaylistInfo> soundlist = new ArrayList<>();
    PlaylistInfo soundid;
    Integer soundlength;
    Integer playingPostion=0;
    MediaPlayer mp;
    private static final String tag = "debug1";
    private Context context;

    public PlaySound(Context context){
        this.context = context;
    }
    public void play(){
        if(mp == null){
            mp = new MediaPlayer();
        }else{
            mp.reset();
        }
        final int id=soundlist.get(playingPostion).getSoundId();
        if(id!=0) {
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(id);
            try {
                mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }else {

        }

        if(playingPostion == 0){
            if(soundlist.size() > 1){
                playingPostion++;
            }
        }else if((playingPostion+1) >= soundlist.size()){
            playingPostion = 0;
        }else{
            playingPostion++;
        }

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(tag,"next>>");
                if(playingPostion != 0) {
                    play();
                }else{
                    mp.stop();
                    mp.release();
                    soundlist.clear();
                    Log.d(tag,"Media Player Stop!!!");
                }
            }
        });

    }

    /*R.raw of Sound*/
    private boolean sound_Word(WhoType who,Character v){
        soundid = new PlaylistInfo();
        switch (who){
            case sound_TH_noi:
                switch (v){
                    case 0 : soundid.setSoundId(R.raw.th_women1_noi_0); break;
                    case 1 : soundid.setSoundId(R.raw.th_women1_noi_1); break;
                    case 2 : soundid.setSoundId(R.raw.th_women1_noi_2); break;
                    case 3 : soundid.setSoundId(R.raw.th_women1_noi_3); break;
                    case 4 : soundid.setSoundId(R.raw.th_women1_noi_4); break;
                    case 5 : soundid.setSoundId(R.raw.th_women1_noi_5); break;
                    case 6 : soundid.setSoundId(R.raw.th_women1_noi_6); break;
                    case 7 : soundid.setSoundId(R.raw.th_women1_noi_7); break;
                    case 8 : soundid.setSoundId(R.raw.th_women1_noi_8); break;
                    case 9 : soundid.setSoundId(R.raw.th_women1_noi_9); break;
                    case 10 : soundid.setSoundId(R.raw.th_women1_noi_10); break;
                    case 11 : soundid.setSoundId(R.raw.th_women1_noi_11); break;
                    case 20 : soundid.setSoundId(R.raw.th_women1_noi_20); break;
                    case 21 : soundid.setSoundId(R.raw.th_women1_noi_21); break;
                    case 30 : soundid.setSoundId(R.raw.th_women1_noi_30); break;
                    case 31 : soundid.setSoundId(R.raw.th_women1_noi_31); break;
                    case 'A' :
                    case 'a' : soundid.setSoundId(R.raw.th_women1_noi_a); break;    //a
                    case 'B' :
                    case 'b' : soundid.setSoundId(R.raw.th_women1_noi_b); break;    //B
                    case 'C' :
                    case 'c' : soundid.setSoundId(R.raw.th_women1_noi_c); break;    //C
                    case 'D' :
                    case 'd' : soundid.setSoundId(R.raw.th_women1_noi_d); break;    //D
                    case 'E' :
                    case 'e' : soundid.setSoundId(R.raw.th_women1_noi_e); break;    //E
                    case 'F' :
                    case 'f' : soundid.setSoundId(R.raw.th_women1_noi_f); break;    //F
                    case 'G' :
                    case 'g' : soundid.setSoundId(R.raw.th_women1_noi_g); break;    //G
                    case 'H' :
                    case 'h' : soundid.setSoundId(R.raw.th_women1_noi_h); break;    //H
                    case 'I' :
                    case 'i' : soundid.setSoundId(R.raw.th_women1_noi_i); break;    //I
                    case 'J' :
                    case 'j' : soundid.setSoundId(R.raw.th_women1_noi_j); break;    //J
                    default:
                        //soundid.setSoundId(R.raw.empty);
                        return false;
                }
                break;
            case sound_EN_noi:
                switch (v){
                    case 0 : soundid.setSoundId(R.raw.en_women_0); break;
                    case 1 : soundid.setSoundId(R.raw.en_women_1); break;
                    case 2 : soundid.setSoundId(R.raw.en_women_2); break;
                    case 3 : soundid.setSoundId(R.raw.en_women_3); break;
                    case 4 : soundid.setSoundId(R.raw.en_women_4); break;
                    case 5 : soundid.setSoundId(R.raw.en_women_5); break;
                    case 6 : soundid.setSoundId(R.raw.en_women_6); break;
                    case 7 : soundid.setSoundId(R.raw.en_women_7); break;
                    case 8 : soundid.setSoundId(R.raw.en_women_8); break;
                    case 9 : soundid.setSoundId(R.raw.en_women_9); break;
                    case 10 : soundid.setSoundId(R.raw.en_women_10); break;
                    case 11 : soundid.setSoundId(R.raw.en_women_11); break;
                    case 12 : soundid.setSoundId(R.raw.en_women_12); break;
                    case 13 : soundid.setSoundId(R.raw.en_women_13); break;
                    case 15 : soundid.setSoundId(R.raw.en_women_15); break;
                    case 20 : soundid.setSoundId(R.raw.en_women_20); break;
                    case 30 : soundid.setSoundId(R.raw.en_women_30); break;
                    case 'A' :
                    case 'a' : soundid.setSoundId(R.raw.en_women_a); break;    //a
                    case 'B' :
                    case 'b' : soundid.setSoundId(R.raw.en_women_b); break;    //B
                    case 'C' :
                    case 'c' : soundid.setSoundId(R.raw.en_women_c); break;    //C
                    case 'D' :
                    case 'd' : soundid.setSoundId(R.raw.en_women_d); break;    //D
                    case 'E' :
                    case 'e' : soundid.setSoundId(R.raw.en_women_e); break;    //E
                    case 'F' :
                    case 'f' : soundid.setSoundId(R.raw.en_women_f); break;    //F
                    case 'G' :
                    case 'g' : soundid.setSoundId(R.raw.en_women_g); break;    //G
                    case 'H' :
                    case 'h' : soundid.setSoundId(R.raw.en_women_h); break;    //H
                    case 'I' :
                    case 'i' : soundid.setSoundId(R.raw.en_women_i); break;    //I
                    case 'J' :
                    case 'j' : soundid.setSoundId(R.raw.en_women_j); break;    //J
                    default:
                        //soundid.setSoundId(R.raw.empty);
                        return false;
                }
                break;
            case sound_TH_aom:
                switch (v){
                    case 0 : soundid.setSoundId(R.raw.th_women2_aom_0); break;
                    case 1 : soundid.setSoundId(R.raw.th_women2_aom_1); break;
                    case 2 : soundid.setSoundId(R.raw.th_women2_aom_2); break;
                    case 3 : soundid.setSoundId(R.raw.th_women2_aom_3); break;
                    case 4 : soundid.setSoundId(R.raw.th_women2_aom_4); break;
                    case 5 : soundid.setSoundId(R.raw.th_women2_aom_5); break;
                    case 6 : soundid.setSoundId(R.raw.th_women2_aom_6); break;
                    case 7 : soundid.setSoundId(R.raw.th_women2_aom_7); break;
                    case 8 : soundid.setSoundId(R.raw.th_women2_aom_8); break;
                    case 9 : soundid.setSoundId(R.raw.th_women2_aom_9); break;
                    case 10 : soundid.setSoundId(R.raw.th_women2_aom_10); break;
                    case 11 : soundid.setSoundId(R.raw.th_women2_aom_11); break;
                    case 20 : soundid.setSoundId(R.raw.th_women2_aom_20); break;
                    case 21 : soundid.setSoundId(R.raw.th_women2_aom_21); break;
                    case 30 : soundid.setSoundId(R.raw.th_women2_aom_30); break;
                    case 31 : soundid.setSoundId(R.raw.th_women2_aom_31); break;
                    case 'A' :
                    case 'a' : soundid.setSoundId(R.raw.th_women2_aom_a); break;    //a
                    case 'B' :
                    case 'b' : soundid.setSoundId(R.raw.th_women2_aom_b); break;    //B
                    case 'C' :
                    case 'c' : soundid.setSoundId(R.raw.th_women2_aom_c); break;    //C
                    case 'D' :
                    case 'd' : soundid.setSoundId(R.raw.th_women2_aom_d); break;    //D
                    case 'E' :
                    case 'e' : soundid.setSoundId(R.raw.th_women2_aom_e); break;    //E
                    case 'F' :
                    case 'f' : soundid.setSoundId(R.raw.th_women2_aom_f); break;    //F
                    case 'G' :
                    case 'g' : soundid.setSoundId(R.raw.th_women2_aom_g); break;    //G
                    case 'H' :
                    case 'h' : soundid.setSoundId(R.raw.th_women2_aom_h); break;    //H
                    case 'I' :
                    case 'i' : soundid.setSoundId(R.raw.th_women2_aom_i); break;    //I
                    case 'J' :
                    case 'j' : soundid.setSoundId(R.raw.th_women2_aom_j); break;    //J
                    default:
                        //soundid.setSoundId(R.raw.empty);
                        return false;
                }
                break;
            case sound_TH_pk:
                break;
            case sound_JP:
                break;
        }

        soundlist.add(soundid);
        return true;
    }
    private boolean sound_Conjuction(WhoType who,Conjuction v){
        soundid = new PlaylistInfo();
        switch (who){
            case sound_Bell:
                switch (v){
                    case bell: soundid.setSoundId(R.raw.bell); break;
                    default:
                        return false;

                }
                break;
            case sound_TH_noi:
                switch (v){
                    case welcome: soundid.setSoundId(R.raw.th_women1_noi_welcome); break;
                    case at_couter: soundid.setSoundId(R.raw.th_women1_noi_at_count); break;
                    case ka: soundid.setSoundId(R.raw.th_women1_noi_ka); break;
                    default:
                        return false;

                }
                break;
            case sound_EN_noi:
                switch (v){
                    case teen : soundid.setSoundId( R.raw.en_women_teen ); break;
                    case aval: soundid.setSoundId(R.raw.en_women_aval); break;
                    case number: soundid.setSoundId(R.raw.en_women_num); break;
                    case welcome: soundid.setSoundId(R.raw.en_women_welcome); break;
                    case at_couter: soundid.setSoundId(R.raw.en_women_atcount); break;
                    case at_room: soundid.setSoundId(R.raw.en_women_atroom); break;
                    case to:  soundid.setSoundId(R.raw.en_women_to); break;
                    default:return false;
                }
                break;

            case sound_TH_aom:
                switch (v){
                    case welcome: soundid.setSoundId(R.raw.th_women2_aom_welcome); break;
                    case at_couter: soundid.setSoundId(R.raw.th_women2_aom_at_count); break;
                    case ka: soundid.setSoundId(R.raw.th_women2_aom_ka); break;
                    case to:  soundid.setSoundId(R.raw.th_women2_aom_to); break;
                    default:
                        return false;
                }
                break;

            case sound_TH_pk:
                break;
            case sound_JP:
                break;
        }
        soundlist.add(soundid);
        return true;
    }

    /*Function*/
    private char[] check_Qtype(char qType, int callingQ){
        char[] Que = new char[4];
        switch (qType){
            case 1:
                Que[0] = (char)((callingQ%10000)/1000);
                Que[1] = (char)((callingQ%1000)/100);
                Que[2] = (char)((callingQ%100)/10);
                Que[3] = (char)((callingQ%10));
                soundlength = 4;
                break;
            case 2:
                Que[0] = (char)((callingQ%1000)/100);
                Que[1] = (char)((callingQ%100)/10);
                Que[2] = (char)((callingQ%10));
                Que[3] = 0;
                soundlength = 3;
                break;
            case 3:
                Que[0] = 0;
                Que[1] = 0;
                Que[2] = 0;
                Que[3] = 0;
                if(((callingQ %10000)/1000) != 0)
                {
                    Que[0] = (char)((callingQ%10000)/1000);
                    Que[1] = (char)((callingQ%1000)/100);
                    Que[2] = (char)((callingQ%100)/10);
                    Que[3] = (char)(callingQ%10);
                    soundlength = 4;
                    break;
                }
                if(((callingQ %1000)/100) != 0)
                {
                    Que[0] = (char)((callingQ%1000)/100);
                    Que[1] = (char)((callingQ%100)/10);
                    Que[2] = (char)(callingQ%10);
                    soundlength = 3;
                    break;
                }
                if(((callingQ %100)/10) != 0)
                {
                    Que[0] = (char)((callingQ%100)/10);
                    Que[1] = (char)(callingQ%10);
                    soundlength = 2;
                    break;
                }
                Que[0] = (char)(callingQ%10);
                soundlength = 1;
                break;
            default:
                Que[0] = (char)((callingQ%1000)/100);
                Que[1] = (char)((callingQ%100)/10);
                Que[2] = (char)((callingQ%10));
                Que[3] = 0;
                soundlength = 4;
        }
        return Que;
    }
    private void sound_Call_QNumber(WhoType who,char qType,Integer callingQ){
        char[] qNo = check_Qtype(qType,callingQ);
        if(soundlength == 4){
            sound_Word(who,qNo[0]);
            sound_Word(who,qNo[1]);
            sound_Word(who,qNo[2]);
            sound_Word(who,qNo[3]);
        }
        if(soundlength == 3){
            sound_Word(who,qNo[0]);
            sound_Word(who,qNo[1]);
            sound_Word(who,qNo[2]);
        }
        if(soundlength == 2){
            sound_Word(who,qNo[0]);
            sound_Word(who,qNo[1]);
        }
        if(soundlength == 1){
            sound_Word(who,qNo[0]);
        }
    }
    private void sound_Call_StationNo(WhoType who, char sta){
        char sta1,sta2,sta3;
        switch (who){
            case sound_EN_noi:
                sta1 = (char)((sta%1000)/100);
                sta2 = (char)((sta%100)/10);
                sta3 = (char)(sta%10);

                if(sta1 != 0){
                    sound_Word(who,sta1);
                }
                if(sta2 != 0){
                    if(sta2 == 1){
                        if(sta3 == 0)
                            sound_Word(who, (char) 10);
                        else if(sta3 == 1)
                            sound_Word(who, (char) 11);
                        else if(sta3 == 2)
                            sound_Word(who, (char) 12);
                        else if(sta3 == 3)
                            sound_Word(who, (char) 13);
                        else if(sta3 == 5)
                            sound_Word(who, (char) 15);
                        else{
                            sound_Word(who,sta3);
                            sound_Conjuction(who,teen);
                        }
                    }
                    else if(sta2 == 2)
                        sound_Word(who, (char) 20);
                    else if(sta2 == 3)
                        sound_Word(who, (char) 30);
                }
                if(sta3 != 0 || sta2 != 1)
                    sound_Word(who,sta3);
                break;
            case sound_TH_noi:
            case sound_TH_aom:
            case sound_TH_pk:
                sta1 = (char)((sta%1000)/100);
                sta2 = (char)((sta%100)/10);
                sta3 = (char)(sta%10);

                if(sta1 != 0){
                    sound_Word(who,sta1);
                }
                if(sta2 != 0){
                    if(sta2 == 1) {
                        if (sta3 == 0)
                            sound_Word(who, (char) 10);
                        else if (sta3 == 1)
                            sound_Word(who, (char) 11);

                    }
                    else if(sta2 == 2) {
                        if (sta3 == 0)
                            sound_Word(who, (char) 20);
                        else if (sta3 == 1)
                            sound_Word(who, (char) 21);
                    }
                    else if(sta2 == 3)
                        if (sta3 == 0)
                            sound_Word(who, (char) 30);
                        else if (sta3 == 1)
                            sound_Word(who, (char) 31);

                }
                if(sta3 != 0 || sta2 != 1)
                    sound_Word(who,sta3);
                break;
            case sound_JP:

                break;
        }

    }

    /*Play Sound Type*/
    public void Sound(int soundType, boolean isCounter, char qType, Character qAlp, Integer qStart, Integer qEnd, char sta) {
        switch (soundType) {
            case  1:
                sound_Bell(WhoType.sound_Bell, isCounter, qType, qAlp, qStart, qEnd,sta);
                break;
            case  2:
                 sound_TH(WhoType.sound_TH_noi, isCounter, qType, qAlp, qStart, qEnd,sta);
                break;
            case  3:
                sound_EN(WhoType.sound_EN_noi, isCounter, qType, qAlp, qStart, qEnd,sta);
                break;
            case  4:
                sound_TH(WhoType.sound_TH_aom, isCounter, qType, qAlp, qStart, qEnd,sta);
                break;
        }
    }
    /*Bell*/
    public void sound_Bell(WhoType who, boolean isCounter, char qType, Character qAlp, Integer qStart, Integer qEnd, char sta){

        sound_Conjuction(who,Conjuction.bell);
        if(playingPostion == 0)
            mp = null;

        if(mp == null){
            playingPostion = 0;
            play();
        }
    }

    /*Sound THAI*/
    public void sound_TH(WhoType who, boolean isCounter, char qType, Character qAlp, Integer qStart, Integer qEnd, char sta){
        sound_Conjuction(who,Conjuction.welcome);
        sound_Word(who,qAlp);
        sound_Call_QNumber(who,qType,qStart);
        if(qEnd!=0) {
            if(sound_Conjuction(who,Conjuction.to)) {
                sound_Word(who, qAlp);
                sound_Call_QNumber(who, qType, qEnd);
            }
        }
        if(isCounter){
            sound_Conjuction(who,Conjuction.at_couter);
            if(sta != 0){
                sound_Call_StationNo(who,sta);
            }
        }
        sound_Conjuction(who,Conjuction.ka);
        if(playingPostion == 0)
            mp = null;

        if(mp == null){
            playingPostion = 0;
            play();
        }
    }


    /*Sound ENG*/
    public void sound_EN(WhoType who, boolean isCounter, char qType, Character qAlp, Integer qStart, Integer qEnd, char sta){
        sound_Conjuction(who,Conjuction.number);
        sound_Word(who,qAlp);
        sound_Call_QNumber(who,qType,qStart);
        if(qEnd!=0) {
            if(sound_Conjuction(who,Conjuction.to)) {
                sound_Word(who, qAlp);
                sound_Call_QNumber(who, qType, qEnd);
            }
        }
        if(isCounter){
            sound_Conjuction(who,Conjuction.at_couter);
            if(sta != 0){
                sound_Call_StationNo(who,sta);
            }
        }

        if(playingPostion == 0)
            mp = null;

        if(mp == null){
            playingPostion = 0;
            play();
        }
    }




}
