package inc.xiddy.Hypixel.Handlers;

import inc.xiddy.Hypixel.Main;

public class ThreadHandler {
	public ThreadHandler() {}

	public void runSyncTask(Runnable task) {
		Main.getInstance().getServer().getScheduler().runTask(Main.getInstance(), task);
//		this.scheduleSyncTask(task, 1);
	}

	public void scheduleSyncTask(Runnable task, double seconds) {
		// Returns task ID
		this.scheduleSyncTask(task, (int) (seconds * 20L));
	}

	public void scheduleSyncTask(Runnable task, long ticks) {
		// Returns task ID
		Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), task, ticks);
	}
}
