package cx.leo.simplychat.utils;

import cx.leo.simplychat.text.minimessage.StyleGradientTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ComponentUtils {

    private final static MiniMessage MINI_RAW = MiniMessage.miniMessage();

    private final static MiniMessage MINI_COMMON = MiniMessage.builder()
            .tags(TagResolver.resolver(
                    TagResolver.standard(),
                    TagResolver.resolver("weblink", ComponentUtils::weblink),
                    StyleGradientTag.RESOLVER
            ))
            .build();

    public static MiniMessage miniRaw() {
        return MINI_RAW;
    }

    public static MiniMessage miniCommon() {
        return MINI_COMMON;
    }

    // "Hello, <weblink:https://github.com/ItsSimplyLeo>click me!</weblink> but not me!"
    public static Tag weblink(final ArgumentQueue queue, final Context ctx) {
        final String link = queue.popOr("The <weblink> tag requires exactly one argument, the link to open").value();

        return Tag.styling(
                NamedTextColor.BLUE,
                TextDecoration.UNDERLINED,
                ClickEvent.openUrl(link),
                HoverEvent.showText(Component.text("Open " + link))
        );
    }

    public static TagResolver playerTags(Player player) {
        return TagResolver.resolver(Arrays.asList(
                Placeholder.parsed("name", player.getName()),
                Placeholder.component("nickname", player.displayName()),
                Placeholder.parsed("prefix", VaultUtil.getPlayerPrefix(player)),
                Placeholder.parsed("suffix", VaultUtil.getPlayerSuffix(player))
        ));
    }
}
