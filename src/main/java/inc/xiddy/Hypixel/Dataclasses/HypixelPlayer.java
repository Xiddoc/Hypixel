package inc.xiddy.Hypixel.Dataclasses;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HypixelPlayer extends CraftPlayer {
	public HypixelPlayer(Entity entity) {
		this((Player) entity);
	}

	public HypixelPlayer(Player player) {
		super((CraftServer) player.getServer(), ((CraftPlayer) player).getHandle());
	}

	@Override
	public boolean teleport(Location location) {
		// Unmount any passengers
		this.eject();
		// If the world to teleport to is not the current world
		if (!location.getWorld().equals(this.getWorld())) {
			// Then move them to that world
			this.entity.teleportTo(location, false);
		} else {
			// Otherwise, just move them within this world
			this.entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		}
		return true;
	}
}
