package com.cstor.tanjiance.utils;

import android.content.Context;

import com.cstor.tanjiance.listener.TaskListener;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocketUtil {
    public static WebSocketClient webSocketClient = null;
    public static Context s_context;
    private static TaskListener listener;

    public static WebSocketClient inistance(Context context) {
        if (webSocketClient == null) {
            init();
        }
//        init();
        s_context = context;
        return webSocketClient;
    }

    public static void addListener(TaskListener taskListener) {
        listener = taskListener;
    }

    private static void init() {
        URI serverURI = URI.create("ws://58.213.47.166:9014/test/oneToMany");

        webSocketClient = new WebSocketClient(serverURI) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                LogUtils.d("WebSocket已连接=============================");
            }

            @Override
            public void onMessage(String message) {
                LogUtils.d("收到服务器消息：" + message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                LogUtils.d("WebSocket已关闭");
            }

            @Override
            public void onError(Exception ex) {
                LogUtils.d("服务器状态：" + ex.toString());
            }
        };
    }

    public static void sendMsg(String s) {
        if (webSocketClient.isClosed() || webSocketClient.isClosing()) {
//            Snackbar.make(v,"Client正在关闭",Snackbar.LENGTH_SHORT).show();
            webSocketClient.connect();
        }
        webSocketClient.send(s);
        LogUtils.d("发送数据：" + s);
    }
    /**
     * 断开连接
     */
    public static void closeConnect() {
        try {
            if (null != webSocketClient) {
                webSocketClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webSocketClient = null;
        }
    }

}