package inc.xiddy.Hypixel.Games.Catch;

import inc.xiddy.Hypixel.Dataclasses.GameEventHandler;
import inc.xiddy.Hypixel.Dataclasses.SmallLocation;
import inc.xiddy.Hypixel.Games.Bedwars.BedwarsTeam;
import inc.xiddy.Hypixel.HypixelUtils;
import inc.xiddy.Hypixel.Main;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class CatchEventHandler extends GameEventHandler {
	private final CatchRunnable game;

	public CatchEventHandler(CatchRunnable game) {
		super(game.getLobby());
		this.game = game;
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (this.verifyState(event)) return;

		// If not player
		if (!(event.getEntity() instanceof Player)) {
			// Exit
			return;
		}
		// Otherwise,
		// Cast to player object
		Player player = (Player) event.getEntity();

		// If player was attacked (PVP)
		if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) &&
			player.getLastDamageCause() instanceof Player) {
			// Update the damager
			this.getGame().getBedwarsPlayerData(player).getLastDamage().updateDamager(
				// To the last person who damaged the player
				(Player) ((EntityDamageByEntityEvent) player.getLastDamageCause()).getDamager()
			);
		} else if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
			// Don't let them take fall damage
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (this.verifyState(event)) return;

		// If block is in not in block range
		if (!(event.getBlock().getLocation().getY() >= this.getGame().getBlockVoidMin() &&
			event.getBlock().getLocation().getY() <= this.getGame().getBlockVoidMax())) {
			// Prevent player from placing the block
			event.setCancelled(true);
			// Send error
			event.getPlayer().sendMessage(ChatColor.RED + "You have reached build limit!");
			return;
		}

		// Otherwise, if not a special block
		// Add block to placed blocks list
//		this.getGame().addPlacedBlock(new SmallLocation(event.getBlock().getLocation()));
	}

	@EventHandler
	public void onDestroy(BlockBreakEvent event) {
		if (this.verifyState(event)) return;

		// Stop them from destroying the map
		event.setCancelled(true);
		// Don't set off anticheat
		Main.getMainHandler().getAnticheatHandler().revokeLeftClick(event.getPlayer());
	}

	public CatchRunnable getGame() {
		return game;
	}
}
