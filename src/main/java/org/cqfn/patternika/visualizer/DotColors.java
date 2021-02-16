package org.cqfn.patternika.visualizer;

import org.cqfn.patternika.ast.ActionType;

/**
 * Dot colors to be used to highlight nodes, which are accessed by their index.
 *
 * @since 2021/02/11
 */
final class DotColors {
    /** Color names. */
    private final String[] colorNames = {
            "gold",
            "darkolivegreen3",
            "aquamarine3",
            "bisque2",
            "burlywood1",
            "cornsilk2",
            "lightpink2",
            "lightgoldenrod1",
            "lightskyblue2",
            "plum3",
            "rosybrown",
            "seagreen3",
            "sandybrown",
            "peachpuff3",
            "darkseagreen",
            "tomato",
            "mediumslateblue",
            "khaki",
            "cadetblue",
            "powderblue",
            "indianred"
    };

    /**
     * Gets a color name by its index.
     *
     * @param index the index of a color.
     * @return the color name.
     */
    public String getColor(final int index) {
        return index < colorNames.length ? colorNames[index] : "coral";
    }

    /**
     * Returns a color for the specified action type.
     *
     * @param type the action type.
     * @return the color name.
     */
    public String getActionColor(final ActionType type) {
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
