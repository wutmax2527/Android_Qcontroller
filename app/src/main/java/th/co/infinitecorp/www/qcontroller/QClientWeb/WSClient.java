package th.co.infinitecorp.www.qcontroller.QClientWeb;

import android.os.Build;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class WSClient {
    public String message;
    private static final String TAG = WSClient.class.getSimpleName();

    private WebSocketClient mWebSocketClient;
    private int port = 38301;

    public WSClient(){
        connectWebSocket();
    }
    public WSClient(int port) {
        Log.d(TAG, "starting local WSClient");
        if(port != 0){
            this.port = port;
        }
        connectWebSocket();
    }

    private void connectWebSocket() {
        Log.d(TAG, "establishing Connection to local WSServer");
        URI uri;
        try {
            uri = new URI("ws://localhost:" + port);
        } catch (URISyntaxException e) {
            Log.e(TAG, "uri-error: " + "ws://localhost:" + port);
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.d(TAG, "Opened");
                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(String s) {
                Log.d(TAG, "received Message: " + s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.d(TAG, "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

    public void sendMessage(String message) {

        if(mWebSocketClient != null) {
            mWebSocketClient.send(message);
        }
        else {
            Log.e(TAG, "No WebSocketClient available. Trying to reconnect and create new Client instance");
            connectWebSocket();
        }
    }
}
