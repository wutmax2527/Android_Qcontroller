package th.co.infinitecorp.www.qcontroller.DataInfo;

public class StationReserveInfo {
    int id;
    byte keyType;
    boolean isReserve;

    public StationReserveInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getKeyType() {
        return keyType;
    }

    public void setKeyType(byte keyType) {
        this.keyType = keyType;
    }

    public boolean isReserve() {
        return isReserve;
    }

    public void setReserve(boolean reserve) {
        isReserve = reserve;
    }
}
