package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.util.Pair;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
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
    public void testContains() {
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
    public void testConnect() {
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
    public void testDisconnect() {
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
     * Tests the {@link HashMapper#redirect(Mapper)} method.
     */
    @Test
    public void redirectTest() {
        List<Pair<String, String>> list = new ArrayList<>();
        list.add(new Pair<>("LOL", "KEK"));
        HashMapper<String> mapper1 = new HashMapper<>();
        list.forEach(pair -> mapper1.connect(pair.getKey(), pair.getVal()));
        assertEquals(mapper1.get("LOL"), "KEK");
        assertEquals(mapper1.get("KEK"), "LOL");

        list = new ArrayList<>();
        list.add(new Pair<>("KEK", "CHEBUREK"));
        HashMapper<String> mapper2 = new HashMapper<>();
        list.forEach(pair -> mapper2.connect(pair.getKey(), pair.getVal()));
        assertEquals(mapper2.get("KEK"), "CHEBUREK");
        assertEquals(mapper2.get("CHEBUREK"), "KEK");

        Mapper<String> mapper = mapper1.redirect(mapper2);
        assertEquals(mapper.get("LOL"), "CHEBUREK");
        assertEquals(mapper.get("CHEBUREK"), "LOL");
    }

}
