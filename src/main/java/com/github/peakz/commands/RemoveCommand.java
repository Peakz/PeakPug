package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.PlayerDAO;
import com.github.peakz.DAO.PlayerDAOImp;
import com.github.peakz.DAO.PlayerObject;
import com.github.peakz.queues.QueueHelper;
import com.github.peakz.queues.QueueManager;

import java.util.ArrayList;

public class RemoveCommand {
	private CommandContext ctx;
	private QueueManager queueManager;

	public RemoveCommand(CommandContext ctx, QueueManager queueManager) {
		this.ctx = ctx;
		this.queueManager = queueManager;
		create();
	}

	private void create() {
		PlayerDAO playerDAO = new PlayerDAOImp();
		PlayerObject player = playerDAO.getPlayer(ctx.getAuthor().getStringID());

		QueueHelper queueHelper = queueManager.getQueueHelper();

		if(queueHelper.getPlayers().size() == 1){
			// If the queue becomes empty, the whole queue helper is renewed
			queueHelper.setPlayers(new ArrayList<>());
			ctx.getMessage().addReaction(":white_check_mark:");

		} else if (queueHelper.isQueued(player, queueHelper)){
			// Removes the player from the queue if it contains at least 1 player
			queueHelper.getPlayers().remove(player);
			ctx.getMessage().addReaction(":white_check_mark:");

			// Checks primary role queue and removes the player from the list they're in
			if(queueHelper.getTanks().contains(player)){
				queueHelper.getTanks().remove(player);
				ctx.getMessage().addReaction(":white_check_mark:");

			} else if (queueHelper.getDps().contains(player)){
				queueHelper.getDps().remove(player);
				ctx.getMessage().addReaction(":white_check_mark:");

			} else if (queueHelper.getSupps().contains(player)){
				queueHelper.getSupps().remove(player);
				ctx.getMessage().addReaction(":white_check_mark:");

			} else if (queueHelper.getFlexes().contains(player)){
				queueHelper.getFlexes().remove(player);
				ctx.getMessage().addReaction(":white_check_mark:");
			}
		}
	}
}
