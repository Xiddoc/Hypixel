package inc.xiddy.hypixel.games.bedwars;

import inc.xiddy.hypixel.games.basegame.ingame.InGamePlayer;
import inc.xiddy.hypixel.utility.HypixelUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.*;

public abstract class BedwarsShopItem {
	private final ItemStack item;
	private final ItemStack payment;

	public BedwarsShopItem(ItemStack item, ItemStack payment) {
		// Set fields
		this.payment = payment;
		this.item = item;
		// Modify item
		ItemMeta meta = this.item.getItemMeta();
		meta.setDisplayName(GREEN + this.getItemAsString());
		// Make description
		List<String> lore = new ArrayList<>();
		lore.add(GRAY + "Cost: " + BedwarsFunds.getResourceColor(getPayment().getType()) + this.getPayment().getAmount() + " " + this.getPaymentResourceAsString());
		lore.add("");
		for (String line : WordUtils.wrap("Great for doing stuff! This is a good description. You're probably getting rushed right now, watch out!", 25).split("\r\n")) {
			lore.add(GRAY + line);
		}
		lore.add("");
		lore.add(YELLOW + "Click to purchase!");
		// Update description
		meta.setLore(lore);
		// Update meta
		this.item.setItemMeta(meta);
	}

	public String getPaymentResourceAsString() {
		// Switch for resource
		switch (this.getPayment().getType()) {
			default:
				return "Iron";
			case GOLD_INGOT:
				return "Gold";
			case DIAMOND:
				return "Diamond";
			case EMERALD:
				return "Emerald";
		}
	}

	public ItemStack getPayment() {
		return this.payment;
	}

	public ItemStack getItem() {
		return this.item;
	}

	public ItemStack getCleanedItem() {
		// Clone the item
		ItemStack clonedItem = this.getItem().clone();
		// Remove the lore
		ItemMeta meta = clonedItem.getItemMeta();
		meta.setLore(new ArrayList<>());
		// Set name color
		if (meta.getDisplayName().startsWith("§")) {
			meta.setDisplayName(RESET + meta.getDisplayName().substring(2));
		}
		// Make unbreakable
		meta.spigot().setUnbreakable(true);
		// Set the meta to the item
		clonedItem.setItemMeta(meta);
		// Return the cleaned item
		return clonedItem;
	}

	public String getItemAsString() {
		return HypixelUtils.capitalize(this.getItem().getType().toString().split("_"));
	}

	public void executeTransaction(InGamePlayer player, BedwarsRunnable game) {
		// Give the player the item (without metadata)
		player.getInventory().addItem(this.getCleanedItem());
	}
}
