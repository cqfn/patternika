package org.cqfn.patternika.visualizer;

import java.io.IOException;

/**
 * Interfaces for visualizer objects, which are responsible
 * for saving abstract-syntax trees to graphical formats.
 *
 * @since 2021/02/08
 */
public interface Visualizer {
    /**
     * Renders data a graphical format.
     *
     * @throws IOException if fails to save rendered data.
     */
    void visualize() throws IOException;
}
