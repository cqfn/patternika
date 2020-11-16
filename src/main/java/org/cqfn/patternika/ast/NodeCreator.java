package org.cqfn.patternika.ast;

import org.cqfn.patternika.source.Fragment;

import java.util.List;

/**
 * Interface for a function that creates a node.
 *
 * <p>Needed to link a node factory with factory methods of specific node classes.
 *
 * @since 2020/11/3
 */
@FunctionalInterface
public interface NodeCreator {

    /**
     * Creates a new node from the specified parameters.
     *
     * @param children List of node children.
     * @param data Node data.
     * @param fragment Node parent.
     * @return New node.
     */
    Node create(List<Node> children, String data, Fragment fragment);

}
