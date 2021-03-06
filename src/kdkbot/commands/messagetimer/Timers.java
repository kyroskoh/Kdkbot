package kdkbot.commands.messagetimer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kdkbot.Kdkbot;
import kdkbot.MessageInfo;
import kdkbot.commands.Command;
import kdkbot.filemanager.Config;

public class Timers extends Command {
	private ArrayList<MessageTimer> timers;
	private Config cfg;
	private String channel;
	
	public Timers(String channel) {
		timers = new ArrayList<MessageTimer>();
		this.channel = channel;
		try {
			this.cfg = new Config("./cfg/" + channel + "/timers.cfg" );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadTimers() {
		try {
			List<String> lines = cfg.getConfigContents();
			Iterator<String> lineItero = lines.iterator();
			while(lineItero.hasNext()) {
				String line = lineItero.next();
				String args[] = line.split("\\|", 4);
				MessageTimer newTimer = new MessageTimer(channel, args[0], args[3], Long.parseLong(args[1]), args[2]);
				timers.add(newTimer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveTimers() {
		List<String> timerData = new ArrayList<String>();
		for(MessageTimer t : timers) {
			timerData.add(t.toString());
		}
		try {
			cfg.saveSettings(timerData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void executeCommand(MessageInfo info) {
		String[] args = info.getSegments();
		boolean found = false;
		if(args.length > 1) {
			switch(args[1]) {
				case "new":
					args = info.getSegments(5);
					MessageTimer newTimer = new MessageTimer(info.channel, args[2], args[4], Long.parseLong(args[3]));
					timers.add(newTimer);
					Kdkbot.instance.sendChanMessage(info.channel, "Added new timer with id " + args[2] + " with a delay of " + args[3] + " seconds and a message of " + args[4]);
					break;
				case "remove":
				case "delete":
					String timerID = args[2];
					// Lets find that timer!
					for(MessageTimer t : timers) {
						if(t.timerID.equalsIgnoreCase(timerID)) {
							t.stop();
							timers.remove(t);
							Kdkbot.instance.sendChanMessage(info.channel, "Deleted timer with id " + timerID);
							found = true;
							break;
						}
					}
					if(!found)
						Kdkbot.instance.sendChanMessage(info.channel, "Couldn't find timer with id " + timerID);
					break;
				case "edit":
					// Timers edit <id> <type> <newValue>
					args = info.getSegments(5); // Must limit the amount of contents, otherwise newValue could be wrong
					timerID = args[2];
					// Lets find that timer!
					Iterator<MessageTimer> iter = timers.iterator();
					while(iter.hasNext()) {
						MessageTimer t = iter.next();
						
						if(t.timerID.equalsIgnoreCase(timerID)) {
							// Timer found, need to stop it, create a new timer, and then add a new MessageTimer with the changed value
							t.stop();
							
							String newTimerID = t.timerID;
							String newMessage = t.message;
							long newDelay = t.delay;
							String newFlags = t.flags;
							
							
							switch(args[3].toLowerCase()) {
								case "msg":
									newMessage = args[4];
									Kdkbot.instance.sendChanMessage(info.channel, "Updated timer " + timerID + "'s message.");
									break;
								case "delay":
									newDelay = Long.parseLong(args[4]);
									Kdkbot.instance.sendChanMessage(info.channel, "Updated timer " + timerID + "'s delay to " + args[4] + " seconds.");
									break;
								case "flags":
									newFlags = args[4];
									Kdkbot.instance.sendChanMessage(info.channel, "Updated timer " + timerID + "'s flags.");
									break;
								case "needslive":
									t.flagsVals.REQUIRES_LIVE = Boolean.parseBoolean(args[4]);
									newFlags = t.flagsVals.toString();
									Kdkbot.instance.sendChanMessage(info.channel, "Updated timer " + timerID + "'s flag Needs Live setting to: " + String.valueOf(Boolean.parseBoolean(args[4])));
									break;
								case "msgcount":
									int msgCount = Integer.parseInt(args[4]);
									if(msgCount <= 0) {
										t.flagsVals.REQUIRES_MSG_COUNT = false;
										newFlags =t.flagsVals.toString();
										Kdkbot.instance.sendChanMessage(info.channel, "Updated timer " + timerID + "'s flag Message Count to no longer be required.");
									} else {
										t.flagsVals.REQUIRES_MSG_COUNT_AMT = args[4];
										newFlags = t.flagsVals.toString();
										Kdkbot.instance.sendChanMessage(info.channel, "Updated timer " + timerID + "'s flag Message Count to no longer be required.");
									}
									break;
							}

							iter.remove();
							timers.add(new MessageTimer(info.channel, newTimerID, newMessage, newDelay, newFlags)); // Add back the timer
							break;
						}
					}
					break;
			}
			this.saveTimers();
		}
	}
	
	
	/**
	 * Updates all timers message amount
	 */
	public void updateTimerMessageCounters() {
		for(MessageTimer t : timers) {
			t.incrementMessageAmount();
		}
	}
}
