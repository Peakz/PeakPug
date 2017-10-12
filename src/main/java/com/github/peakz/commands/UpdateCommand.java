package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.PlayerDAO;
import com.github.peakz.DAO.PlayerDAOImp;
import com.github.peakz.DAO.PlayerObject;

public class UpdateCommand {

	private CommandContext ctx;
	private static PlayerDAO playerDAO = new PlayerDAOImp();

	public UpdateCommand(CommandContext ctx) {
		this.ctx = ctx;
		create();
	}

	private void create() {
		String id = ctx.getAuthor().getStringID();

		/*
		Split the string into 3 parts
		1. The command
		2. Primary role
		3. Secondary role
		 */
		String[] strArr = new String[3];
		int i = 0;

		// Store the split strings
		for(String val : ctx.getMessage().getContent().split(" ", 3)) {
			strArr[i] = val;
			i++;
		}

		// if index 1 is null, we set the value to a new arbitrary value so trigger the switch default case
		if(strArr[1] == null) {
			strArr[1] = "nope";
		}

		// if index 2 is null, we set the value to a new arbitrary value so trigger the switch default case
		if(strArr[2] == null) {
			strArr[2] = "nope";
		}

		// Check if player exists and then switch statement for different cases
		if(playerDAO.checkId(id)) {
			switch (strArr[1].toUpperCase()) {
				case "TANK":
					if (strArr[2].toUpperCase().equals("DPS") || strArr[2].toUpperCase().equals("FLEX") || strArr[2].toUpperCase().equals("SUPP")) {
						updatePlayer(id, strArr[1], strArr[2]);
						ctx.getMessage().addReaction(":white_check_mark:");
					}
					break;

				case "DPS":
					if (strArr[2].toUpperCase().equals("TANK") || strArr[2].toUpperCase().equals("FLEX") || strArr[2].toUpperCase().equals("SUPP")) {
						updatePlayer(id, strArr[1], strArr[2]);
						ctx.getMessage().addReaction(":white_check_mark:");
					}
					break;

				case "FLEX":
					if (strArr[2].toUpperCase().equals("TANK") || strArr[2].toUpperCase().equals("DPS") || strArr[2].toUpperCase().equals("SUPP")) {
						updatePlayer(id, strArr[1], strArr[2]);
						ctx.getMessage().addReaction(":white_check_mark:");
					}
					break;

				case "SUPP":
					if (strArr[2].toUpperCase().equals("TANK") || strArr[2].toUpperCase().equals("FLEX") || strArr[2].toUpperCase().equals("FLEX")) {
						updatePlayer(id, strArr[1], strArr[2]);
						ctx.getMessage().addReaction(":white_check_mark:");
					}
					break;

				default:
					ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " something went wrong, try again.");
					break;
			}
		} else {
			ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " you're not registered. Try again.");
		}
	}

	private static void updatePlayer(String id, String primaryRole, String secondaryRole) {
		PlayerObject player = playerDAO.getPlayer(id);
		player.setPrimaryRole(primaryRole);
		player.setSecondaryRole(secondaryRole);
		playerDAO.updatePlayer(player);
	}
}
