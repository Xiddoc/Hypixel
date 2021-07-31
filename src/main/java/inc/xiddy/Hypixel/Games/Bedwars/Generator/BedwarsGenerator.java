package inc.xiddy.Hypixel.Games.Bedwars.Generator;

import inc.xiddy.Hypixel.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class BedwarsGenerator extends BukkitRunnable {
	private final List<Location> teamGenLocations;
	private final List<Location> diamondGenLocations;
	private final List<Location> emeraldGenLocations;
	private final List<BedwarsGeneratorType> generatorTypes;
	private int tick = 0;

	public BedwarsGenerator(List<Location> teamGenLocations, List<Location> diamondGenLocations, List<Location> emeraldGenLocations) {
		// Make maps
		this.teamGenLocations = teamGenLocations;
		this.diamondGenLocations = diamondGenLocations;
		this.emeraldGenLocations = emeraldGenLocations;
		this.generatorTypes = new ArrayList<>();

		// For each resource, assign a generation duration
		this.generatorTypes.add(new BedwarsGeneratorType(Material.IRON_INGOT, 6, GeneratorLocation.TEAMS, 50));
		this.generatorTypes.add(new BedwarsGeneratorType(Material.GOLD_INGOT, 19, GeneratorLocation.TEAMS, 15));
		this.generatorTypes.add(new BedwarsGeneratorType(Material.DIAMOND, 100, GeneratorLocation.DIAMONDS, 4));
		this.generatorTypes.add(new BedwarsGeneratorType(Material.EMERALD, 100, GeneratorLocation.EMERALDS, 2));
	}

	@Override
	public void run() {
		// This function runs 4 times per second
		// Increment the tick
		this.incrementTicks();

		// For each generated resource
		for (BedwarsGeneratorType resource: this.getGeneratorTypes()) {
			// If this is the right tick
			if (this.getTick() % resource.getRate() == 0) {

				// Switch operator for the gen type
				switch (resource.getLocation()) {

					// Team generator
					case TEAMS: {
						// For each team generator
						for (Location gen : this.getTeamGenLocations()) {
							// Drop resource at gen
							this.dropResourceAtGen(resource, gen);
						}
						break;
					}

					case DIAMONDS: {
						// For each diamond generator
						for (Location gen : this.getDiamondGenLocations()) {
							// Drop resource at gen
							this.dropResourceAtGen(resource, gen);
						}
						break;
					}

					case EMERALDS: {
						// For each emerald generator
						for (Location gen : this.getEmeraldGenLocations()) {
							// Drop resource at gen
							this.dropResourceAtGen(resource, gen);
						}
						break;
					}
				}
			}
		}

	}

	private void dropResourceAtGen(BedwarsGeneratorType resource, Location gen) {
		// If gen is not full
		if (this.getDroppedMaterialAtLocation(resource.getResource(), gen) < resource.getGenCap()) {
			// Synchronously
			Main.getMainHandler().getThreadHandler().runSyncTask(
				// Drop item into the gen
				() -> gen.getWorld().dropItem(gen, new ItemStack(resource.getResource()))
			);
		}
	}

	private int getDroppedMaterialAtLocation(Material material, Location location) {
		// Get total nearby items
		int items = 0;
		ItemStack itemStack;
		// For each nearby entity
		for (Entity entity : location.getWorld().getNearbyEntities(location, 1.5, 1.5, 1.5)) {
			// If the entity is items
			if (entity.getType().equals(EntityType.DROPPED_ITEM)) {
				itemStack = ((Item) entity).getItemStack();
				// If the item is of the material
				if (itemStack.getType().equals(material)) {
					items += itemStack.getAmount();
				}
			}
		}
		// Return it
		return items;
	}

	private void incrementTicks() {
		this.tick ++;
	}

	private int getTick() {
		return this.tick;
	}

	public List<Location> getTeamGenLocations() {
		return teamGenLocations;
	}

	public List<BedwarsGeneratorType> getGeneratorTypes() {
		return generatorTypes;
	}

	public List<Location> getDiamondGenLocations() {
		return diamondGenLocations;
	}

	public List<Location> getEmeraldGenLocations() {
		return emeraldGenLocations;
	}
}
