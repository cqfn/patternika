package org.cqfn.patternika.visualizer.dot;

import org.cqfn.patternika.ast.Action;
import org.cqfn.patternika.ast.ActionTree;
import org.cqfn.patternika.ast.ActionType;
import org.cqfn.patternika.ast.Hole;
import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.TestNode;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests for the {@link DotVisualizer} class.
 *
 * @since 2021/02/19
 */
public class DotVisualizerTest {
/*
    private void buildImage(final Node node) {
        buildImage(new ActionTree("java", node, Collections.emptyList()));
    }

    private void buildImage(final ActionTree tree) {
        final String tool = "C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe";
        final Visualizer visualizer =
                new ImageVisualizer(tool, "D:\\10.png", tree, Collections.emptyMap());
        try {
            visualizer.visualize();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
*/
    /**
     * Builds DOT text for the specified node tree.
     *
     * @param tree the action tree.
     * @return the DOT text.
     */
    private String getDotText(final ActionTree tree) {
        final StringBuilder builder = new StringBuilder();
        final DotVisualizer visualizer = new DotVisualizer(builder, tree, Collections.emptyMap());
        visualizer.visualize();
        return builder.toString();
    }

    /**
     * Builds DOT text for the specified node tree.
     *
     * @param root the node tree root.
     * @return the DOT text.
     */
    private String getDotText(final Node root) {
        final StringBuilder builder = new StringBuilder();
        final DotVisualizer visualizer = new DotVisualizer(builder, root, Collections.emptyMap());
        visualizer.visualize();
        return builder.toString();
    }

    /**
     * Test for a single node.
     */
    @Test
    public void testSingleNode() {
        final Node root = new TestNode("MyNode", 0);
        final String text =
                  "digraph AST {\n"
                + "  node [shape=box style=rounded];\n"
                + "  node_0 [label=<MyNode<br/><font color=\"blue\">0</font>>]; // NODE\n"
                + "}\n";
        final String dotText = getDotText(root);
        Assert.assertEquals(text, dotText);
    }

    /**
     * Test that visualizes the same tree twice and checks that the result has not change.
     */
    @Test
    public void testDoubleVisualizer() {
        final Node root = new TestNode(
                null, "MyNode", null, Collections.emptyList()
            );
        final String text =
                "digraph AST {\n"
                        + "  node [shape=box style=rounded];\n"
                        + "  node_0 [label=<MyNode>]; // NODE\n"
                        + "}\n";
        final StringBuilder builder = new StringBuilder();
        final DotVisualizer visualizer = new DotVisualizer(builder, root, Collections.emptyMap());
        visualizer.visualize();
        Assert.assertEquals(text, builder.toString());
        visualizer.visualize();
        Assert.assertEquals(text, builder.toString());
    }

    /**
     * Test for a single node without data.
     */
    @Test
    public void testSingleNodeNoData() {
        final Node root =
            new TestNode(null, "MyNode", null, Collections.singletonList(
                    new TestNode(null, "MyNode", "", Collections.emptyList()))
                );
        final String text =
                "digraph AST {\n"
              + "  node [shape=box style=rounded];\n"
              + "  node_0 [label=<MyNode>]; // NODE\n"
              + "  node_1 [label=<MyNode>]; // NODE\n"
              + "  node_0 -> node_1 [label=\" 0\"];\n"
              + "}\n";
        final String dotText = getDotText(root);
        Assert.assertEquals(text, dotText);
    }

    /**
     * Test for a node with null children.
     */
    @Test
    public void testNullNodes() {
        final Node root = new TestNode("MyNode", 0, new TestNode(1), null);
        final String text =
                "digraph AST {\n"
                + "  node [shape=box style=rounded];\n"
                + "  node_0 [label=<MyNode<br/><font color=\"blue\">0</font>>]; // NODE\n"
                + "  node_1 [label=<TestNode<br/><font color=\"blue\">1</font>>]; // NODE\n"
                + "  node_0 -> node_1 [label=\" 0\"];\n"
                + "  node_2 [label=<<b>NULL</b>>]; // NODE\n"
                + "  node_0 -> node_2 [label=\" 1\"];\n"
                + "}\n";
        final String dotText = getDotText(root);
        Assert.assertEquals(text, dotText);
    }

