package inc.xiddy.Hypixel.Games.Bedwars;

import inc.xiddy.Hypixel.Constants.Lobby;
import inc.xiddy.Hypixel.Constants.TeamColor;
import inc.xiddy.Hypixel.Dataclasses.GameState;
import inc.xiddy.Hypixel.Dataclasses.HypixelRunnable;
import inc.xiddy.Hypixel.Dataclasses.SmallLocation;
import inc.xiddy.Hypixel.Games.Bedwars.Generator.BedwarsGenerator;
import inc.xiddy.Hypixel.HypixelUtils;
import inc.xiddy.Hypixel.Main;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

import static inc.xiddy.Hypixel.Dataclasses.SmallLocation.floor;
import static org.bukkit.ChatColor.*;

public class BedwarsRunnable extends HypixelRunnable {
	private final int teamSize;
	private final List<BedwarsPlayerData> playerBedwarsDataList;
	private final BedwarsEventHandler eventHandler;
	private final List<SmallLocation> placedBlocks;
	private final List<NPC> npcList;
	private final BedwarsShop shop;
	private List<BedwarsTeam> teams;

	public BedwarsRunnable(Set<Player> players, BedwarsGame bedwarsGame, Lobby lobby, int teamSize) {
		super(players, bedwarsGame, lobby);

		// Set to fields
		this.teamSize = teamSize;
		this.placedBlocks = new ArrayList<>();
		this.playerBedwarsDataList = new ArrayList<>();
		this.eventHandler = new BedwarsEventHandler(this);
		// For each player
		for (Player player: players) {
			// Add to the data map
			this.playerBedwarsDataList.add(new BedwarsPlayerData(player));
		}
		this.npcList = new ArrayList<>();
		this.shop = new BedwarsShop();
	}

