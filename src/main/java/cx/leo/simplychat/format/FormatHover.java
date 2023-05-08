package cx.leo.simplychat.format;

import java.util.List;

public class FormatHover {

    private final String id;
    private final List<String> lines;

    public FormatHover(String id, List<String> lines) {
        this.id = id;
        this.lines = lines;
    }

    public String getId() {
        return id;
    }

    public List<String> getLines() {
        return lines;
    }

}
