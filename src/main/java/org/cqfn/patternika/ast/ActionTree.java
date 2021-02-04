package org.cqfn.patternika.ast;

import java.util.Collections;
import java.util.List;
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
    /** List of actions related to the AST. */
    private final List<Action> actions;

    /**
     * Constructor.
     *
     * @param language the language of the code represented by the tree, not {@code null}.
     * @param root the root of the AST tree, not {@code null}.
     * @param actions the list of actions related to the AST.
     */
    public ActionTree(final String language, final Node root, final List<Action> actions) {
        this.language = Objects.requireNonNull(language);
        this.root = Objects.requireNonNull(root);
        this.actions = Collections.unmodifiableList(Objects.requireNonNull(actions));
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

    /**
     * Returns an unmodifiable list of actions.
     *
     * @return the unmodifiable list of actions.
     */
    public List<Action> getActions() {
        return actions;
    }
}
