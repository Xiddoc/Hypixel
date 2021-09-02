package inc.xiddy.Hypixel.Dataclasses;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

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
		// Teleport and return
		return this.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
	}
}
