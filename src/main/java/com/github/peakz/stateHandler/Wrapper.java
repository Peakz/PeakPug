package com.github.peakz.stateHandler;

import com.github.peakz.PugBot;
import com.github.peakz.queues.QueueManager;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;

public class Wrapper implements WrapperInterface{

	@Override
	public void wrapGuilds(IDiscordClient client) {
		for(IGuild guild : client.getGuilds()){
			PugBot.queueManagers.put(guild, new QueueManager(guild));
		}
	}

	@Override
	public void wrapChannels() {

	}
}
