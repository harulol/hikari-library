package dev.hawu.plugins.api.math.progressions;

/**
 * Represents a progression of integers.
 *
 * @since 1.0
 */
public abstract class Progression {

    protected final long from;
    protected final long step;

    /**
     * Empty constructor to construct a generic progression.
     *
     * @param from The minimum value of this progression.
     * @param step The common difference.
     * @since 1.0
     */
    Progression(final long from, final long step) {
        this.from = from;
        this.step = step;
    }

    /**
     * Gets the term at the index n (1-indexed).
     * <p>
     * The minimum value of this progression is considered
     * the first term ({@code n = 1}).
     *
     * @param n The index to retrieve.
     * @return The term at the provided index.
     * @since 1.0
     */
    public abstract long getTerm(final int n);

    /**
     * Gets the minimum term index that has the value
     * the closest to the provided value, but does not
     * go over it.
     *
     * @param v The value to compute with.
     * @return The index of the term.
     * @since 1.0
     */
    public abstract long getNthTerm(final long v);

}
