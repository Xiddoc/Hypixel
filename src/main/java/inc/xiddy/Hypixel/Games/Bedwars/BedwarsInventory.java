package inc.xiddy.Hypixel.Games.Bedwars;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class BedwarsInventory {
	private final Map<String, ItemStack> hotbar;
	private final ItemStack[] armor;

	public BedwarsInventory(BedwarsTeam team) {
		// Set up variables and default settings
		this.armor = team.getTeamColor().getColoredArmor(true);
		// Set defaults
		this.hotbar = new HashMap<>();
		this.updateHotbar("sword", Material.WOOD_SWORD);
		this.updateHotbar("shears", Material.AIR);
		this.updateHotbar("pic", Material.AIR);
		this.updateHotbar("axe", Material.AIR);
	}

	public void improvePic() {
		// Init material for switch case
		Material upgrade;
		// Switch to operate per case
		switch (this.getHotbarItem("pic").getType()) {
			default:
				upgrade = Material.WOOD_PICKAXE;
				break;
			case WOOD_PICKAXE:
				upgrade = Material.IRON_PICKAXE;
				break;
			case IRON_PICKAXE:
				upgrade = Material.GOLD_PICKAXE;
				break;
			case GOLD_PICKAXE:
				upgrade = Material.DIAMOND_PICKAXE;
		}
		// Update to the upgrade
		this.updateHotbar("pic", upgrade);
	}

	public void unimprovePic() {
		// Init material for switch case
		Material upgrade;
		// Switch to operate per case
		switch (this.getHotbarItem("pic").getType()) {
			default:
				upgrade = Material.AIR;
				break;
			case WOOD_PICKAXE:
			case IRON_PICKAXE:
				upgrade = Material.WOOD_PICKAXE;
				break;
			case GOLD_PICKAXE:
				upgrade = Material.IRON_PICKAXE;
				break;
			case DIAMOND_PICKAXE:
				upgrade = Material.GOLD_PICKAXE;
		}
		// Update to the upgrade
		this.updateHotbar("pic", upgrade);
	}

	public void improveAxe() {
		// Init material for switch case
		Material upgrade;
		// Switch to operate per case
		switch (this.getHotbarItem("axe").getType()) {
			default:
				upgrade = Material.WOOD_AXE;
				break;
			case WOOD_AXE:
				upgrade = Material.IRON_AXE;
				break;
			case IRON_AXE:
				upgrade = Material.GOLD_AXE;
				break;
			case GOLD_AXE:
				upgrade = Material.DIAMOND_AXE;
		}
		// Update to the upgrade
		this.updateHotbar("axe", upgrade);
	}

	public void unimproveAxe() {
		// Init material for switch case
		Material upgrade;
		// Switch to operate per case
		switch (this.getHotbarItem("axe").getType()) {
			default:
				upgrade = Material.AIR;
				break;
			case WOOD_AXE:
			case IRON_AXE:
				upgrade = Material.WOOD_AXE;
				break;
			case GOLD_AXE:
				upgrade = Material.IRON_AXE;
				break;
			case DIAMOND_AXE:
				upgrade = Material.GOLD_AXE;
		}
		// Update to the upgrade
		this.updateHotbar("axe", upgrade);
	}

	public void enableShear() {
		// Enable them
		this.updateHotbar("shear", Material.SHEARS);
	}

	public void setArmor(Material boots, Material leggings) {
		this.getArmor()[0] = new ItemStack(this.makeUnbreakable(boots));
		this.getArmor()[1] = new ItemStack(this.makeUnbreakable(leggings));
	}

	public ItemStack[] getArmor() {
		return armor;
	}

	public ItemStack getHotbarItem(String key) {
		return this.getHotbar().get(key);
	}

	public Map<String, ItemStack> getHotbar() {
		return this.hotbar;
	}

	public void updateHotbar(String location, Material item) {
		this.hotbar.put(location, this.makeUnbreakable(item));
	}

	public ItemStack makeUnbreakable(Material material) {
		// Convert to item stack
		ItemStack item = new ItemStack(material);

		// Null check (AIR can not be unbreakable)
		// If the material is NOT air, then update the metadata
		if (!material.equals(Material.AIR)) {
			// If the item has meta data
			ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(item.getType());
			// Make it unbreakable
			meta.spigot().setUnbreakable(true);
			// Update the item with the new meta
			item.setItemMeta(meta);
		}

		// Return item
		return item;
	}
}
