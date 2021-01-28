package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.ast.Node;

import org.cqfn.patternika.parser.Adapter;
import org.cqfn.patternika.source.Source;

import java.util.function.Function;

/**
 * Implementation of {@link Adapter} that adapts JavaParser AST to the Patternika format.
 *
 * @since 2021/01/26
 */
public class JavaParserAdapter implements Adapter<Node> {
    /** Function that extracts data from JavaParser nodes depending on their types. */
    private final Function<Node, String> data = new NodeDataExtractor();

    /**
     * Adapts the AST build by JavaParser to the Patternika format.
     *
     * @param source the source.
     * @param root the root of the AST built by JavaParser.
     * @return the AST in the Patternika format.
     */
    @Override
    public JavaNode adapt(final Source source, final Node root) {
        final FragmentProvider fragments = new FragmentProvider(source);
        final JavaNodeFactory factory = new JavaNodeFactory(data, fragments);
        return factory.apply(root);
    }
}
