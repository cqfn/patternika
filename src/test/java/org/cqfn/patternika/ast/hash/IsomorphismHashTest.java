package org.cqfn.patternika.ast.hash;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.TestNode;
import org.cqfn.patternika.ast.iterator.Children;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link IsomorphismHash} class.
 *
 * @since 2020/11/27
 */
public class IsomorphismHashTest {

    /**
     * Tests hash calculation for a single node (without children).
     */
    @Test
    public void testSingleNode() {
        final Node node = new TestNode("Type1", 0);
        final Hash hash = new IsomorphismHash();
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
        // Isomorphic nodes: same type, different data.
        Assert.assertEquals(
                hash.getHash(node),
                hash.getHash(new TestNode("Type1", 1))
            );
        // Non-isomorphic nodes: different type, same data.
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
        final Node root = createTree("Type1", 0);
        final Hash hash = new IsomorphismHash();
        // Hash must be non-zero.
        Assert.assertNotEquals(0, createTree("SomeType", 0));
        // Same instance.
        Assert.assertEquals(
                hash.getHash(root),
                hash.getHash(root)
            );
        // Equal trees: all nodes are exact copies.
        Assert.assertEquals(
                hash.getHash(root),
                hash.getHash(createTree("Type1", 0))
            );
        // Isomorphic trees: equal node types, different data.
        Assert.assertEquals(
                hash.getHash(root),
                hash.getHash(createTree("Type1", 1))
            );
        // Totally different trees.
        Assert.assertNotEquals(
                hash.getHash(root),
                hash.getHash(new TestNode(2, new TestNode(1), new TestNode(0)))
            );
        // Different trees: same structure, same data, different node type.
        Assert.assertNotEquals(
                hash.getHash(root),
                hash.getHash(createTree("Type2", 0))
            );
        // Different trees: root has a different type.
        Assert.assertNotEquals(
                hash.getHash(root),
                hash.getHash(
                        new TestNode("Type2", 0,
                                new Children<>(createTree("Type1", 0)).toList())
                    )
                );
        // Different trees: one of the trees has less nodes.
        final Node modified1 = new TestNode(
                "Type1",
                0,
                new TestNode(
                        "Type1",
                        1,
                        new TestNode("Type1", 11),
                        new TestNode("Type1", 12, new TestNode("Type1", 121)),
                        new TestNode("Type1", 13)
                    ),
                new TestNode(
                        "Type1",
                        2,
                        //  Modified: TestNode("Type1", 211) has no child TestNode("Type1", 2111)
                        new TestNode("Type1", 21, new TestNode("Type1", 211)),
                        new TestNode("Type1", 22)
                    ),
                new TestNode(
                        "Type1",
                        3,
                        new TestNode("Type1", 31),
                        new TestNode("Type1", 32)
                    )
            );
        Assert.assertNotEquals(
                hash.getHash(root),
                hash.getHash(modified1)
            );
        // Different trees: one of the trees has a node with a different type.
        final Node modified2 =  new TestNode(
                "Type1",
                0,
                new TestNode(
                        "Type1",
                        1,
                        new TestNode("Type1", 11),
                        new TestNode("Type1", 12, new TestNode("Type1", 121)),
                        new TestNode("Type1", 13)
                    ),
                new TestNode(
                        "Type1",
                        2,
                        // Modified: TestNode("Type1", 2111) -> TestNode("Type2", 2111)
                        new TestNode("Type1", 21,
                                new TestNode("Type1", 211,  new TestNode("Type2", 2111))),
                        new TestNode("Type1", 22)
                    ),
                new TestNode(
                        "Type1",
                        3,
                        new TestNode("Type1", 31),
                        new TestNode("Type1", 32)
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
     * @param type node type.
     * @param data value offset for data (to make nodes have different data).
     * @return new test node tree.
     */
    private Node createTree(final String type, final int data) {
        final TestNode root = new TestNode(
                type,
                data + 0,
                new TestNode(
                        type,
                        data + 1,
                        new TestNode(type, data + 11),
                        new TestNode(type, data + 12, new TestNode(type, data + 121)),
                        new TestNode(type, data + 13)
                    ),
                new TestNode(
                        type,
                        data + 2,
                        new TestNode(type, data + 21,
                                    new TestNode(type, data + 211,
                                            new TestNode(type, data + 2111))),
                        new TestNode(type, data + 22)
                    ),
                new TestNode(
                        type,
                        data + 3,
                        new TestNode(type, data + 31),
                        new TestNode(type, data + 32)
                    )
            );
        return root;
    }

}
