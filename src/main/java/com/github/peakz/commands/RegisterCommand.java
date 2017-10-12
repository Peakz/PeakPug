package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.PlayerDAO;
import com.github.peakz.DAO.PlayerDAOImp;
import com.github.peakz.DAO.PlayerObject;

public class RegisterCommand {
	private CommandContext ctx;
	private String id;
	private static PlayerDAO playerDAO = new PlayerDAOImp();

	public RegisterCommand(CommandContext ctx) {
		this.ctx = ctx;
		this.id = ctx.getAuthor().getStringID();
		create();
	}

	public void create() {
		if(playerDAO.checkId(id)){
			ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " you're already registered.");
		} else {

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

			// Switch statement for different cases
			switch((strArr[1].toUpperCase())) {
				case "TANK":
					if (strArr[2].toUpperCase().equals("DPS") || strArr[2].toUpperCase().equals("FLEX") || strArr[2].toUpperCase().equals("SUPP")) {
						registerPlayer(id, strArr[1], strArr[2]);
						ctx.getMessage().addReaction(":white_check_mark:");
					}
					break;

				case "SUPP":
					if (strArr[2].toUpperCase().equals("DPS") || strArr[2].toUpperCase().equals("FLEX") || strArr[2].toUpperCase().equals("TANK")) {
						registerPlayer(id, strArr[1], strArr[2]);
						ctx.getMessage().addReaction(":white_check_mark:");
					}
					break;

				case "FLEX":
					if (strArr[2].toUpperCase().equals("DPS") || strArr[2].toUpperCase().equals("TANK") || strArr[2].toUpperCase().equals("SUPP")) {
						registerPlayer(id, strArr[1], strArr[2]);
						ctx.getMessage().addReaction(":white_check_mark:");
					}
					break;

				case "DPS":
					if (strArr[2].toUpperCase().equals("TANK") || strArr[2].toUpperCase().equals("FLEX") || strArr[2].toUpperCase().equals("SUPP")) {
						registerPlayer(id, strArr[1], strArr[2]);
						ctx.getMessage().addReaction(":white_check_mark:");
					}
					break;

				default:
					ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " something went wrong, try again.");
					break;
			}
		}
	}

	private static void registerPlayer(String id, String primaryRole, String secondaryRole) {
		playerDAO.insertPlayer(new PlayerObject(id, primaryRole, secondaryRole,1000));
	}
}
