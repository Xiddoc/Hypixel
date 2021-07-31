package inc.xiddy.Hypixel.Games.Bedwars.ShopItems;

import inc.xiddy.Hypixel.Games.Bedwars.BedwarsShopItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KBStickShopItem extends BedwarsShopItem {

	public KBStickShopItem(ItemStack item, ItemStack payment) {
		super(item, payment);
		// Get the item
		ItemStack shopItem = this.getItem();
		// Get the meta
		ItemMeta meta = shopItem.getItemMeta();
		// Add KB
		meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
		// Remove enchants
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		// Add to item
		shopItem.setItemMeta(meta);
	}
}
