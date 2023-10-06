package inc.xiddy.hypixel.games.bedwars.shopitems;

import inc.xiddy.hypixel.games.bedwars.BedwarsShopItem;
import org.bukkit.inventory.ItemStack;

public class FireballShopItem extends BedwarsShopItem {

	public FireballShopItem(ItemStack item, ItemStack payment) {
		super(item, payment);
	}

	@Override
	public String getItemAsString() {
		return "Fireball";
	}
}
