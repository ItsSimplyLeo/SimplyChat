package cx.leo.simplychat.utils;

import cx.leo.simplychat.SimplyChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class ComponentUtils {

    private final static MiniMessage MINI_RAW = MiniMessage.miniMessage();

    private final static MiniMessage MINI_COMMON = MiniMessage.builder()
            .tags(TagResolver.resolver(
                    TagResolver.standard(),
                    TagResolver.resolver("weblink", ComponentUtils::weblink)
            ))
            .build();

    private final static MiniMessage MINI_CHAT = MiniMessage.builder()
            .tags(TagResolver.resolver(
                    TagResolver.standard(),
                    TagResolver.resolver("chat", ComponentUtils::chat)
            ))
            .build();

    public static MiniMessage miniRaw() {
        return MINI_RAW;
    }

    public static MiniMessage miniCommon() {
        return MINI_COMMON;
    }

    public static MiniMessage miniChat() {
        return MINI_CHAT;
    }

    // "Hello, <weblink:https://github.com/ItsSimplyLeo>click me!</a> but not me!"
    public static Tag weblink(final ArgumentQueue queue, final Context ctx) {
        final String link = queue.popOr("The <weblink> tag requires exactly one argument, the link to open").value();

        return Tag.styling(
                NamedTextColor.BLUE,
                TextDecoration.UNDERLINED,
                ClickEvent.openUrl(link),
                HoverEvent.showText(Component.text("Open " + link))
        );
    }

    // <chat:<FORMAT>:<ACTIONSID>>
    public static Tag chat(final ArgumentQueue queue, final Context ctx) {
        final String format = queue.popOr("The <chat> tag missing (1) FormatID").value();
        final String actionId = queue.popOr("The <chat> tag missing (2) ActionID").value();

        return SimplyChat.getInstance().getFormatManager().getFormat(format).getChatResolver(actionId);
    }

}
