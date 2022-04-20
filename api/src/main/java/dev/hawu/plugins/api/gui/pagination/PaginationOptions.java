package dev.hawu.plugins.api.gui.pagination;

import dev.hawu.plugins.api.gui.GuiModel;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Internal
final class PaginationOptions<T> {

    private final ItemStack backTemplate;
    private final Consumer<InventoryClickEvent> backAction;
    private final Set<Integer> backSlots;

    private final BiPredicate<T, String> predicate;
    private final Consumer<GuiModel> lastTouches;

    public PaginationOptions(final @Nullable ItemStack backTemplate, final @Nullable Consumer<@NotNull InventoryClickEvent> backAction,
                             final @NotNull Set<@NotNull Integer> backSlots, final @Nullable BiPredicate<@NotNull T, @NotNull String> predicate,
                             final @Nullable Consumer<@NotNull GuiModel> lastTouches) {
        this.backTemplate = backTemplate;
        this.backAction = backAction;
        this.backSlots = backSlots;
        this.predicate = predicate;
        this.lastTouches = lastTouches;
    }

    @NotNull
    public Collection<? extends @NotNull T> getFilteredItems(final @NotNull Collection<? extends @NotNull T> items, final @Nullable String filter) {
        if(predicate == null || filter == null) return items;
        return items.stream()
            .filter(item -> predicate.test(item, filter))
            .collect(Collectors.toList());
    }

    @Nullable
    public ItemStack getBackTemplate() {
        return backTemplate;
    }

    @Nullable
    public Consumer<@NotNull InventoryClickEvent> getBackAction() {
        return backAction;
    }

    @NotNull
    public Set<@NotNull Integer> getBackSlots() {
        return backSlots;
    }

    @Nullable
    public BiPredicate<@NotNull T, @NotNull String> getPredicate() {
        return predicate;
    }

    @Nullable
    public Consumer<@NotNull GuiModel> getLastTouches() {
        return lastTouches;
    }

}
