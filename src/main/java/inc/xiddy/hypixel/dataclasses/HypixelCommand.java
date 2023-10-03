package inc.xiddy.hypixel.dataclasses;

import inc.xiddy.hypixel.constants.Permission;

public abstract class HypixelCommand {
	private final String commandName;

	private final Permission permission;

	public HypixelCommand(String commandName, Permission permission) {
		this.commandName = commandName;
		this.permission = permission;
	}

	public abstract boolean execute(HypixelPlayer player, String[] args);

	public String getCommandName() {
		return commandName;
	}

	public Permission getPermission() {
		return permission;
	}
}