	@Override
	public void run() {
		// Start loading the game
		this.broadcastMessage(ChatColor.GREEN + "Loading Bedwars game...");

		// Make a new map
		try {
			this.generateMap();
		} catch (FileNotFoundException e) {
			// Print traceback
			e.printStackTrace();
			// Inform of error
			this.broadcastMessage(ChatColor.DARK_RED + "There was an error while creating the game...");
			// Stop game
			this.stopGame();
			return;
		}

		this.broadcastMessage(
			ChatColor.GREEN + "Starting Bedwars game on map " +
				ChatColor.GOLD + this.getMap().getCapitalizedMapName() +
				ChatColor.GREEN + "..."
		);

		// Make teams
		try {
			// Make new list
			List<BedwarsTeam> newTeams = new ArrayList<>();

			// Get teams
			int teamIdx = 0;
			// Make initial team
			BedwarsTeam team = new BedwarsTeam(TeamColor.values()[teamIdx], this.getMap(), this.getTeamSize());
			// For each player
			for (Player player : this.getPlayers()) {
				// If no space for new player, then make a new team
				if (team.isTeamFull()) {
					// Add the team to the list
					newTeams.add(team);
					// Make a new team
					// Increment team index
					teamIdx++;
					// Construct new object
					team = new BedwarsTeam(TeamColor.values()[teamIdx], this.getMap(), this.getTeamSize());
				}

				// Add player to team
				team.addPlayer(player);

				// Add player to data map
				this.getBedwarsPlayerData(player).setData(team);
			}

			// Add last team to the list
			newTeams.add(team);

			// Set the teams
			this.setTeams(newTeams);
		} catch (FileNotFoundException e) {
			// Print traceback
			e.printStackTrace();
			// Inform of error
			this.broadcastMessage(ChatColor.DARK_RED + "There was an error while creating the game...");
			// Stop game
			this.stopGame();
			return;
		}

		// Start the game sequence
		// For each player
		for (Player player : this.getPlayers()) {
			// Remove enderchest items
			player.getEnderChest().clear();

			// Set lobby mode
			// Move player to respawn location
			this.respawn(player);
		}

		// For each team
		List<Location> teamGenLocations = new ArrayList<>();
		for (BedwarsTeam team : teams) {
			// Populate the beds
			Main.getMainHandler().getThreadHandler().runSyncTask(team::generateBed);

			// Save gen location
			teamGenLocations.add(team.getGeneratorLocation());

			// Populate shops
			// Summon NPC (Random player from the team)
			NPC teamshopNPC = CitizensAPI.getNPCRegistry().createNPC(
				EntityType.PLAYER,
				team.getPlayers().toArray(new Player[0])[new Random().nextInt(team.getPlayers().size())].getDisplayName()
			);
			teamshopNPC.setAlwaysUseNameHologram(false);
			// Protect them
			teamshopNPC.setProtected(true);
			// Make them look at nearest player
			LookClose trait = new LookClose();
			trait.setRange(15);
			trait.toggle();
			teamshopNPC.addTrait(trait);
			NPC itemshopNPC = teamshopNPC.clone();
			// Add to NPC list
			this.getNPCs().add(teamshopNPC);
			this.getNPCs().add(itemshopNPC);
			// Async spawn
			Main.getMainHandler().getThreadHandler().runSyncTask(() -> {
				// Summon them at the shop
				teamshopNPC.spawn(team.getTeamshopLocation());
				itemshopNPC.spawn(team.getItemshopLocation());
			});
		}

//		// Clean up dead beds
//		for (BedwarsTeam team: this.getTeams()) {
//			// Check elimination
//			this.checkTeamElimination(team);
//		}

		// Repaint scoreboard
		this.repaintScoreboardForAll();

		// Before starting the generators
		// Add the diamond generators to the list
		List<Location> diamondGenLocations = new ArrayList<>();
		try {
			for (int i = 0; i < 4; i++) {
				// Get the location for the marker
				Location loc = Main.getMainHandler().getDataHandler().read(this.getMap().getPathToMapGlobals() + "\\diamondgen" + (i + 1) + ".json", SmallLocation.class).toLocation();
				// Update the world
				loc.setWorld(this.getMap().getWorld());
				// Add location to generators
				diamondGenLocations.add(loc);
			}
		} catch (FileNotFoundException ignored) {
		}

		// Add the emerald generators to the list
		List<Location> emeraldGenLocations = new ArrayList<>();
		try {
			for (int i = 0; i < 4; i++) {
				// Get the location for the marker
				Location loc = Main.getMainHandler().getDataHandler().read(this.getMap().getPathToMapGlobals() + "\\emeraldgen" + (i + 1) + ".json", SmallLocation.class).toLocation();
				// Update the world
				loc.setWorld(this.getMap().getWorld());
				// Add location to generators
				emeraldGenLocations.add(loc);
			}
		} catch (FileNotFoundException ignored) {
		}

		// Start generators
		BedwarsGenerator generator = new BedwarsGenerator(teamGenLocations, diamondGenLocations, emeraldGenLocations);
		// Run asynchronously once every 5 ticks (4 times per second)
		generator.runTaskTimerAsynchronously(Main.getInstance(), 0, 5);
	}

	private void respawn(Player player) {
		// Update state
		this.getPlayerTeam(player).setPlayerState(player, GameState.RESPAWNING);
		// Synchronously respawn them
		Main.getMainHandler().getThreadHandler().runSyncTask(() -> {
			// Teleport to respawn location
			player.teleport(this.getBedwarsPlayerData(player).getTeam().getRespawnLocation());

			// Set mode
			Main.getMainHandler().getPlayerHandler().getPlayerData(player).setLobby(this.getLobby());

			// Give tools
			for (Map.Entry<String, ItemStack> item : this.getBedwarsPlayerData(player).getBedwarsInventory().getHotbar().entrySet()) {
				player.getInventory().addItem(item.getValue());
			}

			// Give clothes
			player.getInventory().setArmorContents(this.getBedwarsPlayerData(player).getBedwarsInventory().getArmor());
		});
	}

