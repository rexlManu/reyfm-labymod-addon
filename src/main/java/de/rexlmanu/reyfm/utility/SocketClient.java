package de.rexlmanu.reyfm.utility;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.concurrent.Executors;

public class SocketClient extends WebSocketClient {

    private Listener listener;

    public SocketClient(URI serverUri, Listener listener) {
        super(serverUri);
        this.listener = listener;
        this.setConnectionLostTimeout(0);
        this.setSocketFactory(Utils.sslContext.getSocketFactory());
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isOpen()) {
                        sendResponse();
                        try {
                            Thread.sleep(5 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to socket.");
        send("2probe");
    }

    @Override
    public void onMessage(String message) {
        if (message.startsWith("42")) {
            JsonArray array = HttpUtils.JSON_PARSER.parse(message.replaceFirst("42", "")).getAsJsonArray();
            this.listener.handle(array.get(0).getAsString(), array.get(1));
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Socket connection closed: " + reason + ", remotly: " + remote + ", code:" + code);
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reconnect();
            }
        });
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    private void sendResponse() {
        this.send("2");
    }

    public interface Listener {
        void handle(String channel, JsonElement element);
    }
}
