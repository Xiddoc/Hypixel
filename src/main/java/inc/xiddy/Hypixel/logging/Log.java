package inc.xiddy.hypixel.logging;

public class Log {
	public static <T> void error(T message) {
		log(ConsoleColor.RED, message);
	}

	public static <T> void warning(T message) {
		log(ConsoleColor.YELLOW, message);
	}

	public static <T> void info(T message) {
		log(ConsoleColor.BLUE_BRIGHT, message);
	}

	public static <T> void success(T message) {
		log(ConsoleColor.GREEN, message);
	}

	private static <T> void log(ConsoleColor prefix, T message) {
		System.out.println(
			ConsoleColor.RESET + "" + prefix +
				(message == null ? null : message.toString()) +
				ConsoleColor.RESET
		);
	}
}
