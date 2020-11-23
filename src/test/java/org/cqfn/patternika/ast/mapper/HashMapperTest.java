package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.util.Pair;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link HashMapper} class.
 *
 * @since 2020/11/23
 */
public class HashMapperTest {

    /**
     * Tests the {@link HashMapper#connect(Iterable, Iterable)},
     * and {@link HashMapper#contains(Object)} and {@link HashMapper#get(Object)} methods.
     */
    @Test
    public void containsTest() {
        final List<String> list1 = Arrays.asList("one", "two", "three", "four");
        final List<String> list2 = Arrays.asList("1", "2", "3");
        final HashMapper<String> mapper1 = new HashMapper<>();
        mapper1.connect(list1, list2);
        final HashMapper<String> mapper2 = new HashMapper<>();
        mapper2.connect(list2, list1);
        final Consumer<Mapper<String>> asserts = mapper -> {
            assertTrue(mapper.contains("one"));
            assertTrue(mapper.contains("1"));
            assertTrue(mapper.contains("two"));
            assertTrue(mapper.contains("2"));
            assertTrue(mapper.contains("three"));
            assertTrue(mapper.contains("3"));
            assertFalse(mapper.contains("four"));
            assertFalse(mapper.contains("4"));
            assertEquals("one", mapper.get("1"));
            assertEquals("1", mapper.get("one"));
            assertEquals("two", mapper.get("2"));
            assertEquals("2", mapper.get("two"));
            assertEquals("three", mapper.get("3"));
            assertEquals("3", mapper.get("three"));
            assertNull(mapper.get("four"));
            assertNull(mapper.get("4"));
        };
        asserts.accept(mapper1);
        asserts.accept(mapper2);
    }

    /**
     * Tests the {@link HashMapper#connect(Object, Object)} and
     * {@link HashMapper#get(Object)} methods.
     */
    @Test
    public void connectTest() {
        final HashMapper<String> mapper = new HashMapper<>();
        // Connects elements and checks that the mapper contains all of them.
        mapper.connect("one", "1");
        mapper.connect("two", "2");
        mapper.connect("three", "3");
        assertEquals("one", mapper.get("1"));
        assertEquals("1", mapper.get("one"));
        assertEquals("two", mapper.get("2"));
        assertEquals("2", mapper.get("two"));
        assertEquals("three", mapper.get("3"));
        assertEquals("3", mapper.get("three"));
        // Reconnects an existing element by key and checks that it is reconnected correctly.
        mapper.connect("two", "20");
        assertEquals("one", mapper.get("1"));
        assertEquals("1", mapper.get("one"));
        assertEquals("two", mapper.get("20"));
        assertEquals("20", mapper.get("two"));
        assertNull(mapper.get("2"));
        assertEquals("three", mapper.get("3"));
        assertEquals("3", mapper.get("three"));
        // Reconnects an existing element by value and checks that it is reconnected correctly.
        mapper.connect("twenty", "20");
        assertEquals("twenty", mapper.get("20"));
        assertEquals("20", mapper.get("twenty"));
        assertNull(mapper.get("two"));
        assertNull(mapper.get("2"));
    }

    /**
     * Tests the {@link HashMapper#disconnect(Object)} method.
     */
    @Test
    public void disconnectTest() {
        final HashMapper<String> mapper = new HashMapper<>();
        // Connects elements and checks that the mapper contains all of them.
        mapper.connect("one", "1");
        mapper.connect("two", "2");
        mapper.connect("three", "3");
        assertEquals("one", mapper.get("1"));
        assertEquals("1", mapper.get("one"));
        assertEquals("two", mapper.get("2"));
        assertEquals("2", mapper.get("two"));
        assertEquals("three", mapper.get("3"));
        assertEquals("3", mapper.get("three"));
        // Disconnects one element by key and checks that it was disconnected correctly.
        mapper.disconnect("1");
        assertNull(mapper.get("1"));
        assertNull(mapper.get("one"));
        assertEquals("two", mapper.get("2"));
        assertEquals("2", mapper.get("two"));
        assertEquals("three", mapper.get("3"));
        assertEquals("3", mapper.get("three"));
        // Disconnects another element by value and checks that it was disconnected correctly.
        mapper.disconnect("three");
        assertNull(mapper.get("1"));
        assertNull(mapper.get("one"));
        assertEquals("two", mapper.get("2"));
        assertEquals("2", mapper.get("two"));
        assertNull(mapper.get("3"));
        assertNull(mapper.get("three"));
        // Tries to disconnect and non-existent element and check that it handled correctly.
        mapper.disconnect("four");
        assertNull(mapper.get("four"));
        assertEquals("two", mapper.get("2"));
        assertEquals("2", mapper.get("two"));
    }

