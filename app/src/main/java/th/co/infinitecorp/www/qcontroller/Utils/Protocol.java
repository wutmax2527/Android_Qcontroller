package th.co.infinitecorp.www.qcontroller.Utils;

import java.util.Arrays;

public class Protocol {

    /*static byte*/
    final public static byte NONE=  0x00;
    final public static byte STX =  0x02;
    final public static byte EOT =  0x04;
    final public static byte ACK =  0x06;
    final public static byte NACK = 0x15;

    public class RESPONSE_STATUS {
        final public static byte SUCCESS =  0x00;
        final public static byte FAIL =  0x01;
    }
    /*Device Type*/
    public class DeviceType{
        final public static byte NONE_DEVICE = 0x00;
        final public static byte HW_KEYPAD = 0x01;
        final public static byte DISPLAY = 0x02;
        final public static byte QTOUCH = 0x03;
        final public static byte QDISPLAY = 0x04;
        final public static byte QPRINT = 0x05;
        final public static byte QSOUND = 0x06;
        final public static byte SCOREBOX=0x07;
        final public static byte SW_KEYPAD = 0x08;
    }
    /*FrameID*/
    public class FrameID{
        final public static byte NONE = 0x00;
        final public static byte ID1 = 0x01;
        final public static byte ID2= 0x02;
        final public static byte ID3 = 0x03;
        final public static byte ID4 = 0x04;
        final public static byte ID5 = 0x05;
        final public static byte ID6 = 0x06;
    }

    /*Command*/
    /*1.KEYPAD*/
    public class KEYPAD_CMD {
        final public static byte SYNC = 0x00;
        final public static byte STARTUP=0x01;
        final public static byte LOGIN=0x02;
        final public static byte LOGOUT=0x03;
        final public static byte BREAK=0x04;
        final public static byte NEXT = 0x05;
        final public static byte RECALL = 0x06;
        final public static byte DIRECT_CALL = 0x07;
        final public static byte TRANSACTION = 0x08;
        final public static byte CANCEL= 0x09;
        final public static byte TRANSFER= 0x0A;
        final public static byte HELP_ME= 0x0B;
        final public static byte HOLD= 0x0C;
        final public static byte CALLHOLD= 0x0D;
        final public static byte SPAN_CALL= 0x0E;
        final public static byte WALKDIRECT= 0x0F;
        final public static byte ENDTRANS= 0x10;
        final public static byte CHANGEGROUP= 0x11;
        final public static byte SUBDIV= 0x12;
        final public static byte ADDDIV_To_STA= 0x13;
        final public static byte SCORE= 0x14;
        final public static byte DirectCancel= 0x15;
        final public static byte CALL_DIV= 0x16;
        final public static byte RECALL_DIV= 0x17;
        final public static byte REQUEST_CURRENT_STATUS=0x18;
        final public static byte CHECK_CONNECTION=0x19;

