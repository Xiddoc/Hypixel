package inc.xiddy.hypixel.games.bedwars.shopitems;

import inc.xiddy.hypixel.games.bedwars.BedwarsShopItem;
import org.bukkit.inventory.ItemStack;

public class MagicMilkShopItem extends BedwarsShopItem {

	public MagicMilkShopItem(ItemStack item, ItemStack payment) {
		super(item, payment);
	}

	@Override
	public String getItemAsString() {
		return "Magic Milk";
	}
}
