package web.socket.infra.binance;

import lombok.RequiredArgsConstructor;
import web.socket.core.market.TradeMessageListener;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;

@RequiredArgsConstructor
public class BinanceTradeWsClient {

    private static final String BASE_WS_URL = "wss://stream.binance.com:9443/ws/";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final BinanceTradeParser parser = new BinanceTradeParser();

    private WebSocket webSocket;
    private volatile boolean running = false;

    String buildStreamUrl(String symbol) {
        String stream = symbol.toLowerCase() + "@trade";
        return BASE_WS_URL + stream;
    }

    public boolean isRunning() {
        return running;
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

}
