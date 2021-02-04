package org.cqfn.patternika.ast;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * Test for the {@link ActionTree} class.
 */
public class ActionTreeTest {
    /**
     * Basic test for the {@link ActionTree} class.
     */
    @Test
    public void test() {
        final Node root = new TestNode(0);
        final ActionTree tree = new ActionTree("java", root, Collections.emptyList());
        Assert.assertSame("java", tree.getLanguage());
        Assert.assertSame(root, tree.getRoot());
        Assert.assertTrue(tree.getActions().isEmpty());
    }
}
