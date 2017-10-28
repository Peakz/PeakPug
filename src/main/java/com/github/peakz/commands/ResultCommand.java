package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.*;
import com.github.peakz.queues.QueueManager;

public class ResultCommand {
	private CommandContext ctx;
	private QueueManager queueManager;

	public ResultCommand(CommandContext ctx, QueueManager queueManager) {
		this.ctx = ctx;
		this.queueManager = queueManager;
	}

	public void checkWinner(int id, String winner) {
		MatchDAO matchDAO = new MatchDAOImp();
		VerificationDAO vDAO = new VerificationDAOImp();
		MatchObject match = matchDAO.getMatch(id);

		// Check if id belongs to verification
		if (vDAO.checkCaptain(id, ctx.getAuthor().getStringID())) {
			if (!match.getWinner().equals(winner.toUpperCase())) {
				// Check if the verification is verified
				VerificationObject vo = vDAO.getVerification(id, ctx.getAuthor().getStringID());
				if (vo.isVerified()) {
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You have already submitted your verification for this match!");
					ctx.getMessage().addReaction(":white_check_mark:");

					// else if match has a winner but verification isn't verified
				} else if (match.getWinner().equals(winner.toUpperCase()) && match.getWinner() != null) {
					vo.setVerified(true);
					vDAO.updateVerification(vo);
					ctx.getMessage().addReaction(":white_check_mark:");

					// else if match isn't verified
				} else if (match.getWinner().equals("NONE")) {
					match.setWinner(winner);
					vo.setVerified(true);
					matchDAO.updateMatch(match);
					vDAO.updateVerification(vo);
					ctx.getMessage().addReaction(":white_check_mark:");
				}
				System.out.println("no");
			} else {
				ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " Your opponent submitted a different winner! Try again!");
			}
		} else if (vDAO.checkBothVerifications(id)) {
			ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " This match is already verified!");
		} else {
			ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You're not a captain of this match!");
		}
	}
}
