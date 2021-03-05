package org.cqfn.patternika.visualizer;

import org.cqfn.patternika.ast.ActionTree;
import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.visualizer.dot.DotVisualizer;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Renders an abstract syntax tree to a text file in the DOT format.
 *
 * @since 2021/02/17
 */
public class TextVisualizer implements Visualizer {
    /** Name of the file to save the image.*/
    private final String fileName;

    /** The action tree to be visualized. */
    private final ActionTree tree;

    /** Markers to highlight nodes. */
    private final Map<Node, List<Integer>> markers;

    /**
     * Main constructor.
     *
     * @param fileName the name of the file to save the image.
     * @param tree the action tree to be visualized.
     * @param markers markers.
     */
    public TextVisualizer(
            final String fileName,
            final ActionTree tree,
            final Map<Node, List<Integer>> markers) {
        this.fileName = Objects.requireNonNull(fileName);
        this.tree = Objects.requireNonNull(tree);
        this.markers = Objects.requireNonNull(markers);
    }

    /**
     * Additional constructor for a node tree.
     *
     * @param fileName the name of the file to save the image.
     * @param root the root of the node tree to be visualized.
     * @param markers markers.
     */
    public TextVisualizer(
            final String fileName,
            final Node root,
            final Map<Node, List<Integer>> markers) {
        this(fileName, new ActionTree("", root, Collections.emptyList()), markers);
    }

    /**
     * Renders data to a text file in the DOT format.
     *
     * @throws IOException if fails to generate the text file.
     */
    @Override
    public void visualize() throws IOException {
        final StringBuilder builder = new StringBuilder();
        final Visualizer visualizer = new DotVisualizer(builder, tree, markers);
        visualizer.visualize();
        try (Writer write = Files.newBufferedWriter(Paths.get(fileName))) {
            write.write(builder.toString());
        }
    }

}
