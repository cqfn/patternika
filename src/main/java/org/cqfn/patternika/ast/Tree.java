package org.cqfn.patternika.ast;

import java.util.Objects;

/**
 * Stores an abstract syntax tree (AST) node and related information.
 *
 * @since 2020/11/9
 */
public class Tree {
    /** Language of the code represented by the node tree. */
    private final String language;
    /** Root of the node tree. */
    private final Node root;

    /**
     * Constructor.
     *
     * @param language Language of the code represented by the node tree.
     * @param root Root of the node tree.
     */
    public Tree(final String language, final Node root) {
        this.language = Objects.requireNonNull(language);
        this.root = Objects.requireNonNull(root);
    }

    /**
     * Returns the language of the code represented by the node tree.
     *
     * @return Language.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Returns the root node of the tree.
     *
     * @return Root node.
     */
    public Node getRoot() {
        return root;
    }

}