        final public static byte CallReserveQ = (byte) 0xC0;
        final public static byte RealTimeData = (byte) 0xD0;

    }
    /*2.DISPLAY*/
    public class DISPLAY_CMD {
        final public static byte SYNC = 0x00;
        final public static byte SHOW_Q = 0x01;
    }
    /*4.QDISPLAY*/
    public class QTOUCH_CMD {
        final public static byte SYNC = 0x00;
        final public static byte REQUEST_Q = 0x01;
        final public static byte RESET_Q = 0x02;
        final public static byte RESET_SYSTEM = 0x03;
    }
    /*4.QDISPLAY*/
    public class QDISPLAY_CMD {
        final public static byte SYNC = 0x00;
    }
    /*5.QPRINT*/
    public class QPRINT_CMD {
        final public static byte QUERY = 0x00;
        final public static byte SETTICKET = 0x01;
        final public static byte SETEXISTBUTTON = 0x05;
        final public static byte QTICKET = 0x06;
        final public static byte MAINTENANCE_REPORT=0x07;
        final public static byte PING=010;
    }
    public class QPRINT_SUBCMD {
        final public static byte SYNC = 0x00;
        final public static byte PRINT_FlashButton=(byte) 0xF0;
    }
    public class QPRINT_RES_CODE {
        final public static byte SUCCESS = 0x00;
        final public static byte FAIL = 0x01;

    }
    /*6.QSOUND*/
    public class QSOUND_CMD {
        final public static byte SYNC = (byte) 0xFF;
        final public static byte CALLING_SPAN_Q = (byte) 0x03;
    }
    /*7.SCOREBOX*/
    public class SCOREBOX_CMD {
        final public static byte SYNC = 0x00;

    }
    /*8.AllSoftkey*/
    public class  Softkey_CMD{
        final public static byte IDLE = (byte)0x00;
        final public static byte TRNSC = (byte)0x01;
        final public static byte DIRECT_CALL = (byte)0x02;
        final public static byte CANCEL = (byte)0x03;
        final public static byte TRANSFER = (byte)0x04;
        final public static byte DIV = (byte)0x05;
        final public static byte ACALL = (byte)0x06;
        final public static byte PAUSE = (byte)0x07;
        final public static byte HOLD = (byte)0x08;
        final public static byte NEXT = (byte)0x09;
        final public static byte RECALL = (byte)0x0A;
        final public static byte CALLHOLD = (byte)0x0B;
        final public static byte LOGON = (byte)0x0E;
        final public static byte GROUP_CALL = (byte)0x0F;
        final public static byte WALKDIRECT = (byte)0x10;
        final public static byte ENDTRANS = (byte)0x11;
        final public static byte CHANGEGROUP = (byte)0x12;
        final public static byte SUBDIV = (byte)0x13;
        final public static byte GetPullQ = (byte)0x14;

        final public static byte STARTUP = (byte)0x2B;
        final public static byte ADDDIV = (byte)0xAB;
        final public static byte TERMINATE = (byte)0x2C;
        final public static byte RESERVE = (byte)0xF0;
        final public static byte TRANSFER_STA = (byte)0xF1;
        final public static byte BREAK = (byte)0xFF;
        final public static byte VOTE = (byte)0x50;
        final public static byte CALLDIV = (byte)0x60;
        final public static byte RECALLDIV = (byte)0x61;
        final public static byte TRANSFER_To_Employee = (byte)0x22;
        final public static byte TRANSFER_To_Station = (byte)0x23;
        final public static byte TRANSFER_To_DIV = (byte)0x24;
        final public static byte DirectCancel = (byte)0x25;
    }
    public class  Softkey_Update{
        final public static byte IDLE = (byte)0x00;
        final public static byte TRNSC = (byte)0x01;
        final public static byte DIRECT_CALL = (byte)0x02;
        final public static byte CANCEL = (byte)0x03;
        final public static byte TRANSFER = (byte)0x04;
        final public static byte DIV = (byte)0x05;
        final public static byte ACALL = (byte)0x06;
        final public static byte PAUSE = (byte)0x07;
        final public static byte HOLD = (byte)0x08;
        final public static byte NEXT = (byte)0x09;
        final public static byte RECALL = (byte)0x0A;
        final public static byte CALLHOLD = (byte)0x0B;
        final public static byte LOGON = (byte)0x0E;
        final public static byte GROUP_CALL = (byte)0x0F;
        final public static byte WALKDIRECT = (byte)0x10;
        final public static byte ENDTRANS = (byte)0x11;
        final public static byte CHANGEGROUP = (byte)0x12;
        final public static byte SUBDIV = (byte)0x13;
        final public static byte GetPullQ = (byte)0x14;

