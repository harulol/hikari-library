package dev.hawu.plugins.api.gui.pagination;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Internal
final class PaginationControlOptions {

    private final ItemStack previousButtonTemplate;
    private final Set<Integer> previousButtonSlots;
    private final ItemStack nextButtonTemplate;
    private final Set<Integer> nextButtonSlots;

    public PaginationControlOptions(final @NotNull ItemStack previousButtonTemplate, final @NotNull Set<@NotNull Integer> previousButtonSlots,
                                    final @NotNull ItemStack nextButtonTemplate, final @NotNull Set<@NotNull Integer> nextButtonSlots) {
        this.previousButtonTemplate = previousButtonTemplate;
        this.previousButtonSlots = previousButtonSlots;
        this.nextButtonTemplate = nextButtonTemplate;
        this.nextButtonSlots = nextButtonSlots;
    }

    @NotNull
    public ItemStack getPreviousButtonTemplate() {
        return previousButtonTemplate;
    }

    @NotNull
    public Set<@NotNull Integer> getPreviousButtonSlots() {
        return previousButtonSlots;
    }

    @NotNull
    public ItemStack getNextButtonTemplate() {
        return nextButtonTemplate;
    }

    @NotNull
    public Set<@NotNull Integer> getNextButtonSlots() {
        return nextButtonSlots;
    }

}
