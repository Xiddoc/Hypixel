package inc.xiddy.Hypixel.Games.Bedwars;

import inc.xiddy.Hypixel.Dataclasses.HypixelPlayer;
import inc.xiddy.Hypixel.Games.Bedwars.ShopItems.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BedwarsShop {
	@SuppressWarnings("FieldCanBeLocal")
	private final int size = 54;
	private final BedwarsShopItem[] shopItems;

	public BedwarsShop() {
		this.shopItems = new BedwarsShopItem[]{
			new WoolShopItem(new ItemStack(Material.WOOL, 16), new ItemStack(Material.IRON_INGOT, 4)),
			new DefaultShopItem(new ItemStack(Material.STAINED_CLAY, 16), new ItemStack(Material.IRON_INGOT, 12)),
			new DefaultShopItem(new ItemStack(Material.WOOD, 16), new ItemStack(Material.GOLD_INGOT, 4)),
			new DefaultShopItem(new ItemStack(Material.ENDER_STONE, 12), new ItemStack(Material.IRON_INGOT, 24)),
			new DefaultShopItem(new ItemStack(Material.STAINED_GLASS, 4), new ItemStack(Material.IRON_INGOT, 12)),
			new DefaultShopItem(new ItemStack(Material.LADDER, 16), new ItemStack(Material.IRON_INGOT, 12)),
			new DefaultShopItem(new ItemStack(Material.OBSIDIAN, 4), new ItemStack(Material.EMERALD, 4)),
			null, null,
			null, null, null, null, null, null, null, null, null,
			new DefaultShopItem(new ItemStack(Material.GOLDEN_APPLE, 1), new ItemStack(Material.GOLD_INGOT, 3)),
			new TNTShopItem(new ItemStack(Material.TNT, 1), new ItemStack(Material.GOLD_INGOT, 4)),
			new FireballShopItem(new ItemStack(Material.FIREBALL, 1), new ItemStack(Material.IRON_INGOT, 40)),
			new DefaultShopItem(new ItemStack(Material.WATER_BUCKET, 1), new ItemStack(Material.GOLD_INGOT, 3)),
			new MagicMilkShopItem(new ItemStack(Material.MILK_BUCKET, 1), new ItemStack(Material.GOLD_INGOT, 4)),
			new BedbugShopItem(new ItemStack(Material.SNOW_BALL, 1), new ItemStack(Material.IRON_INGOT, 40)),
			new BridgeEggShopItem(new ItemStack(Material.EGG, 1), new ItemStack(Material.EMERALD, 2)),
			new DefaultShopItem(new ItemStack(Material.ENDER_PEARL, 1), new ItemStack(Material.EMERALD, 4)),
			null,
			null, null, null, null, null, null, null, null, null,
			new SwordShopItem(new ItemStack(Material.STONE_SWORD, 1), new ItemStack(Material.IRON_INGOT, 10)),
			new SwordShopItem(new ItemStack(Material.IRON_SWORD, 1), new ItemStack(Material.GOLD_INGOT, 7)),
			new SwordShopItem(new ItemStack(Material.DIAMOND_SWORD, 1), new ItemStack(Material.EMERALD, 4)),
			new KBStickShopItem(new ItemStack(Material.STICK, 1), new ItemStack(Material.GOLD_INGOT, 5)),
		};
	}

	public String getShopName() {
		return "Quick Buy";
	}

	public Inventory getShopInventory() {
		// Make new inventory
		Inventory tempInventory = Bukkit.createInventory(null, this.getShopSize(), getShopName());
		// Add items to it
		tempInventory.setContents(this.getItems());
		// Return the inventory
		return tempInventory;
	}

	public void buyItem(BedwarsRunnable game, HypixelPlayer player, Material item) {
		// Find the item
		// For each shop item
		for (BedwarsShopItem shopItem : this.getShopItems()) {
			// If the item is not air
			if (shopItem != null) {
				// If the shop item is the one that the player took
				if (shopItem.getItem().getType().equals(item)) {
					// The player has enough resources to pay for the item
					int resourceCount = (new BedwarsFunds(player)).howMany(shopItem.getPayment().getType());
					if (resourceCount >= shopItem.getPayment().getAmount()) {
						// Steal the money!
						player.getInventory().removeItem(shopItem.getPayment());

						// Execute the buying method of the object
						shopItem.executeTransaction(player, game);

						// Play success sound
						player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 2);
						// Success message
						player.sendMessage(
							ChatColor.GREEN + "You purchased " +
								ChatColor.GOLD + shopItem.getItemAsString()
						);
					} else {
						// Success message
						player.sendMessage(
							ChatColor.RED + "You don't have enough " + shopItem.getPaymentResourceAsString() + "! Need " +
								(shopItem.getPayment().getAmount() - resourceCount) + " more!"
						);
						// Play sound
						player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 0.5F);
					}
				}
			}
		}
	}

	private ItemStack[] getItems() {
		// Make item stack
		ItemStack[] inventory = new ItemStack[this.getShopSize()];
		// For each material
		for (int i = 0; i < this.getShopItems().length; i++) {
			// Add it to the inventory
			inventory[i] = this.getShopItems()[i] == null ? new ItemStack(Material.AIR) : this.getShopItems()[i].getItem();
		}
		// Return the items
		return inventory;
	}

	private BedwarsShopItem[] getShopItems() {
		return this.shopItems;
	}

	public int getShopSize() {
		return this.size;
	}

}
