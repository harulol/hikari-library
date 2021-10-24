package dev.hawu.plugins.api.collections.tuples;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.Nullable;

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
public class Quintuple<A, B, C, D, E> extends Quadruple<A, B, C, D> {

    private final E fifth;

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
     * @return The fifth value of the tuple, nullable.
     * @since 1.0
     */
    @Nullable
    public final E getFifth() {
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
    @Nullable
    public final E component5() {
        return getFifth();
    }

    @Override
    public String toString() {
        return String.format("Quintuple{first=%s,second=%s,third=%s,fourth=%s,fifth=%s}",
            getFirst() == null ? "null" : getFirst().toString(), getSecond() == null ? "null" : getSecond().toString(),
            getThird() == null ? "null" : getThird().toString(), getFourth() == null ? "null" : getFourth().toString(),
            getFifth() == null ? "null" : getFifth().toString());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getFirst()).append(getSecond()).append(getThird()).append(getFourth()).append(fifth).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Quintuple<?, ?, ?, ?, ?> quintuple = (Quintuple<?, ?, ?, ?, ?>) obj;
        return new EqualsBuilder().append(getFirst(), quintuple.getFirst()).append(getSecond(), quintuple.getSecond())
            .append(getThird(), quintuple.getThird()).append(getFourth(), quintuple.getFourth())
            .append(getFifth(), quintuple.getFifth()).isEquals();
    }

}
