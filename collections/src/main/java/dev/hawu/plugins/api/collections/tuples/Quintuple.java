package dev.hawu.plugins.api.collections.tuples;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an immutable tuple with 4 values.
 *
 * @param <A> The type of the first value.
 * @param <B> The type of the second value.
 * @param <C> The type of the third value.
 * @param <D> The type of the fourth value.
 * @since 1.0
 */
public class Quintuple<A, B, C, D> extends Triplet<A, B, C> {

    private final @Nullable D fourth;

    /**
     * Constructs a quintuple with the provided values.
     *
     * @param first  The first value.
     * @param second The second value.
     * @param third  The third value.
     * @param fourth The fourth value.
     * @since 1.0
     */
    public Quintuple(@Nullable final A first, @Nullable final B second, @Nullable final C third, @Nullable final D fourth) {
        super(first, second, third);
        this.fourth = fourth;
    }

    /**
     * Retrieves the fourth value of the tuple.
     *
     * @return The fourth value of the tuple, nullable.
     * @since 1.0
     */
    @Nullable
    public final D getFourth() {
        return this.fourth;
    }

    /**
     * Retrieves the fourth component for the tuple.
     * This function only delegates the invocation back to
     * {@link Quintuple#getFourth()}.
     *
     * @return The fourth component of the tuple.
     * @since 1.1
     */
    @Nullable
    public final D component4() {
        return getFourth();
    }

    @Override
    public String toString() {
        return String.format("Quintuple{first=%s,second=%s,third=%s,fourth=%s}",
            getFirst() == null ? "null" : getFirst().toString(), getSecond() == null ? "null" : getSecond().toString(),
            getThird() == null ? "null" : getThird().toString(), getFourth() == null ? "null" : getFourth().toString());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getFirst()).append(getSecond()).append(getThird()).append(fourth).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Quintuple<?, ?, ?, ?> quintuple = (Quintuple<?, ?, ?, ?>) obj;
        return new EqualsBuilder().append(getFirst(), quintuple.getFirst()).append(getSecond(), quintuple.getSecond())
                .append(getThird(), quintuple.getThird()).append(fourth, quintuple.fourth).isEquals();
    }

}
