package org.cqfn.patternika.ast;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test for classes {@link Action}, {@link ActionType}, and {@link ActionTree}.
 */
public class ActionTreeTest {
    /**
     * Test for the {@link ActionType} enumeration.
     * Ensures that action types and their textual representations do not change unexpectedly.
     */
    @Test
    public void testActionType() {
        // DELETE, INSERT_BEFORE, INSERT_AFTER, UPDATE
        final int actionTypeCount = 4;
        Assert.assertEquals(actionTypeCount, ActionType.values().length);
        Assert.assertEquals("ActionDelete", ActionType.DELETE.getText());
        Assert.assertEquals("ActionInsertBefore", ActionType.INSERT_BEFORE.getText());
        Assert.assertEquals("ActionInsertAfter", ActionType.INSERT_AFTER.getText());
        Assert.assertEquals("ActionUpdate", ActionType.UPDATE.getText());
    }

    /**
     * Test for the {@link Action} class.
     * Checks the validity of created actions for all action types.
     */
    @Test
    public void testAction() {
        final Node root = new TestNode(0, new TestNode(1, new TestNode(2)));
        final Action delete = new Action(
                ActionType.DELETE,
                root.getChild(0),
                root.getChild(0).getChild(0),
                null
            );
        Assert.assertEquals(ActionType.DELETE, delete.getType());
        Assert.assertSame(root.getChild(0), delete.getParent());
        Assert.assertSame(root.getChild(0).getChild(0), delete.getRef());
        Assert.assertNull(delete.getAccept());
        final Node updated = new TestNode(3);
        final Action update = new Action(
                ActionType.UPDATE,
                root.getChild(0),
                root.getChild(0).getChild(0),
                updated
            );
        Assert.assertEquals(ActionType.UPDATE, update.getType());
        Assert.assertSame(root.getChild(0), update.getParent());
        Assert.assertSame(root.getChild(0).getChild(0), update.getRef());
        Assert.assertSame(updated, update.getAccept());
        final Node inserted = new TestNode(4);
        final Action before = new Action(
                ActionType.INSERT_BEFORE,
                root.getChild(0),
                root.getChild(0).getChild(0),
                inserted
        );
        Assert.assertEquals(ActionType.INSERT_BEFORE, before.getType());
        Assert.assertSame(root.getChild(0), before.getParent());
        Assert.assertSame(root.getChild(0).getChild(0), before.getRef());
        Assert.assertSame(inserted, before.getAccept());
        final Action after = new Action(
                ActionType.INSERT_AFTER,
                root.getChild(0),
                root.getChild(0).getChild(0),
                inserted
        );
        Assert.assertEquals(ActionType.INSERT_AFTER, after.getType());
        Assert.assertSame(root.getChild(0), after.getParent());
        Assert.assertSame(root.getChild(0).getChild(0), after.getRef());
        Assert.assertSame(inserted, after.getAccept());
    }

    /**
     * Test for an action tree without actions.
     */
    @Test
    public void testActionTreeEmpty() {
        final Node root = new TestNode(0);
        final ActionTree tree = new ActionTree("java", root, Collections.emptyList());
        Assert.assertSame("java", tree.getLanguage());
        Assert.assertSame(root, tree.getRoot());
        Assert.assertTrue(tree.getActions().isEmpty());
    }

    /**
     * Test creates a simple tree with action and checks its validity.
     */
    @Test
    public void testActionTree() {
        final Node root =
            new TestNode(0,
                new TestNode(10,
                    new TestNode(100),
                    new TestNode(101, new TestNode(1010))
                ),
                new TestNode(11,
                    new TestNode(110)
                ),
                new TestNode(12,
                    new TestNode(120),
                    new TestNode(121, new TestNode(1210)),
                    new TestNode(122)
                )
            );
        final List<Action> actions = Arrays.asList(
                // 0
                newAction(
                    ActionType.DELETE,
                    root.getChild(0).getChild(1),
                    0,
                    null
                ),
                // 1
                newAction(
                    ActionType.DELETE,
                    root.getChild(2),
                    2,
                    null
                ),
                // 2
                newAction(
                    ActionType.UPDATE,
                    root.getChild(2),
                    0,
                    new TestNode(240)
                ),
                // 3
                newAction(
                    ActionType.INSERT_BEFORE,
                    root.getChild(2),
                    0,
                    new TestNode(239)
                ),
                // 4
                newAction(
                    ActionType.INSERT_AFTER,
                    root.getChild(2),
                    0,
                    new TestNode(241)
                ),
                // 5
                new Action(
                    ActionType.INSERT_BEFORE,
                    root.getChild(1).getChild(0),
                    null,
                    new TestNode(1100)
                ),
                // 6
                new Action(
                    ActionType.INSERT_AFTER,
                    root.getChild(1).getChild(0),
                    null,
                    new TestNode(1101)
                )
            );
        final ActionTree tree = new ActionTree("java", root, actions);
        // Basic checks for ActionTree.
        Assert.assertEquals("java", tree.getLanguage());
        Assert.assertSame(root, tree.getRoot());
        Assert.assertEquals(actions, tree.getActions());
        // Checks for ActionTree.getActionsByParent.
        Assert.assertEquals(Collections.emptyList(), tree.getActionsByParent(root));
        final List<Action> byParent1 = tree.getActionsByParent(root.getChild(0).getChild(1));
        Assert.assertEquals(actions.subList(0, 1), byParent1);
        final List<Action> byParent2 = tree.getActionsByParent(root.getChild(2));
        Assert.assertEquals(actions.subList(1, actions.size() - 2), byParent2);
        final List<Action> byParent3 = tree.getActionsByParent(root.getChild(1).getChild(0));
        Assert.assertEquals(actions.subList(actions.size() - 2, actions.size()), byParent3);
        // Checks for ActionTree.getActionsByRef.
        Assert.assertEquals(Collections.emptyList(), tree.getActionsByRef(root));
        final List<Action> byRef1 = tree.getActionsByRef(root.getChild(0).getChild(1).getChild(0));
        Assert.assertEquals(actions.subList(0, 1), byRef1);
        final List<Action> byRef2 = tree.getActionsByRef(root.getChild(2).getChild(2));
        Assert.assertEquals(actions.subList(1, 2), byRef2);
        final List<Action> byRef3 = tree.getActionsByRef(root.getChild(2).getChild(0));
        Assert.assertEquals(actions.subList(2, actions.size() - 2), byRef3);
    }

    private static Action newAction(
            final ActionType type,
            final Node parent,
            final int refIndex,
            final Node accept) {
        return new Action(type, parent, parent.getChild(refIndex), accept);
    }
}
