package com.github.peakz;

import com.github.peakz.queues.QueueManager;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.obj.IGuild;

public class AnnotationListener {

	@EventSubscriber
	public void onReadyEvent(ReadyEvent event) {
		for(IGuild guild : event.getClient().getGuilds()){
			QueueManager queueManager = new QueueManager();
			queueManager.addQueueHelperInstances();
			PugBot.queueInstances.put(guild, queueManager);
		}
	}

	@EventSubscriber
	public void onGuildJoin(GuildCreateEvent event) {
		QueueManager queueManager = new QueueManager();
		queueManager.addQueueHelperInstances();
		PugBot.queueInstances.put(event.getGuild(), queueManager);
	}
}