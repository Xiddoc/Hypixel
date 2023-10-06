package inc.xiddy.hypixel.constants;

import inc.xiddy.hypixel.utility.HypixelUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public enum TeamColor {
	RED("red", ChatColor.RED, DyeColor.RED, Color.fromRGB(255, 0, 0)),
	BLUE("blue", ChatColor.BLUE, DyeColor.BLUE, Color.fromRGB(0, 0, 255)),
	GREEN("green", ChatColor.GREEN, DyeColor.GREEN, Color.fromRGB(0, 255, 0)),
	YELLOW("yellow", ChatColor.YELLOW, DyeColor.YELLOW, Color.fromRGB(255, 255, 0)),
	AQUA("aqua", ChatColor.AQUA, DyeColor.CYAN, Color.fromRGB(0, 255, 255)),
	WHITE("white", ChatColor.WHITE, DyeColor.WHITE, Color.fromRGB(255, 255, 255)),
	PINK("pink", ChatColor.LIGHT_PURPLE, DyeColor.PINK, Color.fromRGB(255, 0, 255)),
	GRAY("gray", ChatColor.GRAY, DyeColor.GRAY, Color.fromRGB(100, 100, 100));

	private final String text;
	private final ChatColor chatColor;
	private final DyeColor dyeColor;
	private final Color javaColor;

	TeamColor(final String text, final ChatColor chatColor, final DyeColor dyeColor, final Color javaColor) {
		this.text = text;
		this.chatColor = chatColor;
		this.dyeColor = dyeColor;
		this.javaColor = javaColor;
	}

	public static boolean contains(String colorName) {
		// For each color
		for (TeamColor color : values()) {
			// If the colors are equal
			if (color.text.equals(colorName)) {
				return true;
			}
		}
		// Otherwise
		return false;
	}

	public static TeamColor fromString(String str) {
		return TeamColor.valueOf(str.toUpperCase());
	}

	public ChatColor getColorCode() {
		return this.chatColor;
	}

	public String getCapitalizedString() {
		return HypixelUtils.capitalize(this.text);
	}

	@Override
	public String toString() {
		return this.text;
	}

	public DyeColor getDyeColor() {
		return dyeColor;
	}

	public ItemStack[] getColoredArmor(boolean unbreakable) {
		// Get materials and put in stack
		ItemStack[] stack = new ItemStack[]{
			new ItemStack(Material.LEATHER_BOOTS),
			new ItemStack(Material.LEATHER_LEGGINGS),
			new ItemStack(Material.LEATHER_CHESTPLATE),
			new ItemStack(Material.LEATHER_HELMET)
		};
		// For each item
		for (ItemStack item : stack) {
			// Pull the meta data
			ItemMeta itemMeta = item.getItemMeta();
			// Make it unbreakable
			itemMeta.spigot().setUnbreakable(unbreakable);
			// Cast and change leather settings
			LeatherArmorMeta meta = (LeatherArmorMeta) itemMeta;
			// Color it with the team color
			meta.setColor(this.getJavaColor());
			// Set item meta data
			item.setItemMeta(meta);
		}
		// Return stack of items
		return stack;
	}

	public Color getJavaColor() {
		return javaColor;
	}
}

