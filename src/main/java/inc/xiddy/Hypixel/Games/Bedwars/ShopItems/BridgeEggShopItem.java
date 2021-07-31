package inc.xiddy.Hypixel.Games.Bedwars.ShopItems;

import inc.xiddy.Hypixel.Games.Bedwars.BedwarsShopItem;
import org.bukkit.inventory.ItemStack;

public class BridgeEggShopItem extends BedwarsShopItem {

	public BridgeEggShopItem(ItemStack item, ItemStack payment) {
		super(item, payment);
	}

	@Override
	public String getItemAsString() {
		return "Bridge Egg";
	}
}
