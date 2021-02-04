package org.cqfn.patternika.ast;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * Test for the {@link ActionTree} class.
 */
public class ActionTreeTest {
    /**
     * Test for an action tree without actions.
     */
    @Test
    public void testEmpty() {
        final Node root = new TestNode(0);
        final ActionTree tree = new ActionTree("java", root, Collections.emptyList());
        Assert.assertSame("java", tree.getLanguage());
        Assert.assertSame(root, tree.getRoot());
        Assert.assertTrue(tree.getActions().isEmpty());
    }
}
