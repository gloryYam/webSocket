package web.socket.core.alert.model;

// ( 실제 발생한 알림 )
public class PriceAlertEvent {

    public String conditionId;    // 어떤 조건에 의해 발생했는지
    public String symbol;
    public PriceAlertType type;   // RANGE_IN / CROSS_UP / CROSS_DOWN

    public double triggerPrice;   // 트리거 기준 가격 (예: 103)
    public double currentPrice;   // 실제 체결된 현재 가격
    public long eventTime;

    public String message;        // 카톡에 보낼 메시지 (문장)
}
