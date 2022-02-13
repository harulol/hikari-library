package dev.hawu.plugins.api.collections.progressions;

/**
 * Represents a sequence of numbers such that the difference
 * between the consecutive terms is constant.
 *
 * @since 1.0
 */
public class ArithmeticProgression extends Progression {

    /**
     * Constructs a default arithmetic progression
     * with the 1st term provided and a step of 1.
     *
     * @param from The first term of the progression.
     * @since 1.0
     */
    public ArithmeticProgression(final double from) {
        this(from, 1);
    }

    /**
     * Constructs an arithmetic progression with the
     * 1st term and the step provided.
     *
     * @param from The first term of the progression.
     * @param step The step of this progression.
     * @since 1.0
     */
    public ArithmeticProgression(final double from, final double step) {
        super(from, step);
    }

    /**
     * {@inheritDoc}
     * <p>
     * For example, for an arithmetic progression starting from 1,
     * going up by 5 (which is the step), we have:
     * {@code 1, 6, 11, 16, 21, ...}
     * <p>
     * The first term (n = 1) will be 1.
     * The second term (n = 2) will be 6.
     *
     * @since 1.0
     */
    @Override
    public double getTerm(final int n) {
        if(n < 1) throw new IllegalArgumentException("The numbering for terms starts from 1 for arithmetic progressions.");
        return from + step * (n - 1); // a(n) = a(1) + d * a(n-1)
    }

    /**
     * {@inheritDoc}
     * <p>
     * For example, for an arithmetic progression starting from 1,
     * going up by 5 (which is the step), we have:
     * {@code 1, 6, 11, 16, 21, ...}
     * <p>
     * By passing in, for example, a value of {@code 12}, which is greater
     * than the 3rd term, but less than the 4th term.
     * <p>
     * This method should return {@code 3} per specification.
     *
     * @since 1.0
     */
    @Override
    public int getNthTerm(final double v) {
        return (int) Math.floor((v - from) / step) + 1;
    }

}
