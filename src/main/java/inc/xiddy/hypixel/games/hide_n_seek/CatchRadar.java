package inc.xiddy.hypixel.games.hide_n_seek;

import inc.xiddy.hypixel.dataclasses.HypixelPlayer;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;

public class CatchRadar {
	public static void printRadar(HypixelPlayer player, int distance) {
		ChatColor color;
		if (distance > 50) {
			color = ChatColor.WHITE;
		} else if (distance > 15) {
			color = ChatColor.GOLD;
		} else {
			color = ChatColor.RED;
		}

		// Print radar to player
		player.getHandle().playerConnection.sendPacket(
			new PacketPlayOutChat(new ChatComponentText(color + "" + ChatColor.BOLD + "[ " + distance + " ]"), (byte) 2)
		);
	}
}
