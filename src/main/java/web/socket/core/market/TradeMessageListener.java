package web.socket.core.market;

@FunctionalInterface
public interface TradeMessageListener {

    void onMessage(LivePriceEvent event);

    default void onError(Throwable t) {}
    default void onClose() {}

}
