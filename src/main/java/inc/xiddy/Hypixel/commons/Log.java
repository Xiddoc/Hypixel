package inc.xiddy.hypixel.commons;

import inc.xiddy.hypixel.constants.CColor;

public class Log {
	public static <T> void error(T message) {
		log(CColor.RED, message);
	}

	public static <T> void warning(T message) {
		log(CColor.YELLOW, message);
	}

	public static <T> void success(T message) {
		log(CColor.GREEN, message);
	}

	public static <T> void log(CColor prefix, T message) {
		System.out.println(
			CColor.RESET + "" + prefix +
				(message == null ? null : message.toString()) +
				CColor.RESET
		);
	}
}
