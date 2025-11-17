package web.socket.core.market;

public interface TradeMessageListener {

    void onMessage(LivePriceEvent event);

    void onError(Throwable t);
}
