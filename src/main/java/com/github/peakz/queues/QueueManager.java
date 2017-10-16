package com.github.peakz.queues;

import com.github.peakz.DAO.PlayerObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

import java.util.ArrayList;

public class QueueManager {
	private QueueHelper queueHelper = new QueueHelper();
	private IGuild guild;
	private IChannel channel;

	private ArrayList<PlayerObject> temp_team_red = new ArrayList<>();
	private ArrayList<PlayerObject> temp_team_blue = new ArrayList<>();

	public QueueManager(IGuild guild){
		this.guild = guild;
	}

	public ArrayList<PlayerObject> getTemp_team_red() {
		return temp_team_red;
	}

	public void setTemp_team_red(ArrayList<PlayerObject> temp_team_red) {
		this.temp_team_red = temp_team_red;
	}

	public ArrayList<PlayerObject> getTemp_team_blue() {
		return temp_team_blue;
	}

	public void setTemp_team_blue(ArrayList<PlayerObject> temp_team_blue) {
		this.temp_team_blue = temp_team_blue;
	}

	public QueueHelper getQueueHelper() {
		return queueHelper;
	}

	public void setQueueHelper(QueueHelper queueHelper) {
		this.queueHelper = queueHelper;
	}

	public IChannel getChannel() {
		return channel;
	}

	public void setChannel(IChannel channel) {
		this.channel = channel;
	}
}
