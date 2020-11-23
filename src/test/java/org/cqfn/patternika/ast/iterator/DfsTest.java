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
 * Tests for {@link Dfs} and {@link DfsIterator}.
 *
 * @since 2020/11/23
 */
public class DfsTest {

    /**
     * Tests {@link Dfs} and {@link DfsIterator} - single node.
     */
    @Test
    public void testDfsSingle() {
        final Node root = new TestNode(0);
        final Dfs<Node> children = new Dfs<>(root);
        final Iterator<Node> iterator = children.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertSame(root, iterator.next());
    }

    /**
     * Tests {@link Dfs} and {@link DfsIterator} - single node.
     */
    @Test(expected = NoSuchElementException.class)
    public void testDfsNoElement() {
        final Node root = new TestNode(0);
        final Dfs<Node> children = new Dfs<>(root);
        final Iterator<Node> iterator = children.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertSame(root, iterator.next());
        Assert.assertFalse(iterator.hasNext());
        iterator.next(); // Exception.
    }

    /**
     * Tests {@link Dfs} and {@link DfsIterator} - positive test.
     */
    @Test
    public void testDfs() {
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
        final List<String> data = new ArrayList<>();
        for (final Node node : new Dfs<>(root)) {
            data.add(node.getData());
        }
        Assert.assertEquals("Wrong element order!", expectedData, data);
    }

}
