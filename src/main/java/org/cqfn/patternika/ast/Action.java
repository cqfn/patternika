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
    private final Node parentNode;
    /** Reference node for the action. */
    private final Node refNode;
    /** Accept node tree for the action. */
    private final Node acceptNode;

    /**
     * Constructor.
     *
     * @param type the action type.
     * @param parentNode the parent node for the reference node.
     * @param refNode the reference node for the action.
     * @param acceptNode the accept node tree for the action.
     */
    public Action(
            final ActionType type,
            final Node parentNode,
            final Node refNode,
            final Node acceptNode) {
        this.type = Objects.requireNonNull(type);
        this.parentNode = Objects.requireNonNull(parentNode);
        this.refNode = refNode;
        this.acceptNode = acceptNode;
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
    public Node getParentNode() {
        return parentNode;
    }

    /**
     * Returns the reference node for the action (the place where the action is to be applied).
     *
     * @return Reference node.
     */
    public Node getRefNode() {
        return refNode;
    }

    /**
     * Returns the accept node tree for the action (new tree to update the tree).
     *
     * @return Accept node tree.
     */
    public Node getAcceptNode() {
        return acceptNode;
    }

}
