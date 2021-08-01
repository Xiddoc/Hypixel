package inc.xiddy.Hypixel.Games.Bedwars;

import inc.xiddy.Hypixel.Dataclasses.GameEventHandler;
import inc.xiddy.Hypixel.Dataclasses.SmallLocation;
import inc.xiddy.Hypixel.HypixelUtils;
import inc.xiddy.Hypixel.Main;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.minecraft.server.v1_8_R3.EntityFireball;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFireball;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

@SuppressWarnings("deprecation")
public class BedwarsEventHandler extends GameEventHandler {
	private final BedwarsRunnable game;

	public BedwarsEventHandler(BedwarsRunnable game) {
		super(game.getLobby());
		this.game = game;
	}

	@EventHandler
	public void onNPCClick(NPCRightClickEvent event) {
		if (this.verifyState(event)) return;

		// Show them the shop
		event.getClicker().openInventory(this.getGame().getShop().getShopInventory());
	}

	@EventHandler
	public void onChestOpen(InventoryOpenEvent event) {
		if (this.verifyState(event)) return;

		// Get player
		Player player = (Player) event.getPlayer();
		// If player clicked on a chest
		// And the player is not at his base
		if (!this.getGame().isPlayerInBase(player, this.getGame().getPlayerTeam(player)) &&
			event.getInventory().getType().equals(InventoryType.CHEST)) {
			// Don't let them open the chest
			event.setCancelled(true);
			// Send error
			player.sendMessage(ChatColor.DARK_RED + "You can not open that chest!");
		}
	}

