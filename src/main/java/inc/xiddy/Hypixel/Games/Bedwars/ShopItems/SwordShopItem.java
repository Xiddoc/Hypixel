package inc.xiddy.Hypixel.Games.Bedwars.ShopItems;

import inc.xiddy.Hypixel.Games.Bedwars.BedwarsRunnable;
import inc.xiddy.Hypixel.Games.Bedwars.BedwarsShopItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SwordShopItem extends BedwarsShopItem {

	public SwordShopItem(ItemStack item, ItemStack payment) {
		super(item, payment);
	}

	@Override
	public void executeTransaction(Player player, BedwarsRunnable game) {
		// Remove default sword if it exists
		player.getInventory().remove(Material.WOOD_SWORD);
		// Give the player the item (without metadata)
		player.getInventory().addItem(this.getCleanedItem());
	}
}
