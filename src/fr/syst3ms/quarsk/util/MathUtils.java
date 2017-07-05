package fr.syst3ms.quarsk.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ARTHUR on 22/02/2017.
 */
public final class MathUtils {
	private static final double RAD_TO_DEG = 180 / Math.PI;

	//All of the below functions are by bi0qaw cause I honestly can't do that kind of math. Thanks, I guess.
	@NotNull
	public static Vector vectorFromLocations(@NotNull Location from, @NotNull Location to) {
		return new Vector(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ());
	}

	public static float getYaw(@NotNull Vector vector) {
		if (((Double) vector.getX()).equals((double) 0) && ((Double) vector.getZ()).equals((double) 0)) {
			return 0;
		}
		return (float) (Math.atan2(vector.getZ(), vector.getX()) * RAD_TO_DEG);
	}

	public static float getPitch(@NotNull Vector vector) {
		double xy = Math.sqrt(vector.getX() * vector.getX() + vector.getZ() * vector.getZ());
		return (float) (Math.atan(vector.getY() / xy) * RAD_TO_DEG);
	}

	public static float notchYaw(float yaw) {
		float y = yaw - 90;
		if (y < -180) {
			y += 360;
		}
		return y;
	}

	public static float notchPitch(float pitch) {
		return -pitch;
	}
}
