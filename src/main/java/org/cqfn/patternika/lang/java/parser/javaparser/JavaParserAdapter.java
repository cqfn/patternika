package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.ast.Node;
import org.cqfn.patternika.parser.Adapter;
import org.cqfn.patternika.source.Source;

/**
 * Implementation of {@link Adapter} that adapts JavaParser AST to the Patternika format.
 *
 * @since 2021/01/26
 */
public class JavaParserAdapter implements Adapter<Node> {

    /**
     * Adapts the AST build by JavaParser to the Patternika format.
     *
     * @param source the source.
     * @param root   the root of the AST built by JavaParser.
     * @return the AST in the Patternika format.
     */
    @Override
    public JavaNode adapt(final Source source, final Node root) {
        return null;
    }

}
