package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.PlayerObject;
import com.github.peakz.queues.QueueHelper;
import com.github.peakz.queues.QueueManager;
import sx.blah.discord.handle.obj.IUser;

public class PickCommand {
	private CommandContext ctx;
	private QueueManager queueManager;

	public PickCommand(CommandContext ctx, QueueManager queueManager) {
		this.ctx = ctx;
		this.queueManager = queueManager;
	}

	private IUser getMentionedPlayer() {
		return ctx.getMessage().getMentions().get(0);
	}

	public void pickPlayer() {
		String userID = getMentionedPlayer().getStringID();
		QueueHelper queueHelper = queueManager.getQueueHelper("RANKS");

		if(!queueHelper.restrictQueue) {
			for (PlayerObject player : queueHelper.getRankSplayers()) {
				if (player.getId().equals(userID)) {
					queueHelper.pickRankS(ctx, player);
					queueHelper.getRankSplayers().remove(player);
					ctx.getMessage().getChannel().sendMessage(playersLeft(queueHelper));
				}
			}
		}
	}

	private String playersLeft(QueueHelper queueHelper) {
		String playersLeft = "Players left: ";
		for(PlayerObject player : queueHelper.getRankSplayers()) {
			playersLeft += ctx.getGuild().getUserByID(Long.valueOf(player.getId())).getName() + " \n";
		}
		return playersLeft;
	}
}
