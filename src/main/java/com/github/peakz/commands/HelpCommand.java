package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import sx.blah.discord.util.EmbedBuilder;

public class HelpCommand {
	private CommandContext ctx;

	public HelpCommand(CommandContext ctx) {
		this.ctx = ctx;
		create();
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
		builder.withTitle("Roles");
		ctx.getMessage().getChannel().sendMessage(builder.build());
	}

	private void create() {
		EmbedBuilder builder = new EmbedBuilder();
		builder.appendField("!Register role_1 role_2", "Register a primary and secondary role", true);
		builder.appendField("!Update role_1 role_2", "Update primary and secondary role", true);
		builder.appendField("!Role", "How to format roles", true);
		builder.appendField("!Add SoloQ/RankS", "Queue for SoloQ or Rank S mode", true);
		builder.appendField("!Remove SoloQ/RankS", "Exit queue for SoloQ or Rank S", true);
		builder.appendField("!Result match_id winning_color", "Verify SoloQ match (captains only)", true);
		builder.appendField("!Pick #", "Pick a player by their number", true);
		builder.appendField("!Rating", "Shows your rating, only SoloQ atm", true);
		builder.appendField("!Status", "Queue status for SoloQ and Rank S", true);
		builder.appendField("!Wiki", "Link to wiki page", true);


		builder.withColor(185, 255, 173);
		builder.withDescription("Short description of commands, see `!Wiki` for in-depth info");
		builder.withTitle("Help Message");
		ctx.getMessage().getChannel().sendMessage(builder.build());
	}
}
