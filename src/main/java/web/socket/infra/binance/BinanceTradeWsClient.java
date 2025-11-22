package web.socket.infra.binance;

import lombok.RequiredArgsConstructor;
import web.socket.core.market.LivePriceEvent;
import web.socket.core.market.TradeMessageListener;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

@RequiredArgsConstructor
public class BinanceTradeWsClient {

    private static final String BASE_WS_URL = "wss://stream.binance.com:9443/ws/";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final BinanceTradeParser parser = new BinanceTradeParser();

    private WebSocket webSocket;
    private volatile boolean running = false;

    public boolean isRunning() {
        return running;
    }

    String buildStreamUrl(String symbol) {
        String stream = symbol.toLowerCase() + "@trade";
        return BASE_WS_URL + stream;
    }


    public void connect(String symbol, TradeMessageListener listener) {

        if(running) {
            throw new IllegalStateException("이미 연결된 WebSocket 입니다.");
        }

        String url = buildStreamUrl(symbol);

        this.running = true;

        this.webSocket = httpClient.newWebSocketBuilder()
                .buildAsync(URI.create(url), new DummyWebSocketListner())
                .join();
    }

    public void disconnect() {

        if(!running) {
            return;
        }

        running = false;

        if(webSocket != null) {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "client close");
            webSocket = null;
        }
    }


    private static class TradeWebSocketListener implements WebSocket.Listener {

        private final String url;
        private final BinanceTradeParser parser;
        private final TradeMessageListener listener;

        public TradeWebSocketListener(String url, BinanceTradeParser parser, TradeMessageListener listener) {
            this.url = url;
            this.parser = parser;
            this.listener = listener;
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {

            try {
                LivePriceEvent event = parser.parse(data.toString());
            } catch (Exception e) {
                listener.onError(e);
            }

            return null;
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            listener.onClose();
            return null;
        }
    }
}
