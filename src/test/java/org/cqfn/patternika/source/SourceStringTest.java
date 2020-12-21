package org.cqfn.patternika.source;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link SourceString} class.
 *
 * @since 2020/12/18
 */
public class SourceStringTest {

    /**
     * Source text.
     */
    private static final String TEXT =
          "public List<T> toList() {\n"
        + "    final List<T> result = new ArrayList<>();\n"
        + "    forEach(result::add);\n"
        + "    return result;\n"
        + "}";

    /**
     * Test for {@link SourceString}.
     */
    @Test
    public void test() {
        final Source source = new SourceString(TEXT);
        final SourceIterator iter = source.getIterator();
        final Position start = iter.getPosition();
        int count = 0;
        while (iter.getChar() != 0) {
            iter.nextChar();
            count++;
            final String text = source.getFragmentAsString(start, iter.getPosition());
            assertEquals(TEXT.substring(0, count), text);
        }
        final Position end = iter.getPosition();
        assertEquals(0, iter.nextChar());
        assertEquals(TEXT.length(), count);
        assertEquals(TEXT, source.getFragmentAsString(start, end));
        assertTrue(start.compareTo(end) < 0);
        assertTrue(end.compareTo(start) > 0);
        assertEquals(Integer.toString(0), start.toString());
        assertEquals(Integer.toString(TEXT.length()), end.toString());
    }

    /**
     * Test for {@link SourceStringPosition}.
     */
    @Test
    public void testSourceStringPosition() {
        final Position pos1 = new SourceStringPosition(Integer.MIN_VALUE);
        final Position pos2 = new SourceStringPosition(0);
        final Position pos3 = new SourceStringPosition(Integer.MAX_VALUE);
        assertEquals(Integer.MIN_VALUE, pos1.getIndex());
        assertEquals(0, pos2.getIndex());
        assertEquals(Integer.MAX_VALUE, pos3.getIndex());
        assertEquals(Integer.toString(Integer.MIN_VALUE), pos1.toString());
        assertEquals(Integer.toString(0), pos2.toString());
        assertEquals(Integer.toString(Integer.MAX_VALUE), pos3.toString());
        assertTrue(pos1.compareTo(pos2) < 0);
        assertTrue(pos2.compareTo(pos1) > 0);
        assertTrue(pos2.compareTo(pos3) < 0);
        assertTrue(pos3.compareTo(pos2) > 0);
        assertTrue(pos1.compareTo(pos3) < 0);
        assertTrue(pos3.compareTo(pos1) > 0);
    }

    /**
     * Test for {@link SourceStringPosition}.
     * Must produce an exception when comparing it with an incompatible position type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSourceStringPositionException() {
        final Position position1 = new SourceStringPosition(0);
        final Position position2 = new SourceFilePosition(0, 0, 0);
        assertEquals(0, position1.compareTo(position2));
    }

}
