package web.socket.infra.binance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
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


}