	@EventHandler
	public void onChestClick(InventoryClickEvent event) {
		if (this.verifyState(event)) return;

		// If an inventory was clicked (Dropping items)
		if (event.getClickedInventory() != null) {
			// If a shop was clicked
			if (event.getClickedInventory().getTitle().equals(this.getGame().getShop().getShopName())) {
				// If item is not AIR
				if (!event.getCurrentItem().getType().equals(Material.AIR)) {
					// Buy the clicked item
					this.getGame().getShop().buyItem(this.getGame(), (Player) event.getWhoClicked(), event.getCurrentItem().getType());
				}
				// Don't let them steal the item from the shop interface!
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onInventoryMoveItem(InventoryMoveItemEvent event) {
		if (this.verifyState(event)) return;

		// Get player
		Player player = (Player) event.getSource().getViewers().get(0);

		// If player clicked their inventory (to move item from their inventory to another)
		if (event.getSource().getType().equals(InventoryType.PLAYER)) {
			// If the item was a wood sword
			if (event.getItem().getType().equals(Material.WOOD_SWORD)) {
				// Don't let them chest their default sword
				event.setCancelled(true);
			} else if (event.getItem().getType().equals(Material.STONE_SWORD) ||
				event.getItem().getType().equals(Material.IRON_SWORD) ||
				event.getItem().getType().equals(Material.DIAMOND_SWORD)) {
				// If player chested their upgraded sword
				// Give them a default one
				player.getInventory().addItem(
					this.getGame().getBedwarsPlayerData(player).getBedwarsInventory().getHotbarItem("sword")
				);
			}
		}
		Main.getMainHandler().getLogger().error(event.getSource().getTitle());
		if (
			(event.getSource().getType().equals(InventoryType.CHEST) && event.getSource().getTitle().equals("Chest")) ||
			(event.getSource().getType().equals(InventoryType.ENDER_CHEST) && event.getSource().getTitle().equals("Ender Chest"))) {
			// If player clicked a chest (ender chest or team chest)
			// If clicked item is an upgraded sword
			// And they have a default sword
			Main.getMainHandler().getLogger().error(event.getSource().getTitle());
			if (player.getInventory().contains(Material.WOOD_SWORD) &&
				(event.getItem().getType().equals(Material.STONE_SWORD) ||
				event.getItem().getType().equals(Material.IRON_SWORD) ||
				event.getItem().getType().equals(Material.DIAMOND_SWORD))) {
				// Remove the default sword
				player.getInventory().remove(Material.WOOD_SWORD);
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (this.verifyState(event)) return;

		// Convert to player
		// If not player
		if (!(event.getEntity() instanceof Player) || CitizensAPI.getNPCRegistry().isNPC(event.getEntity())) {
			// Exit
			return;
		}
		// Otherwise,
		// Cast to player object
		Player player = (Player) event.getEntity();

		// If player was attacked (PVP)
		if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) &&
			player.getLastDamageCause() instanceof Player) {
			// Update the damager
			this.getGame().getBedwarsPlayerData(player).getLastDamage().updateDamager(
				// To the last person who damaged the player
				(Player) ((EntityDamageByEntityEvent) player.getLastDamageCause()).getDamager()
			);
		} else if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) ||
			event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
			// If player was exploded
			// Lower the damage
			event.setDamage(EntityDamageEvent.DamageModifier.ARMOR, 0);
			event.setDamage(2);
		}

		// If player is going to die (health - damage less or equal to 0)
		if (player.getHealth() - event.getFinalDamage() <= 0) {
			// Do not let them actually die
			event.setCancelled(true);
			// Turn them into a spectator
			this.getGame().setDeadSpectator(player);
		}
	}

	@EventHandler
	public void onEggHit(PlayerEggThrowEvent event) {
		if (this.verifyState(event)) return;

		// Make sure no chicks drop
		event.setHatching(false);
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		if (this.verifyState(event)) return;

		// If the player teleported due to an ender pearl
		if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
			// Don't teleport the player via pearl, teleport them manually (prevent fall damage)
			event.setCancelled(true);
			event.getPlayer().teleport(event.getTo());
			// Play sound to nearby players
			for (Entity entity: event.getPlayer().getWorld().getNearbyEntities(event.getTo(), 5, 5, 5)) {
				// If entity is a player
				if (entity instanceof Player) {
					// Play ender sound
					((Player) entity).playSound(entity.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
				}
			}
		}
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if (this.verifyState(event)) return;

		// If projectile is a snowball
		if (event.getEntity() instanceof Snowball) {
			// Get hit location
			Silverfish fish = (Silverfish) event.getEntity().getLocation().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.SILVERFISH);
			// Give them a bit more health
			fish.setMaxHealth(15);
			fish.setHealth(15);
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (this.verifyState(event)) return;

		// If player is going to die (health - damage less or equal to 0)
		if (event.getPlayer().getLocation().getY() < 0) {
			// Turn them into a spectator
			this.getGame().setDeadSpectator(event.getPlayer());
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (this.verifyState(event)) return;

		// If player put down tnt
		if (event.getBlock().getType().equals(Material.TNT)) {
			// Get block location
			Location blockLoc = event.getBlock().getLocation();
			// Remove block
			event.getBlock().setType(Material.AIR);
			// Summon TNT
			TNTPrimed tnt = (TNTPrimed) blockLoc.getWorld().spawnEntity(SmallLocation.center(blockLoc), EntityType.PRIMED_TNT);
			tnt.setFuseTicks(50);
		} else {
			// Otherwise, if not a special block
			// Add block to placed blocks list
			this.getGame().getPlacedBlocks().add(new SmallLocation(event.getBlock().getLocation()));
		}
	}

	@EventHandler
	public void onPlaceWater(PlayerBucketEmptyEvent event) {
		if (this.verifyState(event)) return;

		// Remove bucket from inventory
		Main.getMainHandler().getThreadHandler().scheduleSyncTask(() -> event.getPlayer().getInventory().remove(Material.BUCKET), 1);
	}

	@EventHandler
	public void onItemDrop(ItemSpawnEvent event) {
		if (this.verifyState(event)) return;

		// For each material that should not drop
		for (Material material: new Material[] {Material.BED, Material.SEEDS, Material.RED_ROSE, Material.TORCH}) {
			// If the material should not drop
			if (event.getEntity().getItemStack().getType().equals(material)) {
				// Don't drop it
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onDestroy(BlockBreakEvent event) {
		if (this.verifyState(event)) return;

		// If the block was a bed
		if (event.getBlock().getType().equals(Material.BED_BLOCK)) {
			// Get the team by the bed's location
			BedwarsTeam bedsTeam = this.getGame().getTeamByBedLocation(event.getBlock().getLocation());
			// Get the player's team
			BedwarsTeam playersTeam = this.getGame().getBedwarsPlayerData(event.getPlayer()).getTeam();

			// If somehow, no one's bed was broken or a bedless team was destroyed
			if (bedsTeam == null) {
				// Just return so nothing happens
				return;
			}

			// If they are equal, prevent the player from breaking his own bed
			if (bedsTeam.equals(playersTeam)) {
				// Stop the breaking
				event.setCancelled(true);
				// Send error
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "You can't break your own bed!");
				// Return to stop code execution
			} else {
				// Otherwise, destroy the bed!
				bedsTeam.setHasBed(false);
				// Start by informing the players
				for (Player player: this.getGame().getPlayers()) {
					// If on the broken bed team
					if (this.getGame().getBedwarsPlayerData(player).getTeam().equals(bedsTeam)) {
						// Credit any new deaths to the bed breaker
						this.getGame().getBedwarsPlayerData(player).getLastDamage().updateDamager(event.getPlayer());

						// Scary sound! Spooky.
						player.playSound(player.getLocation(), Sound.WITHER_DEATH, 1, 1);

						// Title card
						HypixelUtils.sendTitle(
							player,
							ChatColor.RED + "BED DESTROYED!",
							"You will no longer respawn!",
							2, 0.5, 0.5
						);

						// Otherwise, send them a non-personal message
						player.sendMessage(
							ChatColor.BOLD + "BED DESTRUCTION > " + ChatColor.RESET + ChatColor.GRAY +
							"Your bed was destroyed by " + playersTeam.getTeamColor().getColorCode() +
							event.getPlayer().getDisplayName() + ChatColor.GRAY + "!"
						);
					} else {
						// Otherwise,
						// Send them a non-personal message
						player.sendMessage(
							ChatColor.BOLD + "BED DESTRUCTION > " + ChatColor.RESET +
							bedsTeam.getTeamColor().getColorCode() + bedsTeam.getTeamColor().getCapitalizedString() +
							" Bed" + ChatColor.GRAY + " was destroyed by " + playersTeam.getTeamColor().getColorCode() +
							event.getPlayer().getDisplayName() + ChatColor.GRAY + "!"
						);

						// Scary sound! Spooky.
						player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
					}
				}

				// Summon lightning
				event.getBlock().getWorld().strikeLightningEffect(event.getBlock().getLocation());

				// Update scoreboard
				this.getGame().repaintScoreboardForAll();
			}
		} else {
			// Otherwise,
			// Check if it was a map block
			SmallLocation loc = new SmallLocation(event.getBlock().getLocation());
			if (this.getGame().getPlacedBlocks().contains(loc)) {
				// Remove the block from the placed blocks list, but let them destroy the block
				this.getGame().getPlacedBlocks().remove(loc);
			} else {
				// Otherwise,
				// Stop them from destroying the map
				event.setCancelled(true);
				// Don't set off anticheat
				Main.getMainHandler().getAnticheatHandler().revokeLeftClick(event.getPlayer());
			}
		}
	}

	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		if (this.verifyState(event)) return;

		// Set blast radius
		Main.getMainHandler().getLogger().error(event.getEntity().getType());
		double radius = 10;
		double strength = 4.0;
		// Init variables for each player
		Vector difVector;
		// For each player
		for (Entity entity: event.getLocation().getWorld().getNearbyEntities(event.getLocation(), radius, radius, radius)) {
			// Get difference vector of entity to explosion
			difVector = entity.getLocation().add(0.0D, 1.0D, 0.0D).toVector().subtract(event.getEntity().getLocation().toVector());
			double length = difVector.length();
			difVector = difVector.normalize();
			difVector.multiply(strength / length);
			// If entity within the radius
			// AND the difference vector is NOT on the same x position OR z position
			// AND they are not NPCs
			if (entity.getLocation().distance(event.getLocation()) < radius &&
				!(difVector.getX() == 0 || difVector.getZ() == 0) &&
				!CitizensAPI.getNPCRegistry().isNPC(entity)) {
				// Add velocity
				entity.setVelocity(entity.getVelocity().add(difVector.divide(new Vector(1, 5, 1))));
			}
		}

		// For each nearby block
		for (Block block: event.blockList().toArray(new Block[0])) {
			// If glass nearby
			if (block.getType().equals(Material.STAINED_GLASS)) {
				// Minimize damage
				if (Math.random() > 0.25) {
					event.blockList().remove((new Random()).nextInt(event.blockList().size()));
				}
			}
		}

		// For each block that is about to explode
		// If any blocks are protected
		// Or if they are stained-glass (blastproof glass)
		// Remove them from the list
		event.blockList().removeIf(block ->
			!this.getGame().getPlacedBlocks().contains(new SmallLocation(block.getLocation())) ||
				block.getType().equals(Material.STAINED_GLASS)
		);
	}

	@EventHandler
	public void onThrow(PlayerDropItemEvent event) {
		if (this.verifyState(event)) return;

		// Update metadata
		event.getItemDrop().setMetadata("ThrownItem", new FixedMetadataValue(Main.getInstance(), true));

		// If the player threw their default sword
		if (event.getItemDrop().getItemStack().getType().equals(Material.WOOD_SWORD)) {
			// Stop them
			event.setCancelled(true);
		} else if (event.getItemDrop().getItemStack().getType().equals(Material.STONE_SWORD) ||
			event.getItemDrop().getItemStack().getType().equals(Material.IRON_SWORD) ||
			event.getItemDrop().getItemStack().getType().equals(Material.DIAMOND_SWORD)) {
			// If player threw their upgraded sword
			// Give them a default one
			event.getPlayer().getInventory().addItem(
				this.getGame().getBedwarsPlayerData(event.getPlayer()).getBedwarsInventory().getHotbarItem("sword")
			);
		}
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent event) {
		if (this.verifyState(event)) return;

		// Get player
		Player player = event.getPlayer();

		// If the player has a wood sword
		// And they just picked up a better sword
		if (player.getInventory().contains(Material.WOOD_SWORD) &&
			(event.getItem().getItemStack().getType().equals(Material.STONE_SWORD) ||
				event.getItem().getItemStack().getType().equals(Material.IRON_SWORD) ||
				event.getItem().getItemStack().getType().equals(Material.DIAMOND_SWORD))) {
			// Remove the wood sword
			player.getInventory().remove(Material.WOOD_SWORD);
		}

		// If player is picking up gold or iron
		if (event.getItem().getItemStack().getType().equals(Material.IRON_INGOT) ||
			event.getItem().getItemStack().getType().equals(Material.GOLD_INGOT)) {
			// If the items were not thrown
			if (!event.getItem().hasMetadata("ItemThrown")) {
				// Then you should split them
				// For player in teammates
				for (Player teammate : this.getGame().getPlayerTeam(event.getPlayer()).getPlayers()) {
					// If the player is not the original player
					// And the player is nearby the original player
					if (!player.equals(teammate) &&
						player.getLocation().distance(teammate.getLocation()) < 2) {
						// Share the resources with that teammate
						teammate.getInventory().addItem(event.getItem().getItemStack());
						// Play sound
						teammate.playSound(teammate.getLocation(), Sound.ITEM_PICKUP, 1, 1);
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (this.verifyState(event)) return;

		// If player right-clicked
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			// If player is holding a fireball
			if (event.getPlayer().getItemInHand().getType().equals(Material.FIREBALL)) {
				// Don't let them place the fireball
				event.setCancelled(true);

				// If player can shoot fireball
				if (System.currentTimeMillis() > this.getGame().getBedwarsPlayerData(event.getPlayer()).getLastFireball() + 500) {
					// Timestamp fireball
					this.getGame().getBedwarsPlayerData(event.getPlayer()).timestampLastFireball();

					// Shoot custom fireball
					Fireball fireball = event.getPlayer().launchProjectile(Fireball.class);

					// Get NMS handle
					EntityFireball nms = ((CraftFireball) fireball).getHandle();
					// Get direction
					Vector dir = event.getPlayer().getEyeLocation().getDirection().multiply(10);
					// Do some math on the direction
					double x = dir.getX();
					double y = dir.getY();
					double z = dir.getZ();
					double length = Math.sqrt(x * x + y * y + z * z);
					// Set the direction to the fireball
					nms.dirX = x / length * 0.1D;
					nms.dirY = y / length * 0.1D;
					nms.dirZ = z / length * 0.1D;

					// Set speed
					fireball.setVelocity(fireball.getDirection().multiply(5));

					// Make fire
					fireball.setIsIncendiary(true);

					// Remove 1 fireball from hand
					event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);

					// Give the player slowness
					event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 1, false, false));
				} else {
					// Send error
					event.getPlayer().sendMessage(ChatColor.RED + "Please wait 0.5s to use that again");
				}
			}
		}
	}

	@EventHandler
	public void onLaunch(ProjectileLaunchEvent event) {
		if (this.verifyState(event)) return;

		// If player shot the item
		if (event.getEntity().getShooter() instanceof Player) {
			// Get player
			Player player = (Player) event.getEntity().getShooter();

			// If item is a fireball
			if (player.getItemInHand().getType().equals(Material.FIREBALL)) {
//				// Don't let them shoot it
//				event.setCancelled(true);
				// Steal the fireball
				player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
			} else if (player.getItemInHand().getType().equals(Material.EGG)) {
				// If item is an egg
				// Make blocks behind bridge egg
				new BukkitRunnable() {
					private final Egg egg = (Egg) event.getEntity();
					private final byte woolData = getGame().getPlayerTeam(player).getTeamColor().getDyeColor().getWoolData();
					private int ticks = 0;

					@Override
					public void run() {
						// Increment ticks
						this.ticks ++;

						// If egg has travelled for a while
						if (this.ticks > 100) {
							// Stop the async task
							this.cancel();
						} else {
							// Synchronously
							Main.getMainHandler().getThreadHandler().scheduleSyncTask(() -> {
								// Make bridge
								this.egg.getLocation().getBlock().setType(Material.WOOL);
								this.egg.getLocation().getBlock().setData(this.woolData);
							}, 50L);
						}
					}
				}.runTaskTimerAsynchronously(Main.getInstance(), 1, 1);
			}
		}

	}

	@EventHandler
	public void onPlayerEat(PlayerItemConsumeEvent event) {
		if (this.verifyState(event)) return;

		// If player is drinking milk
		if (event.getPlayer().getItemInHand().getType().equals(Material.MILK_BUCKET)) {
			// Stop them from actually drinking the milk (don't let them remove potion effects)
			event.setCancelled(true);
			// Remove the milk bucket
			event.getPlayer().getInventory().remove(event.getPlayer().getItemInHand());
//			// Remove bucket from inventory
//			Main.getMainHandler().getThreadHandler().scheduleSyncTask(() -> {
//				// For each item in the inventory
//				for (ItemStack item: event.getPlayer().getInventory().getContents()) {
//					// If item is a milk bucket
//					if (item.getType().equals(Material.MILK_BUCKET)) {
//						item.setAmount(0);
//						return;
//					}
//				}
//			}, 1);
			// Give player saturation as magic milk timer (30 seconds)
			event.getPlayer().addPotionEffect(
				new PotionEffect(PotionEffectType.SATURATION, 600, 1, false, false)
			);
		}
	}

	public BedwarsRunnable getGame() {
		return game;
	}
}
