package org.cqfn.patternika.visualizer.dot;

import org.cqfn.patternika.ast.Action;
import org.cqfn.patternika.ast.ActionTree;
import org.cqfn.patternika.ast.ActionType;
import org.cqfn.patternika.ast.Hole;
import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.iterator.Children;
import org.cqfn.patternika.visualizer.Visualizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Renders an abstract syntax tree to a DOT text.
 *
 * @since 2021/02/08
 */
public class DotVisualizer implements Visualizer {
    /** Stores the generates DOT text and stores the result. */
    @SuppressWarnings("PMD.AvoidStringBufferField")
    private final StringBuilder builder;
    /** Action tree to be visualized. */
    private final ActionTree tree;
    /** Markers to highlight certain nodes (possibly with multiple colors). */
    private final Map<Node, List<Integer>> markers;
    /** Stores indices of nodes. */
    private final Map<Node, Integer> nodeIndexes;
    /** Stores indices of actions. */
    private final Map<Action, Integer> actionIndexes;
    /** Last index used for a node or an action. */
    private int lastIndex;

    /**
     * Main constructor.
     *
     * @param builder the builder for saving text for a Graphviz file.
     * @param tree the action tree to be visualized.
     * @param markers markers to highlight certain nodes (possibly with multiple colors).
     */
    public DotVisualizer(
            final StringBuilder builder,
            final ActionTree tree,
            final Map<Node, List<Integer>> markers) {
        this.builder = Objects.requireNonNull(builder);
        this.tree = Objects.requireNonNull(tree);
        this.markers = Objects.requireNonNull(markers);
        this.nodeIndexes = new IdentityHashMap<>();
        this.actionIndexes = new IdentityHashMap<>();
        this.lastIndex = -1;
    }

    /**
     * Additional constructor for a node tree.
     *
     * @param builder the builder for saving text for a Graphviz file.
     * @param root the root of the node tree to be visualized.
     * @param markers markers to highlight certain nodes (possibly with multiple colors).
     */
    public DotVisualizer(
            final StringBuilder builder,
            final Node root,
            final Map<Node, List<Integer>> markers) {
        this(builder, new ActionTree("", root, Collections.emptyList()), markers);
    }

    /**
     * Applies the specified writer to append some text.
     *
     * @param writer the writer that appends a portion of text.
     */
    private void append(final DotWriter writer) {
        writer.write(builder);
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
        appendStart();
        appendNode(root, null, -1);
        appendEnd();
    }

    /**
     * Traverses the action tree and assigns all nodes and actions unique indexes.
     *
     * @param node the root node the tree.
     */
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

    /**
     * Appends a node with all its actions and children.
     *
     * @param node the node.
     * @param parentNode the parent of the node.
     * @param childIndex the index of the node in the list of children of the parent.
     */
    private void appendNode(final Node node, final Node parentNode, final int childIndex) {
        final int currentIndex;
        if (node == null) {
            currentIndex = ++lastIndex;
            append(new DotNullNode(currentIndex));
        } else {
            currentIndex = nodeIndexes.get(node);
            append(new DotNode(currentIndex, node.getType(), node.getData(), getNodeStyle(node)));
        }
        if (parentNode != null) {
            final int parentIndex = nodeIndexes.get(parentNode);
            append(DotLink.newNodeToNode(parentIndex, currentIndex, childIndex));
        }
        if (node != null) {
            appendChildren(node);
            appendActions(node);
        }
    }

    /**
     * Returns the writer that generates node style description for the node
     * depending on the node type and marker (if they are assigned to that node).
     *
     * @param node the node.
     * @return the writer for the node style.
     */
    private DotWriter getNodeStyle(final Node node) {
        if (node instanceof Hole) {
            // Holes have a special style.
            return new DotHoleStyle((Hole) node);
        }
        // Nodes have a shape. Most use default shape, some other use a custom shape.
        final DotWriter shapeWriter = new DotNodeShape(node.getType());
        final List<Integer> nodeMarkers = markers.get(node);
        if (nodeMarkers == null) {
            return shapeWriter;
        }
        // Markered nodes have an additional style tag that highlights these nodes.
        final DotWriter markerWriter = new DotMarkerStyle(nodeMarkers);
        return sb -> {
            shapeWriter.write(sb);
            markerWriter.write(sb);
        };
    }

    /**
     * Appends all children of the specified node if there are any.
     *
     * @param node the node that can have children.
     */
    private void appendChildren(final Node node) {
        for (int i = 0; i < node.getChildCount(); ++i) {
            appendNode(node.getChild(i), node, i);
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
        append(DotMultiLink.newNodeToActions(currentIndex, indexes));
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
        append(new DotAction(currentIndex, type));
        if (parentNode != null) {
            final int parentNodeIndex = nodeIndexes.get(parentNode);
            append(DotLink.newNodeToAction(parentNodeIndex, currentIndex));
        }
        final Node ref = action.getRef();
        if (ref != null) {
            final int refIndex = nodeIndexes.get(ref);
            append(DotLink.newActionToNode(currentIndex, refIndex, "ref"));
        }
        final Node accept = action.getAccept();
        if (accept != null) {
            appendNode(accept, null, -1);
            final int acceptIndex = nodeIndexes.get(accept);
            append(DotLink.newActionToNode(currentIndex, acceptIndex, "accept"));
        }
    }

    /**
     * Appends graph start.
     */
    private void appendStart() {
        builder.append("digraph AST {\n")
               .append("  node [shape=box style=rounded];\n");
    }

    /**
     * Appends graph end.
     */
    private void appendEnd() {
        builder.append("}\n");
    }

}
