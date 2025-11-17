package web.socket.infra.binance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import web.socket.core.market.LivePriceEvent;

import java.io.IOException;

/**
 * JSON → DTO 파싱 담당
 */

public class BinanceTradeParser {

    private final ObjectMapper objectMapper;

    public BinanceTradeParser() {
        objectMapper = new ObjectMapper();
    }

    public LivePriceEvent parse(String json) throws IOException {

        BinanceTradeRaw raw = objectMapper.readValue(json, BinanceTradeRaw.class);

        return LivePriceEvent.builder()
                .symbol(raw.symbol)
                .price(raw.price)
                .eventTime(raw.eventTime)
                .build();
    }
}
