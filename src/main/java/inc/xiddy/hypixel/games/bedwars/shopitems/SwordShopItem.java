package inc.xiddy.hypixel.games.bedwars.shopitems;

import inc.xiddy.hypixel.games.basegame.ingame.InGamePlayer;
import inc.xiddy.hypixel.games.bedwars.BedwarsRunnable;
import inc.xiddy.hypixel.games.bedwars.BedwarsShopItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SwordShopItem extends BedwarsShopItem {

	public SwordShopItem(ItemStack item, ItemStack payment) {
		super(item, payment);
	}

	@Override
	public void executeTransaction(InGamePlayer player, BedwarsRunnable game) {
		// Remove default sword if it exists
		player.getInventory().remove(Material.WOOD_SWORD);
		// Give the player the item (without metadata)
		player.getInventory().addItem(this.getCleanedItem());
	}
}
