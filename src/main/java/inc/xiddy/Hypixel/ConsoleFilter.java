package inc.xiddy.Hypixel;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

import java.util.ArrayList;
import java.util.List;

public class ConsoleFilter implements Filter {
	private final List<String> stringList;

	public ConsoleFilter() {
		// Init string list
		this.stringList = new ArrayList<>();
		this.stringList.add("Preparing start region for level");
		this.stringList.add("Title command successfully executed");
	}

	public Result checkMessage(String message) {
		// Filter check
		for (String string : this.stringList) {
			// Make sure message is not null
			if (message != null) {
				// If message contains the string
				if (message.contains(string)) {
					return Result.DENY;
				}
			}
		}
		// Otherwise
		return Result.ACCEPT;
	}

	@Override
	public Result getOnMismatch() {
		return Result.NEUTRAL;
	}

	@Override
	public Result getOnMatch() {
		return Result.NEUTRAL;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
		return this.checkMessage(msg);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
		return this.checkMessage(msg.toString());
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
		return this.checkMessage(msg.getFormattedMessage());
	}

	@Override
	public Result filter(LogEvent event) {
		return this.checkMessage(event.getMessage().getFormattedMessage());
	}
}
