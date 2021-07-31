package inc.xiddy.Hypixel.Games.Lobby;

import inc.xiddy.Hypixel.Dataclasses.GameEventHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class SpectatorEventHandler extends GameEventHandler {
	public SpectatorEventHandler() {
		super(null);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (this.verifyState(event)) return;

		// Do not let spectator take damage
		event.setCancelled(true);
	}
}
