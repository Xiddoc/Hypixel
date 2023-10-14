package inc.xiddy.hypixel.server;

import inc.xiddy.hypixel.Main;

public class Tasks {
	public static void runSyncTask(Runnable task) {
		Main.getInstance().getServer().getScheduler().runTask(Main.getInstance(), task);
	}

	public static void scheduleSyncTask(Runnable task, double seconds) {
		// Returns task ID
		scheduleSyncTask(task, (int) (seconds * 20L));
	}

	public static void scheduleSyncTask(Runnable task, long ticks) {
		// Returns task ID
		Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), task, ticks);
	}
}
