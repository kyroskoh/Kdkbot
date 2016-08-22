package kdkbot.commands.stats;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import kdkbot.Kdkbot;
import kdkbot.MessageInfo;
import kdkbot.channel.UserStat;

public class Stats {
	private String channel;
	
	public Stats(String channel) {
		this.channel = channel;
	}
	
	public void executeCommand(MessageInfo info) {
		String[] args = info.getSegments();
		String subCmd = args[1];
		UserStat userstat = Kdkbot.instance.getChannel(channel).stats.userStats.get(info.sender);
		if(userstat == null) {
			failedToFind(info);
			return;
		}
		
		switch(subCmd) {
			case "time":
				if(userstat.firstJoin == 0) {
					Kdkbot.instance.sendMessage(channel, info.sender + ": You have yet to spend enough time here to have been tracked!");
				} else {
					Kdkbot.instance.sendMessage(channel, info.sender + ": You have spent " + getDurationTime(userstat) + " since " + getFirstJoinDate(userstat));
				}
				break;
			case "msges":
				Kdkbot.instance.sendMessage(channel, info.sender +": You have sent " + getMessageCount(userstat) + " messages, containing " + getCharacterCount(userstat) + " characters.");			
				break;
			case "char":
				Kdkbot.instance.sendMessage(channel, info.sender + ": You have sent " + getCharacterCount(userstat) + " characters.");
				break;
			case "all":
				Kdkbot.instance.sendMessage(channel, info.sender +": You have spent " + getDurationTime(userstat) + " since " + getFirstJoinDate(userstat) + ", having sent " + getMessageCount(userstat) + " messages containing " + getCharacterCount(userstat) + " characters.");
				break;
		}
	}
	
	public void failedToFind(MessageInfo info) {
		Kdkbot.instance.sendMessage(channel, "Could not retriever user stats for " + info.sender);
	}

	public String getFirstJoinDate(UserStat user) {
		return unixToTimestamp(user.firstJoin, "d/M/y");
	}
	
	public String getMessageCount(UserStat user) {
		return String.valueOf(user.messageCount);
	}
	
	public long getMessageCountValue(UserStat user) {
		return user.messageCount;
	}
	
	public String getCharacterCount(UserStat user) {
		return String.valueOf(user.characterCount);
	}
	
	public long getCharacterCountValue(UserStat user) {
		return user.characterCount;
	}
	
	public String getLastJoinDate(UserStat user) {
		return unixToTimestamp(user.lastJoin, "d/M/y");
	}
	
	public String getDurationTime(UserStat user) {
		byte diffSeconds = (byte) (user.timeSpent / 1000 % 60);
		byte diffMinutes = (byte) (user.timeSpent / (60 * 1000) % 60);
		byte diffHours = (byte) (user.timeSpent / (60 * 60 * 1000) % 24);
		long diffDays = user.timeSpent / (24 * 60 * 60 * 1000);

		return diffDays + " days, " + diffHours + " hours, " + diffMinutes + " minutes and " + diffSeconds + " seconds";
	}
		
	/**
	 * Converts a given unix timestamp to a readable format, defaulted to the GMT-6 timezone
	 * @param value the unix timestamp
	 * @param format The format of the timestamp
	 * @return the formatted date, per /format/ param
	 */
	public String unixToTimestamp(long value, String format) {
		return unixToTimestamp(value, format, TimeZone.getTimeZone("GMT-6"));
	}
	
	/**
	 * Converts a given unix timestamp to a readable format
	 * @param value the unix timestamp
	 * @param format The format of the timestamp
	 * @param zone The timezone to format the date for
	 * @return the formatted date, per /format/ param
	 */
	public String unixToTimestamp(long value, String format, TimeZone zone) {
		Date date = new Date(value);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(zone);
		return sdf.format(date);
	}
}
