package org.cqfn.patternika.ast;

/**
 * Represents pair of nodes.
 *
 * @since 2020/11/11
 */
public class NodePair {
    /** First(left) node. **/
    private final Node left;
    /** Second(right) node. **/
    private final Node right;

    /**
     * Public constructor.
     *
     * @param left Node.
     * @param right Node.
     */
    public NodePair(final Node left, final Node right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns left node in pair.
     *
     * @return left node in pair.
     */
    public Node left() {
        return left;
    }

    /**
     * Returns right node in pair.
     *
     * @return right node in pair.
     */
    public Node right() {
        return right;
    }
}
