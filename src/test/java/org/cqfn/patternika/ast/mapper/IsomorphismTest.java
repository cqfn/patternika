package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.TestNode;
import org.cqfn.patternika.ast.iterator.DepthFirst;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * Tests for the {@link Isomorphism} class.
 *
 * @since 2020/12/29
 */
public class IsomorphismTest {

    /**
     * Test for the {@link Isomorphism} class.
     * <p>
     * Adds to nodes to isomorphic groups and checks that they are correctly added.
     */
    @Test
    public void test() {
        final Node node11 = new TestNode("Type2", 11);
        final Node node21 = new TestNode("Type1", 21);
        final Node node22 = new TestNode("Type2", 22);
        final Node node31 = new TestNode("Type2", 31);
        final Node node41 = new TestNode("Type1", 41);
        final Node node42 = new TestNode("Type2", 42);
        final Node node51 = new TestNode("Type1", 51);
        final Node tree = new TestNode(0,
                new TestNode("Type1", 1, node11),
                new TestNode("Type2", 2, node21, node22),
                new TestNode("Type1", 3, node31),
                new TestNode("Type2", 4, node41, node42),
                new TestNode("Type1", 5, node51),
                new TestNode("Type3", 6)
            );
        final Isomorphism isomorphism = new Isomorphism();
        Assert.assertNull(isomorphism.getGroup(0));
        Assert.assertNull(isomorphism.getGroup(new TestNode(0)));
        new DepthFirst<>(tree).forEach(isomorphism::add);
        Assert.assertNull(isomorphism.getGroup(new TestNode(0)));
        Assert.assertEquals(
                Collections.singleton(tree),
                isomorphism.getGroup(tree)
            );
        Assert.assertEquals(
                Collections.singleton(tree.getChild(tree.getChildCount() - 1)),
                isomorphism.getGroup(new TestNode("Type3", 0))
            );
        Assert.assertEquals(
                new HashSet<>(Arrays.asList(tree.getChild(0), tree.getChild(2))),
                isomorphism.getGroup(tree.getChild(0))
            );
        Assert.assertEquals(
                new HashSet<>(Arrays.asList(tree.getChild(1),
                                            tree.getChild(tree.getChildCount() / 2))),
                isomorphism.getGroup(tree.getChild(1))
            );
        Assert.assertEquals(
                new HashSet<>(Arrays.asList(node21, node41, node51)),
                isomorphism.getGroup(new TestNode("Type1", 0))
            );
        Assert.assertEquals(
                new HashSet<>(Arrays.asList(node11, node22, node31, node42)),
                isomorphism.getGroup(new TestNode("Type2", 0))
            );
    }

}
