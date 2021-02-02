package org.cqfn.patternika.ast;

import org.cqfn.patternika.ast.iterator.BfsIterator;
import org.cqfn.patternika.ast.iterator.Children;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

/**
 * Tests for the {@link NodeExt} class.
 *
 * @since 2020/11/24
 */
public class NodeExtTest {

    /**
     * Creates a new node tree for tests.
     *
     * @return new test node tree.
     */
    public Node createTree() {
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
     * Tests the NodeExt constructor.
     * Tess case: it is illegal to wrap NodeExt objects.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNodeExtException() {
        final Node node = new NodeExt(new TestNode(0));
        new NodeExt(node); // Exception!
    }

    /**
     * Test that a NodeExt tree has the same structure as the wrapped node tree.
     * Both node trees are traversed in a BFS manner and node properties are checked for equality.
     */
    @Test
    public void testNodeExt() {
        final Node root = createTree();
        final NodeExt rootExt = new NodeExt(root);
        final Iterator<Node> iterator = new BfsIterator<>(root);
        final Iterator<NodeExt> iteratorExt = new BfsIterator<>(rootExt);
        while (iterator.hasNext() && iteratorExt.hasNext()) {
            assertEquals(iterator.next(), iteratorExt.next());
        }
        Assert.assertFalse(iterator.hasNext());
        Assert.assertFalse(iteratorExt.hasNext());
    }

    /**
     * Checks that the extended node equals the original node.
     * This means methods must return equal values.
     *
     * @param node original node.
     * @param nodeExt extended node.
     */
    private void assertEquals(final Node node, final NodeExt nodeExt) {
        Assert.assertNotSame(node, nodeExt);
        Assert.assertSame(node, nodeExt.getNode());
        Assert.assertEquals(node.getType(), nodeExt.getType());
        Assert.assertEquals(node.getData(), nodeExt.getData());
        Assert.assertEquals(node.getChildCount(), nodeExt.getChildCount());
        Assert.assertEquals(node.isChildCountLimitless(), nodeExt.isChildCountLimitless());
        Assert.assertEquals(node.isChildOrderStrict(), nodeExt.isChildOrderStrict());
        Assert.assertSame(node.getFragment(), nodeExt.getFragment());
        Assert.assertTrue(nodeExt.matches(nodeExt));
        Assert.assertTrue(nodeExt.matches(node));
        Assert.assertTrue(nodeExt.matches(new NodeExt(nodeExt.getNode())));
        Assert.assertFalse(nodeExt.matches(null));
    }

    /**
     * Tests method {@link NodeExt#getParent()}.
     */
    @Test
    public void testNodeExtGetParent() {
        final NodeExt root = new NodeExt(createTree());
        Assert.assertNull(root.getParent());
        testParent(root, 0);
    }

    /**
     * Tests that all children of a node have a valid parent (this parent node).
     *
     * @param parent parent node.
     * @param depth depth, distance to the root.
     */
    private void testParent(final NodeExt parent, final int depth) {
        Assert.assertEquals(depth, parent.getDepth());
        for (final NodeExt child : new Children<>(parent)) {
            Assert.assertSame(parent, child.getParent());
            testParent(child, depth + 1);
        }
    }

    /**
     * Test for methods  {@link NodeExt#getPrevious()}, {@link NodeExt#getNext()},
     * and {@link NodeExt#getOrder()}.
     */
    @Test
    public void testNodeExtRelatives() {
        final NodeExt root = new NodeExt(createTree());
        // Checks root.
        Assert.assertNull(root.getPrevious());
        Assert.assertNull(root.getNext());
        Assert.assertEquals(0, root.getOrder());
        // Checks root.getChild(0).
        Assert.assertEquals(0, root.getChild(0).getOrder());
        Assert.assertNull(root.getChild(0).getPrevious());
        Assert.assertSame(root.getChild(1), root.getChild(0).getNext());
        // Checks root.getChild(1).
        Assert.assertEquals(1, root.getChild(1).getOrder());
        Assert.assertSame(root.getChild(0), root.getChild(1).getPrevious());
        Assert.assertSame(root.getChild(2), root.getChild(1).getNext());
        // Checks root.getChild(2).
        Assert.assertEquals(2, root.getChild(2).getOrder());
        Assert.assertSame(root.getChild(1), root.getChild(2).getPrevious());
        Assert.assertNull(root.getChild(2).getNext());
    }

}