	public void setDeadSpectator(Player player) {
		// Announce death
		// If the last hit was within 10 seconds of the death
		if (this.getBedwarsPlayerData(player).getLastDamage().getTimestamp() + 10 * 1000 > System.currentTimeMillis()) {
			// Set killer
			Player killer = this.getBedwarsPlayerData(player).getLastDamage().getDamager();

			// Accredit the death to the killer
			this.announceDeath(player, killer);

			// Play ding sound to indicate kill
			killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 1, 1);

			// Give player's items to killer
			BedwarsFunds funds = new BedwarsFunds(player);
			// If player had resources on them
			if (funds.hasAnyResources()) {
				// Give the item counts to the player
				killer.getInventory().addItem(
					funds.getIron() > 0 ? new ItemStack(Material.IRON_INGOT, funds.getIron()) : null,
					funds.getGold() > 0 ? new ItemStack(Material.GOLD_INGOT, funds.getGold()) : null,
					funds.getDiamond() > 0 ? new ItemStack(Material.DIAMOND, funds.getDiamond()) : null,
					funds.getEmerald() > 0 ? new ItemStack(Material.EMERALD, funds.getEmerald()) : null
				);
				// Inform them
				killer.sendMessage(
					(funds.getIron() > 0 ? BedwarsFunds.getResourceColor(Material.IRON_INGOT) + "+" + funds.getIron() + " Iron" : "") +
						(funds.getGold() > 0 ? BedwarsFunds.getResourceColor(Material.GOLD_INGOT) + "\n+" + funds.getGold() + " Gold" : "") +
						(funds.getDiamond() > 0 ? BedwarsFunds.getResourceColor(Material.DIAMOND) + "\n+" + funds.getDiamond() + " Diamond" : "") +
						(funds.getEmerald() > 0 ? BedwarsFunds.getResourceColor(Material.EMERALD) + "\n+" + funds.getEmerald() + " Emerald" : "")
				);
			}
		} else {
			// Player killed themselves
			this.announceDeath(player);
		}

		// Set lobby to spectator
		Main.getMainHandler().getPlayerHandler().getPlayerData(player).setLobby(Lobby.SPECTATOR);

		// Teleport them to their bed
		player.teleport(this.getPlayerTeam(player).getBedLocation().add(0, 10, 0));

