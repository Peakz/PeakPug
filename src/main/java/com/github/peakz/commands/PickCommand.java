package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.PlayerObject;
import com.github.peakz.queues.QueueHelper;
import com.github.peakz.queues.QueueManager;

import java.util.Iterator;

public class PickCommand {
	private CommandContext ctx;
	private QueueManager queueManager;
	private int turn = 0;
	PlayerObject player;

	public PickCommand(CommandContext ctx, QueueManager queueManager) {
		this.ctx = ctx;
		this.queueManager = queueManager;
	}

	public void pickPlayer(String number) {
		QueueHelper queueHelper = queueManager.getQueueHelper("RANKS");
		if(pickPlayer(queueHelper, number)) {
			queueHelper.pickRankS(ctx, player);
		}
	}

	private boolean pickPlayer(QueueHelper queueHelper, String number) {
		Iterator<PlayerObject> iter = queueHelper.getRankSplayers().iterator();

		while (iter.hasNext()) {
			PlayerObject player = iter.next();
			if (player.getId().equals(queueHelper.getRankSplayers().get(Integer.parseInt(number) - 1).getId())) {
				this.player = player;
				return true;
			}
		}
		/** if(queueHelper.restrictQueue) {
		 for (PlayerObject player : queueHelper.getRankSplayers()) {
		 if (player.getId().equals(id)) {
		 queueHelper.pickRankS(ctx, player);
		 queueHelper.getRankSplayers().remove(player);
		 ctx.getMessage().getChannel().sendMessage(playersLeft(queueHelper));
		 }
		 }
		 }*/
		return false;
	}

	private String playersLeft(QueueHelper queueHelper) {
		String playersLeft = "Players left: ";
		/** for(PlayerObject player : queueHelper.getRankSplayers()) {
			playersLeft += ctx.getGuild().getUserByID(Long.valueOf(player.getId())).getName() + " \n";
		}*/

		return queueHelper.showPlayersNoMention(ctx, queueHelper.getRankSplayers());
	}
}
