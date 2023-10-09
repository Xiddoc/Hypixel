package inc.xiddy.hypixel.games.sumo;

import inc.xiddy.hypixel.Main;
import inc.xiddy.hypixel.dataclasses.HypixelEventHandler;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class SumoEventHandler extends HypixelEventHandler {
	public SumoEventHandler(SumoRunnable game) {
		super(game);
	}

	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent event) {
		if (this.verifyState(event)) return;

		// If not player
		if (!(event.getEntity() instanceof HypixelPlayer)) {
			// Exit
			return;
		}
		// Otherwise,
		// Cast to player object
		HypixelPlayer player = new HypixelPlayer(event.getEntity());

		// Prevent the damage as they are on the same team
		event.setCancelled(true);

		// If game is over
		if (this.getGame().isGameOver()) return;

		// If player was attacked (PVP)
		if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) &&
			event.getDamager() instanceof HypixelPlayer) {
			// Get the damager
			HypixelPlayer damager = new HypixelPlayer(event.getDamager());
			// If the damager is on the seeker team
			// And the player is also on the seeker team
//			if (this.getGame().getSeekerTeam().getPlayers().contains(damager) &&
//				this.getGame().getHiderTeam().getPlayers().contains(player)) {
//				// End the game in favor of the seekers
//				this.getGame().gameOver(this.getGame().getSeekerTeam().getPlayers());
//			}
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
	public void onDestroy(BlockBreakEvent event) {
		if (this.verifyState(event)) return;

		// Stop them from destroying the map
		event.setCancelled(true);
		// Don't set off anticheat
		Main.getMainHandler().getAnticheatHandler().revokeLeftClick(new HypixelPlayer(event.getPlayer()));
	}

	@Override
	public SumoRunnable getGame() {
		return (SumoRunnable) this.getBaseGame();
	}
}
