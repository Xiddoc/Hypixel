package inc.xiddy.hypixel.games.bedwars.generator;

import org.bukkit.Material;

public class BedwarsGeneratorType {
	private final int rate;
	private final Material resource;
	private final GeneratorLocation location;
	private final int genCap;

	public BedwarsGeneratorType(Material resource, int rate, GeneratorLocation location, int genCap) {
		this.rate = rate;
		this.resource = resource;
		this.location = location;
		this.genCap = genCap;
	}

	public int getRate() {
		return rate;
	}

	public Material getResource() {
		return resource;
	}

	public GeneratorLocation getLocation() {
		return location;
	}

	public int getGenCap() {
		return genCap;
	}
}
