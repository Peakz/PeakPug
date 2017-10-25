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
		builder.appendField("!Add SoloQ/RankS", "QueuePug up SoloQ or Rank S", true);
		builder.appendField("!Remove SoloQ/RankS", "Exit queue for SoloQ or Rank S", true);
		builder.appendField("!Result match_id winning_color", "Captain command to verify results, only SoloQ atm", true);
		builder.appendField("!Pick #", "Pick a player by their number", true);
		builder.appendField("!Rating", "Shows your rating, only SoloQ atm", true);
		builder.appendField("!Status", "Queue status for SoloQ and Rank S", true);
		builder.appendField("!Role", "Shows the roles you register and update profile with", true);

		builder.withColor(185, 255, 173);
		builder.withDescription("Main Tank = `mtank`, Flex Tank = `ftank`, Hitscan = `hitscan`, Projectile = `projectile`, Flex Support = `fsupp`, Main Support = `msupp`");
		builder.withTitle("Roles");
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
		builder.withTitle("Roles");
		ctx.getMessage().getChannel().sendMessage(builder.build());
	}
}
