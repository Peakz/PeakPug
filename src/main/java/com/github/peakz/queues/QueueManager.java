package com.github.peakz.queues;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.PlayerObject;
import com.github.peakz.PugBot;
import sx.blah.discord.handle.obj.IChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QueueManager {
	private IChannel channel;
	private HashMap<String, QueuePug> queueInstances;
	private Map<IChannel, HashMap<String, QueuePug>> channelInstances;

	public QueueManager() {
	}

	public QueueManager(IChannel channel) {
		this.channel = channel;
		this.queueInstances = new HashMap<>();
		channelInstances = new ConcurrentHashMap<>();
	}

	public void addQueueHelperInstances() {
		// Add inner instances
		queueInstances.put("SOLOQ", new QueuePug());
		queueInstances.put("RANKS", new QueuePug());

		// Add outer instances
		channelInstances.put(channel, queueInstances);
		//queueInstances.put("DUOQ", new QueuePug());
	}

	public boolean isQueued(CommandContext ctx, PlayerObject player) {
		return (PugBot.queueInstances.get(ctx.getChannel()).getQueuePug(ctx.getChannel(), "SOLOQ").containsInstance(player) ||
				PugBot.queueInstances.get(ctx.getChannel()).getQueuePug(ctx.getChannel(), "RANKS").containsInstance(player));
	}

	public QueuePug getQueuePug(IChannel channel, String mode) {
		return channelInstances.get(channel).get(mode);
	}
}
