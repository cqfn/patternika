package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.ast.hash.Hash;
import org.cqfn.patternika.ast.hash.IsomorphismHash;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Class for building and storing isomorphic groups of node trees.
 * <p>
 * Grouping is based on a {@link Hash} implementation and
 * has complexity O(N), where N is the number of nodes in the trees to be grouped.
 */
public class Isomorphism {
    /** Calculates hashes (indices) used to identify isomorphic groups. */
    private final Hash hash;
    /** Isomorphic groups identified by indices. */
    private final Map<Integer, Set<Node>> groups;

    /**
     * Default constructor.
     */
    public Isomorphism() {
        this(new IsomorphismHash(), new HashMap<>());
    }

    /**
     * Main constructor. Provided as public to enable dependency injection.
     * Unless there is a need to customize the class, use the default constructor.
     *
     * @param hash Calculator of hash codes.
     * @param groups Container for holding groups.
     */
    public Isomorphism(final Hash hash, final Map<Integer, Set<Node>> groups) {
        this.hash = Objects.requireNonNull(hash);
        this.groups = Objects.requireNonNull(groups);
    }

    /**
     * Add node to one of the isomorphic groups or create new group if needed.
     *
     * @param node root of the tree.
     * @return     index of the corresponding group.
     */
    public int add(final Node node) {
        final int index = hash.getHash(node);
        final Set<Node> group = groups.computeIfAbsent(index, x -> new HashSet<>());
        group.add(node);
        return index;
    }

    /**
     * Get isomorphic group for the given tree.
     *
     * @param node root of the tree.
     * @return set of isomorphic trees.
     */
    public Set<Node> getGroup(final Node node) {
        return getGroup(add(node));
    }

    /**
     * Get isomorphic group by the given index.
     *
     * @param index index of the group.
     * @return set of isomorphic trees or {@code null} if there is no group with such index.
     */
    public Set<Node> getGroup(final int index) {
        return groups.get(index);
    }

}
