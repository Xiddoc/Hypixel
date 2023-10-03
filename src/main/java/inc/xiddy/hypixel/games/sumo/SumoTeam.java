package inc.xiddy.hypixel.games.sumo;

import inc.xiddy.hypixel.constants.TeamColor;
import inc.xiddy.hypixel.dataclasses.GameMap;
import inc.xiddy.hypixel.games.basegame.HypixelTeam;

public class SumoTeam extends HypixelTeam {
	private final boolean seeker;

	public SumoTeam(TeamColor color, GameMap map, int teamSize) {
		// Super
		super(color, map, teamSize);
		// Get seeker
		this.seeker = seeker;
	}

	public boolean isSeeker() {
		return seeker;
	}
}
