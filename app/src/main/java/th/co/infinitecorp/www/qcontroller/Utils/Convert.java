package th.co.infinitecorp.www.qcontroller.Utils;

public class Convert {

    /*Byte & Hex String*/
   /*
    public static byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }
    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }
    public static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }
    public static   String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(byteToHex(byteArray[i]));
        }
        return hexStringBuffer.toString();
    }
    public static byte[] decodeHexString(String hexString) {
        if (hexString.length() % 2 == 1) {
            throw new IllegalArgumentException(
                    "Invalid hexadecimal String supplied.");
        }

        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
        }
        return bytes;
    }
    */
    /*Byte & Char*/
    public  static char[] ByteToCharArray(byte[] bytes) {
        char[] chars=new char[bytes.length];
        for(int i=0;i < bytes.length;i++){
            chars[i]=(char)((byte)bytes[i]&0xff);
        }
        return  chars;
    }
    public  static byte[] CharToByteArray(char[] chars) {
        byte[] bytes=new byte[chars.length];
        for(int i=0;i < chars.length;i++){
            bytes[i]=(byte) chars[i];
        }
        return  bytes;
    }
    public static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }
    public static int ByteToUnsignedInt(byte x) {
        return ((int) x) & 0xff;
    }
    public static byte unsignedByte(byte bytes) {

        if(bytes<0) {
            return (byte) ((256-bytes)*(-1));
        }else {
            return  bytes;
        }

    }

    /*Queue Converter*/
    public static byte GetByteHigh(Short s)
    {
      return (byte)((s>>8)&0xFF);
    }
    public static byte GetByteLow(Short s)
    {
        return (byte)(s&0xFF);
    }
    public static byte GetByteHigh(int s)
    {
        return (byte)((s>>8)&0xFF);
    }
    public static byte GetByteLow(int s)
    {
        return (byte)(s&0xFF);
    }
    public static short GetQNo(byte qH,byte qL)
    {
        return (short) (((((byte)qH&0xFF)<<8)|((byte)qL&0xFF)));
    }
    public static short GetShort(byte bh,byte bl)
    {
        return (short) (((((byte)bh&0xFF)<<8)|((byte)bl&0xFF)));
    }

    public static String QNumberToString(byte qType,byte qAlp,short qNum) {
        short queNum = qNum;
        String qStr="";
        qStr=""+qNum;
        //0=None,1=4Digit,2=3Digit,3=NoZero ,4=5Digit
        if (qType == 1)//4DG
        {
            qStr=String.format("%0$4s", Integer.toString(qNum,10) ).replace(" ","0");
        }
        else if (qType == 3)//NoZero
        {
            qStr=String.valueOf(qNum);
        }
        else if (qType == 4)//5 DG
        {
            qStr=String.format("%0$5s", Integer.toString(qNum,10) ).replace(" ","0");
        }
        else {
            qStr=String.format("%0$3s", Integer.toString(qNum,10) ).replace(" ","0");
        }
        if (qAlp > 0)
            return ((char)qAlp) + qStr;

        return qStr;
    }
    public static byte[] QueueNoToByteArray(String queueNo) {
        //QType->0=None,1=4Digit,2=3Digit,3=NoZero ,4=5Digit,5=6Digit
        byte qType=0x00;
        byte qAlp=0x00;
        byte qH=0x00;
        byte qL=0x00;
        int qNum;
        String strQNum="";
        byte[] bytes=new byte[4];

        int len=queueNo.length();
        char[] chars=new char[len];
        for(int i=0;i<len;i++) {
            chars[i] = queueNo.charAt(i);
        }
        if(len==6) {
            if (chars[0] >= 0x41) {
                //found Alphabet
                qType = 4; //5DG
                qAlp=(byte) chars[0];
                strQNum=queueNo.replace(chars[0],' ');
                strQNum=strQNum.trim();

            } else {
                qType = 5; //6DG
                strQNum=queueNo;
                strQNum=strQNum.trim();

            }
        }else if(len==5) {
            if (chars[0] >= 0x41) {
                //found Alphabet
                qType = 1; //4DG
                qAlp=(byte) chars[0];
                strQNum=queueNo.replace(chars[0],' ');
                strQNum=strQNum.trim();
            } else {
                qType = 4;//5DG
                strQNum=queueNo;
                strQNum=strQNum.trim();
            }
        }else if(len==4){
            if (chars[0] >= 0x41) {
                //found Alphabet
                qType = 2; //3DG
                qAlp=(byte) chars[0];
                strQNum=queueNo.replace(chars[0],' ');
                strQNum=strQNum.trim();
            } else {
                qType = 1;//4DG
                strQNum=queueNo;
                strQNum=strQNum.trim();
            }
        }else if(len==3){
            if (chars[0] >= 0x41) {
                //found Alphabet
                qType = 3; //NoZero
                qAlp=(byte) chars[0];
                strQNum=queueNo.replace(chars[0],' ');
                strQNum=strQNum.trim();
            } else {
                qType = 2;//3DG
                strQNum=queueNo;
                strQNum=strQNum.trim();
            }
        }else if (len==2){
            if (chars[0] >= 0x41) {
                //found Alphabet
                qType = 3; //NoZero
                qAlp=(byte) chars[0];
                strQNum=queueNo.replace(chars[0],' ');
                strQNum=strQNum.trim();
            } else {
                qType = 3;//NoZero
                strQNum=queueNo;
                strQNum=strQNum.trim();
            }
        }else {
            if (chars[0] >= 0x41) {
                //found Alphabet
                qType = 3; //NoZero
                qAlp=(byte) chars[0];
                strQNum=queueNo.replace(chars[0],' ');
                strQNum=strQNum.trim();
            } else {
                qType = 3;//NoZero
                strQNum=queueNo;
                strQNum=strQNum.trim();
            }
        }
        qNum=Integer.valueOf(strQNum);
        qH=(byte)(qNum>>8);
        qL=(byte)(qNum&0xff);

        bytes[0]=qType;
        bytes[1]=qAlp;
        bytes[2]= qH;
        bytes[3]= qL;

        return  bytes;
    }

    /*ByteArrayToHexString*/
    public  static  String ByteToHexString(byte bytes) {
        String str="";
        StringBuilder sb = new StringBuilder(2);
        sb.append(String.format("%02x", bytes));
        str=sb.toString();
        return str;
    }
    public  static  String ByteArrayToHexStringWithSpace(byte[] bytes) {

        String str="";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for(byte b: bytes) {
            sb.append(String.format("%02x", b));
            sb.append(" ");
        }
        str= sb.toString();

        return str;
    }
}
