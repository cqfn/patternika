package org.cqfn.patternika.visualizer;

import org.cqfn.patternika.ast.Action;
import org.cqfn.patternika.ast.ActionTree;
import org.cqfn.patternika.ast.ActionType;
import org.cqfn.patternika.ast.Hole;
import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.iterator.Children;
import org.cqfn.patternika.util.TextUtils;

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
public class TextVisualizer implements Visualizer {
    /** Colors. */
    private static final Colors COLORS = new Colors();
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
    public TextVisualizer(
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
    public TextVisualizer(
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
        append("digraph AST {\n");
        append("  node [shape=box style=rounded];\n");
        appendNode(root, null, -1);
        append("}\n");
    }

    private TextVisualizer append(final String text) {
        builder.append(text);
        return this;
    }

    private TextVisualizer append(final String text, final int index) {
        builder.append(text).append(index);
        return this;
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

    private void appendNode(
            final Node node,
            final Node parentNode,
            final int childIndex) {
        final int currentIndex;
        if (node != null) {
            if (processedNodes.putIfAbsent(node, node) != null) {
                return;
            }
            currentIndex = nodeIndexes.get(node);
            appendNodeHeader(node, currentIndex);
        } else {
            currentIndex = ++lastIndex;
            append("  node_", currentIndex);
            append(" [label=<<b>NULL</b>>]; // NODE\n");
        }
        if (parentNode != null) {
            final int parentIndex = nodeIndexes.get(parentNode);
            append("  node_", parentIndex);
            append(" -> node_", currentIndex);
            append(" [label=\" ", childIndex);
            append("\"];\n");
        }
        if (node != null) {
            appendNodeChildren(node);
            appendNodeActions(node);
        }
    }

    private void appendNodeChildren(final Node node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            appendNode(node.getChild(i), node, i);
        }
    }

    private void appendNodeActions(final Node node) {
        final List<Action> actions = tree.getActionsByParent(node);
        if (!actions.isEmpty()) {
            for (int i = 0; i < actions.size(); i++) {
                appendAction(actions.get(i), i == 0 ? node : null);
            }
            if (actions.size() > 1) {
                builder.append("  action_").append(actionIndexes.get(actions.get(0)));
                for (int i = 1; i < actions.size(); i++) {
                    builder.append(" -> action_").append(actionIndexes.get(actions.get(i)));
                }
                builder.append("\n");
            }
            final int currentIndex = nodeIndexes.get(node);
            builder.append("  { rank=same; node_").append(currentIndex).append(";");
            for (Action action : actions) {
                builder.append(" action_").append(actionIndexes.get(action)).append(";");
            }
            builder.append(" }\n");
        }
    }

    private void appendNodeHeader(final Node node, final int currentIndex) {
        append("  node_", currentIndex).append(" [");
        if (node instanceof Hole) {
            appendHoleStyle((Hole) node);
        } else {
            appendNodeStyle(node);
        }
        final String type = node.getType();
        append("label=<").append(type);
        final String data = node.getData();
        if (data != null && !data.isEmpty()) {
            append("<br/><font color=\"blue\">");
            append(TextUtils.encodeHtml(data));
            append("</font>");
        }
        append(">]; // NODE\n");
    }

    private void appendHoleStyle(final Hole hole) {
        append("style=\"rounded,filled\" color=\"mediumpurple\" fillcolor=\"");
        append(hole.getNumber() < 0 ? "thistle1" : "thistle");
        append("\" penwidth=2 ");
    }

    private void appendNodeStyle(final Node node) {
        // String shape = node.getShape();
        // if (shape != null) {
        //    result.append("shape=").append(shape).append(' ');
        //}
        if (markers.containsKey(node)) {
            final StringBuilder colors = new StringBuilder();
            int count = 0;
            for (Integer marker : markers.get(node)) {
                if (count > 0) {
                    colors.append(':');
                }
                count++;
                colors.append(COLORS.getColor(marker));
            }
            if (count == 1) {
                builder.append("style=\"rounded,filled\" fillcolor=\"");
            } else {
                builder.append("style=striped penwidth=2 fillcolor=\"");
            }
            builder.append(colors.toString()).append("\" ");
        }
    }

    private void appendAction(final Action action, final Node parentNode) {
        final int currentIndex = actionIndexes.get(action);
        final ActionType type = action.getType();
        final String color = getActionColor(type);
        append("  action_", currentIndex);
        append(" [shape=note color=").append(color);
        append(" label=<").append(type.getText());
        append(">];\n");
        if (parentNode != null) {
            final int parentNodeIndex = nodeIndexes.get(parentNode);
            append("  node_", parentNodeIndex);
            append(" -> action_", currentIndex);
            append(";\n");
        }
        final Node ref = action.getRef();
        if (ref != null) {
            final int refIndex = nodeIndexes.get(ref);
            appendNode(ref, null, -1);
            append("  action_", currentIndex);
            append(" -> node_", refIndex);
            append(" [label=\" ref\"];\n");
        }
        final Node accept = action.getAccept();
        if (accept != null) {
            int acceptIndex = nodeIndexes.get(accept);
            appendNode(accept, null, -1);
            append("  action_", currentIndex);
            append(" -> node_", acceptIndex);
            append(" [label=\" accept\"];\n");
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

}
