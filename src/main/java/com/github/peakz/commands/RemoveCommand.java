package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.PlayerObject;
import com.github.peakz.queues.QueueHelper;
import com.github.peakz.queues.QueueManager;

import java.util.Iterator;

public class RemoveCommand {
	private CommandContext ctx;
	private QueueManager queueManager;

	public RemoveCommand(CommandContext ctx, QueueManager queueManager) {
		this.ctx = ctx;
		this.queueManager = queueManager;
	}

	public boolean removeFromMode(String mode) {
		String id = ctx.getAuthor().getStringID();
		Iterator<PlayerObject> iter;
		switch (mode) {
			case "SOLOQ":
				iter = queueManager.getQueueHelper(mode).getPlayers().iterator();
				while(iter.hasNext()) {
					PlayerObject player = iter.next();

					if(player.getId().equals(id))
						queueManager.getQueueHelper(mode).removePrimaryRole(player, queueManager.getQueueHelper(mode));
						return true;
				}
				break;

			case "RANKS":
				iter = queueManager.getQueueHelper(mode).getRankSplayers().iterator();
				while(iter.hasNext()) {
					PlayerObject player = iter.next();
					if(player.getId().equals(id))
						iter.remove();
						return true;
				}
				break;

			case "DUOQ":
				break;

			default:
				ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " wrong mode!");
				break;
		}
		ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " you're not queued!");
		return false;
	}

	private boolean removeFromRoleLists(QueueHelper queueHelper, PlayerObject player, Iterator<PlayerObject> iter) {
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
