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
 * Tests for the {@link HashMapping} class.
 *
 * @since 2020/11/23
 */
public class HashMappingTest {

    /**
     * Tests the {@link HashMapping#connect(Iterable, Iterable)},
     * and {@link HashMapping#contains(Object)} and {@link HashMapping#get(Object)} methods.
     */
    @Test
    public void containsTest() {
        final List<String> list1 = Arrays.asList("one", "two", "three", "four");
        final List<String> list2 = Arrays.asList("1", "2", "3");
        final HashMapping<String> mapping1 = new HashMapping<>();
        mapping1.connect(list1, list2);
        final HashMapping<String> mapping2 = new HashMapping<>();
        mapping2.connect(list2, list1);
        final Consumer<Mapping<String>> asserts = mapping -> {
            assertTrue(mapping.contains("one"));
            assertTrue(mapping.contains("1"));
            assertTrue(mapping.contains("two"));
            assertTrue(mapping.contains("2"));
            assertTrue(mapping.contains("three"));
            assertTrue(mapping.contains("3"));
            assertFalse(mapping.contains("four"));
            assertFalse(mapping.contains("4"));
            assertEquals("one", mapping.get("1"));
            assertEquals("1", mapping.get("one"));
            assertEquals("two", mapping.get("2"));
            assertEquals("2", mapping.get("two"));
            assertEquals("three", mapping.get("3"));
            assertEquals("3", mapping.get("three"));
            assertNull(mapping.get("four"));
            assertNull(mapping.get("4"));
        };
        asserts.accept(mapping1);
        asserts.accept(mapping2);
    }

    /**
     * Tests the {@link HashMapping#connect(Object, Object)} and
     * {@link HashMapping#get(Object)} methods.
     */
    @Test
    public void connectTest() {
        final HashMapping<String> mapping = new HashMapping<>();
        // Connects elements and checks that the mapping contains all of them.
        mapping.connect("one", "1");
        mapping.connect("two", "2");
        mapping.connect("three", "3");
        assertEquals("one", mapping.get("1"));
        assertEquals("1", mapping.get("one"));
        assertEquals("two", mapping.get("2"));
        assertEquals("2", mapping.get("two"));
        assertEquals("three", mapping.get("3"));
        assertEquals("3", mapping.get("three"));
        // Reconnects an existing element by key and checks that it is reconnected correctly.
        mapping.connect("two", "20");
        assertEquals("one", mapping.get("1"));
        assertEquals("1", mapping.get("one"));
        assertEquals("two", mapping.get("20"));
        assertEquals("20", mapping.get("two"));
        assertNull(mapping.get("2"));
        assertEquals("three", mapping.get("3"));
        assertEquals("3", mapping.get("three"));
        // Reconnects an existing element by value and checks that it is reconnected correctly.
        mapping.connect("twenty", "20");
        assertEquals("twenty", mapping.get("20"));
        assertEquals("20", mapping.get("twenty"));
        assertNull(mapping.get("two"));
        assertNull(mapping.get("2"));
    }

    /**
     * Tests the {@link HashMapping#disconnect(Object)} method.
     */
    @Test
    public void disconnectTest() {
        final HashMapping<String> mapping = new HashMapping<>();
        // Connects elements and checks that the mapping contains all of them.
        mapping.connect("one", "1");
        mapping.connect("two", "2");
        mapping.connect("three", "3");
        assertEquals("one", mapping.get("1"));
        assertEquals("1", mapping.get("one"));
        assertEquals("two", mapping.get("2"));
        assertEquals("2", mapping.get("two"));
        assertEquals("three", mapping.get("3"));
        assertEquals("3", mapping.get("three"));
        // Disconnects one element by key and checks that it was disconnected correctly.
        mapping.disconnect("1");
        assertNull(mapping.get("1"));
        assertNull(mapping.get("one"));
        assertEquals("two", mapping.get("2"));
        assertEquals("2", mapping.get("two"));
        assertEquals("three", mapping.get("3"));
        assertEquals("3", mapping.get("three"));
        // Disconnects another element by value and checks that it was disconnected correctly.
        mapping.disconnect("three");
        assertNull(mapping.get("1"));
        assertNull(mapping.get("one"));
        assertEquals("two", mapping.get("2"));
        assertEquals("2", mapping.get("two"));
        assertNull(mapping.get("3"));
        assertNull(mapping.get("three"));
        // Tries to disconnect and non-existent element and check that it handled correctly.
        mapping.disconnect("four");
        assertNull(mapping.get("four"));
        assertEquals("two", mapping.get("2"));
        assertEquals("2", mapping.get("two"));
    }

