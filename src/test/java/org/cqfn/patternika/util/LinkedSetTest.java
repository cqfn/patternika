package org.cqfn.patternika.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link LinkedSet} class.
 *
 * @since 2020/12/1
 */
public class LinkedSetTest {

    /**
     * Basic test.
     */
    @Test
    public void test() {
        final LinkedSet<String> set = new LinkedSet<>();
        Assert.assertTrue(set.isEmpty());
        Assert.assertEquals(0, set.size());
        set.add("one");
        set.add("two");
        Assert.assertFalse(set.isEmpty());
        Assert.assertEquals(2, set.size());
        set.clear();
        Assert.assertTrue(set.isEmpty());
        Assert.assertEquals(0, set.size());
    }

}
