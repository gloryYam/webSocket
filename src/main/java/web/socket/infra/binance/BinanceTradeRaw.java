package web.socket.infra.binance;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 상태/재연결/종료 관리
 */
public class BinanceTradeRaw {

    @JsonProperty("s")
    public String symbol;

    @JsonProperty("p")
    public String price;

    @JsonProperty("E")
    public long eventTime;
}
