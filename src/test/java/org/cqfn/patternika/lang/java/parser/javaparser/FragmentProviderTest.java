package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.Range;
import org.cqfn.patternika.source.Fragment;
import org.cqfn.patternika.source.SourceFile;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * Tests for the {@link FragmentProvider} class.
 *
 * @since 2021/01/29
 */
public class FragmentProviderTest {
    /** Test text. */
    private static final String TEXT =
          "@Override\n"
        + "public Fragment apply(final Node node) {\n"
        + "    final Optional<Range> optionalRange = node.getRange();\n"
        + "    if (!optionalRange.isPresent()) {\n"
        + "        throw new IllegalArgumentException();\n"
        + "    }\n"
        + "    final Range range = optionalRange.get();\n"
        + "    final Position start = converter.apply(range.begin);\n"
        + "    final Position end = converter.apply(range.end);\n"
        + "    return new Fragment(source, start, end);\n"
        + "}";

    private static Optional<Range> newRange(
            final int beginLine,
            final int beginColumn,
            final int endLine,
            final int endColumn) {
        return Optional.of(Range.range(beginLine, beginColumn, endLine, endColumn));
    }

    private static Fragment getFragment(
            final FragmentProvider provider,
            final int beginLine,
            final int beginColumn,
            final int endLine,
            final int endColumn) {
        return provider.apply(newRange(beginLine, beginColumn, endLine, endColumn));
    }

    /**
     * Checks that an exception is thrown on empty range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmpty() {
        final SourceFile source = new SourceFile(TEXT);
        final FragmentProvider provider = new FragmentProvider(source);
        provider.apply(Optional.empty());
    }

    /**
     * Test that gets a fragment covering the entire source.
     */
    @Test
    public void testAllSource() {
        final int beginLine = 1;
        final int beginColumn = 1;
        final int endLine = 11;
        final int endColumn = 2;
        final SourceFile source = new SourceFile(TEXT);
        final FragmentProvider provider = new FragmentProvider(source);
        final Fragment fragment = getFragment(provider, beginLine, beginColumn, endLine, endColumn);
        Assert.assertEquals(TEXT, fragment.toString());
    }

    /**
     * Test that takes a fragment of a source and compares it with the original text.
     */
    @Test
    public void test() {
        final String text = "throw new IllegalArgumentException();";
        final int beginLine = 5;
        final int beginColumn = 9;
        final int endLine = 5;
        final int endColumn = beginColumn + text.length();
        final SourceFile source = new SourceFile(TEXT);
        final FragmentProvider provider = new FragmentProvider(source);
        final Fragment fragment = getFragment(provider, beginLine, beginColumn, endLine, endColumn);
        Assert.assertEquals(text, fragment.toString());
    }

}
