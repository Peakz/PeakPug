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
		builder.appendField("!Register role_1 role_2", "Register a primary and secondary role", true);
		builder.appendField("!Update role_1 role_2", "Update current primary and secondary role", true);
		builder.appendField("!Add SoloQ/RankS", "Queue up for a mode", true);
		builder.appendField("!Remove SoloQ/RankS", "Exit queue for a mode", true);
		builder.appendField("!Result match_id winning_color", "Captain command to verify results, only SoloQ atm", true);
		builder.appendField("!Pick @player", "Pick a player", true);
		builder.appendField("!Rating", "Shows your rating, only SoloQ atm", true);
		builder.appendField("!Status SoloQ/RankS", "Queue status for a mode", true);

		builder.withColor(185, 255, 173);
		builder.withDescription("Tank, DPS, Flex and Supp");
		builder.withTitle("Accepted roles");
		ctx.getMessage().getChannel().sendMessage(builder.build());
	}
}
