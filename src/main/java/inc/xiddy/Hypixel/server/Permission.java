package inc.xiddy.hypixel.server;

import inc.xiddy.hypixel.utility.HypixelUtils;

// TODO Remove this or replace with Rank class that only affects text chat
public enum Permission {
	OWNER("{RED}[OWNER] "),
	DEFAULT("{GRAY}");

	private final String namePrefix;

	Permission(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	public static Permission fromString(String str) {
		return Permission.valueOf(str.toUpperCase());
	}

	@Override
	public String toString() {
		return this.name();
	}

	public String getCapitalizedString() {
		return HypixelUtils.capitalize(this.toString());
	}

	public String getNamePrefix() {
		return namePrefix;
	}
}
