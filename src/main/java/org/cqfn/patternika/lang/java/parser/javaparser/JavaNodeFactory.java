package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.ast.Node;

import org.cqfn.patternika.source.Fragment;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Factory that creates Patternika nodes for JavaParser nodes.
 *
 * @since 2021/01/28
 */
public class JavaNodeFactory implements Function<Node, JavaNode> {
    /** Extracts data from JavaParser nodes depending on their types. */
    private final Function<Node, String> dataExtractor;
    /** Provides code fragments for JavaParser nodes. */
    private final Function<Node, Fragment> fragmentProvider;

    /**
     * Constructor.
     *
     * @param dataExtractor extracts data from JavaParser nodes depending on their types.
     * @param fragmentProvider provides code fragments for JavaParser nodes.
     */
    public JavaNodeFactory(
            final Function<Node, String> dataExtractor,
            final Function<Node, Fragment> fragmentProvider) {
        this.dataExtractor = dataExtractor;
        this.fragmentProvider = fragmentProvider;
    }

    /**
     * Creates a Patternika node for JavaParser node.
     *
     * @param node the JavaParser node.
     * @return a Patternika node.
     */
    @Override
    public JavaNode apply(final Node node) {
        final String data = dataExtractor.apply(node);
        final Supplier<Fragment> fragment = new FragmentSupplier(node, fragmentProvider);
        return new JavaNode(node, this, data, fragment);
    }
}

