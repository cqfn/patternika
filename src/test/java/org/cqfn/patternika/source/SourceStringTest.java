package org.cqfn.patternika.source;

import org.junit.Assert;
import org.junit.Test;

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
        }
        final Position end = iter.getPosition();
        Assert.assertEquals(0, iter.nextChar());
        Assert.assertEquals(TEXT.length(), count);
        Assert.assertEquals(TEXT, source.getFragmentAsString(start, end));
        Assert.assertTrue(start.compareTo(end) < 0);
        Assert.assertTrue(end.compareTo(start) > 0);
        Assert.assertEquals(Integer.toString(0), start.toString());
        Assert.assertEquals(Integer.toString(TEXT.length()), end.toString());
    }

    /**
     * Test for {@link SourceStringPosition}.
     */
    @Test
    public void testSourceStringPosition() {
        final Position pos1 = new SourceStringPosition(Integer.MIN_VALUE);
        final Position pos2 = new SourceStringPosition(0);
        final Position pos3 = new SourceStringPosition(Integer.MAX_VALUE);
        Assert.assertEquals(Integer.MIN_VALUE, pos1.getIndex());
        Assert.assertEquals(0, pos2.getIndex());
        Assert.assertEquals(Integer.MAX_VALUE, pos3.getIndex());
        Assert.assertEquals(Integer.toString(Integer.MIN_VALUE), pos1.toString());
        Assert.assertEquals(Integer.toString(0), pos2.toString());
        Assert.assertEquals(Integer.toString(Integer.MAX_VALUE), pos3.toString());
        Assert.assertTrue(pos1.compareTo(pos2) < 0);
        Assert.assertTrue(pos2.compareTo(pos1) > 0);
        Assert.assertTrue(pos2.compareTo(pos3) < 0);
        Assert.assertTrue(pos3.compareTo(pos2) > 0);
        Assert.assertTrue(pos1.compareTo(pos3) < 0);
        Assert.assertTrue(pos3.compareTo(pos1) > 0);
    }

    /**
     * Test for {@link SourceStringPosition}.
     * Must produce an exception when comparing it with an incompatible position type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSourceStringPositionException() {
        final Position position1 = new SourceStringPosition(0);
        final Position position2 = new SourceFilePosition(0, 0, 0);
        Assert.assertEquals(0, position1.compareTo(position2));
    }

}
