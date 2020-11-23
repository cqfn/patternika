package org.cqfn.patternika.ast.iterator;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.TestNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Tests for {@link Children} and {@link ChildrenIterator}.
 *
 * @since 2020/11/23
 */
public class ChildrenTest {
    /** Default number of children to be used in tests. */
    private static final int CHILDREN_COUNT = 10;

    /**
     * Creates a list of test nodes.
     *
     * @param count number of nodes to be created.
     * @return list of test nodes.
     */
    private static List<Node> createNodes(final int count) {
        final List<Node> result = new ArrayList<>();
        for (int index = 0; index < count; ++index) {
            result.add(new TestNode(index + 1));
        }
        return result;
    }

    /**
     * Tests {@link Children} and {@link ChildrenIterator}. Only positive tests.
     */
    @Test
    public void testChildren() {
        final List<Node> childrenList = createNodes(CHILDREN_COUNT);
        final Node root = new TestNode(0, childrenList);
        final Children<Node> children = new Children<>(root);
        // Check iterator.
        final Iterator<Node> sourceIt = childrenList.iterator();
        final Iterator<Node> targetIt = children.iterator();
        while (sourceIt.hasNext() && targetIt.hasNext()) {
            Assert.assertSame(sourceIt.next(), targetIt.next());
        }
        Assert.assertFalse(sourceIt.hasNext());
        Assert.assertFalse(targetIt.hasNext());
        // Tests indexOf - existing children.
        for (int index = 0; index < CHILDREN_COUNT; ++index) {
            Assert.assertEquals(index, children.indexOf(childrenList.get(index)));
        }
        // Tests indexOf - non-existent child.
        Assert.assertEquals(-1, children.indexOf(new TestNode(0)));
        // Tests toList.
        Assert.assertEquals(childrenList, children.toList());
    }

    /**
     * Tests {@link Children} and {@link ChildrenIterator}.
     * Check that the {@link NoSuchElementException} is thrown after the iterator reaches the end.
     */
    @Test(expected = NoSuchElementException.class)
    public void testChildrenNoElement() {
        final int childrenCount = 2;
        final List<Node> childrenList = createNodes(childrenCount);
        final Node root = new TestNode(0, childrenList);
        final Children<Node> children = new Children<>(root);
        final Iterator<Node> iterator = children.iterator();
        Assert.assertSame(childrenList.get(0), iterator.next());
        Assert.assertSame(childrenList.get(1), iterator.next());
        // Exception.
        iterator.next();
    }

    /**
     * Tests {@link ChildrenIterator} for bound violations.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testChildrenIteratorBounds1() {
        final Node root = new TestNode(0, createNodes(CHILDREN_COUNT));
        new ChildrenIterator<>(root, -1, 1);
    }

    /**
     * Tests {@link ChildrenIterator} for bound violations.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testChildrenIteratorBounds2() {
        final Node root = new TestNode(0, createNodes(CHILDREN_COUNT));
        new ChildrenIterator<>(root, CHILDREN_COUNT, 1);
    }

    /**
     * Tests {@link ChildrenIterator} for bound violations.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testChildrenIteratorBounds3() {
        final Node root = new TestNode(0, createNodes(CHILDREN_COUNT));
        new ChildrenIterator<>(root, 0, -1);
    }

    /**
     * Tests {@link ChildrenIterator} for bound violations.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testChildrenIteratorBounds4() {
        final Node root = new TestNode(0, createNodes(CHILDREN_COUNT));
        new ChildrenIterator<>(root, CHILDREN_COUNT - 1, 2);
    }

    /**
     * Tests {@link ChildrenIterator} for bound violations.
     * Covers the case when end is less than start.
     */
    @Test(expected = NoSuchElementException.class)
    public void testChildrenIteratorBounds5() {
        final Node root = new TestNode(0, createNodes(CHILDREN_COUNT));
        final Iterator<Node> iterator = new ChildrenIterator<>(root, 3, -1);
        iterator.next();
    }

}
