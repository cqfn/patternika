package org.cqfn.patternika.visualizer;

import org.cqfn.patternika.ast.Action;
import org.cqfn.patternika.ast.ActionTree;
import org.cqfn.patternika.ast.ActionType;
import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.iterator.Children;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Visualizer based on the Graphviz visualization tool from Java.
 *
 * @since 2021/02/08
 */
@SuppressWarnings("PMD")
public class GraphvizVisualizer implements Visualizer {
    /** Color names. */
    private static final String[] COLORS = {
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

    /** Action tree to be visualized. */
    private final ActionTree tree;
    /** Builds text for a Graphviz file. */
    private final StringBuilder builder;
    /** Stores indices of nodes. */
    private final Map<Node, Integer> nodeIndexes;
    /** Stores indices of actions. */
    private final Map<Action, Integer> actionIndexes;

    /**
     * Main constructor.
     *
     * @param tree the action tree to be visualized.
     * @param builder the builder for saving text for a Graphviz file.
     */
    public GraphvizVisualizer(final ActionTree tree, final StringBuilder builder) {
        this.tree = Objects.requireNonNull(tree);
        this.builder = Objects.requireNonNull(builder);
        this.nodeIndexes = new IdentityHashMap<>();
        this.actionIndexes = new IdentityHashMap<>();
    }

    /**
     * Additional constructor for an abstract syntax tree.
     *
     * @param root the root of the abstract syntax tree to be visualized.
     * @param builder the builder for saving text for a Graphviz file.
     */
    public GraphvizVisualizer(final Node root, final StringBuilder builder) {
        this(new ActionTree("", root, Collections.emptyList()), builder);
    }

    /**
     * Renders data a graphical format.
     */
    @Override
    public void visualize() {
        if (builder.length() > 0) {
            // Already done.
            return;
        }
        final Node root = tree.getRoot();
        buildIndexes(root, -1);
        builder.append("digraph AST {\n");
        builder.append("  node [shape=box style=rounded];\n");
        visualizeNode(root, null, -1);
        builder.append("}\n");
    }

    private int buildIndexes(final Node node, final int lastIndex) {
        if (node == null || nodeIndexes.containsKey(node)) {
            return lastIndex;
        }
        int currentIndex = lastIndex + 1;
        nodeIndexes.put(node, currentIndex);
        for (final Node child : new Children<>(node)) {
            currentIndex = buildIndexes(child, currentIndex);
        }
        for (final Action action : tree.getActionsByParent(node)) {
            actionIndexes.put(action, ++currentIndex);
            currentIndex = buildIndexes(action.getRef(), currentIndex);
            currentIndex = buildIndexes(action.getAccept(), currentIndex);
        }
        return currentIndex;
    }

    private void visualizeNode(final Node node, final Node parentNode, final int childIndex) {

    }

    private void visualizeAction(final Action action, final Node parentNode) {
        final int currentIndex = actionIndexes.get(action);
        final ActionType type = action.getType();
        final String color = getActionColor(type);
        builder
            .append("  action_")
            .append(currentIndex)
            .append(" [shape=note color=")
            .append(color)
            .append(" label=<")
            .append(type)
            .append(">];\n");
        if (parentNode != null) {
            final int parentNodeIndex = nodeIndexes.get(parentNode);
            builder
                .append("  node_")
                .append(parentNodeIndex).append(" -> action_")
                .append(currentIndex)
                .append(";\n");
        }
        final Node ref = action.getRef();
        if (ref != null) {
            final int refIndex = nodeIndexes.get(ref);
            visualizeNode(ref, null, -1);
            builder
                .append("  action_")
                .append(currentIndex)
                .append(" -> node_")
                .append(refIndex)
                .append(" [label=\" ref\"];\n");
        }
        final Node accept = action.getAccept();
        if (accept != null) {
            int acceptIndex = nodeIndexes.get(accept);
            visualizeNode(accept, null, -1);
            builder
                .append("  action_")
                .append(currentIndex)
                .append(" -> node_")
                .append(acceptIndex)
                .append(" [label=\" accept\"];\n");
        }
    }

    private static String getActionColor(final ActionType type) {
        switch (type) {
            case DELETE:
                return "red";
            case INSERT_AFTER:
            case INSERT_BEFORE:
                return "skyblue";
            case UPDATE:
                return "forestgreen";
            default:
                return "gray";
        }
    }

    private String getColor(final int index) {
        return index < COLORS.length ? COLORS[index] : "coral";
    }

}
