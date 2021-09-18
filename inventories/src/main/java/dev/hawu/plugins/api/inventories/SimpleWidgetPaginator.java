package dev.hawu.plugins.api.inventories;

import dev.hawu.plugins.api.Pair;
import dev.hawu.plugins.api.inventories.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a common paginator that have multiple widgets
 * linked. All panes can be traversed.
 *
 * @since 1.0
 */
public final class SimpleWidgetPaginator {

    private final List<Widget> widgets;

    private ItemStack previous;
    private ItemStack next;

    /**
     * Constructs an empty simple widget paginator
     * for linking multiple widgets together.
     *
     * @since 1.0
     */
    public SimpleWidgetPaginator() {
        this.widgets = new ArrayList<>();
        this.previous = new ItemStackBuilder().material(Material.ARROW).withMeta()
                .displayName("&aPrevious Page")
                .withLore()
                .add("&7Traverse backwards a number of pages", "", "&6Left click &eto go to page &a%prev%&e.", "&6Right click &eto go to the first page.")
                .build().build().build();
        this.next = new ItemStackBuilder().material(Material.ARROW).withMeta()
                .displayName("&aNext Page")
                .withLore()
                .add("&7Traverse forwards a number of pages", "", "&6Left click &eto go to page &a%next%&e.", "&6Right click &eto go to the first page.")
                .build().build().build();
    }

    /**
     * Appends a widget to the list.
     *
     * @param widget The widget to append.
     * @since 1.0
     */
    public void append(final @NotNull Widget widget) {
        widgets.add(widget);
    }

    /**
     * Inserts a widget to the list at a position.
     *
     * @param index  The index to insert at.
     * @param widget The widget to insert.
     * @since 1.0
     */
    public void append(final int index, final @NotNull Widget widget) {
        widgets.add(index, widget);
    }

    /**
     * Retrieves the widget at a position.
     *
     * @param index The index to retrieve at.
     * @return The widget at the said position, should never be null.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     * @since 1.0
     */
    @NotNull
    public Widget get(final int index) {
        return widgets.get(index);
    }

    public void setPrevious(@NotNull final ItemStack previous) {
        this.previous = previous;
    }

    public void setNext(@NotNull final ItemStack next) {
        this.next = next;
    }

    /**
     * Sets the widget at the position index to another.
     *
     * @param index  The index to set.
     * @param widget The widget to set with.
     * @since 1.0
     */
    public void set(final int index, final @NotNull Widget widget) {
        widgets.set(index, widget);
    }

    private boolean hasPrevious(final int index) {
        return widgets.size() > 1 && index > 0;
    }

    private boolean hasNext(final int index) {
        return widgets.size() > 1 && index != widgets.size() - 1;
    }

    /**
     * Traverses all pages iteratively and adds the needed control
     * buttons for traversing as players.
     *
     * @param previousSlots The slots to put buttons that traverse backwards.
     * @param nextSlots     The slots to put buttons that traverse forwards.
     * @since 1.0
     */
    public void createControls(final int[] previousSlots, final int[] nextSlots) {
        for(int i = 0; i < widgets.size(); i++) {
            final Widget widget = get(i);
            if(hasPrevious(i)) {
                for(final int j : previousSlots)
                    widget.createButton(j, previous).withBuilder().withMeta().withLore().loopAndFill(new Pair<>("prev", i));
            }

            if(hasNext(i)) {
                for(final int j : nextSlots)
                    widget.createButton(j, next).withBuilder().withMeta().withLore().loopAndFill(new Pair<>("next", i + 2));
            }
        }
    }

    /**
     * Attempts to open the widget at position index. If the index is
     * out of bounds, attempt to open the first page to a human entity.
     *
     * @param entity The entity to open to.
     * @param page   The page to open.
     * @since 1.0
     */
    public void open(final @NotNull HumanEntity entity, final int page) {
        if(page < widgets.size()) get(page).open(entity);
        else get(0).open(entity);
    }

}
