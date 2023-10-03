package inc.xiddy.Hypixel.Games.Catch;

import inc.xiddy.Hypixel.Constants.TeamColor;
import inc.xiddy.Hypixel.Dataclasses.GameMap;
import inc.xiddy.Hypixel.Games.BaseGame.HypixelTeam;

public class CatchTeam extends HypixelTeam {
	private final boolean seeker;

	public CatchTeam(TeamColor color, GameMap map, int teamSize, boolean seeker) {
		// Super
		super(color, map, teamSize);
		// Get seeker
		this.seeker = seeker;
	}

	public boolean isSeeker() {
		return seeker;
	}
}
