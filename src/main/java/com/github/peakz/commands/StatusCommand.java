package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.queues.QueueManager;
import com.github.peakz.queues.QueuePug;
import sx.blah.discord.util.EmbedBuilder;

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

			case "BOTH":
				QueuePug qpug1 = queueManager.getQueuePug("SOLOQ");
				QueuePug qpug2 = queueManager.getQueuePug("RANKS");

				EmbedBuilder builder = new EmbedBuilder();

				builder.appendField("SoloQ", "Total Players: `" + qpug1.getPlayersQueued() + "`"
						+ "\nMain Tanks - `" + qpug1.getMTQueud() + "/2`"
						+ "\nFlex Tanks - `" + qpug1.getFTQueued() + "/2`"
						+ "\nHitscans - `" + qpug1.getHSQueued() + "/2`"
						+ "\nProjectiles - `" + qpug1.getPJQueued() + "/2`"
						+ "\nFlex Supports - `" + qpug1.getFSQueued() + "/2`"
						+ "\nMain Supports - `" + qpug1.getMSQueued() + "/2`", true);


				builder.appendField("Rank S", "Total Players: `" + qpug2.getPlayersQueued() + "`"
						+ "\nMain Tanks - `" + qpug2.getMTQueud() + "/2`"
						+ "\nFlex Tanks - `" + qpug2.getFTQueued() + "/2`"
						+ "\nHitscans - `" + qpug2.getHSQueued() + "/2`"
						+ "\nProjectiles - `" + qpug2.getPJQueued() + "/2`"
						+ "\nFlex Supports - `" + qpug2.getFSQueued() + "/2`"
						+ "\nMain Supports - `" + qpug2.getMSQueued() + "/2`", true);

				builder.withColor(239, 225, 28);
				builder.withTitle("Queue Status");

				builder.withFooterText("A game will be made after I can make 2/2/2 for both teams");
				ctx.getMessage().getChannel().sendMessage(builder.build());
				break;

			default:
				ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " wrong mode!");
				break;
		}
	}
}
