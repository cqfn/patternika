package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.LiteralStringValueExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithIdentifier;
import com.github.javaparser.ast.nodeTypes.NodeWithStatements;
import com.github.javaparser.ast.type.PrimitiveType;

import org.cqfn.patternika.parser.Adapter;
import org.cqfn.patternika.source.Fragment;
import org.cqfn.patternika.source.Source;

import java.util.function.Function;
import java.util.function.Supplier;

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
     * @param root the root of the AST built by JavaParser.
     * @return the AST in the Patternika format.
     */
    @Override
    public JavaNode adapt(final Source source, final Node root) {
        final FragmentProvider fragmentProvider = new FragmentProvider(source);
        return newJavaNode(root, fragmentProvider);
    }

    /**
     * Creates a Patternika node for a JavaParser node.
     *
     * @param root a JavaParser node.
     * @param fragmentProvider provides code fragments for JavaParser nodes.
     * @return a Patternika node.
     */
    private JavaNode newJavaNode(final Node root, final FragmentProvider fragmentProvider)  {
        final String data = getData(root);
        final boolean limitlessChildren = isChildCountLimitless(root);
        final Function<Node, JavaNode> factory = node -> newJavaNode(node, fragmentProvider);
        final Supplier<Fragment> fragment = new FragmentSupplier(root, fragmentProvider);
        return new JavaNode(root, factory, data, limitlessChildren, fragment);
    }

    /**
     * Gets data from a JaveParser node.
     *
     * @param node the JavaParser node.
     * @return the data from the node or {@code null} if there is not data to return.
     */
    private String getData(final Node node) {
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

    /**
     * Checks whether the node has limits on the number of its children.
     * When a node has no limits, it can have from 0 to N children, where N is any positive number.
     *
     * <p>This information is needed when we want to get rid of some children.
     * When there are no limits, we can safely exclude any number of children.
     *
     * @param node the JavaParser node.
     * @return {@code true} if there are no constraints on child count or {@code false} otherwise.
     */
    private boolean isChildCountLimitless(final Node node) {
        return node instanceof CompilationUnit
                || node instanceof ClassOrInterfaceDeclaration
                || node instanceof EnumDeclaration
                || node instanceof NodeWithStatements
                || node instanceof ArrayInitializerExpr;
    }
}
