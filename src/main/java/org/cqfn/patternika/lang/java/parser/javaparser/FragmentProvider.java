package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;

import org.cqfn.patternika.source.Fragment;
import org.cqfn.patternika.source.Position;
import org.cqfn.patternika.source.Source;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Function that provides code fragments for JavaParser nodes.
 *
 * @since 2021/01/28
 */
public class FragmentProvider implements Function<Node, Fragment> {
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
     * @param node a JavaParser node.
     * @return a code fragment for the specified JavaParser node.
     */
    @Override
    public Fragment apply(final Node node) {
        final Optional<Range> optionalRange = node.getRange();
        if (!optionalRange.isPresent()) {
            throw new IllegalArgumentException();
        }
        final Range range = optionalRange.get();
        final Position start = converter.apply(range.begin);
        final Position end = converter.apply(range.end);
        return new Fragment(source, start, end);
    }
}
