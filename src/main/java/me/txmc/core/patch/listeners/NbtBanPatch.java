package me.txmc.core.patch.listeners;

import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.nio.charset.StandardCharsets;

import static me.txmc.core.util.GlobalUtils.sendPrefixedLocalizedMessage;
import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * Listener for handling the NBT (Named Binary Tag) data of shulker boxes in the player's inventory.
 *
 * <p>This class is part of the 8b8tCore plugin and is responsible for ensuring that shulker boxes
 * containing excessive amounts of data are cleared from the player's inventory upon join.</p>
 *
 * <p>Functionality includes:</p>
 * <ul>
 *     <li>Detecting shulker boxes in the player's inventory upon join</li>
 *     <li>Checking if the shulker box contains written books</li>
 *     <li>Calculating the size of the shulker box's metadata</li>
 *     <li>Clearing the contents of shulker boxes that exceed a specific size threshold</li>
 * </ul>
 *
 * @author Minelord9000 (agarciacorte)
 * @since 2024/08/03 14:49
 */
public class NbtBanPatch implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Iterate through the player's inventory
        for (ItemStack item : player.getInventory().getContents()) {

            // Check if the item is a shulker
            if (item != null && item.getType().toString().endsWith("SHULKER_BOX")) {

                BlockStateMeta blockStateMeta = (BlockStateMeta) item.getItemMeta();

                if (blockStateMeta != null && blockStateMeta.getBlockState() instanceof ShulkerBox) {
                    ShulkerBox shulkerBox = (ShulkerBox) blockStateMeta.getBlockState();
                    Inventory shulkerInventory = shulkerBox.getInventory();


                    // The shulker's metadata is converted to a string, and then the size of the string in bytes is calculated
                    int shulkerSize = calculateStringSizeInBytes(item.getItemMeta().toString());

                    // Check if the shulker exceeds a specific number of bytes, if so, its contents are removed
                    if (shulkerSize > 65000) { // Max shulker size in bytes calculated based on tests
                        shulkerInventory.clear();
                        blockStateMeta.setBlockState(shulkerBox);
                        item.setItemMeta(blockStateMeta);
                        getLogger().info("Cleared Shulker Box in " + player.getName() + "'s inventory with size " + shulkerSize + "bytes named '" + getShulkerName(item) + "'");
                    }

                    sendPrefixedLocalizedMessage(player, "nbtPatch_deleted_item",getShulkerName(item));

                }
            }
        }
    }

    // Method to calculate the size of the given string in bytes
    private int calculateStringSizeInBytes(String data)  {
        byte[] byteArray = data.getBytes(StandardCharsets.UTF_8);
        return byteArray.length;
    }

    private String getShulkerName(ItemStack itemStack) {
        if (itemStack == null) {
            return "";
        }

        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null && meta.hasDisplayName()) {
            return String.valueOf(itemStack.getItemMeta().getDisplayName());
        } else {
            return itemStack.getType().toString().replace("_", " ").toLowerCase();
        }
    }
}
