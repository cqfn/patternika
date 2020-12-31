package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.ast.NodeExt;
import org.cqfn.patternika.ast.hash.Hash;
import org.cqfn.patternika.ast.hash.SimilarityHash;
import org.cqfn.patternika.ast.iterator.Bfs;

import java.util.List;
import java.util.Objects;

/**
 * Mapper for Node objects that builds connections based on Hash logic O(N^2).
 *
 * @since 2019/12/29
c*/
public class HashMapper implements Mapper<NodeExt> {
    /** First node tree root. */
    private final NodeExt root1;
    /** Second node tree root. */
    private final NodeExt root2;
    /** Calculates a similarity hash for nodes. */
    private final Hash similarity;
    /** Mapping to be built. */
    private final Mapping<NodeExt> mapping;

    /**
     * Constructor.
     *
     * @param root1 first node tree to be mapped.
     * @param root2 second node tree to be mapped.
     */
    public HashMapper(final NodeExt root1, final NodeExt root2) {
        this.root1 = Objects.requireNonNull(root1);
        this.root2 = Objects.requireNonNull(root2);
        this.similarity = new SimilarityHash();
        this.mapping = new HashMapping<>();
    }

    /**
     * Builds a mapping.
     *
     * @return container with mappings between the two node trees.
     */
    @Override
    public Mapping<NodeExt> buildMapping() {
        final Downstairs downstairs = new Downstairs(mapping, similarity);
        downstairs.connect(root1, root2);
        // ToDo
        final List<NodeExt> bfsNodes = new Bfs<>(root1).toList();
        downstairs.connectAll(bfsNodes);
        final WeakChain weakChain = new WeakChain(mapping);
        weakChain.disconnect(bfsNodes);
        return mapping;
    }

}
