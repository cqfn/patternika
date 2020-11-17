package org.cqfn.patternika.source;

import java.util.Objects;

/**
 * Represents source code from file (multiline text).
 *
 * @since 2019/12/09
 **/
public class SourceFile implements Source {
    /** Source file text. */
    private final String data;

    /**
     * Constructor.
     *
     * @param text Source file text, not {@code null}.
     */
    public SourceFile(final String text) {
        this.data = Objects.requireNonNull(text);
    }

    /**
     * Returns iterator over the source file text.
     *
     * @return source iterator.
     */
    @Override
    public SourceIterator getIterator() {
        return new SourceFileIterator(data);
    }

    /**
     * Return a string for a source code fragment within the specified range.
     *
     * @param start start position.
     * @param end end position.
     * @return text for the fragment.
     */
    @Override
    public String getFragmentAsString(final Position start, final Position end) {
        SourceFilePosition.checkType(start);
        SourceFilePosition.checkType(end);
        return data.substring(start.getIndex(), end.getIndex());
    }

}