        final public static byte STARTUP = (byte)0x2B;
        final public static byte ADDDIV = (byte)0xAB;
        final public static byte TERMINATE = (byte)0x2C;
        final public static byte RESERVE = (byte)0xF0;
        final public static byte TRANSFER_STA = (byte)0xF1;
        final public static byte BREAK = (byte)0xFF;
        final public static byte VOTE = (byte)0x50;
        final public static byte CALLDIV = (byte)0x60;
        final public static byte RECALLDIV = (byte)0x61;
        final public static byte TRANSFER_To_Employee = (byte)0x22;
        final public static byte TRANSFER_To_Station = (byte)0x23;
        final public static byte TRANSFER_To_DIV = (byte)0x24;
        final public static byte DirectCancel = (byte)0x25;
    }
    public class  Softkey_STARTUP_SUBCMD {
        final public static byte groupinfo = (byte)0x00;
        final public static byte divinfo = (byte)0x01;
        final public static byte breakinfo = (byte)0x02;
        final public static byte divList = (byte)0x03;
        final public static byte holdreasoninfo = (byte)0x04;
        final public static byte userSettingInfo = (byte)0x05;
        final public static byte reserveQ   = (byte)0x06;
        final public static byte counterinfo = (byte)0x07;
        final public static byte notRequest = (byte)0xff;
    }
    public class  Softkey_UPDATEDATA_CMD {
        final public static byte STATUS_DIV = (byte)0xCC;
        final public static byte STATUS_INFO = (byte)0xCD;
        final public static byte DIVISION_INFO = (byte)0xD1;
        final public static byte GROUP_INFO = (byte)0xD2;
        final public static byte BREAK_INFO = (byte)0xD3;
        final public static byte SCOREKEY_PRESS = (byte)0xC1;
        final public static byte GROUP_ID = (byte)0xC2;
        final public static byte LOGIN_INFO = (byte)0xC3;
        final public static byte STATION_WAIT = (byte)0xC5;
        final public static byte HOLD_LIST = (byte)0xC6;
        final public static byte RESERVEQ = (byte)0xF4;
        final public static byte NEXT_QUEUE = (byte)0xF7;
        final public static byte STATION_RESERVE = (byte)0xF8;
        final public static byte PAPER_INFO = (byte)0xF9;
        final public static byte CurrentQ_INFO = (byte)0xFA;
        final public static byte QWAIT_BY_DIV = (byte)0xFB;
        final public static byte COUNTER_STATUS_INFO = (byte)0xFC;
    }

