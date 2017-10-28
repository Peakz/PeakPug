package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.PlayerObject;
import com.github.peakz.queues.QueueManager;
import com.github.peakz.queues.QueuePug;

public class AddCommand {
	private CommandContext ctx;
	private QueueManager queueManager;

	public AddCommand(CommandContext ctx, QueueManager queueManager) {
		this.ctx = ctx;
		this.queueManager = queueManager;
	}

	public void addToMode(String mode, PlayerObject player) {
		if (checkOldRoles(player.getPrimaryRole()) && checkOldRoles(player.getSecondaryRole())) {
			ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " Please update your roles. I only accept the new and updated roles. Type !Help");
		} else if (checkNewRoles(player.getPrimaryRole()) && checkNewRoles(player.getSecondaryRole())) {
			switch (mode) {
				case "SOLOQ":
					QueuePug qpug = queueManager.getQueuePug(ctx.getChannel(), mode);
					if (!qpug.containsInstance(player)) {
						if (!queueManager.getQueuePug(ctx.getChannel(), "RANKS").containsInstance(player)) {
							qpug.addPlayer("SOLOQ", player, ctx);
							ctx.getMessage().addReaction(":white_check_mark:");
						} else {
							ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You cannot queue for multiple modes at once!");
						}
					} else {
						ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You're already queued for SoloQ!");
					}
					break;

				case "RANKS":
					qpug = queueManager.getQueuePug(ctx.getChannel(), mode);
					if (!qpug.containsInstance(player)) {
						if (!queueManager.getQueuePug(ctx.getChannel(), "SOLOQ").containsInstance(player)) {
							qpug.addPlayer(mode, player, ctx);
							ctx.getMessage().addReaction(":white_check_mark:");
						} else {
							ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You cannot queue for multiple modes at once!");
						}
					} else {
						ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You're already queued for Rank S!");
					}
					break;

				default:
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " Wrong mode! Type !Help");
					break;
			}
		}
	}

	private boolean checkOldRoles(String role) {
		switch (role) {
			case "TANK":
			case "DPS":
			case "FLEX":
			case "SUPP":
				return true;

			default:
				return false;
		}
	}

	@SuppressWarnings("Duplicates")
	private boolean checkNewRoles(String role) {
		switch (role) {
			case "mtank":
			case "ftank":
			case "hitscan":
			case "projectile":
			case "fsupp":
			case "msupp":
				return true;

			default:
				return false;
		}
	}
}