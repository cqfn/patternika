package org.cqfn.patternika.ast;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.BiPredicate;

/**
 * Tests for the {@link DeepMatches} class.
 *
 * @since 2020/11/30
 */
public class DeepMatchesTest {

    /**
     * Test case: one of the compared nodes is {@code null}.
     */
    @Test
    public void testNull() {
        final BiPredicate<Node, Node> pred = new DeepMatches();
        Assert.assertFalse(pred.test(null, new TestNode(0)));
        Assert.assertFalse(pred.test(new TestNode(1), null));
    }

    /**
     * Test case: comparing the same instance.
     */
    @Test
    public void testSame() {
        final BiPredicate<Node, Node> pred = new DeepMatches();
        final Node node = new TestNode(0);
        Assert.assertTrue(pred.test(node, node));
        Assert.assertTrue(pred.test(null, null));
    }

    /**
     * Test case: comparing equal single nodes.
     */
    @Test
    public void testEqualSingle() {
        final BiPredicate<Node, Node> pred = new DeepMatches();
        final Node node1 = new TestNode(1);
        final Node node2 = new TestNode(1);
        Assert.assertTrue(pred.test(node1, node2));
        Assert.assertTrue(pred.test(node2, node1));
    }

    /**
     * Test case: comparing non-equal single nodes.
     */
    @Test
    public void testNonEqualSingle() {
        final BiPredicate<Node, Node> pred = new DeepMatches();
        final Node node1 = new TestNode("Type1", 1);
        final Node node2 = new TestNode("Type2", 2);
        Assert.assertFalse(pred.test(node1, node2));
        Assert.assertFalse(pred.test(node2, node1));
    }

    /**
     * Test case: comparing equal and non-equal trees.
     */
    @Test
    public void testTree() {
        final BiPredicate<Node, Node> pred = new DeepMatches();
        // Equal trees.
        Assert.assertTrue(pred.test(createTree(), createTree()));
        // Different trees: one node is missing.
        final Node modified1 = new TestNode(
                0,
                new TestNode(
                        1,
                        new TestNode(11),
                        new TestNode(12, new TestNode(121)),
                        new TestNode(13)
                    ),
                new TestNode(
                        2,
                        // Child TestNode(2111) is missing.
                        new TestNode(21, new TestNode(211)),
                        new TestNode(22)
                    ),
                new TestNode(
                        3,
                        new TestNode(31),
                        new TestNode(32)
                    )
            );
        Assert.assertFalse(pred.test(createTree(), modified1));
        Assert.assertFalse(pred.test(modified1, createTree()));
        // Different tree: one node mismatches.
        final Node modified2 = new TestNode(
                0,
                new TestNode(
                        1,
                        new TestNode(11),
                        new TestNode(12, new TestNode(121)),
                        new TestNode(13)
                    ),
                new TestNode(
                        2,
                        // TestNode(2111) is replaced with TestNode(2112).
                        new TestNode(21, new TestNode(211, new TestNode(2112))),
                        new TestNode(22)
                    ),
                new TestNode(
                        3,
                        new TestNode(31),
                        new TestNode(32)
                    )
            );
        Assert.assertFalse(pred.test(createTree(), modified2));
        Assert.assertFalse(pred.test(modified2, createTree()));
    }

    /**
     * Test case: one of the node trees has holes.
     */
    @Test
    public void testHole() {
        final BiPredicate<Node, Node> pred = new DeepMatches(node -> node instanceof TestHole);
        // Equal trees.
        Assert.assertTrue(pred.test(createTree(), createTree()));
        // Trees with a hole.
        final Node treeWithHole = new TestNode(
                0,
                new TestNode(
                        1,
                        new TestNode(11),
                        new TestNode(12, new TestNode(121)),
                        new TestNode(13)
                    ),
                new TestNode(
                        2,
                        // A hole is inserted here.
                        new TestHole(21),
                        new TestNode(22)
                    ),
                new TestNode(
                        3,
                        new TestNode(31),
                        new TestNode(32)
                    )
        );
        Assert.assertTrue(pred.test(createTree(), treeWithHole));
        Assert.assertTrue(pred.test(treeWithHole, createTree()));
    }

    /**
     * Creates a test node tree.
     *
     * @return new node tree.
     */
    private Node createTree() {
        final TestNode root = new TestNode(
                0,
                new TestNode(
                        1,
                        new TestNode(11),
                        new TestNode(12, new TestNode(121)),
                        new TestNode(13)
                    ),
                new TestNode(
                        2,
                        new TestNode(21, new TestNode(211, new TestNode(2111))),
                        new TestNode(22)
                    ),
                new TestNode(
                        3,
                        new TestNode(31),
                        new TestNode(32)
                    )
            );
        return root;
    }

    /**
     * Test class for a hole.
     */
    private static class TestHole extends TestNode {
         TestHole(final int data) {
            super(data);
         }
    }

}
