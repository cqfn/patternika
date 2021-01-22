package org.cqfn.patternika.source;

import java.util.Objects;

/**
 * Iterator over a source file (multiline text).
 *
 * @since 2019/12/09
 */
class SourceFileIterator implements SourceIterator {
    /** Source file text. */
    private final String data;
    /** Current position (character index), starts from 0. */
    private int index;
    /** Row index, starts from 1. */
    private int row;
    /** Column index, starts from 1. */
    private int column;

    /**
     * Constructor.
     *
     * @param data Source file text, not {@code null}.
     */
    SourceFileIterator(final String data) {
        this.data = Objects.requireNonNull(data);
        this.index = 0;
        this.row = 1;
        this.column = 1;
    }

    @Override
    public char nextChar() {
        if (index >= data.length()) {
            return 0;
        }
        final char result = data.charAt(index++);
        if (result == '\n') {
            row++;
            column = 1;
        } else {
            column++;
        }
        return result;
    }

    @Override
    public char getChar(final int offset) {
        final int pos = index + offset;
        return pos < data.length() ? data.charAt(pos) : 0;
    }

    @Override
    public Position getPosition(final int offset) {
        return new SourceFilePosition(index + offset, row, column + offset);
    }

}
