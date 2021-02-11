package org.cqfn.patternika.visualizer;

import org.cqfn.patternika.ast.Action;
import org.cqfn.patternika.ast.ActionTree;
import org.cqfn.patternika.ast.ActionType;
import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.iterator.Children;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Renders an abstract syntax tree to a Graphviz text.
 *
 * @since 2021/02/08
 */
@SuppressWarnings("PMD")
public class GraphvizTextVisualizer implements Visualizer {
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

    /** Builds text for a Graphviz file. */
    private final StringBuilder builder;
    /** Action tree to be visualized. */
    private final ActionTree tree;
    /** Markers. */
    private final Map<Node, List<Integer>> markers;
    /** Stores indices of nodes. */
    private final Map<Node, Integer> nodeIndexes;
    /** Stores indices of actions. */
    private final Map<Action, Integer> actionIndexes;
    /** Processed nodes. */
    private final Map<Node, Node> processedNodes;
    /** Last index used for a node or an action. */
    private int lastIndex;

    /**
     * Main constructor.
     *
     * @param builder the builder for saving text for a Graphviz file.
     * @param tree the action tree to be visualized.
     * @param markers markers.
     */
    public GraphvizTextVisualizer(
            final StringBuilder builder,
            final ActionTree tree,
            final Map<Node, List<Integer>> markers) {
        this.builder = Objects.requireNonNull(builder);
        this.tree = Objects.requireNonNull(tree);
        this.markers = Objects.requireNonNull(markers);
        this.nodeIndexes = new IdentityHashMap<>();
        this.actionIndexes = new IdentityHashMap<>();
        this.processedNodes = new IdentityHashMap<>();
        this.lastIndex = -1;
    }

    /**
     * Additional constructor for a node tree.
     *
     * @param builder the builder for saving text for a Graphviz file.
     * @param root the root of the node tree to be visualized.
     * @param markers markers.
     */
    public GraphvizTextVisualizer(
            final StringBuilder builder,
            final Node root,
            final Map<Node, List<Integer>> markers) {
        this(builder, new ActionTree("", root, Collections.emptyList()), markers);
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
        buildIndexes(root);
        builder.append("digraph AST {\n");
        builder.append("  node [shape=box style=rounded];\n");
        visualizeNode(root, null, -1);
        builder.append("}\n");
    }

    private void buildIndexes(final Node node) {
        if (node == null || nodeIndexes.containsKey(node)) {
            return;
        }
        nodeIndexes.put(node, ++lastIndex);
        for (final Node child : new Children<>(node)) {
            buildIndexes(child);
        }
        for (final Action action : tree.getActionsByParent(node)) {
            actionIndexes.put(action, ++lastIndex);
            buildIndexes(action.getRef());
            buildIndexes(action.getAccept());
        }
    }

    private void visualizeNode(final Node node, final Node parentNode, final int childIndex) {
        final int currentIndex;
        if (node != null) {
            if (processedNodes.putIfAbsent(node, node) != null) {
                return;
            }
            currentIndex = nodeIndexes.get(node);
            visualizeNode(node, currentIndex);
        } else {
            currentIndex = ++lastIndex;
            builder
                .append("  node_")
                .append(currentIndex)
                .append(" [label=<<b>NULL</b>>]; // NODE\n");
        }
        if (parentNode != null) {
            final int parentIndex = nodeIndexes.get(parentNode);
            builder
                .append("  node_")
                .append(parentIndex)
                .append(" -> node_")
                .append(currentIndex)
                .append(" [label=\" ")
                .append(childIndex)
                .append("\"];\n");
        }
        if (node != null) {
            for (int i = 0; i < node.getChildCount(); i++) {
                visualizeNode(node.getChild(i), node, i);
            }
        }
    }

    private void visualizeNode(final Node node, final int currentIndex) {
        /*
        final String type = node.getType();
        final String data = node.getData();
        if (data != null) {
            label = Text.escapeHtmlEntities(label);
        }
        builder
            .append("  node_")
            .append(currentIndex)
            .append(" [");
        if (node instanceof Hole) {
            result.append("style=\"rounded,filled\" color=\"mediumpurple\" fillcolor=\"")
                    .append(((Hole)node).getNumber() < 0 ? "thistle1" : "thistle")
                    .append("\" penwidth=2 ");
        } else {
            String shape = node.getShape();
            if (shape != null) {
                result.append("shape=").append(shape).append(' ');
            }
            if (markers != null && markers.containsKey(node)) {
                StringBuilder colors = new StringBuilder();
                int count = 0;
                for (Integer marker : markers.get(node)) {
                    if (count > 0)
                        colors.append(':');
                    count++;
                    colors.append(getColor(marker));
                }
                if (count == 1) {
                    result.append("style=\"rounded,filled\" fillcolor=\"")
                          .append(colors.toString()).append("\" ");
                }
                else {
                    result.append("style=striped penwidth=2 fillcolor=\"")
                          .append(colors.toString()).append("\" ");
                }
            }
        }
        if (label != null && label.length() > 0) {
            result.append("label=<")
                    .append(type)
                    .append("<br/><font color=\"blue\">")
                    .append(label)
                    .append("</font>>]; // NODE\n");
        }
        else {
            result.append("label=<").append(type).append(">]; // NODE\n");
        }
        */
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
