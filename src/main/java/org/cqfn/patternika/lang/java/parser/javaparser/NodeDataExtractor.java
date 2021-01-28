package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.ast.Node;

import java.util.function.Function;

/**
 * Function that extracts data from JavaParser nodes depending on their types.
 *
 * @since 2021/01/28
 */
public class NodeDataExtractor implements Function<Node, String> {
    /**
     * Applies this function to the given argument.
     *
     * @param node the JavaParser node.
     * @return the data from the node or {@code null} if there is not data to return.
     */
    @Override
    public String apply(final Node node) {
        return null;
    }
}
