package inc.xiddy.Hypixel.Dataclasses;

import inc.xiddy.Hypixel.Constants.Permission;

public abstract class HypixelCommand {
	private final String commandName;

	private final Permission permission;

	public HypixelCommand(String commandName, Permission permission) {
		this.commandName = commandName;
		this.permission = permission;
	}

	public abstract void execute(HypixelPlayer player, String[] args);

	public String getCommandName() {
		return commandName;
	}

	public Permission getPermission() {
		return permission;
	}
}
