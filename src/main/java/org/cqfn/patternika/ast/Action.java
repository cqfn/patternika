package org.cqfn.patternika.ast;

import java.util.Objects;

/**
 * Action that describes a change in an abstract syntax tree.
 *
 * @since 2020/11/06
 */
@SuppressWarnings("PMD.DataClass")
public class Action {
    /** Action type. */
    private final ActionType type;

    /** Parent node for the reference node (children of this node are updated on action). */
    private final Node parent;

    /** Reference node for the action. */
    private final Node ref;

    /** Accept node tree for the action. */
    private final Node accept;

    /**
     * Constructor.
     *
     * @param type the action type.
     * @param parent the parent node for the reference node.
     * @param ref the reference node for the action.
     * @param accept the accept node tree for the action.
     */
    public Action(
            final ActionType type,
            final Node parent,
            final Node ref,
            final Node accept) {
        this.type = Objects.requireNonNull(type);
        this.parent = parent;
        this.ref = ref;
        this.accept = accept;
    }

    /**
     * Returns identifier that uniquely identifies action type.
     *
     * @return Action type identifier.
     */
    public ActionType getType() {
        return type;
    }

    /**
     * Returns the parent node for the reference node (children of this node are updated on action).
     *
     * @return the parent node for the reference node.
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Returns the reference node for the action (the place where the action is to be applied).
     *
     * @return Reference node.
     */
    public Node getRef() {
        return ref;
    }

    /**
     * Returns the accept node tree for the action (new tree to update the tree).
     *
     * @return Accept node tree.
     */
    public Node getAccept() {
        return accept;
    }

}
