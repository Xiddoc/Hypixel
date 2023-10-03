package inc.xiddy.Hypixel.Games.Sumo;

import inc.xiddy.Hypixel.Constants.TeamColor;
import inc.xiddy.Hypixel.Dataclasses.GameMap;
import inc.xiddy.Hypixel.Games.BaseGame.HypixelTeam;

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
