package cx.leo.simplychat.commands;

import cx.leo.simplychat.SimplyChatPlugin;
import cx.leo.simplychat.commands.argument.StyleCommandArgument;
import cx.leo.simplychat.style.Style;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.bukkit.BukkitCommandManager;
import org.incendo.cloud.bukkit.CloudBukkitCapabilities;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.parser.ParserDescriptor;

public class ChatCommandManager {

    private AnnotationParser<CommandSender> annotationParser;
    private BukkitCommandManager<CommandSender> commandManager;

    public ChatCommandManager(SimplyChatPlugin plugin) {
        try {
            commandManager = new PaperCommandManager<>(
                    plugin,
                    ExecutionCoordinator.simpleCoordinator(),
                    SenderMapper.identity()
            );
        } catch (final Exception e) {
            plugin.getLogger().severe("Failed to initialize the command manager.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        //
        // Register Brigadier mappings
        //
        if (this.commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            this.commandManager.registerBrigadier();
        }

        //
        // Register asynchronous completions
        //
        if (this.commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            ((PaperCommandManager<CommandSender>) commandManager).registerAsynchronousCompletions();
        }

        this.annotationParser = new AnnotationParser<>(
                /* Manager */ commandManager,
                /* Command sender type */ CommandSender.class
        );

        this.commandManager.parserRegistry().registerParser(ParserDescriptor.of(new StyleCommandArgument<>(plugin), Style.class));
    }

    public BukkitCommandManager<CommandSender> getPaperCommandManager() {
        return commandManager;
    }

    public void parse(Object clazz) {
        annotationParser.parse(clazz);
    }
}
