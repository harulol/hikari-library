package dev.hawu.plugins.api.collections.tuples;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents an immutable tuple with 3 values.
 *
 * @param <A> The type of the first value.
 * @param <B> The type of the second value.
 * @param <C> The type of the third value.
 * @since 1.0
 */
public class Triplet<A, B, C> extends Pair<A, B> {

    private final C third;

    /**
     * Quickly construct a triplet using a call that is generally
     * shorter and cleaner than using the normal constructor.
     *
     * @param first  The first value of the triplet.
     * @param second The second value of the triplet.
     * @param third  The third value of the triplet.
     * @param <A>    The type for the first value.
     * @param <B>    The type for the second value.
     * @param <C>    The type for the third value.
     * @return The created triplet.
     * @since 1.2
     */
    @NotNull
    public static <A, B, C> Triplet<A, B, C> of(final @Nullable A first, final @Nullable B second, final @Nullable C third) {
        return new Triplet<>(first, second, third);
    }

    /**
     * Constructs a triplet with the provided values.
     *
     * @param first  The first value.
     * @param second The second value.
     * @param third  The third value.
     * @since 1.0
     */
    public Triplet(@Nullable final A first, @Nullable final B second, @Nullable final C third) {
        super(first, second);
        this.third = third;
    }

    /**
     * Retrieves the third value of the tuple.
     *
     * @return The third value of the tuple.
     * @since 1.0
     */
    @NotNull
    public final C getThird() {
        return Objects.requireNonNull(third, "Third value required to be non-null is null.");
    }

    /**
     * Retrieves the third value of the tuple and does
     * not throw an error if it is null.
     *
     * @return The third value or null.
     * @since 1.2
     */
    @Nullable
    public final C getThirdOrNull() {
        return this.third;
    }

    /**
     * Retrieves the third component for the tuple.
     * This function only delegates the invocation back to
     * {@link Triplet#getThird()}.
     *
     * @return The third component of the tuple.
     * @since 1.0
     */
    @NotNull
    public final C component3() {
        return getThird();
    }

    @Override
    public String toString() {
        return String.format("Triplet{first=%s,second=%s,third=%s}",
            getFirstOrNull() == null ? "null" : getFirst().toString(), getSecondOrNull() == null ? "null" : getSecond().toString(),
            getThirdOrNull() == null ? "null" : getThird().toString());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getFirstOrNull()).append(getSecondOrNull()).append(third).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) obj;
        return new EqualsBuilder()
            .append(getFirstOrNull(), triplet.getFirstOrNull())
            .append(getSecondOrNull(), triplet.getSecondOrNull())
            .append(third, triplet.third)
            .isEquals();
    }

}
