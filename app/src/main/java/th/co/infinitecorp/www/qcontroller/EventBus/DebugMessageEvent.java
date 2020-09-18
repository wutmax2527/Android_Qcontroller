package th.co.infinitecorp.www.qcontroller.EventBus;

public class DebugMessageEvent {
    String message;

    public DebugMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
