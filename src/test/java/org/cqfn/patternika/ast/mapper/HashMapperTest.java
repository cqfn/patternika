package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.util.Pair;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link HashMapper} class.
 *
 * @since 2020/11/23
 */
public class HashMapperTest {

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
