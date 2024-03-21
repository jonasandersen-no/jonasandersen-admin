package no.jonasandersen.admin.adapter.out.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import no.jonasandersen.admin.core.shortcut.ShortcutHtmlFormatter;
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

  @Override
  // language=HTML
  public void onShortcutCreated(Shortcut shortcutFound) {
    var html = STR."""
        <tr hx-swap-oob="afterend:#table-body">
          <tr>
          <td>\{shortcutFound.project()}</td>
          <td>\{shortcutFound.shortcut()}</td>
          <td>\{shortcutFound.description()}</td>
          <td>
            <button
                hx-get="/hx/shortcut/edit/\{shortcutFound.id()}"
                hx-target="#list-item-\{shortcutFound.id()}"
                hx-swap="outerHTML"
                class="btn">
                Edit
              </button>
              <button
                hx-delete="/hx/shortcut/\{shortcutFound.id()}"
                hx-target="#list-item-\{shortcutFound.id()}"
                hx-swap="outerHTML"
                hx-confirm='Are you sure you want to delete this shortcut?'
                class="btn">
                Delete
              </button>
          </td>
          </tr>TAB
        </tr>

        """;

    sendMessage(html);
  }

  @Override
  public void onShortcutUpdated(Shortcut shortcut) {
    String html = ShortcutHtmlFormatter.asTableRow(shortcut);

    sendMessage(html);
  }

  @Override
  // language=HTML
  public void onShortcutDeleted(Long id) {
    var delete = STR."""
        <delete-element hx-swap-oob="delete" id="list-item-\{id}"/>
        """;
    sessionMap.values()
        .forEach(session -> sendMessageViaWebSocket(session, new TextMessage(delete)));
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    logger.info("Websocket connection established, session ID: {}, session remote address: {}",
        session.getId(), session.getRemoteAddress());
    sessionMap.put(session.getId(), session);
  }

  private void sendMessage(String html) {
    TextMessage textMessage = new TextMessage(html);
    sessionMap.values().forEach(session -> sendMessageViaWebSocket(session, textMessage));
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
