package cx.leo.simplychat.format;

import cx.leo.simplychat.utils.ComponentUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Format {

    private final String id;
    private final String content;
    private final HashMap<String, FormatActions> actions;

    public Format(String id, String content) {
        this.id = id;
        this.content = content;
        this.actions = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public HashMap<String, FormatActions> getActions() {
        return actions;
    }

    public FormatActions getAction(String actionsId) {
        return actions.get(actionsId.toLowerCase());
    }

    public void addActions(String actionId, FormatActions actions) {
        this.actions.put(actionId.toLowerCase(), actions);
    }

    public Tag getChatResolver(String actionsId) {
        var mm = ComponentUtils.miniCommon();
        FormatActions actions = getAction(actionsId);
        Component hover = Component.empty();

        for (String line : actions.hoverText()) hover = hover.append(mm.deserialize(line)).append(Component.newline());

        return Tag.styling(
                actions.clickEvent(),
                HoverEvent.showText(hover)
        );
    }

    public Component parse(Player source, Component sourceDisplayName, Component message, Audience viewer) {
        if (viewer instanceof Player player) {
            String playerName = player.getName();
            if (PlainTextComponentSerializer.plainText().serialize(message).toLowerCase().contains(playerName.toLowerCase())) {

                message = message.replaceText(content -> content.matchLiteral(player.getName()).replacement(Component.text("@" + player.getName(), NamedTextColor.YELLOW)).once());

                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.5f);
            }
        }

        return ComponentUtils.miniChat().deserialize(
                content,
                Placeholder.component("displayname", sourceDisplayName),
                Placeholder.component("name", source.name()),
                Placeholder.component("message", message)
        );
    }

}
