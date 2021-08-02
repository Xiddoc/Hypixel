package inc.xiddy.Hypixel.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardHandler {
	public ScoreboardHandler() {
	}

	public void setScoreboard(Player player, String text) {
		// Split by newlines
		String[] lines = text.split("\n");
		// Make a new scoreboard
		Scoreboard scoreBoard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = scoreBoard.registerNewObjective(lines[0], "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		// For each line
		int newLineLength = 1;
		for (int i = 1; i < lines.length; i++) {
			// Split by newline
			if (lines[i].equals("")) {
				lines[i] = new String(new char[newLineLength]).replace("\0", " ");
				newLineLength++;
			}
			// Generate a score, then set the line number
			obj.getScore(lines[i]).setScore(lines.length - i);
		}

		// Set the scoreboard to the player
		player.setScoreboard(scoreBoard);
	}
}
