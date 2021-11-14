package dev.hawu.plugins.api.collections.tuples;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents an immutable tuple with 4 values.
 *
 * @param <A> The type of the first value.
 * @param <B> The type of the second value.
 * @param <C> The type of the third value.
 * @param <D> The type of the fourth value.
 * @since 1.0
 */
public class Quadruple<A, B, C, D> extends Triplet<A, B, C> {

    private final D fourth;

    /**
     * Quickly construct a quadruple using a call that is generally
     * shorter and cleaner than using the normal constructor.
     *
     * @param first  The first value of the quadruple.
     * @param second The second value of the quadruple.
     * @param third  The third value of the quadruple.
     * @param fourth The fourth value of the quadruple.
     * @param <A>    The type for the first value.
     * @param <B>    The type for the second value.
     * @param <C>    The type for the third value.
     * @param <D>    The type for the fourth value.
     * @return The created quadruple.
     * @since 1.2
     */
    @NotNull
    public static <A, B, C, D> Quadruple<A, B, C, D> of(final @Nullable A first, final @Nullable B second,
                                                        final @Nullable C third, final @Nullable D fourth) {
        return new Quadruple<>(first, second, third, fourth);
    }

    /**
     * Constructs a quintuple with the provided values.
     *
     * @param first  The first value.
     * @param second The second value.
     * @param third  The third value.
     * @param fourth The fourth value.
     * @since 1.0
     */
    public Quadruple(@Nullable final A first, @Nullable final B second, @Nullable final C third, @Nullable final D fourth) {
        super(first, second, third);
        this.fourth = fourth;
    }

    /**
     * Retrieves the fourth value of the tuple.
     *
     * @return The fourth value of the tuple.
     * @since 1.0
     */
    @NotNull
    public final D getFourth() {
        return Objects.requireNonNull(fourth, "Fourth value of quadruple required to be non-null is null.");
    }

    /**
     * Retrieves the fourth value of the tuple and does
     * not throw an error if it is null.
     *
     * @return The fourth value or null.
     * @since 1.2
     */
    @Nullable
    public final D getFourthOrNull() {
        return this.fourth;
    }

    /**
     * Retrieves the fourth component for the tuple.
     * This function only delegates the invocation back to
     * {@link Quadruple#getFourth()}.
     *
     * @return The fourth component of the tuple.
     * @since 1.0
     */
    @NotNull
    public final D component4() {
        return getFourth();
    }

    @Override
    public String toString() {
        return String.format("Quadruple{first=%s,second=%s,third=%s,fourth=%s}",
            getFirstOrNull() == null ? "null" : getFirst().toString(), getSecondOrNull() == null ? "null" : getSecond().toString(),
            getThirdOrNull() == null ? "null" : getThird().toString(), getFourthOrNull() == null ? "null" : getFourth().toString());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getFirstOrNull()).append(getSecondOrNull()).append(getThirdOrNull()).append(fourth).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Quadruple<?, ?, ?, ?> quadruple = (Quadruple<?, ?, ?, ?>) obj;
        return new EqualsBuilder()
            .append(getFirstOrNull(), quadruple.getFirstOrNull())
            .append(getSecondOrNull(), quadruple.getSecondOrNull())
            .append(getThirdOrNull(), quadruple.getThirdOrNull())
            .append(fourth, quadruple.fourth).isEquals();
    }

}
