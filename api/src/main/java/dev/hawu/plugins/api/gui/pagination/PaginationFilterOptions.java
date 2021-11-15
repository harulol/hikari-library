package dev.hawu.plugins.api.gui.pagination;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;

@Internal
final class PaginationFilterOptions {

    private final ItemStack filterTemplate;
    private final Set<Integer> filterSlots;
    private final Consumer<InventoryClickEvent> filterEvent;

    public PaginationFilterOptions(final @Nullable ItemStack filterTemplate, final @NotNull Set<@NotNull Integer> filterSlots,
                                   final @Nullable Consumer<@NotNull InventoryClickEvent> filterEvent) {
        this.filterTemplate = filterTemplate;
        this.filterSlots = filterSlots;
        this.filterEvent = filterEvent;
    }

    @Nullable
    public ItemStack getFilterTemplate() {
        return filterTemplate;
    }

    @NotNull
    public Set<@NotNull Integer> getFilterSlots() {
        return filterSlots;
    }

    @Nullable
    public Consumer<@NotNull InventoryClickEvent> getFilterEvent() {
        return filterEvent;
    }

}
