package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.LiteralStringValueExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithIdentifier;
import com.github.javaparser.ast.type.PrimitiveType;

import java.util.function.Function;

/**
 * Function that extracts data from JavaParser nodes depending on their types.
 *
 * @since 2021/01/28
 */
public class NodeDataExtractor implements Function<Node, String> {
    /**
     * Applies this function to the given argument.
     *
     * @param node the JavaParser node.
     * @return the data from the node or {@code null} if there is not data to return.
     */
    @Override
    public String apply(final Node node) {
        if (node instanceof Modifier) {
            return ((Modifier) node).getKeyword().asString();
        }
        if (node instanceof NodeWithIdentifier) {
            return ((NodeWithIdentifier<?>) node).getIdentifier();
        }
        if (node instanceof PrimitiveType) {
            return ((PrimitiveType) node).getType().asString();
        }
        if (node instanceof LiteralStringValueExpr) {
            return ((LiteralStringValueExpr) node).getValue();
        }
        if (node instanceof BinaryExpr) {
            return ((BinaryExpr) node).getOperator().asString();
        }
        return null;
    }
}
