package th.co.infinitecorp.www.qcontroller.DataInfo.DATANOW;

import th.co.infinitecorp.www.qcontroller.Management.QueueMgr;
import th.co.infinitecorp.www.qcontroller.Utils.DateTime;

public class AllQueueInfo {

    /*
    int servid;
    int tranno;
    String req_datetime;
    byte division_id;
    int waitq;
    byte staopen;
    byte station_id;
    byte group_id;
    int user_id;
    int subdiv_id;
    int waitsec;
    int holdsec;
    int servsec;
    String sttime;
    int status;
    int gstaopen;
    String queueno;
    int queue_doc;
    int score;
    int upload_state;
    int ABANDON;
    String MOBILE;
    String QALP;
    String ENDTIME;

    String _holdtime;
    byte _pvDiv;
    int _qNum;	 	//qType-qAlp-qH-qL
    byte _printerNo;
    byte _button;
    int _priority_wait;

    int _device_id;
    byte _lang;
    int _current_waiting;
    int _current_waitsec;
    int _next_notify;
    int _next_send;
    QueueMgr.TransferToStatus _TransferToStatus;
    QueueMgr.TransferToTarget _TransferToTarget;
    long _customerID;

    */
    String reqDateTime;
    String stDateTime;
    String endDateTime;
    String queueNo;
    byte qType;
    byte qAlp;
    short qNum;
    short estWaitSec;
    Integer servid;
    Integer tranno;
    Integer userId;
    Integer  servesec;
    Integer  waitSec;
    Integer holdSec;

    short waitq;

    byte lang;
    byte divisionId;
    byte stationId;
    byte groupId;
    byte pvDiv;
    byte score;
    byte status;
    byte subdivId;
    byte staOpen;

}
