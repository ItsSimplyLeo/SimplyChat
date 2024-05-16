package cx.leo.simplychat.format;

import cx.leo.simplychat.listener.ChatListener;
import cx.leo.simplychat.user.User;
import cx.leo.simplychat.utils.ComponentUtils;
import cx.leo.simplychat.utils.PlaceholderUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
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

    public Tag getChatResolver(ArgumentQueue queue, Player player) {
        String action = queue.popOr("Action ID was not found.").value();

        var mm = ComponentUtils.miniCommon();
        FormatActions actions = getAction(action);

        if (actions == null) throw new IllegalArgumentException("Tried to use invalid action in format.");

        Component hover = Component.empty();

        var placeholders = ComponentUtils.playerTags(player);

        var it = actions.hoverText().iterator();
        while (it.hasNext()) {
            hover = hover.append(mm.deserialize(PlaceholderUtil.parse(player, it.next()), TagResolver.resolver(placeholders)));
            if (it.hasNext()) hover = hover.append(Component.newline());
        }

        if (actions.clickEvent() == null) return Tag.styling(HoverEvent.showText(hover));
        else return Tag.styling(
                ClickEvent.clickEvent(actions.clickEvent().action(), actions.clickEvent().value().replace("<name>", player.getName())),
                HoverEvent.showText(hover)
        );
    }

    public Component parse(ChatListener listener, User user, Player source, Component displayName, Component message, Audience viewer) {
        return ComponentUtils.miniCommon().deserialize(
                PlaceholderUtil.parse(source, content),
                //Placeholder.component("name", displayName),
                ComponentUtils.playerTags(source),
                Placeholder.component("styled_name", user.getNicknameStyle().apply(source.getName())),
                Placeholder.component("styled_message", handleExtras(listener, source, user.getChatStyle().apply(PlainTextComponentSerializer.plainText().serialize(message)), viewer)),
                Placeholder.component("message", handleExtras(listener, source, message, viewer)),
                TagResolver.resolver("action", (queue, context) -> getChatResolver(queue, source))
        );
    }

    private Component handleExtras(ChatListener listener, Player source, Component message, Audience viewer) {
        String messageString = PlainTextComponentSerializer.plainText().serialize(message);
        if (viewer instanceof Player player) {
            String playerName = player.getName();
            if (messageString.toLowerCase().contains(playerName.toLowerCase())) {
                message = message.replaceText(content -> content.matchLiteral(player.getName())
                        .replacement(ComponentUtils.miniCommon().deserialize("<yellow>@<player></yellow>", Placeholder.unparsed("player", playerName)))
                        .once());
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.5f);
            }

            message = listener.handleShowItem(source, message, messageString);
        }
        return message;
    }

}
