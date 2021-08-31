package inc.xiddy.Hypixel.Games.Catch;

import inc.xiddy.Hypixel.Dataclasses.HypixelEventHandler;
import inc.xiddy.Hypixel.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class CatchEventHandler extends HypixelEventHandler {
	public CatchEventHandler(CatchRunnable game) {
		super(game);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (this.verifyState(event)) return;

		// Get player
		Player player = event.getPlayer();

		// If game hasn't started, don't execute
		if (this.getGame().getGameTimer().getElapsedTime() < 30) {
			return;
		}

		// If player is going to die by falling into the void
		if (player.getLocation().getY() < this.getGame().getPlayerVoid()) {
			// Move them back to the spawn point
			this.getGame().spawn(player, false, false);
		}

		// If it was a hider who moved
		if (this.getGame().getHiderTeam().getPlayers().contains(player)) {
			// Update for each seeker
			this.getGame().getSeekerTeam().getPlayers().forEach(seeker -> {
				// Print the radar
				this.getGame().getRadar().printRadar(
					// To the seeker
					seeker,
					// Showing the distance between their location
					(int) seeker.getLocation().distance(
						// To the hiders location
						this.getGame().getHiderTeam().getPlayers().toArray(new Player[0])[0].getLocation()
					)
				);
			});
		} else {
			// If a seeker moved
			// Then it is not necessary to update the radar for every other player
			// Print the radar
			this.getGame().getRadar().printRadar(
				// To the seeker
				player,
				// Showing the distance between their location
				(int) player.getLocation().distance(
					// To the hiders location
					this.getGame().getHiderTeam().getPlayers().toArray(new Player[0])[0].getLocation()
				)
			);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (this.verifyState(event)) return;

		// Prevent natural (lava, etc.) damage
		event.setCancelled(true);
		// Remove fire ticks to prevent massive fire picture taking up half your screen
		event.getEntity().setFireTicks(0);
	}

	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent event) {
		if (this.verifyState(event)) return;

		// If not player
		if (!(event.getEntity() instanceof Player)) {
			// Exit
			return;
		}
		// Otherwise,
		// Cast to player object
		Player player = (Player) event.getEntity();

		// Prevent the damage as they are on the same team
		event.setCancelled(true);

		// If game is over
		if (this.getGame().isGameOver()) return;

		// If player was attacked (PVP)
		if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) &&
			event.getDamager() instanceof Player) {
			// Get the damager
			Player damager = (Player) event.getDamager();
			// If the damager is on the seeker team
			// And the player is also on the seeker team
			if (this.getGame().getSeekerTeam().getPlayers().contains(damager) &&
				this.getGame().getHiderTeam().getPlayers().contains(player)) {
				// End the game in favor of the seekers
				this.getGame().gameOver(this.getGame().getSeekerTeam().getPlayers());
			}
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (this.verifyState(event)) return;

		// Don't let players place blocks
		event.setCancelled(true);
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
