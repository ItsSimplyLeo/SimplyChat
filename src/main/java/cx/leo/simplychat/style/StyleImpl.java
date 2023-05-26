package cx.leo.simplychat.style;

import cx.leo.simplychat.utils.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;

public class StyleImpl implements Style {

    private final String id;
    private final Style.Type type;
    protected final String[] colors;

    protected StyleImpl(String id, Style.Type type, String... colors) {
        this.id = id;
        this.type = type;
        this.colors = colors;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean bold() {
        return false;
    }

    @Override
    public boolean italics() {
        return false;
    }

    @Override
    public Style.Type getType() {
        return type;
    }

    @Override
    public Component apply(String content) {
        Component product;
        switch (type) {
            case SOLID -> product = Component.text(content).color(TextColor.fromHexString(colors[0]));
            case MULTI -> {
                char[] chars = content.toCharArray();

                StringBuilder sb = new StringBuilder();
                int count = 0;

                for (char character : chars) {
                    if (count + 1 > colors.length) count = 0;

                    sb.append(colors[count]).append(character);
                    count++;
                }

                product = ComponentUtils.miniCommon().deserialize(sb.toString());
            }
            case GRADIENT -> {
                var it = Arrays.stream(colors).iterator();
                StringBuilder sb = new StringBuilder("<gradient");
                while (it.hasNext()) sb.append(":").append(it.next());
                sb.append(">");

                product = ComponentUtils.miniCommon().deserialize(sb + content + "</gradient>");
            }
            default -> product = Component.text("");
        }

        if (italics()) product = product.decorate(TextDecoration.ITALIC);
        if (bold()) product = product.decorate(TextDecoration.BOLD);

        return product;
    }

}
