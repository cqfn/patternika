package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.ast.NodeExt;
import org.cqfn.patternika.ast.hash.SimilarityHash;
import org.cqfn.patternika.ast.iterator.Bfs;

import java.util.List;
import java.util.Objects;

/**
 * Mapper for Node objects that builds connections based on Greed logic O(N^2).
 *
 * @since 2019/10/31
c*/
public class GreedMapper implements Mapper<NodeExt> {
    /** First node tree root. */
    private final NodeExt root1;
    /** Second node tree root. */
    private final NodeExt root2;

    /**
     * Constructor.
     *
     * @param root1 first node tree to be mapped.
     * @param root2 second node tree to be mapped.
     */
    public GreedMapper(final NodeExt root1, final NodeExt root2) {
        this.root1 = Objects.requireNonNull(root1);
        this.root2 = Objects.requireNonNull(root2);
    }

    /**
     * Builds a mapping.
     *
     * @return container with mappings between the two node trees.
     */
    @Override
    public Mapping<NodeExt> buildMapping() {
        final Mapping<NodeExt> mapping = new HashMapping<>();
        final Downstairs downstairs = new Downstairs(mapping, new SimilarityHash());
        final WeakChain weakChain = new WeakChain(mapping);
        // Builds connection starting from root (a fast way).
        downstairs.connect(root1, root2);
        // Adds additional connections where possible.
        final List<NodeExt> bfsNodes = new Bfs<>(root1).toList();
        downstairs.connectAll(bfsNodes);
        // Remove weak chain connections.
        weakChain.disconnect(bfsNodes);
        return mapping;
    }

}
