package com.github.peakz.queues;

import sx.blah.discord.handle.obj.IUser;

public class RankSHelper {
	private QueueHelper queueHelper;
	private IUser user;

	public RankSHelper(QueueHelper queueHelper, IUser user) {
		this.queueHelper = queueHelper;
		this.user = user;
	}

	private void startPickPhase(int turn) {

	}
}

/**
 *
 * // if it's red's turn to pick
 if(tempMatch.getTeam_red().getCaptain().getId().equals(ctx.getAuthor().getStringID()) && turn == 0) {
 if(entry.getValue().getQueueHelper("RANKS").pickRankS(ctx, player, turn, tempMatch)) {
 turn++;
 } else {
 ctx.getMessage().addReaction(":exclamation:");
 }

 // if it's blue's turn to pick
 } else if (tempMatch.getTeam_blue().getCaptain().getId().equals(ctx.getAuthor().getStringID()) && turn == 1) {
 if(entry.getValue().getQueueHelper("RANKS").pickRankS(ctx, player, turn, tempMatch)) {
 turn--;
 if(entry.getValue().getQueueHelper("RANKS").getRankSplayers().size() == 0) {
 MatchDAO matchDAO = new MatchDAOImp();
 matchDAO.insertMatch(tempMatch);
 }
 } else {
 ctx.getMessage().addReaction(":exclamation:");
 }
 */
