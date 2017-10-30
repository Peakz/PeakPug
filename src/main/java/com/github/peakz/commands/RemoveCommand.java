package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.PlayerObject;
import com.github.peakz.queues.QueueManager;
import com.github.peakz.queues.QueuePug;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

public class RemoveCommand {
	private CommandContext ctx;
	private QueueManager queueManager;
	private PlayerObject player;

	public RemoveCommand(CommandContext ctx, QueueManager queueManager) {
		this.ctx = ctx;
		this.queueManager = queueManager;
		this.player = new PlayerObject();
	}

	public void removeFromMode(String mode) {
		switch (mode) {
			case "SOLOQ":
				QueuePug qpug = queueManager.getQueuePug(ctx.getChannel(), mode);
				player = qpug.getPlayer(ctx.getAuthor().getStringID());

				if (qpug.containsInstance((player))) {
					player = qpug.getPlayer(ctx.getAuthor().getStringID());
					qpug.removePlayer(mode, player);

					Emoji e = EmojiManager.getForAlias("white_check_mark");
					ctx.getMessage().addReaction(e);
				} else {
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You're not queued!");
				}
				break;

			case "RANKS":
				qpug = queueManager.getQueuePug(ctx.getChannel(), mode);
				player = qpug.getPlayer(ctx.getAuthor().getStringID());

				if (qpug.containsInstance((player))) {
					qpug.removePlayer(mode, player);

					Emoji e = EmojiManager.getForAlias("white_check_mark");
					ctx.getMessage().addReaction(e);
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

	public void removeAdmin(PlayerObject p, String mode) {
		this.player = p;
		removeFromMode(mode);
	}
}