    /**
     * Tests the {@link HashMapper#merge(Mapper)} method.
     * Basic merge without conflicts (without overlapping and with overlapping).
     */
    @Test
    public void mergeTest() {
        // Merge without overlapping.
        final int noOverlappingMiddle = 3;
        mergeTest(noOverlappingMiddle, noOverlappingMiddle);
        // Merge with overlapping.
        final int overlappingStart = 2;
        final int overlappingEnd = 4;
        mergeTest(overlappingEnd, overlappingStart);
    }

    /**
     * Tests the {@link HashMapper#merge(Mapper)} method with/without overlapping.
     * Elements tp be mapped are taken from an array. Method parameters set positions
     * to take elements for the first and for the second mappings to be merged.
     *
     * @param firstEnd end index for the elements of the first mapping.
     * @param secondStart start index for the elements of the second mapping.
     */
    private void mergeTest(final int firstEnd, final int secondStart) {
        final int firstStart = 0;
        final int secondEnd = 6;
        final List<String> keys = Arrays.asList("one", "two", "three", "four", "five", "six");
        final List<String> values = Arrays.asList("1", "2", "3", "4", "5", "6");
        final HashMapper<String> mapper1 = new HashMapper<>();
        for (int index = firstStart; index < firstEnd; ++index) {
            mapper1.connect(keys.get(index), values.get(index));
        }
        final HashMapper<String> mapper2 = new HashMapper<>();
        for (int index = secondStart; index < secondEnd; ++index) {
            mapper2.connect(keys.get(index), values.get(index));
        }
        // Merges two mappers and checks the result.
        // Also, checks that original mappers are not modified.
        final Mapper<String> merged = mapper1.merge(mapper2);
        for (int index = firstStart; index < secondEnd; ++index) {
            final String key = keys.get(index);
            final String value = values.get(index);
            assertEquals(value, merged.get(key));
            assertEquals(key, merged.get(value));
            if (index < firstEnd) {
                assertEquals(value, mapper1.get(key));
                assertEquals(key, mapper1.get(value));
            }
            if (index < secondStart) {
                assertNull(mapper2.get(key));
                assertNull(mapper2.get(value));
            }
            if (index >= secondStart) {
                assertEquals(value, mapper2.get(key));
                assertEquals(key, mapper2.get(value));
            }
            if (index >= firstEnd) {
                assertNull(mapper1.get(key));
                assertNull(mapper1.get(value));
            }
        }
    }

    /**
     * Tests the {@link HashMapper#merge(Mapper)} method.
     * Merge with conflicts must cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void mergeConflictTest() {
        final HashMapper<String> mapper1 = new HashMapper<>();
        mapper1.connect("one", "1");
        mapper1.connect("two", "2");
        mapper1.connect("three", "4"); // Merge conflict is here.
        final HashMapper<String> mapper2 = new HashMapper<>();
        mapper2.connect("four", "4");
        mapper2.connect("five", "5");
        mapper2.connect("six", "6");
        mapper1.merge(mapper2);
    }

    /**
     * Tests the {@link HashMapper#redirect(Mapper)} method.
     */
    @Test
    public void redirectTest() {
        // Fills the first mapper and checks it.
        final List<Pair<String, String>> list1 =
                Collections.singletonList(new Pair<>("LOL", "KEK"));
        final HashMapper<String> mapper1 = new HashMapper<>();
        list1.forEach(pair -> mapper1.connect(pair.getKey(), pair.getVal()));
        assertEquals(mapper1.get("LOL"), "KEK");
        assertEquals(mapper1.get("KEK"), "LOL");
        // Fills the second mapper and checks it.
        final List<Pair<String, String>> list2 =
                Collections.singletonList(new Pair<>("KEK", "CHEBUREK"));
        final HashMapper<String> mapper2 = new HashMapper<>();
        list2.forEach(pair -> mapper2.connect(pair.getKey(), pair.getVal()));
        assertEquals(mapper2.get("KEK"), "CHEBUREK");
        assertEquals(mapper2.get("CHEBUREK"), "KEK");
        // Creates a redirected mapper and checks it.
        final Mapper<String> mapper = mapper1.redirect(mapper2);
        assertEquals(mapper.get("LOL"), "CHEBUREK");
        assertEquals(mapper.get("CHEBUREK"), "LOL");
        // Checks that original mappings were not modified.
        assertEquals(mapper1.get("LOL"), "KEK");
        assertEquals(mapper1.get("KEK"), "LOL");
        assertEquals(mapper2.get("KEK"), "CHEBUREK");
        assertEquals(mapper2.get("CHEBUREK"), "KEK");
    }

}
