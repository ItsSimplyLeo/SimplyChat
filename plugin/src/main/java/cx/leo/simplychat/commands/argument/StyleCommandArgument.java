package cx.leo.simplychat.commands.argument;

import cx.leo.simplychat.SimplyChatPlugin;
import cx.leo.simplychat.style.Style;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.exception.parsing.ParserException;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class StyleCommandArgument<C> implements ArgumentParser<C, Style>, SuggestionProvider<C> {

    private final SimplyChatPlugin plugin;

    public StyleCommandArgument(SimplyChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NonNull ArgumentParseResult<Style> parse(
            @NonNull CommandContext<C> context,
            @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.peekString(); // Does not remove the string from the input!
        Style style = plugin.getStyleManager().getStyle(input);
        commandInput.readString(); // Removes the string from the input.
        if (style == null) return ArgumentParseResult.failure(new StyleParserException(input, context));
        else return ArgumentParseResult.success(style);
    }

    @Override
    public @NonNull CompletableFuture<@NonNull Iterable<@NonNull Suggestion>> suggestionsFuture(@NonNull CommandContext<C> context, @NonNull CommandInput input) {
        String suggest = input.input();

        if (suggest.length() > 0) {
            List<Suggestion> filtered = plugin.getStyleManager().getStyles().values().stream()
                    .map(Style::getId)
                    .filter(name -> name.startsWith(suggest))
                    .map(Suggestion::suggestion)
                    .collect(Collectors.toList());

            if (!filtered.isEmpty()) {
                return CompletableFuture.completedFuture(filtered);
            }
        }

        return CompletableFuture.completedFuture(plugin.getStyleManager().getStyles().values().stream().map(style -> Suggestion.suggestion(style.getId())).toList());
    }

    public static final class StyleParserException extends ParserException {

        private final String input;

        public StyleParserException(
                final @NonNull String input,
                final @NonNull CommandContext<?> context
        ) {
            super(
                    StyleCommandArgument.class,
                    context,
                    Caption.of(""),
                    CaptionVariable.of("input", input)
            );
            this.input = input;
        }

        /**
         * Returns the supplied input.
         *
         * @return string value
         */
        public String input() {
            return this.input;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final StyleParserException that = (StyleParserException) o;
            return this.input.equals(that.input);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.input);
        }
    }

}