package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.PlayerObject;
import com.github.peakz.queues.QueueManager;
import com.github.peakz.queues.QueuePug;

public class RemoveCommand {
	private CommandContext ctx;
	private QueueManager queueManager;

	public RemoveCommand(CommandContext ctx, QueueManager queueManager) {
		this.ctx = ctx;
		this.queueManager = queueManager;
	}

	public void removeFromMode(String mode) {
		PlayerObject player = new PlayerObject();
		switch (mode) {
			case "SOLOQ":
				QueuePug qpug = queueManager.getQueuePug(mode);
				player = qpug.getPlayer(ctx.getAuthor().getStringID());

				if(qpug.queuedPlayers.contains(player)) {
					player = qpug.getPlayer(ctx.getAuthor().getStringID());
					qpug.removePlayer(mode, player);
					ctx.getMessage().addReaction(":white_check_mark:");
				} else {
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You're not queued!");
				}
				break;

			case "RANKS":
				qpug = queueManager.getQueuePug(mode);
				player = qpug.getPlayer(ctx.getAuthor().getStringID());

				if(qpug.queuedPlayers.contains(player)) {
					qpug.removePlayer(mode, player);
					ctx.getMessage().addReaction(":white_check_mark:");
				} else {
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You're not queued!");
				}
				break;

			case "DUOQ":
				break;

			default:
				ctx.getMessage().addReaction(":exclamation:");
				break;
		}
	}
}
