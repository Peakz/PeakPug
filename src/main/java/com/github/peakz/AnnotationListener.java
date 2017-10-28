package com.github.peakz;

import com.github.peakz.queues.QueueManager;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.ChannelCreateEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class AnnotationListener {

	@EventSubscriber
	public void onReadyEvent(ReadyEvent event) {
		for (IGuild guild : event.getClient().getGuilds()) {
			for (IChannel channel : guild.getChannels()) {
				if (channel.getName().contains("pug")) {
					QueueManager queueManager = new QueueManager(channel);
					queueManager.addQueueHelperInstances();
					PugBot.queueInstances.put(channel, queueManager);
				}
			}
		}
	}

	@EventSubscriber
	public void onChannelJoin(ChannelCreateEvent event) {
		if (event.getChannel().getName().contains("pug")) {
			QueueManager queueManager = new QueueManager(event.getChannel());
			queueManager.addQueueHelperInstances();
			PugBot.queueInstances.put(event.getChannel(), queueManager);
		}
	}
}