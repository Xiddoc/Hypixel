package inc.xiddy.hypixel.utility;

import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.*;

@SuppressWarnings("deprecation")
public class HypixelUtils {
	public static int getPlayerPing(HypixelPlayer player) {
		return player.getHandle().ping;
	}

	public static Set<HypixelPlayer> getOnlinePlayers() {
		// Make new set for the new objects
		Set<HypixelPlayer> players = new HashSet<>();
		// Add all players
		Bukkit.getOnlinePlayers().forEach(player -> players.add(new HypixelPlayer(player)));
		// Return the object
		return players;
	}

	public static HypixelPlayer getPlayer(String name) {
		// Get player
		Player player = Bukkit.getPlayer(name);
		// If object is not null
		return player != null ? new HypixelPlayer(player) : null;
	}

	public static HypixelPlayer getOfflinePlayer(String name) {
		// Get offline player
		Player player = Bukkit.getOfflinePlayer(name).getPlayer();
		// If object is not null
		return player != null ? new HypixelPlayer(player) : null;
	}

	public static HypixelPlayer getPlayer(UUID uuid) {
		// Get player
		Player player = Bukkit.getPlayer(uuid);
		// If object is not null
		return player != null ? new HypixelPlayer(player) : null;
	}

	public static HypixelPlayer getOfflinePlayer(UUID uuid) {
		// Get offline player
		Player player = Bukkit.getOfflinePlayer(uuid).getPlayer();
		// If object is not null
		return player != null ? new HypixelPlayer(player) : null;
	}

	public static void sendTitle(HypixelPlayer player, String title, String subtitle, double stayIn, double fadeIn, double fadeOut) {
		sendTitle(player, title, subtitle, (int) (stayIn * 20), (int) (fadeIn * 20), (int) (fadeOut * 20));
	}

	public static void sendTitle(HypixelPlayer player, String title, String subtitle, int stayInTicks, int fadeInTicks, int fadeOutTicks) {
		// Set animation time
		sendCommand("title " + player.getDisplayName() + " times " + fadeInTicks + " " + stayInTicks + " " + fadeOutTicks);
		// Set title
		sendCommand("title " + player.getDisplayName() + " title \"" + title.replaceAll("\\\\", "\\\\").replaceAll("\"", "\\\"") + "\"");
		// Set subtitle
		sendCommand("title " + player.getDisplayName() + " subtitle \"" + subtitle.replaceAll("\\\\", "\\\\").replaceAll("\"", "\\\"") + "\"");
	}

	public static void hidePlayerName(HypixelPlayer player) {
		// https://www.spigotmc.org/threads/hiding-nametags-in-minecraft-1-8.285887/
		// Spawn armor stand
		ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
		// Invisible
		stand.setVisible(false);
		// Set metadata
		stand.setMetadata("HideNametag", new FixedMetadataValue(Main.getInstance(), true)); //Optional
		// Set passenger
		player.setPassenger(stand);
	}

	public static void sendCommand(String command) {
		Main.getInstance().getServer().dispatchCommand(
			Main.getInstance().getServer().getConsoleSender(),
			command
		);
	}

	public static <T> T randomFromArray(T[] array) {
		return array[(new Random()).nextInt(array.length)];
	}

	public static String capitalize(String word) {
		return capitalize(word.split(" "));
	}

	public static String capitalize(String[] words) {
		// New SB
		StringBuilder result = new StringBuilder();
		// For each word
		for (String word : words) {
			// Capitalize it
			result
				.append(word.substring(0, 1).toUpperCase())
				.append(word.toLowerCase().substring(1))
				.append(" ");
		}
		// Return the string
		return result.substring(0, result.length() - 1);
	}

	public static HypixelPlayer getPlayerGlobal(String playerName) {
		// Get player
		HypixelPlayer targetOnline = getPlayer(playerName);
		HypixelPlayer targetOffline = getOfflinePlayer(playerName);
		return XORReturn(targetOnline, targetOffline);
	}

	public static HypixelPlayer getPlayerGlobal(UUID playerUUID) {
		// Get player
		HypixelPlayer targetOnline = getPlayer(playerUUID);
		HypixelPlayer targetOffline = getOfflinePlayer(playerUUID);
		return XORReturn(targetOnline, targetOffline);
	}

	private static <T> T XORReturn(T targetOnline, T targetOffline) {
		// If player has never logged on
		if (targetOnline == null && targetOffline == null) {
			// Return null
			return null;
		} else {
			// If they are online
			if (targetOnline != null) {
				// Return that player
				return targetOnline;
			} else {
				// Otherwise, return the offline player
				return targetOffline;
			}
		}
	}

	@SuppressWarnings("unused")
	public static List<Block> getNearbyBlocks(Location location, int radius) {
		// Make new list
		List<Block> blocks = new ArrayList<>();
		Block block;
		// For each x in the radius
		for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
			// For each y in the radius
			for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
				// For each z in the radius
				for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
					// Get the block
					block = location.getWorld().getBlockAt(x, y, z);
					// If the block is not air
					if (!block.getType().equals(Material.AIR)) {
						// Add it to the list
						blocks.add(block);
					}
				}
			}
		}
		return blocks;
	}

	public static void explode(Location explosionLocation) {
		// Set blast radius
		double radius = 8;
		double strength = 2;
		// Init variables for each player
		Vector difVector;
		// For each player
		for (Entity entity : explosionLocation.getWorld().getNearbyEntities(explosionLocation, radius, radius, radius)) {
			// Get difference vector of entity to explosion
			difVector = entity.getLocation().add(0.0D, 1.0D, 0.0D).toVector().subtract(explosionLocation.toVector());
			// Math to make knockback differ by distance from explosion
			double length = difVector.length();
			difVector = difVector.normalize();
			difVector.multiply(strength / length);
			// If entity within the radius
			// AND the difference vector is NOT on the same x position OR z position
			// AND they are not NPCs
			if (entity.getLocation().distance(explosionLocation) < radius &&
				!(difVector.getX() == 0 || difVector.getZ() == 0) &&
				!CitizensAPI.getNPCRegistry().isNPC(entity)) {
				// Add velocity to the entity
				entity.setVelocity(entity.getVelocity().add(difVector.divide(new Vector(1, 5, 1))));
			}
		}
	}
}
