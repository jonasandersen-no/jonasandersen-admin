package no.jonasandersen.admin;

import no.jonasandersen.admin.adapter.out.websocket.WebSocketBroadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final WebSocketBroadcaster webSocketBroadcaster;

    @Autowired
    public WebSocketConfiguration(WebSocketBroadcaster webSocketBroadcaster) {
        this.webSocketBroadcaster = webSocketBroadcaster;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketBroadcaster, "/ws/shortcut");
    }

}
