package web.socket.infra.binance;

import lombok.RequiredArgsConstructor;

import java.net.http.HttpClient;
import java.net.http.WebSocket;

@RequiredArgsConstructor
public class BinanceTradeWsClient {

    private static final String BASE_WS_URL = "wss://stream.binance.com:9443/ws/";

    private final HttpClient httpClient;
    private final BinanceTradeParser binanceTradeParser;

    private WebSocket webSocket;
    private volatile boolean running = false;
}