    /**
     * Test for a node with custom shapes ("Trait" and "TraitBlock").
     */
    @Test
    public void testNodeShape() {
        final Node root = new TestNode(
                "TraitBlock", 0,
                new TestNode("Trait", 1), new TestNode("Trait", 2)
            );
        final String text =
                "digraph AST {\n"
              + "  node [shape=box style=rounded];\n"
              + "  node_0 [shape=folder"
              + " label=<TraitBlock<br/><font color=\"blue\">0</font>>]; // NODE\n"
              + "  node_1 [shape=tab label=<Trait<br/><font color=\"blue\">1</font>>]; // NODE\n"
              + "  node_0 -> node_1 [label=\" 0\"];\n"
              + "  node_2 [shape=tab label=<Trait<br/><font color=\"blue\">2</font>>]; // NODE\n"
              + "  node_0 -> node_2 [label=\" 1\"];\n"
              + "}\n";
        final String dotText = getDotText(root);
        Assert.assertEquals(text, dotText);
    }

    /**
     * Test for a hole node.
     */
    @Test
    public void testHole() {
        final Node root = new TestNode(0,
                new TestHole(-1),
                new TestHole(1)
            );
        final String text =
                "digraph AST {\n"
                + "  node [shape=box style=rounded];\n"
                + "  node_0 [label=<TestNode<br/><font color=\"blue\">0</font>>]; // NODE\n"
                + "  node_1 [style=\"rounded,filled\" "
                + "color=\"mediumpurple\" fillcolor=\"thistle1\" penwidth=2 "
                + "label=<Hole<br/><font color=\"blue\">#-1</font>>]; // NODE\n"
                + "  node_0 -> node_1 [label=\" 0\"];\n"
                + "  node_2 [style=\"rounded,filled\" "
                + "color=\"mediumpurple\" fillcolor=\"thistle\" penwidth=2 "
                + "label=<Hole<br/><font color=\"blue\">#1</font>>]; // NODE\n"
                + "  node_0 -> node_2 [label=\" 1\"];\n"
                + "}\n";
        final String dotText = getDotText(root);
        Assert.assertEquals(text, dotText);
    }

    /**
     * Tests highlighting nodes with markers..
     */
    @Test
    public void testMarkers() {
        final Node root =
            new TestNode(0,
                    new TestNode(10),
                    new TestNode(11, new TestNode(111)),
                    new TestNode(12, new TestNode(121)),
                    new TestNode(13)
               );
        final int maxMarkerIndex = 21;
        final Map<Node, List<Integer>> markers = new IdentityHashMap<>();
        markers.put(root, Collections.singletonList(0));
        markers.put(root.getChild(0), Collections.singletonList(1));
        markers.put(root.getChild(1), Arrays.asList(1, 2));
        markers.put(root.getChild(1).getChild(0), Arrays.asList(0, 1, 2));
        markers.put(root.getChild(2), Collections.singletonList(2));
        markers.put(root.getChild(root.getChildCount() - 1),
                    Collections.singletonList(maxMarkerIndex));
        final String text =
              "digraph AST {\n"
            + "  node [shape=box style=rounded];\n"
            + "  node_0 [style=\"rounded,filled\" fillcolor=\"gold\""
            + " label=<TestNode<br/><font color=\"blue\">0</font>>]; // NODE\n"
            + "  node_1 [style=\"rounded,filled\" fillcolor=\"darkolivegreen3\""
            + " label=<TestNode<br/><font color=\"blue\">10</font>>]; // NODE\n"
            + "  node_0 -> node_1 [label=\" 0\"];\n"
            + "  node_2 [style=striped penwidth=2 fillcolor=\"darkolivegreen3:aquamarine3\""
            + " label=<TestNode<br/><font color=\"blue\">11</font>>]; // NODE\n"
            + "  node_0 -> node_2 [label=\" 1\"];\n"
            + "  node_3 [style=striped penwidth=2 fillcolor=\"gold:darkolivegreen3:aquamarine3\""
            + " label=<TestNode<br/><font color=\"blue\">111</font>>]; // NODE\n"
            + "  node_2 -> node_3 [label=\" 0\"];\n"
            + "  node_4 [style=\"rounded,filled\" fillcolor=\"aquamarine3\""
            + " label=<TestNode<br/><font color=\"blue\">12</font>>]; // NODE\n"
            + "  node_0 -> node_4 [label=\" 2\"];\n"
            + "  node_5 [label=<TestNode<br/><font color=\"blue\">121</font>>]; // NODE\n"
            + "  node_4 -> node_5 [label=\" 0\"];\n"
            + "  node_6 [style=\"rounded,filled\" fillcolor=\"coral\""
            + " label=<TestNode<br/><font color=\"blue\">13</font>>]; // NODE\n"
            + "  node_0 -> node_6 [label=\" 3\"];\n"
            + "}\n";
        final StringBuilder builder = new StringBuilder();
        final DotVisualizer visualizer = new DotVisualizer(builder, root, markers);
        visualizer.visualize();
        Assert.assertEquals(text, builder.toString());
    }

