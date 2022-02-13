package dev.hawu.plugins.api.collections.progressions;

/**
 * Represents a sequence of non-zero numbers where each term
 * after the first is found by multiplying the previous one
 * by a fixed, non-zero number called the common ratio.
 *
 * @since 1.0
 */
public final class GeometricProgression extends Progression {

    /**
     * Empty constructor to construct a geometric progression
     * with provided first term and common ratio.
     *
     * @param from The first term of the progression.
     * @param step The common ratio.
     * @since 1.0
     */
    public GeometricProgression(final double from, final double step) {
        super(from, step);
        if(from == 0 || step == 0) throw new IllegalArgumentException("Neither the first term nor the step could be 0.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * For example, for a geometric progression starting from 1,
     * raising up by 5 (which is the step), we have:
     * {@code 1, 5, 25, 125, 625, ...}
     * <p>
     * The first term (n = 1) will be 1.
     * The second term (n = 2) will be 5.
     *
     * @since 1.0
     */
    @Override
    public double getTerm(final int n) {
        return from * Math.pow(step, n - 1);
    }

    private double log(final double base, final double value) {
        return Math.log(value) / Math.log(base);
    }

    /**
     * {@inheritDoc}
     * <p>
     * For example, for a geometric progression starting from 1,
     * raising up by 5 (which is the step), we have:
     * {@code 1, 5, 25, 125, 625, ...}
     * <p>
     * By passing in, for example, a value of {@code 126}, which is greater
     * than the 4th term, but less than the 5th term.
     * <p>
     * This method should return {@code 4} per specification.
     *
     * @since 1.0
     */
    @Override
    public int getNthTerm(final double v) {
        return (int) Math.floor(log(step, v / from)) + 1;
    }

}
