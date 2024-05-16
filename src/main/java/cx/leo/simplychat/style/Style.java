package cx.leo.simplychat.style;

import net.kyori.adventure.text.Component;

public interface Style {

    /**
     *
     * @return id of the style
     */
    String getId();

    /**
     * Get colors for the style.
     *
     * @return an array of the colors for the style
     */
    String[] getColors();

    /**
     *
     * @return Whether the style should be bold
     */
    boolean bold();

    /**
     *
     * @return Whether the style should be italics
     */
    boolean italics();

    /**
     *
     * @return the type of style see {@link Type}
     */
    Type getType();

    /**
     *
     * @param content string to apply the style to
     * @return the component with the applied style
     */
    Component apply(String content);


    enum Type {
        SOLID, MULTI, GRADIENT
    }

    StyleImpl DEFAULT = new StyleImpl("EMPTY", Type.SOLID, "");
}