    /*Uart Address*/
    public class Uart_Address {
        final public static byte Printer1 = 0x40;
        final public static byte Printer2 = 0x41;
        final public static byte Printer3 = 0x42;
        final public static byte Printer4 = 0x43;
        final public static byte Sound1 = 0x48;
        final public static byte Sound2 = 0x49;
        final public static byte Sound3 = 0x4A;
        final public static byte Sound4 = 0x4B;
        final public static byte Sound5 = 0x4C;
        final public static byte Sound6 = 0x4D;
        final public static byte Sound7 = 0x4E;
        final public static byte Sound8 = 0x4F;
        final public static byte Sound9 = 0x50;
        final public static byte Sound10 = 0x51;
    }
    /*Data Frame*/
    public class FRAME_STATUS {
        final public static byte CORRECT= 0x00;
        final public static byte STX_ERROR= 0x01;
        final public static byte EOT_ERROR=0x02;
        final public static byte SUM_ERROR=0x03;
        final public static byte INCORRECT=0x04;
        final public static byte TIMEOUT=0x05;
        final public static byte LENGTH_ERROR=0x06;
    }
    public static short GetLength(byte bh,byte bl) {
        return (short) ((bh<<8)|(bl));
    }
    public static byte CheckSum(byte[] bytes) {

        if(bytes==null) return 0x00;

        byte sum=0x00;
        for(int i=1;i< (bytes.length-2);i++)
        {
            sum^=bytes[i];
        }
        return sum;
    }
    public static boolean Verify_Sum(byte[] bytes,byte checkSum) {
        if(bytes==null) return false;

        byte sum=0x00;
        for(int i=1;i< (bytes.length-2);i++) {
            sum^=bytes[i];
        }
        if(sum==checkSum) {
            return true;
        }else {
            return false;
        }
    }
    public static boolean Verify_DataFrame(byte[] bytes) {

        if(bytes==null) return false;

        byte stx=bytes[0];
        byte eot=bytes[bytes.length-1];
        byte sum=bytes[bytes.length-2];
        int len= Protocol.GetLength(bytes[0],bytes[1]);
        byte checkSum=0x00;
        for(int i=1;i< (bytes.length-2);i++) {
            checkSum^=bytes[i];
        }

        if((stx==STX)&&(eot==EOT)&&(sum==checkSum)) {
            return true;
        }else {
            return false;
        }
    }
    public static byte Verifed_DataFrame(byte[] bytes) {

        if(bytes==null)
            return  FRAME_STATUS.TIMEOUT;

        if(bytes.length==0)
            return  FRAME_STATUS.TIMEOUT;

        if(bytes.length<5)
            return  FRAME_STATUS.LENGTH_ERROR;

        byte stx=bytes[0];
        byte eot=bytes[bytes.length-1];
        byte sum=bytes[bytes.length-2];
        int len= Protocol.GetLength(bytes[0],bytes[1]);
        byte checkSum=0x00;
        for(int i=1;i< (bytes.length-2);i++) {
            checkSum^=bytes[i];
        }
        if(stx!=STX){
            return FRAME_STATUS.STX_ERROR;
        }
        if(eot!=EOT){
            return FRAME_STATUS.SUM_ERROR;
        }
        if(sum!=checkSum){
            return FRAME_STATUS.SUM_ERROR;
        }
        if((stx==STX)&&(eot==EOT)&&(sum==checkSum)) {
            return FRAME_STATUS.CORRECT;
        }else {
            return (byte) FRAME_STATUS.INCORRECT;
        }
    }
    /*Version Format Protocol*/
    public static byte[] prepareData_Protocol_V1(byte cmd,byte deviceId,byte[] bytes){
        byte[] sBytes=new byte[1024*4];
        int idx=0;

        sBytes[idx++]=Protocol.STX;
        sBytes[idx++]=deviceId;
        sBytes[idx++]=0x00;
        sBytes[idx++]=0x00;
        sBytes[idx++]=cmd;

        if(bytes!=null) {
            for (int i = 0; i < bytes.length; i++) {
                sBytes[idx++] = (byte) bytes[i];
            }
        }

        sBytes[2]=(byte) ((idx-4)>>8);
        sBytes[3]=(byte) ((idx-4)&0xff);

        byte sum=0x00;
        for(int i=1;i<idx;i++)
        {
            sum^=sBytes[i];
        }
        sBytes[idx++]=sum;
        sBytes[idx++]=Protocol.EOT;

        byte[] b = Arrays.copyOf(sBytes, idx);
        return b;
    }
    public static byte[] prepareData_Protocol_V2(byte cmd,byte frameId,byte deviceType,byte deviceId,byte answer,byte status,byte[] bytes){
        byte[] sBytes=new byte[1024*4];
        int idx=0;

        sBytes[idx++]=Protocol.STX;
        sBytes[idx++]=0x00;
        sBytes[idx++]=0x00;
        sBytes[idx++]=cmd;
        sBytes[idx++]=frameId;
        sBytes[idx++]=deviceType;
        sBytes[idx++]=deviceId;
        //Version
        sBytes[idx++]=0x00;
        sBytes[idx++]=0x00;
        sBytes[idx++]=0x00;
        sBytes[idx++]=0x00;

        sBytes[idx++]=0x00;
        sBytes[idx++]=0x00;
        sBytes[idx++]=0x00;
        sBytes[idx++]=0x00;
        sBytes[idx++]=answer;
        sBytes[idx++]=status;

        if(bytes!=null) {
            for (int i = 0; i < bytes.length; i++) {
                int d=(bytes[i]&0xFF);
                sBytes[idx++] = (byte) d;
            }
        }

        sBytes[1]=(byte) ((idx-3)>>8);
        sBytes[2]=(byte) ((idx-3)&0xff);

        byte sum=0x00;
        for(int i=1;i<idx;i++)
        {
            sum^=sBytes[i];
        }
        sBytes[idx++]=sum;
        sBytes[idx++]=Protocol.EOT;

        byte[] b = Arrays.copyOf(sBytes, idx);
        return b;
    }
    public static byte[] prepareData_Protocol_V3(byte cmd,byte frameId,byte deviceType,byte deviceId,byte[] bytes){
        byte[] sBytes=new byte[1024*4];
        int idx=0;

        sBytes[idx++]=Protocol.STX;
        sBytes[idx++]=0x00;
        sBytes[idx++]=0x00;
        sBytes[idx++]=0x00;
        sBytes[idx++]=0x00;
        sBytes[idx++]=cmd;
        sBytes[idx++]=frameId;
        sBytes[idx++]=deviceType;
        sBytes[idx++]=deviceId;

        if(bytes!=null) {
            for (int i = 0; i < bytes.length; i++) {
                sBytes[idx++] = (byte) bytes[i];
            }
        }

        sBytes[1]=(byte) (((idx-5)>>24)&0xff);
        sBytes[2]=(byte) (((idx-5)>>16)&0xff);
        sBytes[3]=(byte) (((idx-5)>>8)&0xff);
        sBytes[4]=(byte) ((idx-5)&0xff);

        byte sum=0x00;
        for(int i=1;i<idx;i++)
        {
            sum^=sBytes[i];
        }
        sBytes[idx++]=sum;
        sBytes[idx++]=Protocol.EOT;

        byte[] b = Arrays.copyOf(sBytes, idx);
        return b;
    }
    public static byte[] PrepareData_Protocol_V4(byte cmd,byte deviceId,byte[] bytes){
        byte[] sBytes=new byte[1024*10];
        int idx=0;

        sBytes[idx++]=Protocol.STX;
        sBytes[idx++]=0x00;
        sBytes[idx++]=0x00;
        sBytes[idx++]=deviceId;
        sBytes[idx++]=cmd;

        if(bytes!=null) {
            for (int i = 0; i < bytes.length; i++) {
                sBytes[idx++] = (byte) bytes[i];
            }
        }

        sBytes[1]=(byte) ((idx-3)>>8);
        sBytes[2]=(byte) ((idx-3)&0xff);

        byte sum=0x00;
        for(int i=1;i<idx;i++)
        {
            sum^=sBytes[i];
        }
        sBytes[idx++]=sum;
        sBytes[idx++]=Protocol.EOT;

        byte[] b = Arrays.copyOf(sBytes, idx);
        return b;
    }
    public static byte[] PrepareData_Protocol_V5(byte cmd,byte[] bytes){
        byte[] sBytes=new byte[1024*10];
        int idx=0;

        sBytes[idx++]=Protocol.STX;
        sBytes[idx++]=0x00;
        sBytes[idx++]=0x00;
        sBytes[idx++]=(byte) cmd;

        if(bytes!=null) {
            for (int i = 0; i < bytes.length; i++) {
                sBytes[idx++] = (byte) bytes[i];
            }
        }

        sBytes[1]=(byte) ((idx-3)>>8);
        sBytes[2]=(byte) ((idx-3)&0xff);

        byte sum=0x00;
        for(int i=1;i<idx;i++)
        {
            sum^=sBytes[i];
        }
        sBytes[idx++]=(byte) sum;
        sBytes[idx++]=Protocol.EOT;

        byte[] b = Arrays.copyOf(sBytes, idx);
        return b;
    }
    public static byte[] PrepareData_Protocol_V6(byte cmd,byte deviceId,byte[] bytes){
        byte[] sBytes=new byte[1024*10];
        int idx=0;

        sBytes[idx++]=Protocol.STX;
        sBytes[idx++]=0x00;
        sBytes[idx++]=0x00;
        sBytes[idx++]=(byte) cmd;
        sBytes[idx++]=(byte)deviceId;
        if(bytes!=null) {
            for (int i = 0; i < bytes.length; i++) {
                sBytes[idx++] = (byte) bytes[i];
            }
        }

        sBytes[1]=(byte) ((idx-3)>>8);
        sBytes[2]=(byte) ((idx-3)&0xff);

        byte sum=0x00;
        for(int i=1;i<idx;i++)
        {
            sum^=sBytes[i];
        }
        sBytes[idx++]=(byte) sum;
        sBytes[idx++]=Protocol.EOT;

        byte[] b = Arrays.copyOf(sBytes, idx);
        return b;
    }
    public static byte[] Answer_Protocol_V2(byte cmd,byte frameId,byte deviceType,byte deviceId,byte ans) {

        return Protocol.prepareData_Protocol_V2(cmd,frameId,deviceType,deviceId,ans, RESPONSE_STATUS.SUCCESS,null);
    }



}
