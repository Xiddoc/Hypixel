package inc.xiddy.hypixel.games.hide_n_seek;

import inc.xiddy.hypixel.constants.TeamColor;
import inc.xiddy.hypixel.dataclasses.GameMap;
import inc.xiddy.hypixel.games.basegame.HypixelTeam;

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
