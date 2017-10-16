package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.*;
import com.github.peakz.queues.QueueHelper;
import com.github.peakz.queues.QueueManager;

import java.util.ArrayList;

public class ResultCommand {
	private CommandContext ctx;
	private QueueManager queueManager;

	public ResultCommand(CommandContext ctx, QueueManager queueManager) {
		this.ctx = ctx;
		this.queueManager = queueManager;
		queueManager.setQueueHelper(new QueueHelper());
		create(queueManager.getQueueHelper());
	}

	private void create(QueueHelper queueHelper) {
		String[] strArr = new String[3];
		int i = 0;

		// Store the split strings
		for(String val : ctx.getMessage().getContent().split(" ", 3)) {
			strArr[i] = val;
			i++;
		}

		// check team color in the command
		if(strArr[2].toUpperCase().equals("RED") || strArr[2].toUpperCase().equals("BLUE")) {
			// create a MatchObject and get the match for the match id that's typed in.
			MatchDAO matchDAO = new MatchDAOImp();
			// MatchObject match = matchDAO.getMatch(Integer.parseInt(strArr[1]));

			int match_id = Integer.parseInt(strArr[1]);
			String user_id = ctx.getAuthor().getStringID();

			// check if the match exists
			if (matchDAO.checkMatchID(match_id)) {
				VerificationDAO verificationDAO = new VerificationDAOImp();
				// get verification
				VerificationObject current_vo = verificationDAO.getVerification(match_id, user_id);
				// check if there's a verification linked to this user's id
				if (current_vo.getCaptain_id().equals(user_id)) {
					// check if both verification requests are already handled
					if (verificationDAO.checkBothVerifications(Integer.parseInt(strArr[1]))) {
						ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " match has already been verified by both captains!");
					} else {
						// find and verify a match for the captain that sent the message
						if (current_vo.getCaptain_id().equals(user_id)) {
							// check first if there's a mismatch in verifications or if there's no verification at all
							String winner = verificationDAO.getWinner(match_id);
							if (winner.equals(strArr[2].toUpperCase())) {
								current_vo.setVerified(true);
								verificationDAO.updateVerification(current_vo);
								ctx.getMessage().addReaction(":white_check_mark:");

								// check if both verifications are valid once again and if so, proceed to update mmr accordingly
								if (verificationDAO.checkBothVerifications(match_id)) {
									ArrayList<VerificationObject> vos = verificationDAO.getVerifications(match_id);
									if(queueHelper.updateMMR(vos.get(1), vos.get(2), winner)) {
										ctx.getMessage().getChannel().sendMessage("Updated the match: " + match_id + " and each player's MMR");
									}
								}
							} else {
								// reply if there's a mismatch in team colors reported by the two captains
								ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " Your opponent has verified a different winner already. If the winning team color was typed wrong, please contact an admin");
							}
						}
					}
					// reply if match is already reported by a captain
				} else {
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " you're not captain for the match id: " + match_id);
				}
				// reply if the match doesn't exist
			} else {
				ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " this match doesn't exist");
			}
			// reply if there's an attempt to report a team color other than red or blue
		} else {
			ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " try typing in the winning team color again. Either \"Red\" or \"Blue\"");
		}
	}
}
