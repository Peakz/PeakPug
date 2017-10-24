package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.queues.QueueManager;
import com.github.peakz.queues.QueuePug;

public class StatusCommand {
	private CommandContext ctx;
	private QueueManager queueManager;

	public StatusCommand(CommandContext ctx, QueueManager queueManager) {
		this.ctx = ctx;
		this.queueManager = queueManager;
	}

	public void showStatus(String mode) {
		QueuePug qpug = queueManager.getQueuePug(mode);
		switch (mode) {
			case "SOLOQ":
				ctx.getMessage().getChannel().sendMessage("[SoloQ: " + qpug.getPlayersQueued() + "]");
				break;

			case "RANKS":
				ctx.getMessage().getChannel().sendMessage("[RankS -" +
						" main tanks: " + qpug.getMTQueud() + "/2+" +
						" flex tanks: " + qpug.getFTQueued() + "/2+" +
						" hitscans: " + qpug.getHSQueued() + "/2+" +
						" projectile: " + qpug.getPJQueued() + "/2+" +
						" flex supports: " + qpug.getFSQueued() + "/2+" +
						" main supports: " + qpug.getMSQueued() + "/2+" + " ]");
				break;

			case "BOTH":
				QueuePug qpug1 = queueManager.getQueuePug("SOLOQ");
				QueuePug qpug2 = queueManager.getQueuePug("RANKS");
				ctx.getMessage().getChannel().sendMessage("`[SoloQ: " + qpug1.getPlayersQueued() + "]`" + " `[Rank S: " + qpug2.getPlayersQueued() + "]`");
				break;

			default:
				ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " wrong mode!");
				break;
		}
	}
}
