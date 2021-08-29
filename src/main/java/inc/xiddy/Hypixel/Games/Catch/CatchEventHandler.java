package inc.xiddy.Hypixel.Games.Catch;

import inc.xiddy.Hypixel.Dataclasses.GameEventHandler;
import inc.xiddy.Hypixel.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class CatchEventHandler extends GameEventHandler {
	public CatchEventHandler(CatchRunnable game) {
		super(game);
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
			// Get the damager
			Player damager = (Player) ((EntityDamageByEntityEvent) player.getLastDamageCause()).getDamager();
			// For each team
			for (CatchTeam team: this.getGame().getTeams()) {
				// If this is the damager's team
				if (team.getPlayers().contains(damager)) {
					// If damager is on the same team as the player
					if (team.getPlayers().contains(player)) {
						// Prevent the damage
						event.setCancelled(true);
					} else {
						// If the damager is a seeker
						if (team.isSeeker()) {
							// End the game
							this.getGame().stopGame();
						}
					}
					// Exit the event
					return;
				}
			}

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
		System.out.println("placed");
	}

	@EventHandler
	public void onDestroy(BlockBreakEvent event) {
		if (this.verifyState(event)) return;

		// Stop them from destroying the map
		event.setCancelled(true);
		// Don't set off anticheat
		Main.getMainHandler().getAnticheatHandler().revokeLeftClick(event.getPlayer());
	}

	@Override
	public CatchRunnable getGame() {
		return (CatchRunnable) this.getBaseGame();
	}
}
