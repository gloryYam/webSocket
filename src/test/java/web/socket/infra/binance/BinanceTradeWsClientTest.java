package web.socket.infra.binance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import web.socket.core.market.LivePriceEvent;
import web.socket.core.market.TradeMessageListener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BinanceTradeWsClientTest {

    @Test
    void test1() {

        BinanceTradeWsClient client = new BinanceTradeWsClient();

        String url = client.buildStreamUrl("BTCUSDT");

        assertThat(url).isEqualTo("wss://stream.binance.com:9443/ws/btcusdt@trade");
    }

    @Test
    void test2() {

        BinanceTradeWsClient client = new BinanceTradeWsClient();

        assertThat(client.isRunning()).isFalse();
    }

    @Test
    void test3() {

        BinanceTradeWsClient client = new BinanceTradeWsClient();

        client.connect("BTCUSDT", event -> {});

        assertThat(client.isRunning()).isTrue();
    }

    @Test
    void test4() {

        BinanceTradeWsClient client = new BinanceTradeWsClient();
        TradeMessageListener listener = event -> {};

        client.connect("BTCUSDT", listener);

        assertThatThrownBy(() -> client.connect("BTCUSDT", listener))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 연결된 WebSocket 입니다.");
    }

    @Test
    void disconnect_shouldSetRunningFalse() {

        BinanceTradeWsClient client = new BinanceTradeWsClient();

        client.connect("BTCUSDT", event -> {});

        client.disconnect();

        assertThat(client.isRunning()).isFalse();
    }


    @Test
    void disconnect_shouldBeIdempotent() {
        BinanceTradeWsClient client = new BinanceTradeWsClient();
        client.connect("BTCUSDT", event -> {});

        client.disconnect();
        client.disconnect(); // 두 번째 호출도 예외 없어야

        assertThat(client.isRunning()).isFalse();
    }


    @Test
    void onText_shouldParseJsonAndDeliverEventToListener() {
        BinanceTradeParser parser = new BinanceTradeParser();
        CollectingListener listener  = new CollectingListener();

        BinanceTradeWsClient.TradeWebSocketListener wsListener =
                new BinanceTradeWsClient.TradeWebSocketListener(
                        "wss://stream.binance.com:9443/ws/btcusdt@trade",
                        parser,
                        listener
                );

        String jsonData = """
                        {
                          "s": "BTCUSDT",
                          "p": "50000.12",
                          "E": 1700000000000
                        }
                        """;

        // when
        wsListener.onText(null, jsonData, true);

        // then
        assertThat(listener.lastEvent).isNotNull();
        assertThat(listener.lastEvent.getSymbol()).isEqualTo("BTCUSDT");
        assertThat(listener.lastEvent.getPrice()).isEqualTo("50000.12");
        assertThat(listener.lastEvent.getEventTime()).isEqualTo(1700000000000L);
    }

    @Test
    void isRunning_shouldBeFalseInitially() {
        BinanceTradeWsClient client = new BinanceTradeWsClient();

        assertThat(client.isRunning()).isFalse();
    }

    @Test
    void connect_shouldSetRunningTrue() {
        BinanceTradeWsClient client = new BinanceTradeWsClient();

        TradeMessageListener listener = event -> {}; // no-op

        client.connect("BTCUSDT", listener);

        assertThat(client.isRunning()).isTrue();
    }

    @Test
    void connect_shouldNotAllowSecondConnect() {
        BinanceTradeWsClient client = new BinanceTradeWsClient();
        TradeMessageListener listener = event -> {};

        client.connect("BTCUSDT", listener);

        assertThatThrownBy(() -> client.connect("BTCUSDT", listener))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 연결된 WebSocket");
    }


    @Test
    void serverClose_shouldSetRunningFalse() {

        BinanceTradeWsClient client = new BinanceTradeWsClient();
        TradeMessageListener listener = event -> {};

        // 강제로 running=true 상태로 만들기
        client.connect("BTCUSDT", listener);

        // WebSocketListener를 직접 생성해서 onClose 호출하기
        BinanceTradeWsClient.TradeWebSocketListener wsListener =
                new BinanceTradeWsClient.TradeWebSocketListener(
                        "dummy-url",
                        new BinanceTradeParser(),
                        listener,
                        () -> client.markClosedByServer()  // 나중에 만들 함수
                );

        // when
        wsListener.onClose(null, 1000, "server closed");

        // then
        assertThat(client.isRunning()).isFalse();
    }

    static class CollectingListener implements TradeMessageListener {
        LivePriceEvent lastEvent;

        @Override
        public void onMessage(LivePriceEvent event) {
            this.lastEvent = event;
        }
    }
}



