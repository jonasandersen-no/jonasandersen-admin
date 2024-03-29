package no.jonasandersen.admin.adapter.out.websocket;

import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
  public static final String SHORTCUT = "shortcut/";
  public static final String HX = "hx/";

  private final TemplateEngine jteTemplateEngine;

  private final Map<String, List<WebSocketSession>> sessionMap = new ConcurrentHashMap<>();

  public WebSocketBroadcaster(TemplateEngine jteTemplateEngine) {
    this.jteTemplateEngine = jteTemplateEngine;
  }

  @Override
  public void onShortcutCreated(Shortcut shortcut) {

    String html = generateHtml("insertTable.jte", Model.from(shortcut));

var est =     STR."""
        <tbody id="target-body">
            <tr>
                <td>\{shortcut.shortcut()}</td>
                <td>\{shortcut.description()}</td>
                <td>
                    <button
                            hx-get="/hx/shortcut/edit/\{shortcut.id()}"
                            hx-target="#list-item-\{shortcut.id()}"
                            hx-swap="outerHTML"
                            class="btn">
                        Edit
                    </button>
                    <button
                            hx-delete="/hx/shortcut/\{shortcut.id()}"
                            hx-target="#list-item-\{shortcut.id()}"
                            hx-swap="outerHTML"
                            hx-confirm='Are you sure you want to delete this shortcut?'
                            class="btn">
                        Delete
                    </button>
                </td>
            </tr>
        </tbody>
        """;
    sendMessage(html.replaceAll("\r\n", "").replaceAll("\n", ""));
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
    sessionMap
        .forEach((key, value) -> sendMessageViaWebSocket(key, value, new TextMessage(delete)));
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    logger.info("Websocket connection established, session ID: {}, project: {}",
        session.getId(), getProject(session));

    if (sessionMap.containsKey(getProject(session))) {
      sessionMap.get(getProject(session)).add(session);
    } else {
      List<WebSocketSession> socketSessions = new ArrayList<>();
      socketSessions.add(session);
      sessionMap.put(getProject(session), Collections.synchronizedList(socketSessions));
    }
  }

  static String getProject(WebSocketSession session) {
    String path = Objects.requireNonNull(session.getUri()).getPath();
    List<String> list = Arrays.stream(path.split("/")).toList();
    return list.getLast();
  }

  private void sendMessage(String html) {
    TextMessage textMessage = new TextMessage(html);
    sessionMap.forEach((key, value) -> sendMessageViaWebSocket(key, value, textMessage));
  }

  private void sendMessageViaWebSocket(String project, List<WebSocketSession> sessionList,
      TextMessage message) {
    for (WebSocketSession session : sessionList) {
      try {
        logger.info("Sending message to project {} with session: {}", project, session.getId());
        session.sendMessage(message);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
    logger.info("Websocket Text Message received from session: {}, with message: {}",
        session.toString(), message.toString());
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
    logger.info("Websocket connection closed, session ID: {}, close status: {}",
        session.getId(), closeStatus);

    sessionMap.entrySet().stream()
        .filter(entry -> entry.getValue().contains(session))
        .forEach(entry -> entry.getValue().remove(session));

    logger.info("Removed session: {}", session.getId());
  }

  String generateHtml(String jteFileName, Model model) {
    StringOutput stringOutput = new StringOutput();
    jteTemplateEngine.render(SHORTCUT + HX + jteFileName, model.model(), stringOutput);
    return stringOutput.toString();
  }

}
