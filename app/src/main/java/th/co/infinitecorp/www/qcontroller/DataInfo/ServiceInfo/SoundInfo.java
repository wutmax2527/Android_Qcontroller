package th.co.infinitecorp.www.qcontroller.DataInfo.ServiceInfo;

public class SoundInfo {

    public static class SoundType {

        public enum Type{
            bell,
            TH_noi,
            EN_noi,
            TH_aom,
            TH_pk,
            bell_Custom,
            TH_Custom,
            EN_Custom,
        }
        public enum WhoType{
            sound_Bell,
            sound_TH_noi,
            sound_EN_noi,
            sound_TH_aom,
            sound_TH_pk,
            sound_JP,
            sound_Bell_Custom,
            sound_TH_Custom,
            sound_EN_Custom,
        }

        public enum Conjuction{
            bell,
            teen,
            aval,
            number,
            at_couter,
            at_room,
            ka,
            welcome,
            to,
        }
    }
}
