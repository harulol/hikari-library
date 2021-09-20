package dev.hawu.plugins.api.inventories.style;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CyclableStyleTest {

    @Test
    @DisplayName("Cycling Test")
    public void testCycle() {
        final ItemStack air = new ItemStack(Material.AIR);
        final CyclableStyle style = new CyclableStyle(air, air, air, air, air);
        style.cycleForwards(2);
        style.cycleForwards(11);
        Assertions.assertEquals(3, style.getPointer());

        style.cycleBackwards(14);
        Assertions.assertEquals(4, style.getPointer());
    }

}
