package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import sx.blah.discord.util.EmbedBuilder;

public class HelpCommand {
	private CommandContext ctx;

	public HelpCommand(CommandContext ctx) {
		this.ctx = ctx;
		create();
	}

	private void create() {
		EmbedBuilder builder = new EmbedBuilder();
		builder.appendField("!Update role_1 role_2", "Update current primary and secondary role", false);
		builder.appendField("!Add", "Queue up", true);
		builder.appendField("!Remove", "Exit queue", true);
		builder.appendField("!Result match_id winner_color (red or blue)", "Captain command to record result from a match", false);
		builder.appendField("!Rating", "Your rating", true);
		builder.appendField("!Status", "Queue status", true);

		builder.withColor(185, 255, 173);
		builder.withDescription("Register primary and secondary role");
		builder.withTitle("!Register role_1 role_2");
		ctx.getMessage().getChannel().sendMessage(builder.build());
	}
}
