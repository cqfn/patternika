package org.cqfn.patternika.visualizer.dot;

import org.cqfn.patternika.ast.ActionType;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Action for a Dot graph.
 *
 * @since 2021/02/16
 */
public class DotAction implements Consumer<StringBuilder> {
    /** The action node index. */
    private final int index;
    /** The action type. */
    private final ActionType type;

    /**
     * Constructor.
     *
     * @param index the action node index.
     * @param type the action type.
     */
    public DotAction(final int index, final ActionType type) {
        this.index = index;
        this.type = Objects.requireNonNull(type);
    }

    /**
     * Writes the action to a string builder.
     *
     * @param builder the string builder.
     */
    @Override
    public void accept(final StringBuilder builder) {
        builder.append("  action_").append(index)
               .append(" [shape=note color=")
               .append(getColor())
               .append(" label=<")
               .append(type.getText())
               .append(">];\n");
    }

    /**
     * Returns a color for the specified action type.
     *
     * @return the color name.
     */
    private String getColor() {
        switch (type) {
            case DELETE:
                return "red";
            case INSERT_AFTER:
            case INSERT_BEFORE:
                return "skyblue";
            case UPDATE:
                return "forestgreen";
            default:
                return "gray";
        }
    }

}