    /**
     * Test for a tree with a single action.
     */
    @Test
    public void testSingleActionTree() {
        final Node root =
                new TestNode(0);
        final Action action =
                new Action(ActionType.INSERT_BEFORE, root, null, new TestNode(0));
        final ActionTree tree =
                new ActionTree("java", root, Collections.singletonList(action));
        final String text =
                "digraph AST {\n"
                + "  node [shape=box style=rounded];\n"
                + "  node_0 [label=<TestNode<br/><font color=\"blue\">0</font>>]; // NODE\n"
                + "  action_1 [shape=note color=skyblue label=<ActionInsertBefore>];\n"
                + "  node_0 -> action_1;\n"
                + "  node_2 [label=<TestNode<br/><font color=\"blue\">0</font>>]; // NODE\n"
                + "  action_1 -> node_2 [label=\" accept\"];\n"
                + "  { rank=same; node_0; action_1; }\n"
                + "}\n";
        final String dotText = getDotText(tree);
        Assert.assertEquals(text, dotText);
    }

    /**
     * Test for a tree with actions.
     */
    @Test
    public void testActionTree() {
        final Node root =
            new TestNode(0,
                new TestNode(10),
                new TestNode(11),
                new TestNode(12)
            );
        final List<Action> actions = Arrays.asList(
                new Action(ActionType.DELETE, root, root.getChild(0), null),
                new Action(ActionType.INSERT_BEFORE, root, root.getChild(1), new TestNode(20)),
                new Action(ActionType.INSERT_AFTER, root, root.getChild(1), new TestNode(21)),
                new Action(ActionType.UPDATE, root, root.getChild(2), new TestNode(22))
            );
        final ActionTree tree = new ActionTree("java", root, actions);
        final String text =
              "digraph AST {\n"
            + "  node [shape=box style=rounded];\n"
            + "  node_0 [label=<TestNode<br/><font color=\"blue\">0</font>>]; // NODE\n"
            + "  node_1 [label=<TestNode<br/><font color=\"blue\">10</font>>]; // NODE\n"
            + "  node_0 -> node_1 [label=\" 0\"];\n"
            + "  node_2 [label=<TestNode<br/><font color=\"blue\">11</font>>]; // NODE\n"
            + "  node_0 -> node_2 [label=\" 1\"];\n"
            + "  node_3 [label=<TestNode<br/><font color=\"blue\">12</font>>]; // NODE\n"
            + "  node_0 -> node_3 [label=\" 2\"];\n"
            + "  action_4 [shape=note color=red label=<ActionDelete>];\n"
            + "  node_0 -> action_4;\n"
            + "  action_4 -> node_1 [label=\" ref\"];\n"
            + "  action_5 [shape=note color=skyblue label=<ActionInsertBefore>];\n"
            + "  action_5 -> node_2 [label=\" ref\"];\n"
            + "  node_6 [label=<TestNode<br/><font color=\"blue\">20</font>>]; // NODE\n"
            + "  action_5 -> node_6 [label=\" accept\"];\n"
            + "  action_7 [shape=note color=skyblue label=<ActionInsertAfter>];\n"
            + "  action_7 -> node_2 [label=\" ref\"];\n"
            + "  node_8 [label=<TestNode<br/><font color=\"blue\">21</font>>]; // NODE\n"
            + "  action_7 -> node_8 [label=\" accept\"];\n"
            + "  action_9 [shape=note color=forestgreen label=<ActionUpdate>];\n"
            + "  action_9 -> node_3 [label=\" ref\"];\n"
            + "  node_10 [label=<TestNode<br/><font color=\"blue\">22</font>>]; // NODE\n"
            + "  action_9 -> node_10 [label=\" accept\"];\n"
            + "  action_4 -> action_5 -> action_7 -> action_9\n"
            + "  { rank=same; node_0; action_4; action_5; action_7; action_9; }\n"
            + "}\n";
        final String dotText = getDotText(tree);
        Assert.assertEquals(text, dotText);
    }

    /**
     * Test hole.
     */
    private static class TestHole extends TestNode implements Hole {
        /** Hole number. */
        private final int holeNumber;

        /**
         * Constructor.
         *
         * @param holeNumber the hole number.
         */
        TestHole(final int holeNumber) {
            super(null, "Hole", null, Collections.emptyList());
            this.holeNumber = holeNumber;
        }

        /**
         * Gets the number of the hole that uniquely identifies it.
         *
         * @return the number of the hole.
         */
        @Override
        public int getNumber() {
            return holeNumber;
        }

        /**
         * Returns data associated with the node (in a textual format).
         *
         * @return node data.
         */
        @Override
        public String getData() {
            return "#" + holeNumber;
        }
    }

}
