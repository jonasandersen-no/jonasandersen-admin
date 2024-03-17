package no.jonasandersen.admin.adapter.out.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import no.jonasandersen.admin.ShortcutHtmlFormatter;
import no.jonasandersen.admin.core.shortcut.domain.Shortcut;
import no.jonasandersen.admin.core.shortcut.port.Broadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketBroadcaster extends TextWebSocketHandler implements Broadcaster {

  private static final Logger logger = LoggerFactory.getLogger(WebSocketBroadcaster.class);

  private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
  private Optional<TextMessage> currentTextMessage = Optional.empty();

  @Override
  public void onShortcutCreated(Shortcut shortcut) {
    String html = ShortcutHtmlFormatter.tableRow(shortcut);

    TextMessage textMessage = new TextMessage(html);
    currentTextMessage = Optional.of(textMessage);
    sessionMap.values().forEach(session -> sendMessageViaWebSocket(session, textMessage));
  }

  @Override
  public void onShortcutUpdated(Shortcut shortcut) {
    String html = ShortcutHtmlFormatter.tableRow(shortcut);

    TextMessage textMessage = new TextMessage(html);
    currentTextMessage = Optional.of(textMessage);
    sessionMap.values().forEach(session -> sendMessageViaWebSocket(session, textMessage));
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    logger.info("Websocket connection established, session ID: {}, session remote address: {}",
        session.getId(), session.getRemoteAddress());
    sessionMap.put(session.getId(), session);
    currentTextMessage.ifPresent(message -> sendMessageViaWebSocket(session, message));
  }


  private void sendMessageViaWebSocket(WebSocketSession session, TextMessage message) {
    try {
      logger.info("Sending message to session: {}", session.getId());
      session.sendMessage(message);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
      throws Exception {
    logger.info("Websocket Text Message received from session: {}, with message: {}",
        session.toString(), message.toString());
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
      throws Exception {
    logger.info("Websocket connection closed, session ID: {}, close status: {}",
        session.getId(), closeStatus);
    try (WebSocketSession _ = sessionMap.remove(session.getId())) {
      logger.info("Removed session: {}", session.getId());
    }
  }
}
