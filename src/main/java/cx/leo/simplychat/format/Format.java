package cx.leo.simplychat.format;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Format {

    private final String id;
    private final String content;

    private HashMap<String, FormatHover> hovers;

    public Format(String id, String content) {
        this.id = id;
        this.content = content;
        this.hovers = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public HashMap<String, FormatHover> getHovers() {
        return hovers;
    }

    public void addHover(FormatHover hover) {
        hovers.put(hover.getId(), hover);
    }

    public Component parse(Player source, Component sourceDisplayName, Component message, Audience viewer) {
        if (viewer instanceof Player player) {
            String playerName = player.getName();
            if (LegacyComponentSerializer.legacySection().serialize(message).toLowerCase().contains(playerName.toLowerCase())) {

                message = message.replaceText(content -> content.matchLiteral(player.getName()).replacement(Component.text("@" + player.getName(), NamedTextColor.YELLOW)).once());

                //viewer.playSound(Sound.sound(Key.key("BLOCK_NOTE_BLOCK_PLING"), Sound.Source.BLOCK, 1, 1));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.5f);
            }
        }

        return MiniMessage.miniMessage().deserialize(content, Placeholder.component("name", sourceDisplayName), Placeholder.component("message", message));
    }

}
