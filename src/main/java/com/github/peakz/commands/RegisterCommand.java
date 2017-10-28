package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.PlayerDAO;
import com.github.peakz.DAO.PlayerDAOImp;
import com.github.peakz.DAO.PlayerObject;

public class RegisterCommand {
	private CommandContext ctx;
	private String id;

	public RegisterCommand(CommandContext ctx) {
		this.ctx = ctx;
		this.id = ctx.getAuthor().getStringID();
	}

	public void registerPlayer(String role1, String role2) {
		role1 = role1.toLowerCase();
		role2 = role2.toLowerCase();

		if (roleSwitch(role1) && roleSwitch(role2)) {
			PlayerObject player = new PlayerObject();
			player.setId(ctx.getAuthor().getStringID());
			player.setPrimaryRole(role1);
			player.setSecondaryRole(role2);
			player.setRating(1000);

			PlayerDAO playerDAO = new PlayerDAOImp();
			playerDAO.insertPlayer(player);
			ctx.getMessage().addReaction(":white_check_mark:");
		} else {
			ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " One or more of your roles are wrong! Type !Help");
		}
	}

	@SuppressWarnings("Duplicates")
	public boolean roleSwitch(String role) {
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
