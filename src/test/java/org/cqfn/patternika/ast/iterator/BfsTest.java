package org.cqfn.patternika.ast.iterator;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.TestNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Tests for {@link Bfs} and {@link BfsIterator}.
 *
 * @since 2020/11/23
 */
public class BfsTest {

    /**
     * Tests {@link Bfs} and {@link BfsIterator} - single node.
     */
    @Test
    public void testBfsSingle() {
        final Node root = new TestNode(0);
        final Bfs<Node> children = new Bfs<>(root);
        final Iterator<Node> iterator = children.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertSame(root, iterator.next());
    }

    /**
     * Tests {@link Bfs} and {@link BfsIterator} - single node.
     */
    @Test(expected = NoSuchElementException.class)
    public void testBfsNoElement() {
        final Node root = new TestNode(0);
        final Bfs<Node> children = new Bfs<>(root);
        final Iterator<Node> iterator = children.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertSame(root, iterator.next());
        Assert.assertFalse(iterator.hasNext());
        iterator.next(); // Exception.
    }

    /**
     * Tests {@link Bfs} and {@link BfsIterator} - positive test.
     */
    @Test
    public void testBfs() {
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
        final List<String> expectedData = Arrays.asList(
                "0", "1", "2", "3", "11", "12", "13", "21", "22", "31", "32", "121", "211", "2111"
            );
        final List<String> data = new ArrayList<>();
        for (final Node node : new Bfs<>(root)) {
            data.add(node.getData());
        }
        Assert.assertEquals("Wrong element order!", expectedData, data);
    }

}
