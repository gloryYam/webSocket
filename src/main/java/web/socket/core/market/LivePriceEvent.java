package web.socket.core.market;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LivePriceEvent {

    public String symbol;   // 예: "BTCUSDT"
    public String price;    // 현재 체결 가격
    public long eventTime;  // 이벤트 발생 시간 (epoch ms)

    public LivePriceEvent(String symbol, String price, long eventTime) {
        this.symbol = symbol;
        this.price = price;
        this.eventTime = eventTime;
    }


}
