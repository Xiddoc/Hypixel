package inc.xiddy.Hypixel.Dataclasses;

import inc.xiddy.Hypixel.Constants.Lobby;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public abstract class HypixelRunnable extends BukkitRunnable {
	@Override
	public void run() { }

	public abstract void stopGame();

	public abstract Lobby getLobby();

	public abstract Set<Player> getPlayers();
}
