package org.cqfn.patternika.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Tests for the {@link DoubleListIterator} class.
 *
 * @since 2020/12/30
 */
public class DoubleListIteratorTest {

    /**
     * Tests basic functionality.
     */
    @Test
    public void test() {
        final List<List<Integer>> list = Arrays.asList(
                Collections.singletonList(0),
                null,
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5),
                Collections.emptyList(),
                Collections.singletonList(6),
                Arrays.asList(7, 8, 9),
                Collections.emptyList()
            );
        final int maxIndex = 10;
        final Iterator<Integer> iter1 = new DoubleListIterator<>(list);
        for (int index = 0; index < maxIndex; ++index) {
            Assert.assertTrue(iter1.hasNext());
            Assert.assertEquals(Integer.valueOf(index), iter1.next());
        }
        final Iterator<Integer> iter2 = new DoubleListIterator<>(
                new ReversedListIterator<>(list),
                ReversedListIterator::new
            );
        for (int index = maxIndex - 1; index >= 0; --index) {
            Assert.assertTrue(iter2.hasNext());
            Assert.assertEquals(Integer.valueOf(index), iter2.next());
        }
    }

    /**
     * Tests that an exception is raised when there are not elements left.
     */
    @Test(expected = NoSuchElementException.class)
    public void testException() {
        final List<List<Integer>> list = Arrays.asList(
                Collections.emptyList(),
                null,
                Collections.emptyList()
            );
        final Iterator<Integer> iter = new DoubleListIterator<>(list);
        Assert.assertFalse(iter.hasNext());
        iter.next();
    }
}
