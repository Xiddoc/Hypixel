package inc.xiddy.Hypixel.Dataclasses;

import inc.xiddy.Hypixel.Main;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class HypixelTimer extends BukkitRunnable {
	private int elapsedTime = 0;

	public HypixelTimer(int tickDelay) {
		// Start this task
		this.runTaskTimerAsynchronously(Main.getInstance(), tickDelay, 20);
	}

	public abstract void onLoop();

	@Override
	public void run() {
		// Execute 'on loop' function
		this.onLoop();
		// Increment time by 1 second
		this.elapsedTime ++;
	}

	public int getElapsedTime() {
		return elapsedTime;
	}

}
