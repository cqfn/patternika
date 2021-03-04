package org.cqfn.patternika.visualizer;

import org.cqfn.patternika.ast.ActionTree;
import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.visualizer.dot.DotVisualizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Renders an abstract syntax tree to an image using the external Graphviz tool.
 *
 * @since 2021/02/19
 */
public class ImageVisualizer implements Visualizer {
    /** Path to the Graphviz that performs rendering. */
    private final String graphvizPath;

    /** Name of the file to save the image.*/
    private final String imagePath;

    /** The action tree to be visualized. */
    private final ActionTree tree;

    /** Markers to highlight nodes. */
    private final Map<Node, List<Integer>> markers;

    /**
     * Main constructor.
     *
     * @param graphvizPath the path to the Graphviz that performs rendering.
     * @param imagePath the name of the file to save the image.
     * @param tree the action tree to be visualized.
     * @param markers markers.
     */
    public ImageVisualizer(
            final String graphvizPath,
            final String imagePath,
            final ActionTree tree,
            final Map<Node, List<Integer>> markers) {
        this.graphvizPath = Objects.requireNonNull(graphvizPath);
        this.imagePath = Objects.requireNonNull(imagePath);
        this.tree = Objects.requireNonNull(tree);
        this.markers = Objects.requireNonNull(markers);
    }

    /**
     * Additional constructor for a node tree.
     *
     * @param graphvizPath the path to the Graphviz that performs rendering.
     * @param imagePath the name of the file to save the image.
     * @param root the root of the node tree to be visualized.
     * @param markers markers.
     */
    public ImageVisualizer(
            final String graphvizPath,
            final String imagePath,
            final Node root,
            final Map<Node, List<Integer>> markers) {
        this(
            graphvizPath,
            imagePath,
            new ActionTree("", root, Collections.emptyList()),
            markers
        );
    }

    /**
     * Renders data a graphical format.
     *
     * @throws IOException if fails to save rendered data.
     */
    @Override
    public void visualize() throws IOException {
        final StringBuilder builder = new StringBuilder();
        final Visualizer visualizer = new DotVisualizer(builder, tree, markers);
        visualizer.visualize();
        renderToImage(builder.toString());
    }

    /**
     * Renders DOT text to an image file.
     *
     * @param text the text in the DOT format.
     */
    private void renderToImage(final String text) throws IOException {
        final ProcessBuilder builder = new ProcessBuilder(graphvizPath, "-Tpng", "-o", imagePath);
        final Process process = builder.start();
        writeToStream(process.getOutputStream(), text);
        try {
            final int exitCode = process.waitFor();
            if (exitCode != 0) {
                final String errMsg = readFromStream(process.getErrorStream());
                throw new IOException(errMsg);
            }
        } catch (final InterruptedException ex) {
            throw new IOException(
                    "The current thread failed to finish image rendering as it was interrupted.",
                    ex
                );
        }
    }

    /**
     * Writes the specified text to the stream and closes it.
     *
     * @param output the output stream.
     * @param text the text to be written.
     */
    private static void writeToStream(final OutputStream output, final String text) {
        try (PrintStream writer = new PrintStream(output)) {
            writer.print(text);
        }
    }

    /**
     * Reads a text from a stream and closes it.
     *
     * @param input the input stream.
     * @return the text.
     * @throws IOException if failed to read some data.
     */
    private static String readFromStream(final InputStream input) throws IOException {
        final StringBuilder builder = new StringBuilder();
        try (InputStream reader = input) {
            for (int ch = reader.read(); ch != -1; ch = reader.read()) {
                builder.append((char) ch);
            }
        }
        return builder.toString();
    }
}
