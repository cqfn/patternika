package org.cqfn.patternika.ast;

import org.cqfn.patternika.source.Fragment;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests default method implementations of the {@link Node} interface.
 *
 * @since 2020/11/24
 */
public class NodeTest {

    /**
     * Tests the {@link Node#isChildCountLimitless()} method.
     * Case 1: limitless=true.
     */
    @Test
    public void testIsChildCountLimitless1() {
        final Node node = createNode(true);
        Assert.assertTrue(node.isChildCountLimitless());
        Assert.assertFalse(node.isChildOrderStrict());
    }

    /**
     * Tests the {@link Node#isChildCountLimitless()} method.
     * Case 2: limitless=false.
     */
    @Test
    public void testIsChildCountLimitless2() {
        final Node node = createNode(false);
        Assert.assertFalse(node.isChildCountLimitless());
        Assert.assertTrue(node.isChildOrderStrict());
    }

    /**
     * Creates a test node.
     *
     * @param limitlessChilds whether the node has limits on the number of its children.
     * @return new node.
     */
    private Node createNode(final boolean limitlessChilds) {
        return new Node() {
            @Override
            public String getType() {
                return null;
            }

            @Override
            public String getData() {
                return null;
            }

            @Override
            public Fragment getFragment() {
                return null;
            }

            @Override
            public int getChildCount() {
                return 0;
            }

            @Override
            public Node getChild(final int index) {
                return null;
            }

            @Override
            public boolean matches(final Node other) {
                return false;
            }

            @Override
            public boolean isChildCountLimitless() {
                return limitlessChilds;
            }
        };
    }
}
