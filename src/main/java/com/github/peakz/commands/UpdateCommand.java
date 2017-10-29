package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.PlayerDAO;
import com.github.peakz.DAO.PlayerDAOImp;
import com.github.peakz.PugBot;

public class UpdateCommand {

	private CommandContext ctx;

	public UpdateCommand(CommandContext ctx) {
		this.ctx = ctx;
	}

	public void updatePlayer(String role1, String role2) {
		PlayerDAO playerDAO = new PlayerDAOImp();
		String id = ctx.getAuthor().getStringID();

		role1 = role1.toLowerCase();
		role2 = role2.toLowerCase();

		if (roleSwitch(role1) && roleSwitch(role2)) {
			if (playerDAO.checkId(id)) {
				if(PugBot.queueInstances.get(ctx.getChannel()).isQueued(ctx, playerDAO.getPlayer(id))) {
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " Remove yourself from the queue before updating roles!");
				} else {
					playerDAO.updatePlayerRoles(id, role1, role2);
					ctx.getMessage().addReaction(":white_check_mark:");
				}
			} else {
				ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You're not registered yet! Type !Help");
			}
		}
	}

	@SuppressWarnings("Duplicates")
	private boolean roleSwitch(String role) {
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
