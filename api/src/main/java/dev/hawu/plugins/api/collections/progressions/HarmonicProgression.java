package dev.hawu.plugins.api.collections.progressions;

/**
 * Represents a sequence of numbers that form into
 * a harmonic slope, representable by y = 1/x.
 * <p>
 * A harmonic progression functions the same as an arithmetic
 * progression.
 *
 * @since 1.5
 */
public final class HarmonicProgression extends ArithmeticProgression {

    /**
     * Constructs a simple harmonic progression, with the starting
     * point and 1 as the step.
     *
     * @param start The starting point.
     * @since 1.5
     */
    public HarmonicProgression(final double start) {
        super(start);
    }

    /**
     * Constructs a harmonic progression with specified starting
     * point and step.
     *
     * @param start The starting point.
     * @param step The step.
     * @since 1.5
     */
    public HarmonicProgression(final double start, final double step) {
        super(start, step);
    }

    /**
     * Gets the term at the index.
     *
     * @param n The index to retrieve.
     * @return The term at the index.
     * @since 1.5
     */
    @Override
    public double getTerm(final int n) {
        return 1 / super.getTerm(n);
    }

    /**
     * Attempts to find the index that points to a term of the progression
     * that is the closet to the value, without going below.
     *
     * @param v The value to compute with.
     * @return The index of the term that is closest to the value.
     * @since 1.5
     */
    @Override
    public int getNthTerm(final double v) {
        return super.getNthTerm(1 / v);
    }

}
