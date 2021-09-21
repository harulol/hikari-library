package dev.hawu.plugins.api.inventories;

import dev.hawu.plugins.api.Pair;
import dev.hawu.plugins.api.inventories.item.ItemStackBuilder;
import dev.hawu.plugins.api.inventories.style.StaticStyle;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an effectively doubly linked list of widgets,
 * with buttons to traverse backwards and forwards.
 *
 * @since 2.0
 */
public final class WidgetPaginator {

    private final List<Widget> widgets = new ArrayList<>();
    private final List<Integer> previousSlots = new ArrayList<>();
    private final List<Integer> nextSlots = new ArrayList<>();

    // Items to be used as template for the next and previous buttons.
    // Lore lines with %prev% and %next% will be replaced accordingly.
    private ItemStack next;
    private ItemStack previous;

    /**
     * Constructs a simple widget paginator.
     *
     * @since 2.0
     */
    public WidgetPaginator() {}

    /**
     * Adds a widget to the underlying list.
     *
     * @param widget The widget to add.
     * @since 2.0
     */
    public void append(@NotNull final Widget widget) {
        widgets.add(widget);
    }

    /**
     * Adds a widget to the underlying list at the position provided.
     *
     * @param index  The position to insert.
     * @param widget The widget to add.
     * @since 2.0
     */
    public void append(final int index, @NotNull final Widget widget) {
        widgets.add(index, widget);
    }

    /**
     * Adds all the provided integers to be slots to place
     * buttons that traverse the list backwards.
     *
     * @param indicies The slots to place previous buttons.
     * @since 2.0
     */
    public void addAllPreviousSlots(final int @NotNull ... indicies) {
        Arrays.stream(indicies).forEach(previousSlots::add);
    }

    /**
     * Adds all the provided integers to be slots to place
     * buttons that traverse the list forwards.
     *
     * @param indices The slots to place next buttons.
     * @since 2.0
     */
    public void addAllNextSlots(final int @NotNull ... indices) {
        Arrays.stream(indices).forEach(nextSlots::add);
    }

    /**
     * Sets the template for the next button. The tag {@code %next%} in its lore
     * will be replaced with the next page accordingly in {@link WidgetPaginator#createControls()}.
     *
     * @param item The template item.
     * @since 2.0
     */
    public void setNext(@NotNull final ItemStack item) {
        next = item;
    }

    /**
     * Sets the template for the previous button. The tag {@code %prev%} in its lore
     * will be replaced with the previous page accordingly in {@link WidgetPaginator#createControls()}.
     *
     * @param item The template item.
     * @since 2.0
     */
    public void setPrevious(@NotNull final ItemStack item) {
        previous = item;
    }

    private boolean hasNext(int index) {
        return widgets.size() > 1 && index != widgets.size() - 1;
    }

    private boolean hasPrevious(int index) {
        return widgets.size() > 1 && index != 0;
    }

    /**
     * Runs through the underlying list, and places buttons for players
     * to traverse the pagination.
     * <p>
     * First page will not have buttons to traverse backwards, and last page
     * will not have buttons to traverse forwards.
     *
     * @since 2.0
     */
    public void createControls() {
        for(int i = 0; i < widgets.size(); i++) {
            final Widget widget = get(i);
            if(hasNext(i)) {
                final ItemStack nextButton;
                if(next != null) {
                    nextButton = new ItemStackBuilder(next).withMeta().withLore().loopAndFill(new Pair<>("next", i + 2)).build().build().build();
                } else {
                    nextButton = new ItemStackBuilder().material(Material.ARROW)
                            .withMeta().displayName("&aNext Page")
                            .withLore().add("&7Traverse forwards in the pagination.",
                                    " ", "&6Left Click &eto go to page " + (i + 2) + ".",
                                    "&6Right Click &eto go to last page.")
                            .build().build().build();
                }

                final Button<StaticStyle> button = new Button<>(new StaticStyle(nextButton));
                final int current = i;
                button.setHandler(event -> {
                    event.setCancelled(true);
                    get(current + 1).open(event.getWhoClicked());
                });
                previousSlots.forEach(j -> widget.put(j, button));
            }

            if(hasPrevious(i)) {
                final ItemStack prevButton;
                if(next != null) {
                    prevButton = new ItemStackBuilder(next).withMeta().withLore().loopAndFill(new Pair<>("prev", i)).build().build().build();
                } else {
                    prevButton = new ItemStackBuilder().material(Material.ARROW)
                            .withMeta().displayName("&aPrevious Page")
                            .withLore().add("&7Traverse backwards in the pagination.",
                                    " ", "&6Left Click &eto go to page " + (i) + ".",
                                    "&6Right Click &eto go to first page.")
                            .build().build().build();
                }

                final Button<StaticStyle> button = new Button<>(new StaticStyle(prevButton));
                final int current = i;
                button.setHandler(event -> {
                    event.setCancelled(true);
                    get(current - 1).open(event.getWhoClicked());
                });
                nextSlots.forEach(j -> widget.put(j, button));
            }
        }
    }

    /**
     * Gets the widget at the provided position.
     *
     * @param index The position to retrieve at.
     * @return The widget at the position, or throw an error.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     * @since 2.0
     */
    @NotNull
    public Widget get(final int index) {
        return widgets.get(index);
    }

    /**
     * Opens the pagination to a human entity. This attempts to open
     * the page at the position provided first, and only if this fails
     * will it open the first page to the entity.
     *
     * @param entity The entity to open to.
     * @param page   The page to open.
     * @since 2.0
     */
    public void open(@NotNull final HumanEntity entity, final int page) {
        if(page >= widgets.size()) widgets.get(0).open(entity);
        else widgets.get(page).open(entity);
    }

}
