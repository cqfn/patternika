package org.cqfn.patternika.source;

import java.util.Objects;

/**
 * Represents source code from a string.
 *
 * @since 2019/10/28
 */
public class SourceString implements Source {
    /** Source code represented as a string. */
    private final String data;

    /**
     * Constructor.
     *
     * @param string Source code represented as a string, not {@code null}.
     */
    public SourceString(final String string) {
        this.data = Objects.requireNonNull(string);
    }

    /**
     * Returns iterator over the string.
     *
     * @return source iterator.
     */
    @Override
    public SourceIterator getIterator() {
        return new SourceStringIterator(data);
    }

    /**
     * Return a fragment of the string in the specified position range.
     *
     * @param start start position.
     * @param end end position.
     * @return fragment of the string.
     */
    @Override
    public String getFragmentAsString(final Position start, final Position end) {
        SourceStringPosition.checkType(start);
        SourceStringPosition.checkType(end);
        return data.substring(start.getIndex(), end.getIndex());
    }

}
