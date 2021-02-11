package org.cqfn.patternika.visualizer;

import org.cqfn.patternika.ast.ActionTree;
import org.cqfn.patternika.ast.Node;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Renders an abstract syntax tree to an image.
 *
 * @since 2021/02/11
 */
public class ImageVisualizer implements Visualizer {
    /** Name of the file to save the image.*/
    private final String fileName;
    /** Buffer that stores Graphviz text. */
    @SuppressWarnings("PMD.AvoidStringBufferField")
    private final StringBuilder textBuilder;
    /** Visualizer that renders a tree to a Graphviz text. */
    private final Visualizer textVisualizer;

    /**
     * Main constructor.
     *
     * @param fileName the name of the file to save the image.
     * @param tree the action tree to be visualized.
     * @param markers markers.
     */
    public ImageVisualizer(
            final String fileName,
            final ActionTree tree,
            final Map<Node, List<Integer>> markers) {
        this.fileName = Objects.requireNonNull(fileName);
        this.textBuilder = new StringBuilder();
        this.textVisualizer = new TextVisualizer(textBuilder, tree, markers);
    }

    /**
     * Additional constructor for a node tree.
     *
     * @param fileName the name of the file to save the image.
     * @param root the root of the node tree to be visualized.
     * @param markers markers.
     */
    public ImageVisualizer(
            final String fileName,
            final Node root,
            final Map<Node, List<Integer>> markers) {
        this(fileName, new ActionTree("", root, Collections.emptyList()), markers);
    }

    /**
     * Renders data a graphical format.
     *
     * @throws IOException if fails to generate an image file.
     */
    @Override
    public void visualize() throws IOException {
        if (textBuilder.length() == 0) {
            textVisualizer.visualize();
        }
        GraphvizUtils.fromDotContent(textBuilder.toString(), fileName);
    }

}
