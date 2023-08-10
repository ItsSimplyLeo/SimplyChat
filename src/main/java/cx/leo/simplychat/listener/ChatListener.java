package cx.leo.simplychat.listener;

import cx.leo.simplychat.SimplyChat;
import cx.leo.simplychat.format.Format;
import cx.leo.simplychat.user.User;
import cx.leo.simplychat.user.UserManager;
import cx.leo.simplychat.utils.ComponentUtils;
import cx.leo.simplychat.utils.VaultUtil;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ChatListener implements Listener, ChatRenderer {

    private final SimplyChat plugin;
    private final UserManager userManager;

    public ChatListener(SimplyChat plugin) {
        this.plugin = plugin;
        this.userManager = plugin.getUserManager();
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer(this);
    }

    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component displayName, @NotNull Component message, @NotNull Audience viewer) {
        Format format = plugin.getFormatManager().getFormat(VaultUtil.getPrimaryGroup(source));

        FileConfiguration config = plugin.getConfig();
        String rawMessage = PlainTextComponentSerializer.plainText().serialize(message);

        User user = userManager.getUser(source);

        displayName = user.getNicknameStyle().apply(source.getName());
        message = userManager.getUser(source).getChatStyle().apply(rawMessage);
        message = handleShowItem(config, source, message);

        return format.parse(source, displayName, message, viewer);
    }

    private Component handleShowItem(FileConfiguration config, Player source, Component message) {
        String plainMessage = PlainTextComponentSerializer.plainText().serialize(message);

        if (config.getBoolean("item-hover.enabled", false) && source.hasPermission("simplychat.showitem")) {
            for (String replacement : config.getStringList("item-hover.replacements")) {
                if (plainMessage.contains(replacement)) {
                    Component itemComponent = Component.empty();
                    ItemStack itemStack = source.getInventory().getItemInMainHand();
                    if (itemStack.getType() == Material.AIR) itemComponent = ComponentUtils.miniCommon().deserialize(config.getString("item-hover.formats.empty", "<yellow>Nothing!"));
                    else {
                        Component itemName = Component.translatable(itemStack.getType());

                        if (itemStack.hasItemMeta()) {
                            ItemMeta meta = itemStack.getItemMeta();
                            var displayName = meta.displayName();
                            if (displayName != null) itemName = displayName;
                        }

                        var amt = Placeholder.parsed("amount", String.valueOf(itemStack.getAmount()));
                        var itm = Placeholder.component("item", itemName);

                        String itemFormat;

                        if (itemStack.getAmount() > 1) itemFormat = config.getString("item-hover.formats.multiple", "<dark_gray>[<aqua>x<amount></aqua><white><item></white>]</dark_gray>");
                        else itemFormat = config.getString("item-hover.formats.single", "<dark_gray>[<white><item></white>]</dark_gray>");

                        itemComponent = itemComponent.append(
                                ComponentUtils
                                        .miniCommon()
                                        .deserialize(itemFormat, amt, itm)
                        ).hoverEvent(itemStack);
                    }

                    message = message.replaceText(TextReplacementConfig.builder().matchLiteral(replacement).replacement(itemComponent).once().build());
                    break;
                }
            }
        }
        return message;
    }
}
