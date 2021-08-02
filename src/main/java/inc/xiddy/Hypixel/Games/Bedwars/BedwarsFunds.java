package inc.xiddy.Hypixel.Games.Bedwars;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BedwarsFunds {
	private int emerald = 0;
	private int diamond = 0;
	private int gold = 0;
	private int iron = 0;

	public BedwarsFunds(Player player) {
		// For each item in the players inventory
		for (ItemStack item : player.getInventory().getContents()) {
			// If item not AIR
			if (item != null) {
				// Increment each type of item
				switch (item.getType()) {
					case IRON_INGOT:
						this.iron += item.getAmount();
						break;
					case GOLD_INGOT:
						this.gold += item.getAmount();
						break;
					case DIAMOND:
						this.diamond += item.getAmount();
						break;
					case EMERALD:
						this.emerald += item.getAmount();
						break;
				}
			}
		}
	}

	public static ChatColor getResourceColor(Material resource) {
		// Switch for resource
		switch (resource) {
			default:
				return ChatColor.WHITE;
			case GOLD_INGOT:
				return ChatColor.GOLD;
			case DIAMOND:
				return ChatColor.AQUA;
			case EMERALD:
				return ChatColor.DARK_GREEN;
		}
	}

	public boolean hasAnyResources() {
		return this.getIron() + this.getGold() + this.getDiamond() + this.getEmerald() > 0;
	}

	public int getIron() {
		return iron;
	}

	public int getGold() {
		return gold;
	}

	public int getDiamond() {
		return diamond;
	}

	public int getEmerald() {
		return emerald;
	}

	public int howMany(Material resource) {
		// Switch for resource
		switch (resource) {
			case IRON_INGOT:
				return this.getIron();
			case GOLD_INGOT:
				return this.getGold();
			case DIAMOND:
				return this.getDiamond();
			case EMERALD:
				return this.getEmerald();
		}
		// If the resource wasn't one of the achievable resources, then return false
		return -1;
	}
}
