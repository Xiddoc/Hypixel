package inc.xiddy.Hypixel.Games.Catch;

import inc.xiddy.Hypixel.Dataclasses.HypixelEventHandler;
import inc.xiddy.Hypixel.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class CatchEventHandler extends HypixelEventHandler {
	public CatchEventHandler(CatchRunnable game) {
		super(game);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (this.verifyState(event)) return;

		// If player is going to die by falling into the void
		if (event.getPlayer().getLocation().getY() < this.getGame().getPlayerVoid()) {
			// Move them back to the spawn point
			this.getGame().spawn(event.getPlayer());
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (this.verifyState(event)) return;

		// If not player
		if (!(event.getEntity() instanceof Player)) {
			// Exit
			return;
		}
		// Otherwise,
		// Cast to player object
		Player player = (Player) event.getEntity();

		// Prevent the damage as they are on the same team
		event.setCancelled(true);

		// If game is over
		if (this.getGame().isGameOver()) return;

		// If player was attacked (PVP)
		if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) &&
			player.getLastDamageCause() instanceof Player) {
			// Get the damager
			Player damager = (Player) ((EntityDamageByEntityEvent) player.getLastDamageCause()).getDamager();
			// If the damager is on the seeker team
			// And the player is also on the seeker team
			if (this.getGame().getSeekerTeam().getPlayers().contains(damager) &&
				this.getGame().getHiderTeam().getPlayers().contains(player)) {
				// End the game in favor of the seekers
				this.getGame().gameOver(this.getGame().getSeekerTeam().getPlayers());
			}
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (this.verifyState(event)) return;

		// If block is in not in block range
		if (!(event.getBlock().getLocation().getY() >= this.getGame().getBlockVoidMin() &&
			event.getBlock().getLocation().getY() <= this.getGame().getBlockVoidMax())) {
			// Prevent player from placing the block
			event.setCancelled(true);
			// Send error
			event.getPlayer().sendMessage(ChatColor.RED + "You have reached build limit!");
			return;
		}

		// Otherwise, if not a special block
		// Add block to placed blocks list
		System.out.println("placed");
	}

	@EventHandler
	public void onDestroy(BlockBreakEvent event) {
		if (this.verifyState(event)) return;

		// Stop them from destroying the map
		event.setCancelled(true);
		// Don't set off anticheat
		Main.getMainHandler().getAnticheatHandler().revokeLeftClick(event.getPlayer());
	}

	@Override
	public CatchRunnable getGame() {
		return (CatchRunnable) this.getBaseGame();
	}
}
