package org.cqfn.patternika.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
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
    /** Actions grouped by the parent node. */
    private final Map<Node, List<Action>> actionsByParent;
    /** Actions grouped by the reference node. */
    private final Map<Node, List<Action>> actionsByRef;

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
        this.actions = Collections.unmodifiableList(actions);
        this.actionsByParent = new IdentityHashMap<>();
        this.actionsByRef = new IdentityHashMap<>();
        for (final Action action : actions) {
            addToMap(actionsByParent, action.getParent(), action);
            addToMap(actionsByRef, action.getRef(), action);
        }
    }

    private static <K, V> void addToMap(final Map<K, List<V>> map, final K key, final V value) {
        final List<V> values = map.computeIfAbsent(key, x -> new ArrayList<>());
        values.add(value);
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

    /**
     * Returns an unmodifiable list of actions for the given parent.
     *
     * @param parent the parent node for actions.
     * @return the unmodifiable list of actions for the given parent.
     */
    public List<Action> getActionsByParent(final Node parent) {
        return Collections.unmodifiableList(actionsByParent.get(parent));
    }

    /**
     * Returns an unmodifiable list of actions for the given reference node.
     *
     * @param ref the reference node for actions.
     * @return the unmodifiable list of actions for the given reference node.
     */
    public List<Action> getActionsByRef(final Node ref) {
        return Collections.unmodifiableList(actionsByRef.get(ref));
    }
}
