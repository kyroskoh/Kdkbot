package kdkbot.channel;

import kdkbot.*;
import kdkbot.commands.*;

public class Channel {
	private static Kdkbot KDKBOT;
	public Commands commands;
	private String channel;
	
	public Channel() {
		// Do nothing
	}
	
	/**
	 * Constructs a new channel instance
	 * @param instance the instance of the bot to be assigned for this channel
	 * @param channel the channel name in which to join and reign control over
	 */
	public Channel(Kdkbot instance, String channel) {
		this.KDKBOT = instance;
		this.channel = channel;
		this.commands = new Commands(instance, channel);
		this.commands.loadSenderRanks();
		this.joinChannel();
	}
	
	public void joinChannel() {
		this.KDKBOT.joinChannel(this.channel);
	}
	
	public void leaveChannel() {
		this.KDKBOT.partChannel(this.channel);
	}
	
	public String getChannel() {
		return this.channel;
	}
	
	public void leaveChannel(String reason) {
		this.KDKBOT.partChannel(this.channel, reason);
	}
	
	public void kickUser(String nick) {
		this.KDKBOT.kick(this.channel, nick);
	}
	
	public void kickUser(String nick, String reason) {
		this.KDKBOT.kick(this.channel, nick, reason);
	}

}
