package org.cqfn.patternika.source;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link SourceFile} class.
 *
 * @since 2020/12/21
 */
public class SourceFileTest {

    /**
     * Source file text.
     */
    private static final String TEXT =
              "/**\n"
        + "     * Test for {@link SourceFilePosition}.\n"
        + "     * Must produce an exception when comparing it with an incompatible position type.\n"
        + "     */\n"
        + "    @Test(expected = IllegalArgumentException.class)\n"
        + "    public void testSourceStringPositionException() {\n"
        + "        final Position position1 = new SourceFilePosition(0, 0, 0);\n"
        + "        final Position position2 = new SourceStringPosition(0);\n"
        + "        assertEquals(0, position1.compareTo(position2));\n"
        + "    }";

    /**
     * Test for {@link SourceFile}.
     */
    @Test
    public void test() {
        final Source source = new SourceFile(TEXT);
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
        assertEquals("1.1", start.toString());
        assertEquals("10.6", end.toString());
    }

    /**
     * Test for {@link SourceFilePosition}.
     */
    @Test
    public void testSourceFilePosition() {
        final Position pos1 =
                new SourceFilePosition(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        final Position pos2 =
                new SourceFilePosition(0, 0, 0);
        final Position pos3 =
                new SourceFilePosition(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertEquals(Integer.MIN_VALUE, pos1.getIndex());
        assertEquals(0, pos2.getIndex());
        assertEquals(Integer.MAX_VALUE, pos3.getIndex());
        assertEquals(String.format("%d.%d", Integer.MIN_VALUE, Integer.MIN_VALUE), pos1.toString());
        assertEquals(String.format("%d.%d", 0, 0), pos2.toString());
        assertEquals(String.format("%d.%d", Integer.MAX_VALUE, Integer.MAX_VALUE), pos3.toString());
        assertTrue(pos1.compareTo(pos2) < 0);
        assertTrue(pos2.compareTo(pos1) > 0);
        assertTrue(pos2.compareTo(pos3) < 0);
        assertTrue(pos3.compareTo(pos2) > 0);
        assertTrue(pos1.compareTo(pos3) < 0);
        assertTrue(pos3.compareTo(pos1) > 0);
    }

    /**
     * Test for {@link SourceFilePosition}.
     * Must produce an exception when comparing it with an incompatible position type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSourceStringPositionException() {
        final Position position1 = new SourceFilePosition(0, 0, 0);
        final Position position2 = new SourceStringPosition(0);
        assertEquals(0, position1.compareTo(position2));
    }

    /**
     * Test for methods {@link Position#min} and {@link Position#max}
     * for class {@link SourceFilePosition}.
     */
    @Test
    public void testSourceFilePositionMinMax() {
        final Position pos1 =
                new SourceFilePosition(0, 0, 0);
        final Position pos2 =
                new SourceFilePosition(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertEquals(pos1, Position.min(pos1, pos2));
        assertEquals(pos1, Position.min(pos2, pos1));
        assertEquals(pos1, Position.min(pos1, null));
        assertEquals(pos1, Position.min(null, pos1));
        assertNull(null, Position.min(null, null));
        assertEquals(pos2, Position.max(pos1, pos2));
        assertEquals(pos2, Position.max(pos2, pos1));
        assertEquals(pos1, Position.max(pos1, null));
        assertEquals(pos1, Position.max(null, pos1));
        assertNull(null, Position.max(null, null));
    }

    /**
     * Test for methods {@link SourceFilePosition#equals} and
     * {@link SourceFilePosition#hashCode} .
     */
    @Test
    public void testSourceFilePositionEqualsHashCode() {
        final Position pos1 = new SourceFilePosition(31, 2, 1);
        final Position pos2 = new SourceFilePosition(0, 1, 1);
        final Position pos3 = new SourceFilePosition(31, 2, 1);
        assertEquals(pos1, pos3);
        assertEquals(pos1.hashCode(), pos3.hashCode());
        assertNotEquals(pos1, pos2);
        assertNotEquals(pos1.hashCode(), pos2.hashCode());
        assertNotEquals(pos1, null);
        assertNotEquals(pos1, new SourceStringPosition(31));
    }

}
