package inc.xiddy.hypixel.games.bedwars.shopitems;

import inc.xiddy.hypixel.games.bedwars.BedwarsShopItem;
import org.bukkit.inventory.ItemStack;

public class TNTShopItem extends BedwarsShopItem {

	public TNTShopItem(ItemStack item, ItemStack payment) {
		super(item, payment);
	}

	@Override
	public String getItemAsString() {
		return "TNT";
	}
}
