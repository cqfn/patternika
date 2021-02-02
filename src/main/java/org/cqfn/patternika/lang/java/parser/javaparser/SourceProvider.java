package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.Provider;
import org.cqfn.patternika.source.SourceIterator;

import java.util.Objects;

/**
 * Provider for JavaParser that reads characters from {@link SourceIterator}.
 *
 * @since 2021/01/21
 */
public class SourceProvider implements Provider {
    /** Source iterator. */
    private final SourceIterator iterator;

    /**
     * Constructor.
     *
     * @param iterator a source iterator.
     */
    public SourceProvider(final SourceIterator iterator) {
        this.iterator = Objects.requireNonNull(iterator);
    }

    /**
     * Reads characters from source into an array.
     *
     * @param array the destination array.
     * @param offset the offset at which to start storing characters.
     * @param length the maximum possible number of characters to read.
     * @return the number of characters read, or -1 at the end of the stream.
     */
    @Override
    public int read(final char[] array, final int offset, final int length) {
        if (offset < 0) {
            throw new IllegalArgumentException("Illegal offset: " + offset);
        }
        if (length <= 0) {
            throw new IllegalArgumentException("Illegal length: " + length);
        }
        if (iterator.getChar() == 0) {
            return -1;
        }
        int count = 0;
        final int end = Math.min(array.length, offset + length);
        for (int index = offset; index < end; ++index) {
            final char val = iterator.nextChar();
            if (val == 0) {
                break;
            }
            array[index] = val;
            count++;
        }
        return count;
    }

    /**
     * Closes the provider.
     */
    @Override
    public void close() {
        // Do not need to do anything.
    }

}
