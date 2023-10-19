package inc.xiddy.hypixel.games.hide_n_seek;

import inc.xiddy.hypixel.dataclasses.HypixelEventHandler;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class HiderCaughtHandler extends HypixelEventHandler {
	public HiderCaughtHandler(CatchRunnable game) {
		super(game);
	}

	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent event) {
		if (this.verifyState(event)) return;

		if (!(event.getEntity() instanceof Player)) {
			return;
		}

		HypixelPlayer player = new HypixelPlayer(event.getEntity());
		event.setCancelled(true);

		// If game is over
		if (this.getGame().isGameOver()) return;

		// If player was attacked (PVP)
		if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) && event.getDamager() instanceof Player) {
			// Get the damager
			HypixelPlayer damager = new HypixelPlayer(event.getDamager());
			// If the damager is on the seeker team
			// And the player is also on the seeker team
			if (this.getGame().getSeekerTeam().getPlayers().contains(damager) &&
				this.getGame().getHiderTeam().getPlayers().contains(player)) {
				// End the game in favor of the seekers
				this.getGame().gameOver(this.getGame().getSeekerTeam().getPlayers());
			}
		}
	}

	@Override
	public CatchRunnable getGame() {
		return (CatchRunnable) this.getBaseGame();
	}
}
