package org.cqfn.patternika.graph;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class with set of useful methods for using graphiz app via java.
 * More information about graphviz API you can find here: https://github.com/nidi3/graphviz-java.
 */
public final class GraphvizUtils {
    /**
     * It's utility class.
     */
    private GraphvizUtils() { }

    /**
     * Generates graph from .dot file.
     *
     * @param dotFilePath Dot file path for graph generation.
     * @param targetImagePath Target image path for graph.
     * @throws IOException if I/O fails.
     */
    public static void fromDotFile(final String dotFilePath,
                                   final String targetImagePath) throws IOException {
        try (InputStream dot = GraphvizUtils.class.getResourceAsStream(dotFilePath)) {
            final MutableGraph g = new Parser().read(dot);
            Graphviz.fromGraph(g).render(Format.PNG).toFile(new File(targetImagePath));
        }
    }

    /**
     * Generates graph from .dot content.
     * @param dotContent Dot content for graph generation.
     * @param targetImagePath Target image path for graph.
     * @throws IOException if I/O fails.
     */
    public static void fromDotContent(final String dotContent,
                                      final String targetImagePath) throws IOException {
        final MutableGraph g = new Parser().read(dotContent);
        Graphviz.fromGraph(g).render(Format.PNG).toFile(new File(targetImagePath));
    }
}