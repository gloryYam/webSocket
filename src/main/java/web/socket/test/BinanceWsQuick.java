package web.socket.test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

public class BinanceWsQuick {
    public static void main(String[] args) throws Exception {

        String stream = "btcusdt@kline_1m";
        String url = "wss://stream.binance.com:9443/ws/" + stream;

        System.out.println("Connecting : " + url);

        HttpClient httpClient = HttpClient.newHttpClient();
        WebSocket ws = httpClient.newWebSocketBuilder()
                .buildAsync(URI.create(url), new WebSocket.Listener() {     // buildAsync 연결을 만들어라.
                                                                            // URI.create(url) 어디로 연결할지.
                                                                            // new WebSocket.Listener() 서버가 메세지를 보내면 어떻게 처리할지
                    @Override
                    public void onOpen(WebSocket webSocket) {
                        System.out.println("WS OPEN");
                        WebSocket.Listener.super.onOpen(webSocket);
                    }

                    @Override
                    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                        // 들어온 JSON 그대로 출력
                        System.out.println("MSG: " + data);
                        return WebSocket.Listener.super.onText(webSocket, data, last);
                    }

                    @Override
                    public void onError(WebSocket webSocket, Throwable error) {
                        System.out.println("WS ERROR: " + error.getMessage());
                    }

                }).join();

        // 프로그램이 바로 끝나지 않도록 대기
        Thread.sleep(30_000); // 60초 동안 데이터 보기
        ws.sendClose(WebSocket.NORMAL_CLOSURE, "bye");
    }
}
