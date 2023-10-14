package inc.xiddy.hypixel.commands;

import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public abstract class HypixelCommand implements CommandExecutor {
	private final static String ALL_PERMS = "*";
	private final CommandInfo commandInfo;

	public HypixelCommand() {
		this.commandInfo = this.getClass().getDeclaredAnnotation(CommandInfo.class);
		Objects.requireNonNull(commandInfo, "Commands must be annotated with CommandInfo class.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < commandInfo.minArgCount()) {
			sender.sendMessage(ChatColor.DARK_RED + "Invalid usage of command.");
			return false;
		}

		if (!isCommandAvailableToAll() && !sender.hasPermission(commandInfo.permission())) {
			sender.sendMessage(ChatColor.DARK_RED + "You don't have permission to execute this Hypixel command.");
			return true;
		}

		this.executeCommand(sender, args);
		return true;
	}

	private void executeCommand(CommandSender sender, String[] args) {
		if (commandInfo.requiresPlayer()) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.DARK_RED + "You must be a player to execute this command.");
				return;
			}

			this.execute(new HypixelPlayer((Player) sender), args);
			return;
		}

		this.execute(sender, args);
	}

	public void execute(HypixelPlayer player, String[] args) {}
	public void execute(CommandSender sender, String[] args) {}

	private boolean isCommandAvailableToAll() {
		return commandInfo.permission().equals(ALL_PERMS);
	}

	public CommandInfo getCommandInfo() {
		return commandInfo;
	}
}
