package inc.xiddy.hypixel.commands.implementations.perms;

import inc.xiddy.hypixel.commands.CommandInfo;
import inc.xiddy.hypixel.commands.HypixelCommand;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;


@CommandInfo(name = "addperms", minArgCount = 2, permission = "hypixel.admin")
@SuppressWarnings("unused")
public class AddPerms extends HypixelCommand {

	@Override
	public void execute(HypixelPlayer player, String[] args) {
		String playerName = args[0];
		String permission = args[1];
//
//
//		api.getUserManager().getUser(args)
//
//		LuckPerms api = LuckPermsProvider.get();

		player.sendMessage("test");

	}
}
