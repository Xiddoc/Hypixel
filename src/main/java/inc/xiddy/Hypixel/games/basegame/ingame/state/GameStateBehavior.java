package inc.xiddy.hypixel.games.basegame.ingame.state;

import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import org.bukkit.entity.Player;

public interface GameStateBehavior {
	void onEnterState(HypixelPlayer player);
	void onExitState(HypixelPlayer player);
}
