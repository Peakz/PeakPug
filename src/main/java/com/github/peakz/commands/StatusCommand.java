package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.queues.QueueHelper;
import com.github.peakz.queues.QueueManager;

public class StatusCommand {
	private CommandContext ctx;
	private QueueManager queueManager;

	public StatusCommand(CommandContext ctx, QueueManager queueManager) {
		this.ctx = ctx;
		this.queueManager = queueManager;
	}

	public void showStatus(String mode) {
		switch (mode) {
			case "SOLOQ":
				QueueHelper queueHelper = queueManager.getQueueHelper("SOLOQ");
				if (queueHelper.getPlayers().size() == 1) {
					ctx.getMessage().getChannel().sendMessage("" + queueHelper.getPlayers().size() + " player in queue");
				} else {
					ctx.getMessage().getChannel().sendMessage("" + queueHelper.getPlayers().size() + " players in queue");
				}
				break;

			case "RANKS":
				queueHelper = queueManager.getQueueHelper("RANKS");
				if (queueHelper.pickedUsers.size() > 1) {

				}
				if (queueHelper.getPlayers().size() == 1) {
					ctx.getMessage().getChannel().sendMessage("" + queueHelper.getRankSplayers().size() + " player in queue");
				} else {
					ctx.getMessage().getChannel().sendMessage("" + queueHelper.getRankSplayers().size() + " players in queue");
				}
				break;

			case "BOTH":
				queueHelper = queueManager.getQueueHelper("SOLOQ");
				String str = "";
				str += "SoloQ: " + queueHelper.getPlayers().size() + " ";
				queueHelper = queueManager.getQueueHelper("SOLOQ");
				str += "RankS: " + queueHelper.getPlayers().size();

			default:
				ctx.getMessage().getChannel().sendMessage("wrong mode");
				break;
		}
	}
}
