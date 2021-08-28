package inc.xiddy.Hypixel.Dataclasses;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@SuppressWarnings("unused")
public class SmallLocation {
	private double x;
	private double y;
	private double z;
	private float pitch;
	private float yaw;

	public SmallLocation() {
		super();
	}

	public SmallLocation(SmallLocation other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		this.pitch = other.pitch;
		this.yaw = other.yaw;
	}

	public SmallLocation(double x, double y, double z, float pitch, float yaw) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	public SmallLocation(Location location) {
		// Set each value
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
	}

	public static SmallLocation center(SmallLocation location) {
		// Propagate to other static method
		return new SmallLocation(center(location.toLocation()));
	}

	public static Location center(Location location) {
		// Do not destroy old instance
		Location copy = location.clone();

		// Change fields
		copy.setX((int) copy.getX() + 0.5);
		copy.setY((int) copy.getY() + 0.5);
		copy.setZ((int) copy.getZ() + 0.5);
		copy.setPitch(0);
		copy.setYaw(Math.round(copy.getYaw() / 90) * 90);

		// Return new instance
		return copy;
	}

	public static SmallLocation floor(SmallLocation location) {
		// Propagate to other static method
		return new SmallLocation(floor(location.toLocation()));
	}

	public static Location floor(Location location) {
		// Do not destroy old instance
		Location copy = location.clone();

		// Change fields
		copy.setX((int) copy.getX());
		copy.setY((int) copy.getY());
		copy.setZ((int) copy.getZ());
		copy.setPitch(0);
		copy.setYaw(0);

		// Return new instance
		return copy;
	}

	public Location toLocation(World world) {
		return new Location(world, this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
	}

	public Location toLocation(String worldName) {
		return this.toLocation(Bukkit.getWorld(worldName));
	}

	public Location toLocation() {
		return this.toLocation((World) null);
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	@Override
	public boolean equals(Object o) {
		// Null / class checks
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		// Cast and floor
		SmallLocation thatFloored = floor((SmallLocation) o);
		SmallLocation thisFloored = floor(this);
		// Check fields
		return Double.compare(thatFloored.getX(), thisFloored.getX()) == 0 &&
			Double.compare(thatFloored.getY(), thisFloored.getY()) == 0 &&
			Double.compare(thatFloored.getZ(), thisFloored.getZ()) == 0;
	}
}
