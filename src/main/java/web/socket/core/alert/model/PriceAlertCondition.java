package web.socket.core.alert.model;

public class PriceAlertCondition {

    public String id;             // 알림 ID (UUID 등)
    public String symbol;         // 예: "BTCUSDT"
    public PriceAlertType type;   // RANGE_IN, CROSS_UP, CROSS_DOWN

    // RANGE_IN용
    public Double lowerPrice;     // 하한(예: 98)
    public Double upperPrice;     // 상한(예: 103)

    // CROSS_UP / CROSS_DOWN용
    public Double targetPrice;    // 목표 가격 (예: 103 또는 98)

    public boolean oneTime;       // 한 번 울리고 비활성화 할지 여부
    public boolean active;        // 활성/비활성 상태
}
