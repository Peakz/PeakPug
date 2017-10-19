package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.PlayerDAO;
import com.github.peakz.DAO.PlayerDAOImp;
import com.github.peakz.DAO.PlayerObject;
import com.github.peakz.queues.QueueHelper;
import com.github.peakz.queues.QueueManager;

public class RemoveCommand {
	private CommandContext ctx;
	private QueueManager queueManager;

	public RemoveCommand(CommandContext ctx, QueueManager queueManager) {
		this.ctx = ctx;
		this.queueManager = queueManager;
	}

	public void removeFromMode(String mode) {
		PlayerDAO playerDAO = new PlayerDAOImp();
		PlayerObject player = playerDAO.getPlayer(ctx.getAuthor().getStringID());

		QueueHelper queueHelper;
		switch (mode) {
			case "SOLOQ":
				queueHelper = queueManager.getQueueHelper("SOLOQ");
				if(queueHelper.isQueued(player, "SOLOQ")) {
					queueHelper.getPlayers().remove(player);
					if(removeFromRoleLists(queueHelper, player)) {
						ctx.getMessage().addReaction(":white_check_mark:");
					}
				} else {
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " you're not queued!");
				}
				break;

			case "RANKS":
				queueHelper = queueManager.getQueueHelper("RANKS");
				if(queueHelper.isQueued(player, "RANKS")) {
					queueHelper.getRankSplayers().remove(player);
					ctx.getMessage().addReaction(":white_check_mark:");
				} else {
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " you're not queued!");
				}
				break;

			case "DUOQ":
				break;

			default:
				ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " wrong mode!");
				break;
		}

	}

	private boolean removeFromRoleLists(QueueHelper queueHelper, PlayerObject player) {
		// Checks primary role queue and removes the player from the list they're in
		if(queueHelper.getTanks().contains(player)){
			queueHelper.getTanks().remove(player);
			return true;
		} else if (queueHelper.getDps().contains(player)){
			queueHelper.getDps().remove(player);
			return true;
		} else if (queueHelper.getSupps().contains(player)){
			queueHelper.getSupps().remove(player);
			return true;
		} else if (queueHelper.getFlexes().contains(player)){
			queueHelper.getFlexes().remove(player);
			return true;
		}
		return false;
	}
}
