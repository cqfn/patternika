package org.cqfn.patternika.ast;

import org.cqfn.patternika.source.Fragment;

import java.util.List;

/**
 * Interface for a function that creates a node.
 * <p>
 * Needed to link a node factory with factory methods of specific node classes.
 *
 * @since 2020/11/3
 */
@FunctionalInterface
public interface NodeCreator {
    /**
     * Creates a new node from the specified parameters.
     *
     * @param fragment fragment associated with the node.
     * @param data node data.
     * @param children list of node children.
     * @return new node.
     */
    Node create(Fragment fragment, String data, List<Node> children);
}
