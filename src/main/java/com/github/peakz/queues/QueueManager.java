package com.github.peakz.queues;

import sx.blah.discord.handle.obj.IChannel;

import java.util.concurrent.ConcurrentHashMap;

public class QueueManager {
	private IChannel channel;
	private ConcurrentHashMap<String, QueuePug> queueInstances = new ConcurrentHashMap<>();
	private ConcurrentHashMap<IChannel, ConcurrentHashMap<String, QueuePug>> channelInstances = new ConcurrentHashMap<>();

	public QueueManager(IChannel channel) {
		this.channel = channel;
	}

	public void addQueueHelperInstances() {
		// Add inner instances
		queueInstances.put("SOLOQ", new QueuePug());
		queueInstances.put("RANKS", new QueuePug());

		// Add outer instances
		channelInstances.put(channel, queueInstances);
		//queueInstances.put("DUOQ", new QueuePug());
	}

	public QueuePug getQueuePug(IChannel channel, String mode) {
		mode = mode.toUpperCase();
		return channelInstances.get(channel).get(mode);
	}
}
