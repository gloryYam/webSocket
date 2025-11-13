package web.socket.core.market;

public class LivePriceEvent {

    public String symbol;   // 예: "BTCUSDT"
    public double price;    // 현재 체결 가격
    public long eventTime;  // 이벤트 발생 시간 (epoch ms)
}
