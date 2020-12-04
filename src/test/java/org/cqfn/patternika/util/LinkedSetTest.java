package org.cqfn.patternika.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Tests for the {@link LinkedSet} class.
 *
 * @since 2020/12/1
 */
public class LinkedSetTest {

    /**
     * Test for constructor {@link LinkedSet#LinkedSet()}.
     */
    @Test
    public void testLinkedSet() {
        final LinkedSet<String> set = new LinkedSet<>();
        Assert.assertTrue(set.isEmpty());
        Assert.assertEquals(0, set.size());
        Assert.assertNull(set.getFirst());
        Assert.assertNull(set.getLast());
    }

    /**
     * Test for constructor {@link LinkedSet#LinkedSet(Iterable)}.
     */
    @Test
    public void testLinkedSetIterable() {
        final List<String> items = Arrays.asList("one", "two", "three", "four", "five");
        final LinkedSet<String> set = new LinkedSet<>(items);
        Assert.assertFalse(set.isEmpty());
        Assert.assertEquals(items.size(), set.size());
        Assert.assertEquals("one", set.getFirst());
        Assert.assertEquals("two", set.getNext("one"));
        Assert.assertEquals("five", set.getLast());
        Assert.assertEquals("four", set.getPrevious("five"));
        Assert.assertEquals(items, Arrays.asList(set.toArray()));
    }

    /**
     * Test for methods {@link LinkedSet#contains} and {@link LinkedSet#containsAll}.
     */
    @Test
    public void testContains() {
        final List<String> items = Arrays.asList("one", "two", "three", "four", "five");
        final LinkedSet<String> set = new LinkedSet<>(items);
        for (final String item : items) {
            Assert.assertTrue(set.contains(item));
        }
        Assert.assertFalse(set.contains("zero"));
        Assert.assertFalse(set.contains(null));
        Assert.assertTrue(set.containsAll(items));
        Assert.assertTrue(set.containsAll(Arrays.asList("four", "three", "five")));
        Assert.assertFalse(set.containsAll(Arrays.asList("one", "zero", "three")));
    }

    /**
     * Test for the iterator returned by method {@link LinkedSet#iterator()}.
     * Only iterates, does not remove elements.
     */
    @Test
    public void testIterator() {
        final List<String> items = Arrays.asList("one", "two", "three");
        final LinkedSet<String> set = new LinkedSet<>(items);
        // We iterate two times to ensure that we get a new iterator each time.
        for (int i = 0; i < 2; ++i) {
            final Iterator<String> iter = set.iterator();
            for (final String item : items) {
                Assert.assertTrue(iter.hasNext());
                Assert.assertEquals(item, iter.next());
            }
            Assert.assertFalse(iter.hasNext());
        }
    }

    /**
     * Test for the iterator returned by method {@link LinkedSet#iterator()}.
     * Only iterates, does not remove elements.
     * Tests that an exception is thrown when there are no more elements.
     */
    @Test(expected = NoSuchElementException.class)
    public void testIteratorWithException() {
        final LinkedSet<String> set = new LinkedSet<>(Arrays.asList("one", "two"));
        final Iterator<String> iter = set.iterator();
        Assert.assertTrue(iter.hasNext());
        Assert.assertEquals("one", iter.next());
        Assert.assertTrue(iter.hasNext());
        Assert.assertEquals("two", iter.next());
        Assert.assertFalse(iter.hasNext());
        iter.next(); // NoSuchElementException
    }

    /**
     * Test for method {@link LinkedSet#add)}.
     */
    @Test
    public void testAdd() {
        final List<String> items = Arrays.asList("one", "two", "three", "four", "five");
        final LinkedSet<Object> set = new LinkedSet<>();
        for (final String item : items) {
            Assert.assertTrue(set.add(item));
        }
        Assert.assertEquals(items, Arrays.asList(set.toArray()));
    }

    /**
     * Test for method {@link LinkedSet#add)}.
     * Check that adding an existing element causes an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddException() {
        final LinkedSet<String> set = new LinkedSet<>();
        Assert.assertTrue(set.add("one"));
        Assert.assertTrue(set.add("two"));
        Assert.assertTrue(set.add("one"));
    }

    /**
     * Test for method {@link LinkedSet#addAll)}.
     */
    @Test
    public void testAddAll() {
        final List<String> items = Arrays.asList("one", "two", "three", "four", "five");
        final LinkedSet<Object> set = new LinkedSet<>();
        Assert.assertTrue(set.addAll(items));
        Assert.assertFalse(set.addAll(Collections.emptyList()));
        Assert.assertEquals(items, Arrays.asList(set.toArray()));
    }

    /**
     * Test for method {@link LinkedSet#toString()}.
     */
    @Test
    public void testToString() {
        final LinkedSet<String> set1 = new LinkedSet<>(Collections.singletonList("one"));
        final List<String> items = Arrays.asList("one", "two", "three", "four", "five");
        final LinkedSet<String> set2 = new LinkedSet<>(items);
        final LinkedSet<Object> set3 = new LinkedSet<>();
        set3.add("one");
        set3.add(set3);
        set3.add("three");
        Assert.assertEquals("{}", new LinkedSet<>().toString());
        Assert.assertEquals("{one}", set1.toString());
        Assert.assertEquals("{one, two, three, four, five}", set2.toString());
        Assert.assertEquals("{one, (this set), three}", set3.toString());
    }

}