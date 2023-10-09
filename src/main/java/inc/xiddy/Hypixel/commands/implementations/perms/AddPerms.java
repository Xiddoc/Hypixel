package inc.xiddy.hypixel.commands.implementations.perms;

import inc.xiddy.hypixel.commands.CommandInfo;
import inc.xiddy.hypixel.commands.HypixelCommand;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;


@CommandInfo(name = "addperms", minArgCount = 2, permission = "hypixel.admin")
@SuppressWarnings("unused")
public class AddPerms extends HypixelCommand {

	@Override
	public void execute(HypixelPlayer player, String[] args) {
		String playerName = args[0];
		String permission = args[1];

		LuckPerms api = LuckPermsProvider.get();

		User user = api.getUserManager().getUser(playerName);
		if (user == null) {
			player.sendMessage(ChatColor.DARK_RED + "Not a valid or online username.");
			return;
		}

		user.data().add(Node.builder(permission).build());
		api.getUserManager().saveUser(user);

		player.sendMessage(
			ChatColor.GREEN + "Permission '" + permission + "' added to " +
				user.getUsername() + "(" + user.getUniqueId() + ")!");
	}
}
