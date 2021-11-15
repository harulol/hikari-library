package dev.hawu.plugins.api.gui.pagination;

import dev.hawu.plugins.api.gui.GuiElement;
import dev.hawu.plugins.api.gui.GuiModel;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Internal
final class PaginationData<T> {

    private final Collection<T> collection;
    private final Supplier<GuiModel> modelSupplier;
    private final List<Integer> allowedSlots;
    private final BiFunction<T, Integer, GuiElement<?>> itemGenerator;

    public PaginationData(final @NotNull Collection<@NotNull T> collection, final @NotNull Supplier<@NotNull GuiModel> modelSupplier,
                          final @NotNull List<@NotNull Integer> allowedSlots, final @NotNull BiFunction<@NotNull T, @NotNull Integer, @Nullable GuiElement<?>> itemGenerator) {
        this.collection = collection;
        this.modelSupplier = modelSupplier;
        this.allowedSlots = allowedSlots;
        this.itemGenerator = itemGenerator;
    }

    @NotNull
    public Collection<@NotNull T> getCollection() {
        return collection;
    }

    @NotNull
    public Supplier<@NotNull GuiModel> getModelSupplier() {
        return modelSupplier;
    }

    @NotNull
    public List<@NotNull Integer> getAllowedSlots() {
        return allowedSlots;
    }

    @NotNull
    public BiFunction<@NotNull T, @NotNull Integer, @Nullable GuiElement<?>> getItemGenerator() {
        return itemGenerator;
    }

}
