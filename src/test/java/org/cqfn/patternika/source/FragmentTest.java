package org.cqfn.patternika.source;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Tests for the {@link Fragment} class.
 *
 * @since 2020/12/28
 */
public class FragmentTest {

    /**
     * Source file text.
     */
    private static final String TEXT =
          "/**\n"
        + " * Test for {@link SourceFilePosition}.\n"
        + " * Must produce an exception when comparing it with an incompatible position type.\n"
        + " */\n"
        + "@Test(expected = IllegalArgumentException.class)\n"
        + "public void testSourceStringPositionException() {\n"
        + "    final Position position1 = new SourceFilePosition(0, 0, 0);\n"
        + "    final Position position2 = new SourceStringPosition(0);\n"
        + "    assertEquals(0, position1.compareTo(position2));\n"
        + "}";

    /**
     * Basic tests for class {@link Fragment}.
     */
    @Test
    public void test() {
        final Source source = new SourceFile(TEXT);
        final Fragment fragment = new Fragment(source);
        assertSame(source, fragment.getSource());
        assertEquals(TEXT.length(), fragment.getLength());
        assertEquals(TEXT, fragment.toString());
        assertEquals(new SourceFilePosition(0, 1, 1), fragment.getStart());
        final int lineCount = 10;
        assertEquals(new SourceFilePosition(TEXT.length(), lineCount, 2), fragment.getEnd());
        final Fragment fragment2 = new Fragment(fragment);
        assertEquals(fragment2.getSource(), fragment.getSource());
        assertEquals(fragment2.getStart(), fragment.getStart());
        assertEquals(fragment2.getEnd(), fragment.getEnd());
    }

    /**
     * Tests for fragments with discarded ends.
     */
    @Test
    public void testEndless() {
        final Source source = new SourceFile(TEXT);
        final Fragment fragment = new Fragment(source);
        final Fragment noStart = fragment.withDiscardedStart();
        final Fragment noEnd = fragment.withDiscardedEnd();
        // Testing noStart.
        assertEquals(source, noStart.getSource());
        assertNull(noStart.getStart());
        assertEquals(fragment.getEnd(), noStart.getEnd());
        assertEquals(-1, noStart.getLength());
        assertEquals("", noStart.toString());
        // Testing noEnd.
        assertEquals(source, noEnd.getSource());
        assertEquals(fragment.getStart(), noEnd.getStart());
        assertNull(noEnd.getEnd());
        assertEquals(-1, noEnd.getLength());
        assertEquals("", noEnd.toString());
    }

}
