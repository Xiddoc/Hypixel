package inc.xiddy.hypixel.handlers;

import inc.xiddy.hypixel.commands.*;
import inc.xiddy.hypixel.constants.Permission;
import inc.xiddy.hypixel.dataclasses.HypixelCommand;
import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import inc.xiddy.hypixel.dataclasses.PlayerData;
import inc.xiddy.hypixel.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class CommandHandler {
	private final List<HypixelCommand> hypixelCommands;
	private final List<String> bukkitCommands = Arrays.asList("plugins", "timings", "version", "?", "about", "pl", "rl", "ver");

	@SuppressWarnings({"SpellCheckingInspection", "RedundantSuppression"})
	public CommandHandler() {
		// Register the commands
		this.hypixelCommands = new ArrayList<>();
		// Bedwars commands
		this.hypixelCommands.add(new BedwarsSetup("setupbedwars", Permission.OWNER));
		// Global commands
		this.hypixelCommands.add(new Ping("ping", Permission.DEFAULT));
		this.hypixelCommands.add(new StartGame("startgame", Permission.OWNER));
		this.hypixelCommands.add(new StopGame("stopgame", Permission.OWNER));
		this.hypixelCommands.add(new SaveLocation("savelocation", Permission.OWNER));
		this.hypixelCommands.add(new SetRole("setrole", Permission.OWNER));
		this.hypixelCommands.add(new SetVelocity("setvelocity", Permission.OWNER));
	}

	public void execute(Command cmd, HypixelPlayer player, String[] args) {
		// Get command
		HypixelCommand hypixelCommand = this.getCommand(cmd);
		if (hypixelCommand != null) {
			// Get player data
			PlayerData playerData = Main.getMainHandler().getPlayerHandler().getPlayerData(player);
			// If player's role has a greater (or equal) permission code than the command's permission code
			if (playerData.getRole().getPermissionCode() >= hypixelCommand.getPermission().getPermissionCode()) {
				// Then execute the command
				hypixelCommand.execute(player, args);
			} else {
				// Otherwise, send error message
				this.sendInvalidPermissions(player, playerData, hypixelCommand);
			}
		} else {
			// Otherwise, send error message
			this.sendInvalidCommand(player);
		}
	}

	public void sendInvalidCommand(HypixelPlayer player) {
		// Send error
		player.sendMessage(
			ChatColor.DARK_RED + "Invalid command."
		);
	}

	public void sendInvalidPermissions(HypixelPlayer player, PlayerData playerData, HypixelCommand hypixelCommand) {
		// Send error
		player.sendMessage(
			ChatColor.DARK_RED + "Invalid permissions (" +
				playerData.getRole().getCapitalizedString() + " > " +
				hypixelCommand.getPermission().getCapitalizedString() + ")."
		);
	}

	public boolean isBukkitCommandRegistered(String command) {
		// If it is in the list of bukkit commands
		return this.getBukkitCommands().contains(command);
	}

	public boolean isHypixelCommandRegistered(String command) {
		// If command is invalid, it will return null
		return this.getCommand(command) != null;
	}

	public boolean isCommandRegistered(String command) {
		// If it is in either the bukkit or hypixel commands
		return this.isBukkitCommandRegistered(command) || this.isHypixelCommandRegistered(command);
	}

	public HypixelCommand getCommand(Command command) {
		return this.getCommand(command.getName());
	}

	public HypixelCommand getCommand(String commandName) {
		// For each command
		for (HypixelCommand hypixelCommand : this.getCommands()) {
			// Check if command is registered
			if (hypixelCommand.getCommandName().equalsIgnoreCase(commandName)) {
				return hypixelCommand;
			}
		}
		// No command found
		return null;
	}

	public List<HypixelCommand> getCommands() {
		return this.hypixelCommands;
	}

	public List<String> getBukkitCommands() {
		return bukkitCommands;
	}
}
