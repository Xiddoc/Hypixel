package inc.xiddy.hypixel.commands.implementations;

import inc.xiddy.hypixel.commands.CommandInfo;
import inc.xiddy.hypixel.commands.HypixelCommand;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import org.bukkit.util.Vector;

@CommandInfo(name = "setvelocity", minArgCount = 3, permission = "hypixel.admin")
@SuppressWarnings("unused")
public class SetVelocity extends HypixelCommand {

	@Override
	public void execute(HypixelPlayer player, String[] args) {
		// Set player's velocity
		player.setVelocity(new Vector(
			Integer.parseInt(args[0]),
			Integer.parseInt(args[1]),
			Integer.parseInt(args[2])
		));
	}
}
