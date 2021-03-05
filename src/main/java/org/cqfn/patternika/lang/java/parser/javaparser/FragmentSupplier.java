package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import org.cqfn.patternika.source.Fragment;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Lazy provider for source node fragment.
 * It is implemented lazy because getting a code fragment is not always required.
 *
 * @since 2021/01/28
 */
public class FragmentSupplier implements Supplier<Fragment> {
    /** JavaParser node associated with the code fragment. */
    private final Node node;

    /** Function that provides code fragments for JavaParser ranges. */
    private final Function<Optional<Range>, Fragment> provider;

    /** Source code fragment (lazy). */
    private Fragment fragment;

    /**
     * Constructor.
     *
     * @param node the JavaParser node associated with the code fragment.
     * @param provider the function that provides code fragments for JavaParser ranges.
     */
    public FragmentSupplier(final Node node, final Function<Optional<Range>, Fragment> provider) {
        this.node = Objects.requireNonNull(node);
        this.provider = Objects.requireNonNull(provider);
    }

    /**
     * Returns a code fragment.
     *
     * @return a code fragment.
     */
    @Override
    public Fragment get() {
        if (fragment == null) {
             fragment = provider.apply(node.getRange());
        }
        return fragment;
    }
}
