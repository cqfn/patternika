package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.ast.Node;
import org.cqfn.patternika.source.Source;

/**
 * Interface for adapters that convert the AST build by JavaParser
 * to the Patternika format.
 *
 * @since 2021/01/23
 */
public interface Adapter {
    /**
     * Adapts the AST build by JavaParser to the Patternika format.
     *
     * @param source the source.
     * @param root the root of the AST built by JavaParser.
     * @return the AST in
     */
    JavaNode adapt(Source source, Node root);
}
