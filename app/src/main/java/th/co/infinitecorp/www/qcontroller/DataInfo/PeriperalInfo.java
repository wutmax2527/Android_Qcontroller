package th.co.infinitecorp.www.qcontroller.DataInfo;

import java.net.Socket;

public class PeriperalInfo {
    Integer id;
    byte deviceType;
    byte deviceId;
    String ip;
    byte status;
    Socket socket;

    public  PeriperalInfo(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(byte deviceType) {
        this.deviceType = deviceType;
    }

    public byte getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(byte deviceId) {
        this.deviceId = deviceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
