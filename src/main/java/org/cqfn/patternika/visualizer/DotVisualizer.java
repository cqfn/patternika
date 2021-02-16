package org.cqfn.patternika.visualizer;

import org.cqfn.patternika.ast.Action;
import org.cqfn.patternika.ast.ActionTree;
import org.cqfn.patternika.ast.ActionType;
import org.cqfn.patternika.ast.Hole;
import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.iterator.Children;
import org.cqfn.patternika.util.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Renders an abstract syntax tree to a Dot text.
 *
 * @since 2021/02/08
 */
@SuppressWarnings("PMD")
public class DotVisualizer implements Visualizer {
    /** Builds text for a Graphviz file. */
    private final StringBuilder builder;
    /** Another builder. */
    private final DotBuilder builder2;
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
    public DotVisualizer(
            final StringBuilder builder,
            final ActionTree tree,
            final Map<Node, List<Integer>> markers) {
        this.builder = Objects.requireNonNull(builder);
        this.builder2 = new DotBuilder(builder);
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
    public DotVisualizer(
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
        builder2.appendStart();
        appendNode(root, null, -1);
        builder2.appendEnd();
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
            builder2.appendNullNode(childIndex);
        }
        if (parentNode != null) {
            final int parentIndex = nodeIndexes.get(parentNode);
            builder2.appendParentNodeLink(parentIndex, currentIndex, childIndex);
        }
        if (node != null) {
            for (int i = 0; i < node.getChildCount(); ++i) {
                appendNode(node.getChild(i), node, i);
            }
            appendActions(node);
        }
    }

    private void appendNodeHeader(final Node node, final int currentIndex) {
        builder.append("  node_").append(currentIndex).append(" [");
        appendNodeStyle(node);
        final String type = node.getType();
        builder.append("label=<").append(type);
        final String data = node.getData();
        if (data != null && !data.isEmpty()) {
            builder.append("<br/><font color=\"blue\">");
            builder.append(TextUtils.encodeHtml(data));
            builder.append("</font>");
        }
        builder.append(">]; // NODE\n");
    }

    private void appendNodeStyle(final Node node) {
        if (node instanceof Hole) {
            builder.append("style=\"rounded,filled\" color=\"mediumpurple\" fillcolor=\"")
                   .append(((Hole) node).getNumber() < 0 ? "thistle1" : "thistle")
                   .append("\" penwidth=2 ");
        } else {
            // String shape = node.getShape();
            // if (shape != null) {
            //    result.append("shape=").append(shape).append(' ');
            //}
            final List<Integer> nodeMarkers = markers.get(node);
            if (nodeMarkers != null) {
                builder2.appendMarkerStyle(nodeMarkers);
            }
        }
    }

    /**
     * Appends all actions of the specified node if there are any.
     *
     * @param node the node that can have actions.
     */
    private void appendActions(final Node node) {
        final List<Action> actions = tree.getActionsByParent(node);
        if (actions.isEmpty()) {
            return;
        }
        final List<Integer> indexes = new ArrayList<>(actions.size());
        for (int i = 0; i < actions.size(); i++) {
            final Action action = actions.get(i);
            appendAction(action, i == 0 ? node : null);
            indexes.add(actionIndexes.get(action));
        }
        final int currentIndex = nodeIndexes.get(node);
        builder2.appendNodeActionLinks(currentIndex, indexes);
    }

    /**
     * Appends an action.
     *
     * @param action the action.
     * @param parentNode the parent node the action is connected to.
     */
    private void appendAction(final Action action, final Node parentNode) {
        final int currentIndex = actionIndexes.get(action);
        final ActionType type = action.getType();
        builder2.appendAction(currentIndex, type);
        if (parentNode != null) {
            final int parentNodeIndex = nodeIndexes.get(parentNode);
            builder2.appendNodeActionLink(parentNodeIndex, currentIndex);
        }
        final Node ref = action.getRef();
        if (ref != null) {
            final int refIndex = nodeIndexes.get(ref);
            builder2.appendActionNodeLink(currentIndex, refIndex, "ref");
        }
        final Node accept = action.getAccept();
        if (accept != null) {
            appendNode(accept, null, -1);
            final int acceptIndex = nodeIndexes.get(accept);
            builder2.appendActionNodeLink(currentIndex, acceptIndex, "accept");
        }
    }

}
