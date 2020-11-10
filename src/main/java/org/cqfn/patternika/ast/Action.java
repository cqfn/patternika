package org.cqfn.patternika.ast;

/**
 * Action that describes a change in a node tree.
 *
 * @since 2020/11/6
 */
public interface Action {

    /**
     * Returns identifier that uniquely identifies action type.
     *
     * @return Action type identifier.
     */
    String getType();

    /**
     * Returns the reference node for the action (node to be updated).
     *
     * @return Reference node.
     */
    Node getRefNode();

    /**
     * Returns the accept node tree for the action (new tree to update the reference node).
     *
     * @return Accept node tree.
     */
    Node getAcceptNode();

}
