package inc.xiddy.Hypixel.Games.Catch;

import inc.xiddy.Hypixel.Dataclasses.HypixelPlayer;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

public class CatchRadar {
	public CatchRadar() {}

	public void printRadar(HypixelPlayer player, int distance) {
		// Switch case for color
		ChatColor color;
		if (distance > 50) {
			// Set color
			color = ChatColor.WHITE;
		} else if (distance > 15) {
			// Set color
			color = ChatColor.GOLD;
		} else {
			// Set color
			color = ChatColor.RED;
		}

		// Print radar to player
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(
			new PacketPlayOutChat(new ChatComponentText(color + "" + ChatColor.BOLD + "[ " + distance + " ]"), (byte) 2)
		);
	}
}
