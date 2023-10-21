package inc.xiddy.hypixel.games.hide_n_seek;

import inc.xiddy.hypixel.dataclasses.HypixelEventHandler;
import inc.xiddy.hypixel.games.basegame.ingame.InGamePlayer;
import inc.xiddy.hypixel.games.hide_n_seek.mechanics.CatchRadar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class BaseCatchEventHandler extends HypixelEventHandler {
	public BaseCatchEventHandler(CatchRunnable game) {
		super(game);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (this.verifyState(event)) return;

		// Get player
		InGamePlayer player = new InGamePlayer(event.getPlayer());

		// If game hasn't started, don't execute
		if (this.getGame().getGameTimer().getElapsedTime() < 30) {
			return;
		}

		// If player is going to die by falling into the void
		boolean isHider = this.getGame().getHiderTeam().getPlayers().contains(player);
		if (player.getLocation().getY() < this.getGame().getPlayerVoid()) {
			// Move them back to the spawn point
			this.getGame().spawn(player, false, isHider);
		}

		// If it was a hider who moved
		if (isHider) {
			// Update for each seeker
			this.getGame().getSeekerTeam().getPlayers().forEach(seeker -> {
				// Print the radar
				CatchRadar.printRadar(
					// To the seeker
					seeker,
					// Showing the distance between their location
					(int) seeker.getLocation().distance(
						// To the hiders location
						this.getGame().getHiderTeam().getPlayers().toArray(new InGamePlayer[0])[0].getLocation()
					)
				);
			});
		} else {
			// If a seeker moved
			// Then it is not necessary to update the radar for every other player
			// Print the radar
			CatchRadar.printRadar(
				// To the seeker
				player,
				// Showing the distance between their location
				(int) player.getLocation().distance(
					// To the hiders location
					this.getGame().getHiderTeam().getPlayers().toArray(new InGamePlayer[0])[0].getLocation()
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
	}

	@Override
	public CatchRunnable getGame() {
		return (CatchRunnable) this.getBaseGame();
	}
}
