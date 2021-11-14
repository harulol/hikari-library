package dev.hawu.plugins.api.collections.tuples;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents an immutable tuple with 5 values.
 *
 * @param <A> The type of the first value.
 * @param <B> The type of the second value.
 * @param <C> The type of the third value.
 * @param <D> The type of the fourth value.
 * @param <E> The type of the fifth value.
 * @since 1.0
 */
public final class Quintuple<A, B, C, D, E> extends Quadruple<A, B, C, D> {

    private final E fifth;

    /**
     * Quickly construct a quintuple using a call that is generally
     * shorter and cleaner than using the normal constructor.
     *
     * @param first  The first value of the quintuple.
     * @param second The second value of the quintuple.
     * @param third  The third value of the quintuple.
     * @param fourth The fourth value of the quintuple.
     * @param fifth  The fifth value of the quintuple.
     * @param <A>    The type for the first value.
     * @param <B>    The type for the second value.
     * @param <C>    The type for the third value.
     * @param <D>    The type for the fourth value.
     * @param <E>    The type for the fifth value.
     * @return The created quintuple.
     * @since 1.2
     */
    @NotNull
    public static <A, B, C, D, E> Quintuple<A, B, C, D, E> of(final @Nullable A first, final @Nullable B second, final @Nullable C third,
                                                              final @Nullable D fourth, final @Nullable E fifth) {
        return new Quintuple<>(first, second, third, fourth, fifth);
    }

    /**
     * Constructs a quintuple with the provided values.
     *
     * @param first  The first value.
     * @param second The second value.
     * @param third  The third value.
     * @param fourth The fourth value.
     * @param fifth  The fifth value.
     * @since 1.0
     */
    public Quintuple(@Nullable final A first, @Nullable final B second, @Nullable final C third, @Nullable final D fourth, @Nullable final E fifth) {
        super(first, second, third, fourth);
        this.fifth = fifth;
    }

    /**
     * Retrieves the fifth value of the tuple.
     *
     * @return The fifth value of the tuple.
     * @since 1.0
     */
    @NotNull
    public E getFifth() {
        return Objects.requireNonNull(fifth, "Fifth value of quintuple required to be non-null, is null.");
    }

    /**
     * Retrieves the fifth value of the tuple and does
     * not throw an error if it is null.
     *
     * @return The fifth value or null.
     * @since 1.2
     */
    @Nullable
    public E getFifthOrNull() {
        return this.fifth;
    }

    /**
     * Retrieves the fifth component for the tuple.
     * This function only delegates the invocation back to
     * {@link Quintuple#getFifth()}.
     *
     * @return The fifth component of the tuple.
     * @since 1.0
     */
    @NotNull
    public E component5() {
        return getFifth();
    }

    @Override
    public String toString() {
        return String.format("Quintuple{first=%s,second=%s,third=%s,fourth=%s,fifth=%s}",
            getFirstOrNull() == null ? "null" : getFirst().toString(), getSecondOrNull() == null ? "null" : getSecond().toString(),
            getThirdOrNull() == null ? "null" : getThird().toString(), getFourthOrNull() == null ? "null" : getFourth().toString(),
            getFifthOrNull() == null ? "null" : getFifth().toString());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getFirstOrNull()).append(getSecondOrNull())
            .append(getThirdOrNull()).append(getFourthOrNull()).append(fifth).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Quintuple<?, ?, ?, ?, ?> quintuple = (Quintuple<?, ?, ?, ?, ?>) obj;
        return new EqualsBuilder()
            .append(getFirstOrNull(), quintuple.getFirstOrNull())
            .append(getSecondOrNull(), quintuple.getSecondOrNull())
            .append(getThirdOrNull(), quintuple.getThirdOrNull())
            .append(getFourthOrNull(), quintuple.getFourthOrNull())
            .append(getFifthOrNull(), quintuple.getFifthOrNull()).isEquals();
    }

}
