package inc.xiddy.hypixel.games.hub;

import inc.xiddy.hypixel.constants.Lobby;
import inc.xiddy.hypixel.dataclasses.HypixelEventHandler;
import inc.xiddy.hypixel.games.basegame.HypixelRunnable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class SpectatorEventHandler extends HypixelEventHandler {
	public SpectatorEventHandler() {
		super((Lobby) null);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (this.verifyState(event)) return;

		// Do not let spectator take damage
		event.setCancelled(true);
	}

	@Override
	public HypixelRunnable getGame() {
		return null;
	}
}
