package org.cqfn.patternika.source;

import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertSame(source, fragment.getSource());
        Assert.assertEquals(TEXT.length(), fragment.getLength());
        Assert.assertEquals(TEXT, fragment.toString());
        Assert.assertEquals(new SourceFilePosition(0, 1, 1), fragment.getStart());
        final int lineCount = 10;
        Assert.assertEquals(new SourceFilePosition(TEXT.length(), lineCount, 2),
                            fragment.getEnd());
        final Fragment fragment2 = new Fragment(fragment);
        Assert.assertEquals(fragment2.getSource(), fragment.getSource());
        Assert.assertEquals(fragment2.getStart(), fragment.getStart());
        Assert.assertEquals(fragment2.getEnd(), fragment.getEnd());
    }

    /**
     * Tests for the constructor {@link Fragment#Fragment(Source, Position, Position)}.
     */
    @Test
    public void testFragment() {
        final Source source = new SourceString(TEXT);
        final Position pos1 = new SourceStringPosition(0);
        final Position pos2 = new SourceStringPosition(TEXT.length() / 2);
        final Fragment fragment1 = new Fragment(source, pos1, pos2);
        Assert.assertSame(pos1, fragment1.getStart());
        Assert.assertSame(pos2, fragment1.getEnd());
        final Fragment fragment2 = new Fragment(source, pos2, pos1);
        Assert.assertSame(pos1, fragment2.getStart());
        Assert.assertSame(pos2, fragment2.getEnd());
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
        Assert.assertEquals(source, noStart.getSource());
        Assert.assertNull(noStart.getStart());
        Assert.assertNull(noStart.getStartFragment());
        Assert.assertEquals(fragment.getEnd(), noStart.getEnd());
        Assert.assertEquals(-1, noStart.getLength());
        Assert.assertEquals("", noStart.toString());
        // Testing noEnd.
        Assert.assertEquals(source, noEnd.getSource());
        Assert.assertEquals(fragment.getStart(), noEnd.getStart());
        Assert.assertNull(noEnd.getEnd());
        Assert.assertNull(noEnd.getEndFragment());
        Assert.assertEquals(-1, noEnd.getLength());
        Assert.assertEquals("", noEnd.toString());
    }

    /**
     * Tests for the {@link Fragment#getStartFragment()}
     * and {@link Fragment#getEndFragment()} methods.
     */
    @Test
    public void testStartEndFragment() {
        final Source source = new SourceString(TEXT);
        final Position pos1 = new SourceStringPosition(0);
        final Position pos2 = new SourceStringPosition(TEXT.length() / 2);
        final Fragment fragment = new Fragment(source, pos1, pos2);
        Assert.assertSame(pos1, fragment.getStartFragment().getStart());
        Assert.assertSame(pos1, fragment.getStartFragment().getEnd());
        Assert.assertSame(pos2, fragment.getEndFragment().getStart());
        Assert.assertSame(pos2, fragment.getEndFragment().getEnd());
    }

    /**
     * Tests for the {@link Fragment#contains(Fragment)} method.
     */
    @Test
    public void testContains() {
        final int offset = TEXT.length() / 4;
        final Source source = new SourceString(TEXT);
        final Position start = new SourceStringPosition(0);
        final Position end = new SourceStringPosition(TEXT.length());
        final Position pos1 = new SourceStringPosition(offset);
        final Position pos2 = new SourceStringPosition(TEXT.length() - offset);
        final Fragment fragment1 = new Fragment(source, start, end);
        final Fragment fragment2 = new Fragment(source, pos1, pos2);
        final Fragment fragment3 = new Fragment(source, start, pos2);
        final Fragment fragment4 = new Fragment(source, pos1, end);
        final Fragment fragment5 = new Fragment(new SourceString(TEXT));
        Assert.assertTrue(fragment1.contains(fragment2));
        Assert.assertFalse(fragment2.contains(fragment1));
        Assert.assertTrue(fragment1.contains(fragment1));
        Assert.assertFalse(fragment3.contains(fragment4));
        Assert.assertFalse(fragment4.contains(fragment3));
        Assert.assertFalse(fragment1.contains(fragment5));
        Assert.assertFalse(fragment5.contains(fragment1));
    }

}
