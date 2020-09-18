package th.co.infinitecorp.www.qcontroller.EventBus;

public class BufferDataEvent {
    byte[] bytes;

    public BufferDataEvent(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
