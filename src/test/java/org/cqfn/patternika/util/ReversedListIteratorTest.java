package org.cqfn.patternika.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Tests for the {@link ReversedListIterator} class.
 *
 * @since 2020/12/30
 */
public class ReversedListIteratorTest {

    /**
     * Tests basic functionality.
     */
    @Test
    public void test() {
        final List<Integer> list = Arrays.asList(1, 2, 3);
        final Iterator<Integer> iter = new ReversedListIterator<>(list);
        for (int index = list.size() - 1; index >= 0; --index) {
            Assert.assertTrue(iter.hasNext());
            Assert.assertEquals(list.get(index), iter.next());
        }
        Assert.assertFalse(iter.hasNext());
        Assert.assertFalse(new ReversedListIterator<Integer>(Collections.emptyList()).hasNext());
    }

    /**
     * Tests that an exception is raised when there are not elements left.
     */
    @Test(expected = NoSuchElementException.class)
    public void testException() {
        final Iterator<Integer> iter = new ReversedListIterator<>(Collections.singletonList(1));
        Assert.assertEquals(Integer.valueOf(1), iter.next());
        Assert.assertFalse(iter.hasNext());
        iter.next(); // Causes an exception.
    }

}
