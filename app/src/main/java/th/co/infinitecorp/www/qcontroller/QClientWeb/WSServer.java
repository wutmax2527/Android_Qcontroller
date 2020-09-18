package th.co.infinitecorp.www.qcontroller.QClientWeb;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

import th.co.infinitecorp.www.qcontroller.EventBus.DebugMessageEvent;

public class WSServer extends WebSocketServer {

    private static final String TAG = WSServer.class.getSimpleName();
    private int port = 20500;
    WSClient wsClient;

    public WSServer( InetSocketAddress address ) {
        super( address );
        Log.d(TAG, "creating instance of local WSServer");
        EventBus.getDefault().post(new DebugMessageEvent("creating instance of local WSServer"));
        wsClient = new WSClient(port);
    }

    public WSServer(int port)
    {
        super( new InetSocketAddress( port ) );
        Log.d(TAG, "creating instance of local WSServer on port " + port);
        EventBus.getDefault().post(new DebugMessageEvent("creating instance of local WSServer on port " + port));
        wsClient = new WSClient(port);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.d(TAG, "connection Details: " + conn.getLocalSocketAddress() + "; " + conn.getRemoteSocketAddress());
        Log.d(TAG, "Connection successfully opened on port: " + this.getPort());
        EventBus.getDefault().post(new DebugMessageEvent("***Connection successfully opened on port: " + this.getPort()));
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.d(TAG, "Connection closed");
        EventBus.getDefault().post(new DebugMessageEvent("Connection closed"));
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Log.d(TAG, "***Received Message: " + message);
        EventBus.getDefault().post(new DebugMessageEvent("Received Message: " + message));

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.d(TAG, "onError()", ex);
        EventBus.getDefault().post(new DebugMessageEvent("onError()ex="+ ex));
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        EventBus.getDefault().post(new DebugMessageEvent("onStart()"));
    }
}
