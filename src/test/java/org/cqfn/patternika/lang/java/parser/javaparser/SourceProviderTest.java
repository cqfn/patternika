package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.Provider;
import org.cqfn.patternika.source.Source;
import org.cqfn.patternika.source.SourceFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Tests for the {@link SourceProvider} class.
 *
 * @since 2021/01/25
 */
public class SourceProviderTest {
    /**
     * Test text to be read.
     */
    private static final String TEXT =
          "/**\n"
        + " * Creates and returns a modifiable list of node children.\n"
        + " *\n"
        + " * @return a new modifiable list of children.\n"
        + " */\n"
        + "public List<T> toList() {\n"
        + "    final List<T> result = new ArrayList<>(parent.getChildCount());\n"
        + "    forEach(result::add);\n"
        + "    return result;\n"
        + "}";

    /**
     * Test that reads all text from the provided in one read.
     */
    @Test
    public void testReadAll() throws IOException {
        final Source source = new SourceFile(TEXT);
        try (Provider provider = new SourceProvider(source.getIterator())) {
            final char[] buffer = new char[TEXT.length()];
            final int length = provider.read(buffer, 0, buffer.length);
            Assert.assertEquals(TEXT.length(), length);
            Assert.assertEquals(TEXT, new String(buffer));
            Assert.assertEquals(-1, provider.read(buffer, 0, buffer.length));
        }
    }

    /**
     * Test that reads all text from the provided in several reads.
     */
    @Test
    public void testReadByParts() throws IOException {
        final Source source = new SourceFile(TEXT);
        final int bufferSize = 40;
        final char[] buffer = new char[bufferSize];
        try (Provider provider = new SourceProvider(source.getIterator())) {
            final StringBuilder builder = new StringBuilder();
            int totalLength = 0;
            int length;
            do {
                length = provider.read(buffer, 0, buffer.length);
                if (length != -1) {
                    builder.append(buffer, 0, length);
                    totalLength += length;
                }
            } while (length == bufferSize);
            Assert.assertEquals(TEXT.length(), totalLength);
            Assert.assertEquals(TEXT, builder.toString());
            Assert.assertEquals(-1, provider.read(buffer, 0, buffer.length));
        }
    }

    /**
     * Test that reads all text from the provided in several reads with offset.
     */
    @Test
    public void testReadByPartsWithOffset() throws IOException {
        final Source source = new SourceFile(TEXT);
        final int bufferSize = 64;
        final int offset = 16;
        final char[] buffer = new char[bufferSize];
        try (Provider provider = new SourceProvider(source.getIterator())) {
            final StringBuilder builder = new StringBuilder();
            int totalLength = 0;
            int length;
            do {
                length = provider.read(buffer, offset, bufferSize - offset);
                if (length != -1) {
                    builder.append(buffer, offset, length);
                    totalLength += length;
                }
            } while (length == bufferSize - offset);
            Assert.assertEquals(TEXT.length(), totalLength);
            Assert.assertEquals(TEXT, builder.toString());
            Assert.assertEquals(-1, provider.read(buffer, 0, buffer.length));
        }
    }

    /**
     * Test that checks that an exception is thrown on illegal length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalLength() throws IOException {
        final Source source = new SourceFile(TEXT);
        final int bufferSize = 40;
        final char[] buffer = new char[bufferSize];
        try (Provider provider = new SourceProvider(source.getIterator())) {
            provider.read(buffer, 2, -1);
        }
    }

    /**
     * Test that checks that an exception is thrown on illegal offset.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalOffset() throws IOException {
        final Source source = new SourceFile(TEXT);
        final int bufferSize = 40;
        final char[] buffer = new char[bufferSize];
        try (Provider provider = new SourceProvider(source.getIterator())) {
            provider.read(buffer, -1, bufferSize);
        }
    }

}
