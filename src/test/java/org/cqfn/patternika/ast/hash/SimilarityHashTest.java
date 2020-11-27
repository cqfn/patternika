package org.cqfn.patternika.ast.hash;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.TestNode;

import org.cqfn.patternika.ast.iterator.Children;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link SimilarityHash} class.
 *
 * @since 2020/11/27
 */
public class SimilarityHashTest {

    /**
     * Tests hash calculation for a single node (without children).
     */
    @Test
    public void testSingleNode() {
        final Node node = new TestNode("Type1", 0);
        final Hash hash = new SimilarityHash();
        // Hash must be non-zero.
        Assert.assertNotEquals(0, new TestNode(0));
        // Same instance.
        Assert.assertEquals(
                hash.getHash(node),
                hash.getHash(node)
            );
        // Equal nodes: type and data are the same.
        Assert.assertEquals(
                hash.getHash(node),
                hash.getHash(new TestNode("Type1", 0))
            );
        // Same type, different data.
        Assert.assertNotEquals(
                hash.getHash(node),
                hash.getHash(new TestNode("Type1", 1))
            );
        // Different type, same data.
        Assert.assertNotEquals(
                hash.getHash(node),
                hash.getHash(new TestNode("Type2", 0))
            );
    }

    /**
     * Tests hash calculation for a node tree.
     */
    @Test
    public void testNodeTree() {
        final Node root = createTree();
        final Hash hash = new SimilarityHash();
        // Hash must be non-zero.
        Assert.assertNotEquals(0, createTree());
        // Same instance.
        Assert.assertEquals(
                hash.getHash(root),
                hash.getHash(root)
            );
        // Equal trees: all nodes are exact copies.
        Assert.assertEquals(
                hash.getHash(root),
                hash.getHash(createTree())
            );
        // Equal trees: all nodes are exact copies.
        Assert.assertEquals(
                hash.getHash(root),
                hash.getHash(createTree())
            );
        // Totally different trees.
        Assert.assertNotEquals(
                hash.getHash(root),
                hash.getHash(new TestNode(2, new TestNode(1), new TestNode(0)))
            );
        // Different trees: root has a different type.
        Assert.assertNotEquals(
                hash.getHash(root),
                hash.getHash(new TestNode("Test", 0, new Children<>(createTree()).toList()))
            );
        // Different trees: one of the trees has less nodes.
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
                        new TestNode(21, new TestNode(211)),
                        new TestNode(22)
                    ),
                new TestNode(
                        3,
                        new TestNode(31),
                        new TestNode(32)
                    )
            );
        Assert.assertNotEquals(
                hash.getHash(root),
                hash.getHash(modified1)
            );
        // Different trees: one of the trees has a different node.
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
                        new TestNode(21, new TestNode(211, new TestNode(2112))),
                        new TestNode(22)
                    ),
                new TestNode(
                        3,
                        new TestNode(31),
                        new TestNode(32)
                    )
            );
        Assert.assertNotEquals(
                hash.getHash(root),
                hash.getHash(modified2)
            );
    }

    /**
     * Creates a new node tree for tests.
     *
     * @return new test node tree.
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

}
