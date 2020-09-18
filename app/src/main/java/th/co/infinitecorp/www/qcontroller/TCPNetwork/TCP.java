package th.co.infinitecorp.www.qcontroller.TCPNetwork;

public class TCP {
    public class Server{
        public class ListenPort {
            final public static int Mobile = 20000;
            final public static int INNET = 20001;
            final public static int KEYPAD = 20002;
            final public static int DISPLAY = 20003;
            final public static int QTOUCH = 20004;
            final public static int QDISPLAY = 20005;
            final public static int Periperal = 20006;
            final public static int SoftkeyCMD = 6000;
            final public static int SoftkeyUpdate = 9915;
            final public static int Web = 8080;
            final public static int WS_QClientWeb = 20500;
            final public static int QClientWeb = 20501;
            final public static int WS_QTouchWeb = 20600;
            final public static int QTouchWeb = 20601;
        }
    }
    public class Client{
        public class TargetPort {
            final public static int TEST= 21111;
            final public static  int KEYPAD= 20102;
            final public static  int DISPLAY= 20103;
            final public static  int QTOUCH= 20104;
            final public static  int QDISPLAY= 20105;
            final public static  int Periperal= 20106;
        }
    }
}
