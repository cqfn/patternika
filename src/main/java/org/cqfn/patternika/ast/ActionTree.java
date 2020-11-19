package org.cqfn.patternika.ast;

import java.util.Objects;

/**
 * Stores an abstract syntax tree (AST) with actions and related information.
 *
 * @since 2020/11/9
 */
public class ActionTree {
    /** Language of the code represented by the node tree. */
    private final String language;
    /** Root of the AST tree. */
    private final Node root;

    /**
     * Constructor.
     *
     * @param language language of the code represented by the tree, not {@code null}.
     * @param root root of the AST tree, not {@code null}.
     */
    public ActionTree(final String language, final Node root) {
        this.language = Objects.requireNonNull(language);
        this.root = Objects.requireNonNull(root);
    }

    /**
     * Returns the language of the code represented by the node tree.
     *
     * @return language.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Returns the root node of the AST tree.
     *
     * @return root node.
     */
    public Node getRoot() {
        return root;
    }

}
