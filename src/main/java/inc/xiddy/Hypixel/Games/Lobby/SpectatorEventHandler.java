package inc.xiddy.Hypixel.Games.Lobby;

import inc.xiddy.Hypixel.Constants.Lobby;
import inc.xiddy.Hypixel.Dataclasses.HypixelEventHandler;
import inc.xiddy.Hypixel.Dataclasses.HypixelRunnable;
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
