package org.cqfn.patternika.ast.visitor;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.TestNode;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for the node visitor infrastructure.
 *
 * @since 2020/12/18
 */
public class NodeVisitorTest {
    /** Node visiting log. **/
    private final List<String> log = new ArrayList<>();

    /**
     * Visitor action for log.
     */
    private enum Act {
        /** Enter action. */
        ENTER('E'),
        /** Leave action. */
        LEAVE('L'),
        /** Visit action. */
        VISIT('V');
        /** Character that identifies the action. */
        private final char actId;

        Act(final char actId) {
            this.actId = actId;
        }

        @Override
        public String toString() {
            return Character.toString(actId);
        }
    }

    /**
     * Adds a record for the log.
     *
     * @param visitorClass visitor class.
     * @param action action.
     * @param node node.
     */
    private void addToLog(
            final Class<?> visitorClass,
            final Act action,
            final Node node) {
        log.add(
            String.format(
                "%s-%s-%s{%s,%s}",
                visitorClass.getSimpleName(),
                action,
                node.getClass().getSimpleName(),
                node.getType(),
                node.getData()
            )
        );
    }

    /**
     * Tests that a node tree containing nodes of different types
     * (can extend each other) is correctly processed with visitors.
     * This means that the log contains correct records.
     */
    @Test
    public void test() {
        final Node root =
            new NodeA("A", 0,
                new NodeB("B", 0),
                new NodeC("C", 0,
                    new NodeA("A", 1),
                    new TestNode("T", 0)
                ),
                new TestNode("T", 1,
                    new NodeB("B", 1),
                    new NodeD("D", 0,
                        new NodeA("A", 2),
                        new NodeC("C", 1)
                    )
                )
            );
        final List<String> expectedLog = Arrays.asList(
            "NodeAVisitor-E-NodeA{A,0}",
                "NodeBVisitor-E-NodeB{B,0}",
                "NodeBVisitor-L-NodeB{B,0}",
                "NodeCVisitor-V-NodeC{C,0}",
                    "NodeBVisitor-E-NodeB{B,1}",
                    "NodeBVisitor-L-NodeB{B,1}",
                    "NodeBVisitor-E-NodeD{D,0}",
                        "NodeAVisitor-E-NodeA{A,2}",
                        "NodeAVisitor-L-NodeA{A,2}",
                        "NodeCVisitor-V-NodeC{C,1}",
                    "NodeBVisitor-L-NodeD{D,0}",
            "NodeAVisitor-L-NodeA{A,0}"
        );
        final NodeVisitor visitor = new NodeVisitorComposite()
                .register(NodeA.class, new NodeAVisitor())
                .register(NodeB.class, new NodeBVisitor())
                .register(NodeC.class, new NodeCVisitor());
        final NodeTraversal traversal = new NodeTraversal(visitor);
        traversal.process(root);
        Assert.assertEquals(expectedLog, log);
    }

    /**
     * Custom test node.
     */
    private static class NodeA extends TestNode {
        NodeA(final String type, final int data, final Node... children) {
            super(type, data, children);
        }
    }

    /**
     * Custom test node.
     */
    private static class NodeB extends TestNode {
        NodeB(final String type, final int data, final Node... children) {
            super(type, data, children);
        }
    }

    /**
     * Custom test node.
     */
    private static class NodeC extends NodeA {
        NodeC(final String type, final int data, final Node... children) {
            super(type, data, children);
        }
    }

    /**
     * Custom test node.
     */
    private static class NodeD extends NodeB {
        NodeD(final String type, final int data, final Node... children) {
            super(type, data, children);
        }
    }

    /**
     * Visitor for {@link NodeA}.
     */
    private class NodeAVisitor extends NodeVisitorTyped<NodeA> {
        @Override
        public boolean enterNode(final NodeA node) {
            addToLog(getClass(), Act.ENTER, node);
            return true;
        }

        @Override
        public void leaveNode(final NodeA node) {
            addToLog(getClass(), Act.LEAVE, node);
        }
    }

    /**
     * Visitor for {@link NodeB}.
     */
    private class NodeBVisitor extends NodeVisitorTyped<NodeB> {
        @Override
        public boolean enterNode(final NodeB node) {
            addToLog(getClass(), Act.ENTER, node);
            return true;
        }

        @Override
        public void leaveNode(final NodeB node) {
            addToLog(getClass(), Act.LEAVE, node);
        }
    }

    /**
     * Visitor for {@link NodeC}.
     */
    private class NodeCVisitor extends NodeVisitorTypedLeaf<NodeC> {
        @Override
        public void visitNode(final NodeC node) {
            addToLog(getClass(), Act.VISIT, node);
        }
    }

}
