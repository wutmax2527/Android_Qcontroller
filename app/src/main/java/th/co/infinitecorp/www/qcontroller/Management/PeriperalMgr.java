package th.co.infinitecorp.www.qcontroller.Management;

import android.content.Context;

import java.net.Socket;

import th.co.infinitecorp.www.qcontroller.DataInfo.PeriperalInfo;
import th.co.infinitecorp.www.qcontroller.DataInfo.TcpSocketInfo;
import th.co.infinitecorp.www.qcontroller.Utils.GData;

public class PeriperalMgr {
    /*Status of Queue*/
    public static class status {

        final public static byte InActive  =  0;
        final public static byte ACTIVE = 1;
    }
    public static boolean HandlePeriperalIp(Context context, byte deviceType, byte deviceId, String ip, Socket socket) {
        PeriperalInfo periperalInfo = new PeriperalInfo();
        boolean found = false;
        Integer index = 0;
        if (GData.PeriperalInfos.size() > 0) {
            for (PeriperalInfo p : GData.PeriperalInfos) {
                if ((p.getDeviceType() == deviceType) && (p.getDeviceId() == deviceId)) {
                    periperalInfo = p;
                    periperalInfo.setIp(ip);
                    periperalInfo.setSocket(socket);
                    periperalInfo.setStatus((byte) status.ACTIVE);
                    found = true;
                    break;
                }
                index++;
            }
        }
        if (found) {
            if ((GData.PeriperalInfos.size() > 0) && (index < GData.PeriperalInfos.size())) {
                GData.PeriperalInfos.set(index, periperalInfo);
            }
            return true;//LogMgr.Save_PeriperalInfo(context, GData.PeriperalInfos);
        }
        return false;
    }

    public static boolean HandleTcpSocket(Context context, byte deviceType, byte deviceId, String ip, Socket socket) {
        TcpSocketInfo  tcpSocketInfo = new TcpSocketInfo();
        boolean found = false;
        Integer index = 0;
        if (GData.TcpSocketInfos.size() > 0) {
            for (TcpSocketInfo t : GData.TcpSocketInfos) {
                if ((t.getDeviceType() == deviceType) && (t.getDeviceId() == deviceId)) {
                    tcpSocketInfo = t;
                    tcpSocketInfo.setIp(ip);
                    tcpSocketInfo.setSocket(socket);
                    tcpSocketInfo.setStatus((byte) status.ACTIVE);
                    found = true;
                    break;
                }
                index++;
            }
        }
        if (found) {
            if ((GData.TcpSocketInfos.size() > 0) && (index < GData.TcpSocketInfos.size())) {
                GData.TcpSocketInfos.set(index, tcpSocketInfo);
            }
            return true;
        }
        return false;
    }
    public static boolean HandleTcpSocket_Connect(Context context ,String ip, Socket socket) {
        TcpSocketInfo  tcpSocketInfo = new TcpSocketInfo();
        boolean found = false;
        Integer index = 0;
        if (GData.TcpSocketInfos.size() > 0) {
            for (TcpSocketInfo t : GData.TcpSocketInfos) {
                if ((t.getIp() == ip)) {
                    tcpSocketInfo = t;
                    tcpSocketInfo.setIp(ip);
                    tcpSocketInfo.setSocket(socket);
                    tcpSocketInfo.setStatus((byte) status.ACTIVE);
                    found = true;
                    break;
                }
                index++;
            }
        }
        if (found) {
            if ((GData.TcpSocketInfos.size() > 0) && (index < GData.TcpSocketInfos.size())) {
                GData.TcpSocketInfos.set(index, tcpSocketInfo);
            }
            return true;
        }
        return false;
    }
    public static boolean HandleTcpSocket_Disconnect(Context context ,String ip, Socket socket) {
        TcpSocketInfo  tcpSocketInfo = new TcpSocketInfo();
        boolean found = false;
        Integer index = 0;
        if (GData.TcpSocketInfos.size() > 0) {
            for (TcpSocketInfo t : GData.TcpSocketInfos) {
                if ((t.getIp() == ip)) {
                    tcpSocketInfo = t;
                    tcpSocketInfo.setIp(ip);
                    tcpSocketInfo.setSocket(socket);
                    tcpSocketInfo.setStatus((byte) status.InActive);
                    found = true;
                    break;
                }
                index++;
            }
        }
        if (found) {
            if ((GData.TcpSocketInfos.size() > 0) && (index < GData.TcpSocketInfos.size())) {
                GData.TcpSocketInfos.set(index, tcpSocketInfo);
            }
            return true;
        }
        return false;
    }
}
