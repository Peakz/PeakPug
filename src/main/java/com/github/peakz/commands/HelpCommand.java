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
		builder.appendField("!Pick #", "Pick a player by their number", true);
		builder.appendField("!Rating", "Shows your rating, only SoloQ atm", true);
		builder.appendField("!Status", "Queue status for a mode", true);
		builder.appendField("!Role", "Shows the roles you have to sign up with for Rank S", true);

		builder.withColor(185, 255, 173);
		builder.withDescription("Tank, DPS, Flex and Supp");
		builder.withTitle("Accepted roles");
		ctx.getMessage().getChannel().sendMessage(builder.build());
	}

	public static void rolesRankS(CommandContext ctx) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.appendField("Main tank", "mtank", true);
		builder.appendField("Flex tank", "ftank", true);
		builder.appendField("Hitscan", "hitscan", true);
		builder.appendField("Projectile", "projectile", true);
		builder.appendField("Flex Support", "fsupp", true);
		builder.appendField("Main Support", "msupp", true);

		builder.withColor(185, 255, 173);
		builder.withTitle("Accepted roles when using !Add RankS role_1 role_2");
		ctx.getMessage().getChannel().sendMessage(builder.build());
	}
}
