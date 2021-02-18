package org.cqfn.patternika.ast.iterator;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.TestNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Tests for {@link DepthFirst} and {@link DepthFirstIterator}.
 *
 * @since 2020/11/23
 */
public class DepthFirstTest {

    /**
     * Tests {@link DepthFirst} and {@link DepthFirstIterator} - single node.
     */
    @Test
    public void testSingle() {
        final Node root = new TestNode(0);
        final DepthFirst<Node> children = new DepthFirst<>(root);
        final Iterator<Node> iterator = children.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertSame(root, iterator.next());
    }

    /**
     * Tests {@link DepthFirst} and {@link DepthFirstIterator} - single node.
     */
    @Test(expected = NoSuchElementException.class)
    public void testNoElement() {
        final Node root = new TestNode(0);
        final DepthFirst<Node> children = new DepthFirst<>(root);
        final Iterator<Node> iterator = children.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertSame(root, iterator.next());
        Assert.assertFalse(iterator.hasNext());
        iterator.next(); // Exception.
    }

    /**
     * Tests {@link DepthFirst} and {@link DepthFirstIterator} - positive test.
     */
    @Test
    public void test() {
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
        final List<String> expectedData =
                Arrays.asList(
                                "11",
                                "121", "12",
                                "13",
                            "1",
                                "2111", "211", "21",
                                "22",
                            "2",
                                "31", "32",
                            "3",
                        "0"
                    );
        final List<Node> nodes = new DepthFirst<>((Node) root).toList();
        final List<String> data = nodes.stream().map(Node::getData).collect(Collectors.toList());
        Assert.assertEquals("Wrong element order!", expectedData, data);
    }

}
