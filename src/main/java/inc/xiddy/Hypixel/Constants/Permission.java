package inc.xiddy.Hypixel.Constants;

import inc.xiddy.Hypixel.HypixelUtils;

@SuppressWarnings("SpellCheckingInspection")
public enum Permission {
	OWNER(1000, "{RED}[OWNER] "),
	YOUTUBE(1000, "{RED}[{WHITE}YOUTUBE{RED}] "),
	MVPPLUSPLUS(500, "{GOLD}[MVP++] "),
	MVPPLUS(250, "{GOLD}[MVP+] "),
	DEFAULT(0, "{GRAY}");

	private final int permissionCode;
	private final String namePrefix;

	Permission(final int permissionCode, String namePrefix) {
		this.permissionCode = permissionCode;
		this.namePrefix = namePrefix;
	}

	public static Permission fromString(String str) {
		return Permission.valueOf(str.toUpperCase());
	}

	public int getPermissionCode() {
		return permissionCode;
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
