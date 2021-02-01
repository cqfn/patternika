package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.Range;

import org.cqfn.patternika.source.Fragment;
import org.cqfn.patternika.source.Position;
import org.cqfn.patternika.source.Source;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Function that provides code fragments for JavaParser ranges.
 *
 * @since 2021/01/28
 */
public class FragmentProvider implements Function<Optional<Range>, Fragment> {
    /** Source of code fragments. */
    private final Source source;
    /** Function that converts JavaParser positions to positions in the Patternika format. */
    private final PositionConverter converter;

    /**
     * Constructor.
     *
     * @param source the source of code fragments.
     */
    public FragmentProvider(final Source source) {
        this.source = Objects.requireNonNull(source);
        this.converter = new PositionConverter(source);
    }

    /**
     * Provides provides a code fragment for specified JavaParser node.
     *
     * @param optionalRange the JavaParser range for the fragment.
     * @return the code fragment for the specified range.
     */
    @Override
    public Fragment apply(final Optional<Range> optionalRange) {
        if (!optionalRange.isPresent()) {
            throw new IllegalArgumentException();
        }
        final Range range = optionalRange.get();
        final Position start = converter.apply(range.begin);
        // The end column is included in the range.
        final Position end = converter.apply(range.end.withColumn(range.end.column + 1));
        return new Fragment(source, start, end);
    }
}
