package cx.leo.simplychat.format;

import net.kyori.adventure.text.event.ClickEvent;

import java.util.List;

public record FormatActions(String id, ClickEvent clickEvent, List<String> hoverText) {}