package inc.xiddy.Hypixel.Games.Bedwars.ShopItems;

import inc.xiddy.Hypixel.Games.Bedwars.BedwarsRunnable;
import inc.xiddy.Hypixel.Games.Bedwars.BedwarsShopItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WoolShopItem extends BedwarsShopItem {

	public WoolShopItem(ItemStack item, ItemStack payment) {
		super(item, payment);
	}

	@Override
	public String getItemAsString() {
		return "Wool";
	}

	@Override
	public void executeTransaction(Player player, BedwarsRunnable game) {
		// Get team wool
		// Add it to the player's inventory
		player.getInventory().addItem(
			game.getPlayerTeam(player).getWool(this.getItem().getAmount())
		);
	}
}
