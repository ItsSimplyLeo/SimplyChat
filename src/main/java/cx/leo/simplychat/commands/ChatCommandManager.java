package cx.leo.simplychat.commands;

import cloud.commandframework.CommandTree;
import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.arguments.parser.ParserParameters;
import cloud.commandframework.arguments.parser.StandardParameters;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import cx.leo.simplychat.SimplyChatPlugin;
import org.bukkit.command.CommandSender;

import java.util.function.Function;

public class ChatCommandManager {

    private AnnotationParser<CommandSender> annotationParser;
    private BukkitCommandManager<CommandSender> commandManager;

    public ChatCommandManager(SimplyChatPlugin plugin) {

        final Function<CommandTree<CommandSender>, CommandExecutionCoordinator<CommandSender>> executionCoordinatorFunction =
                AsynchronousCommandExecutionCoordinator.<CommandSender>builder().build();

        final Function<CommandSender, CommandSender> mapperFunction = Function.identity();

        try {
            commandManager = new PaperCommandManager<>(
                    /* Owning plugin */ plugin,
                    /* Coordinator function */ executionCoordinatorFunction,
                    /* Command Sender -> C */ mapperFunction,
                    /* C -> Command Sender */ mapperFunction
            );
        } catch (final Exception e) {
            plugin.getLogger().severe("Failed to initialize the command manager.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        //
        // Register Brigadier mappings
        //
        if (this.commandManager.hasCapability(CloudBukkitCapabilities.BRIGADIER)) {
            this.commandManager.registerBrigadier();
        }

        //
        // Register asynchronous completions
        //
        if (this.commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            ((PaperCommandManager<CommandSender>) commandManager).registerAsynchronousCompletions();
        }

        //
        // Create the annotation parser. This allows you to define commands using methods annotated with
        // @CommandMethod
        //
        final Function<ParserParameters, CommandMeta> commandMetaFunction = p ->
                CommandMeta.simple()
                        // This will allow you to decorate commands with descriptions
                        .with(CommandMeta.DESCRIPTION, p.get(StandardParameters.DESCRIPTION, "No description"))
                        .build();

        this.annotationParser = new AnnotationParser<>(
                /* Manager */ commandManager,
                /* Command sender type */ CommandSender.class,
                /* Mapper for command meta instances */ commandMetaFunction
        );
    }

    public BukkitCommandManager<CommandSender> getPaperCommandManager() {
        return commandManager;
    }

    public void parse(Object clazz) {
        annotationParser.parse(clazz);
    }
}
