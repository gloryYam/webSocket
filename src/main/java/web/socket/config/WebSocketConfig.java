package web.socket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 *  WebSocketConfigurer > WebSocket 엔드포인트를 등록하는 설정 "인터페이스"
 */

@Configuration
@Slf4j
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        log.info( "Registering WebSocket handler at /ws" );

        DefaultHandshakeHandler defaultHandshakeHandler = new DefaultHandshakeHandler();    // DefaultHandshakeHandler > 스프링에서 기본적으로 등록해줌 하지만 직접 등록하는 이유는 나중에 커스트마이징을 위해
                                                                                            // 왜? 핸드셰이크 과정에서 인증/권한 체크, 로그 기록 등의 로직을 추가하고 싶으면
                                                                                            // DefaultHandshakeHandler 를 상속받아서 CustomHandshakeHandler 를 만들어 등록하면 된다.

        registry.addHandler(webSocketHandler, "ws-stocks")
                .setAllowedOrigins("*")
                .setHandshakeHandler(defaultHandshakeHandler);

        log.info( "WebSocket handler registered successfully with dedicated thread pool" );
    }

    /**
     * ServletServerContainerFactoryBean 을 나중에 고려해보기
     * WebSocket 의 기본 설정(환경)을 바꿀 수 있게 해줌 >
     * ( size )textMessage, binaryMessage / (timeOut) session, async
     */
}
