package org.cqfn.patternika.ast;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Predicate for checking that two node trees are equal.
 *
 * @since 2020/11/5
 */
public class DeepEquals implements BiPredicate<Node, Node> {
    /**
     * Predicate for checking whether a node is a hole.
     * Used to avoid explicit dependency on the Hole interface.
     */
    private final Predicate<Node> isHole;

    /**
     * Constructor.
     *
     * @param isHole Predicate that checks whether a node is a hole.
     */
    public DeepEquals(final Predicate<Node> isHole) {
        this.isHole = Objects.requireNonNull(isHole);
    }

    /**
     * Tests two node trees for equality.
     *
     * @param root1 First node tree.
     * @param root2 Second node tree.
     * @return {@code true} is the node trees are equal or {@code false} otherwise.
     */
    @Override
    public boolean test(final Node root1, final Node root2) {
        // Equal node trees must have an equal (compatible) type for their roots.
        if (!testEquality(root1, root2)) {
            return false;
        }
        // If one of the root nodes is a hole, the node trees are considered equal.
        if (isHole.test(root1) || isHole.test(root2)) {
            return true;
        }
        // In equal node trees, roots must have equal children.
        return testChildren(root1, root2);
    }

    private boolean testEquality(final Node node1, final Node node2) {
        if (node1 == node2) {
            return true;
        }
        if (node1 == null || node2 == null) {
            return false;
        }
        // We compare them both ways, as equals can be asymmetric.
        return node1.equals(node2) || node2.equals(node1);
    }

    private boolean testChildren(final Node root1, final Node root2) {
        final int count1 = root1.getChildCount();
        final int count2 = root2.getChildCount();
        if (count1 == count2) {
            for (int index = 0; index < count1; ++index) {
                if (!test(root1.getChild(index), root2.getChild(index))) {
                    return false;
                }
            }
        }
        return true;
    }

}
