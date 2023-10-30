package inc.xiddy.hypixel.games.bedwars.shopitems;

import inc.xiddy.hypixel.games.basegame.ingame.InGamePlayer;
import inc.xiddy.hypixel.games.bedwars.BedwarsRunnable;
import inc.xiddy.hypixel.games.bedwars.BedwarsShopItem;
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
	public void executeTransaction(InGamePlayer player, BedwarsRunnable game) {
		// Get team wool
		// Add it to the player's inventory
		player.getInventory().addItem(
			game.getPlayerTeam(player).getWool(this.getItem().getAmount())
		);
	}
}
