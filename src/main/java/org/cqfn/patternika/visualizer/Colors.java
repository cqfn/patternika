package org.cqfn.patternika.visualizer;

/**
 * Colors to be used to highlight nodes, which are accessed by their index.
 *
 * @since 2021/02/11
 */
public class Colors {
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

}
