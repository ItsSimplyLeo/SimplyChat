package cx.leo.simplychat.listener;

import cx.leo.simplychat.ChatManager;
import cx.leo.simplychat.SimplyChatPlugin;
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

    private final SimplyChatPlugin plugin;
    private final ChatManager chatManager;
    private final UserManager userManager;

    public ChatListener(SimplyChatPlugin plugin) {
        this.plugin = plugin;
        this.chatManager = plugin.getChatManager();
        this.userManager = plugin.getUserManager();
    }

    @EventHandler
    public void onChat(@NotNull AsyncChatEvent event) {
        final Player player = event.getPlayer();
        if (chatManager.chatMuted() && !player.hasPermission("simplychat.bypass.mute")) {
            event.getPlayer().sendMessage(ComponentUtils.miniCommon().deserialize(plugin.getConfig().getString("messages.chat-muted", "<red>Chat is currently locked!")));
            event.setCancelled(true);
        }

        if (chatManager.slowModeEnabled() && !player.hasPermission("simplychat.bypass.slowmode")) {
            long lastChatTime = chatManager.getSlowModeMap().getOrDefault(player.getUniqueId(), 0L);
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastChatTime < chatManager.slowModeTime()) {
                player.sendMessage(ComponentUtils.miniCommon().deserialize(plugin.getConfig().getString("messages.slow-mode", "<yellow>Please wait before sending another message!")));
                event.setCancelled(true);
                return;
            }
            chatManager.getSlowModeMap().put(player.getUniqueId(), currentTime);
        }

        event.renderer(this);
    }

    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component displayName, @NotNull Component message, @NotNull Audience viewer) {
        Format format;
        if (plugin.isVaultEnabled()) format = plugin.getFormatManager().getFormat(VaultUtil.getPrimaryGroup(source));
        else format = plugin.getFormatManager().getDefaultFormat();

        User user = userManager.getUser(source);
        displayName = user.getNicknameStyle().apply(source.getName());
        return format.parse(this, user, source, displayName, message, viewer);
    }

    public Component handleShowItem(Player source, Component messageComponent, String plainMessage) {
        FileConfiguration config = plugin.getConfig();
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

                    messageComponent = messageComponent.replaceText(TextReplacementConfig.builder().matchLiteral(replacement).replacement(itemComponent).once().build());
                    break;
                }
            }
        }
        return messageComponent;
    }
}