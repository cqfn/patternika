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
 * Tests for {@link DepthSorted}.
 *
 * @since 2020/12/30
 */
public class DepthSortedTest {

    /**
     * Test {@link DepthSorted} - single node.
     */
    @Test
    public void testSingle() {
        final Node root = new TestNode(0);
        final Iterable<Node> children = new DepthSorted<>(root);
        final Iterator<Node> iterator = children.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertSame(root, iterator.next());
    }

    /**
     * Tests {@link DepthSorted} - no nodes.
     */
    @Test(expected = NoSuchElementException.class)
    public void testElementException() {
        final Node root = new TestNode(0);
        final Iterable<Node> children = new DepthSorted<>(root);
        final Iterator<Node> iterator = children.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertSame(root, iterator.next());
        Assert.assertFalse(iterator.hasNext());
        iterator.next(); // Exception.
    }

    /**
     * Tests {@link DepthSorted} - positive test.
     */
    @SuppressWarnings("PMD.UseUnderscoresInNumericLiterals")
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
                new TestNode(21,
                    new TestNode(211,
                        new TestNode(2111,
                            new TestNode(21111)
                        )
                    )
                ),
                new TestNode(22)
            ),
            new TestNode(
                3,
                new TestNode(31),
                new TestNode(32)
            )
        );
        final List<String> expectedData = Arrays.asList(
            "0",
            "2",
            "21",
            "1", "211",
            "12", "2111", "3",
            "11", "121", "13", "21111", "22", "31", "32"
        );
        final List<TestNode> nodes = new DepthSorted<>(root).toList();
        final List<String> data = nodes.stream().map(Node::getData).collect(Collectors.toList());
        Assert.assertEquals("Wrong element order!", expectedData, data);
    }

}
