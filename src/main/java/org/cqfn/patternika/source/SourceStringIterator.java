package org.cqfn.patternika.source;

import java.util.Objects;

/**
 * Iterator over a string.
 *
 * @since 2019/10/28
 */
class SourceStringIterator implements SourceIterator {
    /** Source code represented as a string. */
    private final String data;
    /** Current position. */
    private int index;

    /**
     * Constructor.
     *
     * @param data Source code represented as a string, not {@code null}.
     */
    SourceStringIterator(final String data) {
        this.data = Objects.requireNonNull(data);
        this.index = 0;
    }

    @Override
    public char nextChar() {
        return index < data.length() ? data.charAt(index++) : 0;
    }

    @Override
    public char getChar(final int offset) {
        final int pos = index + offset;
        return pos < data.length() ? data.charAt(pos) : 0;
    }

    @Override
    public Position getPosition(final int offset) {
        return new SourceStringPosition(index + offset);
    }

}
