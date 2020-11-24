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
        final Node node = createNode(-1);
        Assert.assertEquals(-1, node.getMaxChildCount());
        Assert.assertTrue(node.isChildCountLimitless());
    }

    /**
     * Tests the {@link Node#isChildCountLimitless()} method.
     * Case 2: limitless=false.
     */
    @Test
    public void testIsChildCountLimitless2() {
        final Node node = createNode(0);
        Assert.assertEquals(0, node.getMaxChildCount());
        Assert.assertFalse(node.isChildCountLimitless());
    }

    /**
     * Creates a test node.
     *
     * @param maxChildCount maximum child count.
     * @return new node.
     */
    private Node createNode(final int maxChildCount) {
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
            public int getMaxChildCount() {
                return maxChildCount;
            }

            @Override
            public Node getChild(final int index) {
                return null;
            }

            @Override
            public boolean matches(final Node other) {
                return false;
            }
        };
    }
}
