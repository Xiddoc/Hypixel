package inc.xiddy.Hypixel.Handlers;

import inc.xiddy.Hypixel.Constants.CColor;

public class LoggerHandler {

	public LoggerHandler() {}

	public <T> void log(CColor prefix, T message) {
		System.out.println(
			CColor.RESET + "" + prefix +
				(message == null ? null : message.toString()) +
			CColor.RESET
		);
	}

	public <T> void error(T message) {
		this.log(CColor.RED, message);
	}

	public <T> void warning(T message) {
		this.log(CColor.YELLOW, message);
	}

	public <T> void success(T message) {
		this.log(CColor.GREEN, message);
	}

	public <T> void text(T message) {
		this.log(CColor.WHITE, message);
	}
}
