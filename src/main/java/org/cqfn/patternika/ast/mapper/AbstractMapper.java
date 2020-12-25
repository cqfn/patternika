package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.ast.NodeExt;
import org.cqfn.patternika.ast.iterator.Bfs;

import java.util.List;
import java.util.Objects;

/**
 * Creates a mapping for node trees.
 * Connections are built using an algorithm based on BFS KMP logic O(N^2).
 *
 * NOTE: Draft to be further reviewed and refactored.
 *
 * @since 2019/10/31
 */
public abstract class AbstractMapper implements Mapper<NodeExt> {
    /** First node tree. */
    private final NodeExt treeRoot1;
    /** Second node tree. */
    private final NodeExt treeRoot2;
    /** Mappings to be created. */
    private final Mapping<NodeExt> mapping;

    /**
     * Constructor.
     *
     * @param treeRoot1 first node tree to be mapped.
     * @param treeRoot2 second node tree to be mapped.
     */
    public AbstractMapper(final NodeExt treeRoot1, final NodeExt treeRoot2) {
        this.treeRoot1 = Objects.requireNonNull(treeRoot1);
        this.treeRoot2 = Objects.requireNonNull(treeRoot2);
        this.mapping = new HashMapping<>();
    }

    /**
     * Builds a mapping.
     *
     * @return container with mappings between the two node trees.
     */
    @Override
    public Mapping<NodeExt> buildMapping() {
        final Downstairs downstairsMapper = new Downstairs(mapping);
        // let's try to build some connections fast first.
        if (treeRoot1.getType().equals(treeRoot2.getType())) {
            mapping.connect(treeRoot1, treeRoot2);
            downstairsMapper.connect(treeRoot1);
        }
        // Then let's use some inherited class's logic to extend our connections.
        buildMapping(treeRoot1, treeRoot2);
        // let's add some additional connections where possible.
        final List<NodeExt> bfsNodes = new Bfs<>(treeRoot1).toList();
        for (final NodeExt node : bfsNodes) {
            if (!mapping.contains(node)) {
                downstairsMapper.connect(node.getParent());
            }
        }
        // And remove those connections
        // that will cause a line of updates instead of one deletion and insertion.
        new WeakChain(mapping).disconnect(bfsNodes);
        return mapping;
    }

    /**
     * Inherited class should provide implementation of building mapping.
     *
     * @param root1 root of the first given tree.
     * @param root2 root of the second given tree.
     */
    protected abstract void buildMapping(NodeExt root1, NodeExt root2);

}
