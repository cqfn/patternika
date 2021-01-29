package org.cqfn.patternika.lang.java.parser.javaparser;

import org.cqfn.patternika.source.Position;
import org.cqfn.patternika.source.SourceFile;
import org.cqfn.patternika.source.SourceFilePosition;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the {@link PositionConverter} class.
 *
 * @since 2021/01/29
 */
public class PositionConverterTest {
    /** Source text for tests. */
    private static final String TEXT =
          "/**\n"
        + " * Main constructor.\n"
        + " *\n"
        + " * @param source source, not {@code null}.\n"
        + " * @param start start position, can be {@code null} meaning no start.\n"
        + " * @param end end position, can be {@code null} meaning no end.\n"
        + " */\n"
        + "public Fragment(final Source source, final Position start, final Position end) {\n"
        + "    this.source = Objects.requireNonNull(source);\n"
        + "    // Start is always a lesser existing position.\n"
        + "    if (start == null || end == null || start.compareTo(end) < 0) {\n"
        + "        this.start = start;\n"
        + "        this.end = end;\n"
        + "    } else {\n"
        + "        this.start = end;\n"
        + "        this.end = start;\n"
        + "    }\n"
        + "}";
    /** Test source code. */
    private static final SourceFile SOURCE = new SourceFile(TEXT);

    /**
     * Creates a new JavaParser position.
     *
     * @param line line.
     * @param column column.
     * @return a new JavaParser position.
     */
    private static com.github.javaparser.Position newPosition(final int line, final int column) {
        return new com.github.javaparser.Position(line, column);
    }

    /**
     * Tests that the specified position is correctly converted (found in the source).
     *
     * @param converter converter.
     * @param line line for the position.
     * @param column column for the position.
     */
    private static void testPosition(
            final PositionConverter converter,
            final int line,
            final int column) {
        final Position pos = converter.apply(newPosition(line, column));
        if (!(pos instanceof SourceFilePosition)) {
            Assert.fail(pos + " is not a source file position!");
        }
        final SourceFilePosition filePos = (SourceFilePosition) pos;
        Assert.assertEquals(line, filePos.getRow());
        Assert.assertEquals(column, filePos.getColumn());
    }

    /**
     * Tests position conversion for the specified line.
     *
     * @param converter the converter.
     * @param line the line to be tested.
     * @param columns the number of columns in the specified line.
     */
    private void testLine(
            final PositionConverter converter,
            final int line,
            final int columns) {
        if (columns > 0) {
            testPosition(converter, line, 1);
            testPosition(converter, line, columns);
            testPosition(converter, line, columns / 2 + 1);
        }
    }

    /**
     * Basic test for position conversion. Checks that positions are converted correctly.
     */
    @Test
    public void test() {
        final PositionConverter converter = new PositionConverter(SOURCE);
        final List<Integer> lines = new ArrayList<>();
        int columns = 0;
        for (final char val : TEXT.toCharArray()) {
            if (val == '\n') {
                lines.add(columns);
                columns = 0;
            } else {
                columns++;
            }
        }
        for (int i = 0; i < lines.size(); i += 2) {
            final int cols = lines.get(i);
            testLine(converter, i + 1, cols);
        }
        for (int i = 1; i < lines.size(); i += 2) {
            final int cols = lines.get(i);
            testLine(converter, i + 1, cols);
        }
        Assert.assertTrue(true);
    }

    /**
     * Checks that an exception is thrown when the specified position
     * does not exist in the source (line is greater than the last line in the source).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLineOutOfBounds() {
        final PositionConverter converter = new PositionConverter(SOURCE);
        testPosition(converter, 31, 1);
    }

    /**
     * Checks that an exception is thrown when the specified position
     * does not exist in the source (column is greater than the length of the target line).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testColumnOutOfBounds() {
        final PositionConverter converter = new PositionConverter(SOURCE);
        testPosition(converter, 1, 31);
    }
}
