package inc.xiddy.Hypixel.Handlers;

import inc.xiddy.Hypixel.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class ThreadHandler {
	public ThreadHandler() {}

	public BukkitTask runSyncTask(Runnable task) {
		return Main.getInstance().getServer().getScheduler().runTask(Main.getInstance(), task);
	}

	public boolean isTaskRunning(BukkitTask task) {
		// For each task
		for (BukkitTask bukkitTask: Bukkit.getScheduler().getPendingTasks()) {
			// If the bukkit task has the same ID as the given task
			if (bukkitTask.getTaskId() == task.getTaskId()) {
				// Return true
				return true;
			}
		}
		// Otherwise, return false if no match found
		return false;
	}

	public void pendSyncTask(Runnable task) {
		BukkitTask bukkitTask = this.runSyncTask(task);
		//noinspection StatementWithEmptyBody
		while (this.isTaskRunning(bukkitTask)) {}
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