    /**
     * Tests the {@link HashMapping#connected(Object, Object)} method.
     */
    @Test
    public void connectedTest() {
        final HashMapping<String> mapping = new HashMapping<>();
        // Connects elements and checks that the mapping contains all of them.
        mapping.connect("one", "1");
        mapping.connect("two", "2");
        // Checks that proper elements are connected.
        assertTrue(mapping.connected("one", "1"));
        assertTrue(mapping.connected("1", "one"));
        assertTrue(mapping.connected("two", "2"));
        assertTrue(mapping.connected("2", "two"));
        // Checks that wrong elements are not connected.
        assertFalse(mapping.connected("one", "2"));
        assertFalse(mapping.connected("2", "one"));
        assertFalse(mapping.connected("two", "1"));
        assertFalse(mapping.connected("1", "two"));
        assertFalse(mapping.connected("one", "3"));
        assertFalse(mapping.connected("3", "one"));
        // Removes one of the connections and checks again.
        mapping.disconnect("two");
        assertTrue(mapping.connected("one", "1"));
        assertTrue(mapping.connected("1", "one"));
        assertFalse(mapping.connected("two", "2"));
        assertFalse(mapping.connected("2", "two"));
    }

    /**
     * Tests the {@link HashMapping#merge(Mapping)} method.
     * <p>
     * Basic merge without conflicts and without overlapping.
     */
    @Test
    public void mergeNoOverlappingTest() {
        final int middle = 3;
        mergeTest(middle, middle);
    }

    /**
     * Tests the {@link HashMapping#merge(Mapping)} method.
     * <p>
     * Basic merge without conflicts and with overlapping.
     */
    @Test
    public void mergeOverlappingTest() {
        final int overlapStart = 2;
        final int overlapEnd = 4;
        mergeTest(overlapEnd, overlapStart);
    }

    /**
     * Tests the {@link HashMapping#merge(Mapping)} method with/without overlapping.
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
        final HashMapping<String> mapping1 = new HashMapping<>();
        for (int index = firstStart; index < firstEnd; ++index) {
            mapping1.connect(keys.get(index), values.get(index));
        }
        final HashMapping<String> mapping2 = new HashMapping<>();
        for (int index = secondStart; index < secondEnd; ++index) {
            mapping2.connect(keys.get(index), values.get(index));
        }
        // Merges two mapping containers and checks the result.
        // Also, checks that original mapping containers are not modified.
        final Mapping<String> merged = mapping1.merge(mapping2);
        for (int index = firstStart; index < secondEnd; ++index) {
            final String key = keys.get(index);
            final String value = values.get(index);
            assertEquals(value, merged.get(key));
            assertEquals(key, merged.get(value));
            if (index < firstEnd) {
                assertEquals(value, mapping1.get(key));
                assertEquals(key, mapping1.get(value));
            }
            if (index < secondStart) {
                assertNull(mapping2.get(key));
                assertNull(mapping2.get(value));
            }
            if (index >= secondStart) {
                assertEquals(value, mapping2.get(key));
                assertEquals(key, mapping2.get(value));
            }
            if (index >= firstEnd) {
                assertNull(mapping1.get(key));
                assertNull(mapping1.get(value));
            }
        }
    }

    /**
     * Tests the {@link HashMapping#merge(Mapping)} method.
     * Merge with conflicts must cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void mergeConflictTest() {
        final HashMapping<String> mapping1 = new HashMapping<>();
        mapping1.connect("one", "1");
        mapping1.connect("two", "2");
        mapping1.connect("three", "4"); // Merge conflict is here.
        final HashMapping<String> mapping2 = new HashMapping<>();
        mapping2.connect("four", "4");
        mapping2.connect("five", "5");
        mapping2.connect("six", "6");
        mapping1.merge(mapping2);
    }

    /**
     * Tests the {@link HashMapping#redirect(Mapping)} method.
     */
    @Test
    public void redirectTest() {
        // Fills the first mapping container and checks it.
        final List<Pair<String, String>> list1 =
                Collections.singletonList(new Pair<>("LOL", "KEK"));
        final HashMapping<String> mapping1 = new HashMapping<>();
        list1.forEach(pair -> mapping1.connect(pair.getKey(), pair.getVal()));
        assertEquals(mapping1.get("LOL"), "KEK");
        assertEquals(mapping1.get("KEK"), "LOL");
        // Fills the second mapping container and checks it.
        final List<Pair<String, String>> list2 =
                Collections.singletonList(new Pair<>("KEK", "CHEBUREK"));
        final HashMapping<String> mapping2 = new HashMapping<>();
        list2.forEach(pair -> mapping2.connect(pair.getKey(), pair.getVal()));
        assertEquals(mapping2.get("KEK"), "CHEBUREK");
        assertEquals(mapping2.get("CHEBUREK"), "KEK");
        // Creates a redirected mapping container and checks it.
        final Mapping<String> mapping = mapping1.redirect(mapping2);
        assertEquals(mapping.get("LOL"), "CHEBUREK");
        assertEquals(mapping.get("CHEBUREK"), "LOL");
        // Checks that original mappings were not modified.
        assertEquals(mapping1.get("LOL"), "KEK");
        assertEquals(mapping1.get("KEK"), "LOL");
        assertEquals(mapping2.get("KEK"), "CHEBUREK");
        assertEquals(mapping2.get("CHEBUREK"), "KEK");
    }

}