		// If the player still has a bed
		if (this.getPlayerTeam(player).hasBed()) {
			// Update state
			this.getPlayerTeam(player).setPlayerState(player, GameState.RESPAWNING);
			// Asynchronously run
			new BukkitRunnable() {
				private final Player spectatorPlayer = player;
				private int timeLeft = 5;

				@Override
				public void run() {
					// If time is up
					if (timeLeft == 0) {
						// Clear title
						HypixelUtils.sendTitle(
							this.spectatorPlayer,
							"", "", 1, 1, 1
						);
						// Stop the runnable
						this.cancel();
						// Respawn player
						respawn(this.spectatorPlayer);
					} else {
						// Send title
						HypixelUtils.sendTitle(
							this.spectatorPlayer,
							ChatColor.RED + "YOU DIED!",
							ChatColor.YELLOW + "You will respawn in " + ChatColor.RED + this.timeLeft + ChatColor.YELLOW + " seconds!",
							20, 0, 0
						);
						// Decrement timer
						this.timeLeft--;
					}
				}
			}.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);
		} else {
			// Update state
			this.getPlayerTeam(player).setPlayerState(player, GameState.SPECTATING);
		}
	}

	private void broadcastMessage(String message) {
		// For each player
		for (Player player : this.getPlayers()) {
			// Send the message
			player.sendMessage(message);
		}
	}

	public BedwarsPlayerData getBedwarsPlayerData(Player player) {
		// For each data point
		for (BedwarsPlayerData data : this.getAllBedwarsPlayerData()) {
			// If the player matches the data point
			if (data.getPlayer().equals(player)) {
				// Return this point
				return data;
			}
		}
		// Otherwise, throw exception
		throw new RuntimeException();
	}

	public void announceDeath(Player victim) {
		this.announceDeath(victim, null);
	}

	public void announceDeath(Player victim, Player killer) {
		// Init message
		String message;
		BedwarsTeam victimTeam = this.getPlayerTeam(victim);

		// If he died without anyone killing him
		// Also make sure to add "FINAL KILL!" if necessary
		if (killer == null) {
			// Say that he died
			message = victimTeam.getTeamColor().getColorCode() + victim.getDisplayName() +
				ChatColor.GRAY + " died. " +
				(victimTeam.hasBed() ? "" : ChatColor.AQUA + "" + ChatColor.BOLD + "FINAL KILL!");
		} else {
			// Otherwise,
			// Add a bit about who killed them
			message = victimTeam.getTeamColor().getColorCode() + victim.getDisplayName() +
				ChatColor.GRAY + " was killed by " +
				this.getPlayerTeam(killer).getTeamColor().getColorCode() + killer.getDisplayName() +
				ChatColor.GRAY + ". " +
				(victimTeam.hasBed() ? "" : ChatColor.AQUA + "" + ChatColor.BOLD + "FINAL KILL!");
		}

		// Announce the message to everyone in the game
		this.broadcastMessage(message);

		// Check if the team still has any players left
		this.checkTeamElimination(victimTeam);
	}

	private void checkTeamElimination(BedwarsTeam team) {
		// Check if the team still has any players left
		if (team.isEliminated()) {
			// If everyone is dead
			this.broadcastMessage(
				ChatColor.BOLD + "\nTEAM ELIMINATED > " + ChatColor.RESET +
					team.getTeamColor().getColorCode() + team.getTeamColor().getCapitalizedString() +
					" Team" + ChatColor.RED + " has been eliminated!\n"
			);

			// Make sure bed is gone
			Main.getMainHandler().getThreadHandler().runSyncTask(team::removeBed);

			// Repaint scoreboard
			this.repaintScoreboardForAll();

			// Check if won
//			this.checkWinner();
		}
	}

	public void repaintScoreboardForAll() {
		// For each player
		for (Player player : this.getPlayers()) {
			// Repaint the board for them
			this.repaintScoreboard(player);
		}
	}

	public void repaintScoreboard(Player player) {
		StringBuilder str = new StringBuilder();
		// Start by making header
		str.append(YELLOW).append(BOLD).append("BEDWARS\n")
			.append(GRAY).append(new SimpleDateFormat("MM/dd/yy").format(new Date()))
			.append(DARK_GRAY).append(" m").append(this.getTaskId()).append("E")
			.append(WHITE).append("\n\nDiamond II in ").append(GREEN).append("0:00\n\n");
		// For each team
		for (BedwarsTeam team : this.getTeams()) {
			// Add team name to the scoreboard
			str.append(team.getTeamColor().getColorCode())
				.append(team.getTeamColor().getCapitalizedString().charAt(0))
				.append(" ").append(WHITE).append(team.getTeamColor().getCapitalizedString()).append(": ");
			// Ternary operator for displaying the team status
			str.append(team.hasBed() ? GREEN + "✔" : (team.isEliminated() ? RED + "✗" : GREEN + "" + team.getAlivePlayers().size()));
			// If this is the player's team
			if (team.getPlayers().contains(player)) {
				// Add "YOU" next to team
				str.append(GRAY).append(" YOU");
			}
			// Add newline for next iteration
			str.append("\n");
		}
		// Make footer
		str.append(WHITE).append("\nKills: ").append(GREEN).append("0\n")
			.append(WHITE).append("Final Kills: ").append(GREEN).append("0\n")
			.append(WHITE).append("Beds Broken: ").append(GREEN).append("0\n\n")
			.append(YELLOW).append("www.hypixel.net");
		// Update the lobby scoreboard
		Main.getMainHandler().getThreadHandler().scheduleSyncTask(
			() -> Main.getMainHandler().getPlayerHandler().getPlayerData(player).setScoreboard(str.toString()),
			1L
		);
	}

	private void checkWinner() {
		// Marker for "found one alive team"
		// If marker is null, we have not found an alive team yet
		// If marker is a team, then we have found one team alive
		// If by the end the marker is null... then somehow everyone is dead
		// If by the end the marker is a team, then a team has won!
		// If through parsing, a team is found alive, but the marker is already true,
		// Then return since there are multiple teams alive (game still in progress)
		Main.getMainHandler().getLogger().error("CHECK");
		BedwarsTeam aliveTeam = null;
		// For each team
		for (BedwarsTeam team : this.getTeams()) {
			// If team is still alive
			if (!team.isEliminated()) {
				// If the marker is already true
				if (aliveTeam != null) {
					// Exit
					return;
				} else {
					// Update the marker
					aliveTeam = team;
				}
			}
		}
		// Game over protocol
		// If there is a winner
		if (aliveTeam != null) {
			// Game over in their favor
			this.gameOver(aliveTeam.getPlayers());
		} else {
			// Somehow... everyone lost
			this.gameOver(new HashSet<>());
		}
	}

	public boolean isPlayerInBase(Player player, BedwarsTeam team) {
		// Get locations
		Location respawnLoc = team.getRespawnLocation();
		Location playerLoc = player.getLocation().clone();
		// Normalize Y values
		respawnLoc.setY(0);
		playerLoc.setY(0);
		// If the distance between the player
		// To the team's respawn point
		// Is less than 20 blocks
		// Then return true
		return respawnLoc.distance(playerLoc) < 20;
	}

	private int getTeamCount() {
		return (int) Math.ceil(((double) this.getPlayers().size()) / ((double) this.getTeamSize()));
	}

	public void stopGame() {
		// Kill all NPCs
		for (NPC npc : this.getNPCs()) {
			CitizensAPI.getNPCRegistry().deregister(npc);
		}

		// Stop game mechanics
		this.internalStopGame();

		// Synchronously destroy the map
		Main.getMainHandler().getThreadHandler().runSyncTask(this::destroyMap);
	}

	public BedwarsTeam getTeamByBedLocation(Location location) {
		// Get cleaned location
		Location loc = floor(location);
		// For each team
		for (BedwarsTeam team : this.getTeams()) {
			// Check if the location is equal to the current bed location or the location in front of it (bed head)
			if (loc.equals(floor(team.getBedLocations()[0])) ||
				loc.equals(floor(team.getBedLocations()[1]))) {
				// Return that team
				return team;
			}
		}
		// If no teams are applicable
		return null;
	}

	public List<BedwarsTeam> getTeams() {
		return teams;
	}

	private void setTeams(List<BedwarsTeam> teams) {
		this.teams = teams;
	}

	private int getTeamSize() {
		return teamSize;
	}

	public List<BedwarsPlayerData> getAllBedwarsPlayerData() {
		return this.playerBedwarsDataList;
	}

	public List<NPC> getNPCs() {
		return this.npcList;
	}

	public BedwarsTeam getPlayerTeam(Player player) {
		return this.getBedwarsPlayerData(player).getTeam();
	}

	public BedwarsEventHandler getEventHandler() {
		return eventHandler;
	}

	public List<SmallLocation> getPlacedBlocks() {
		return placedBlocks;
	}

	public void addPlacedBlock(SmallLocation block) {
		this.getPlacedBlocks().add(block);
	}

	public void removePlacedBlock(SmallLocation block) {
		this.getPlacedBlocks().remove(block);
	}

	public boolean isPlacedBlock(SmallLocation block) {
		return this.getPlacedBlocks().contains(block);
	}

	public BedwarsShop getShop() {
		return this.shop;
	}

	public int getPlayerVoid() {
		return 0;
	}

	public int getBlockVoidMin() {
		return 70;
	}

	public int getBlockVoidMax() {
		return 130;
	}
}
