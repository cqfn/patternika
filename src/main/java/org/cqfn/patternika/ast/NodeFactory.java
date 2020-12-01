package org.cqfn.patternika.ast;

import org.cqfn.patternika.source.Fragment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Factory responsible for creating nodes of specific types.
 * <p>
 * The factory is configured with factory functions for specific nodes types.
 * This is more reliable than using reflection as it guarantees that node classes
 * provide proper facilities (constructors, factory methods) to create their instances.
 *
 * @since 2020/11/18
 */
public class NodeFactory {
    /** Factories (creators) for specific node types. */
    private final Map<String, NodeCreator> creators = new HashMap<>();

    /**
     * Registers a creator for a specific node type.
     *
     * @param type node type, not {@code null}.
     * @param creator creator for the specified node type, not {@code null}.
     * @return this factory.
     * @throws IllegalArgumentException if a creator for the specified type is already registered.
     */
    public NodeFactory register(final String type, final NodeCreator creator) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(creator);
        final NodeCreator old = creators.put(type, creator);
        if (old != null) {
            throw new IllegalArgumentException("Creator type " + type + " is already registered!");
        }
        return this;
    }

    /**
     * Creates a new node of the specified type from the specified parameters.
     *
     * @param type node type.
     * @param fragment fragment associated with the node.
     * @param data node data.
     * @param children list of node children.
     * @return new node.
     * @throws IllegalArgumentException if the node type is unknown.
     */
    public Node createNode(
            final String type,
            final Fragment fragment,
            final String data,
            final List<Node> children) {
        final NodeCreator creator = creators.get(type);
        if (creator == null) {
            throw new IllegalArgumentException("Unknown node type: " + type);
        }
        return creator.create(fragment, data, children);
    }

    /**
     * Creates a new node of the specified type from the specified parameters.
     *
     * @see #createNode(String, Fragment, String, List)
     * @param type node type.
     * @param fragment fragment associated with the node.
     * @param data node data.
     * @param children list of node children.
     * @return new node.
     * @throws IllegalArgumentException if the node type is unknown.
     */
    public Node createNode(
            final String type,
            final Fragment fragment,
            final String data,
            final Node... children) {
        return createNode(type, fragment, data, Arrays.asList(children));
    }

}
