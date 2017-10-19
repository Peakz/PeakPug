package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.*;
import com.github.peakz.queues.QueueHelper;
import com.github.peakz.queues.QueueManager;
import sx.blah.discord.util.EmbedBuilder;

import java.util.ArrayList;

public class AddCommand {
	private CommandContext ctx;
	private QueueManager queueManager;

	public AddCommand(CommandContext ctx, QueueManager queueManager){
		this.ctx = ctx;
		this.queueManager = queueManager;
	}

	public void addToMode(String mode) {
		switch (mode) {
			case "SOLOQ":
				QueueHelper queueHelper = queueManager.getQueueHelper("SOLOQ");
				soloQMode(queueHelper.getTemp_team_red(), queueHelper.getTemp_team_blue(), queueHelper);
				break;

			case "RANKS":
				queueHelper = queueManager.getQueueHelper("RANKS");
				rankSMode(queueHelper);
				break;

			case "DUOQ":
				break;

			default:
				break;
		}
	}

	private void soloQMode(ArrayList<PlayerObject> temp_team_red, ArrayList<PlayerObject> temp_team_blue, QueueHelper queueHelper) {
		String id = ctx.getAuthor().getStringID();
		PlayerDAO playerDAO = new PlayerDAOImp();
		PlayerObject player = playerDAO.getPlayer(id);

		if(playerDAO.checkId(id) && (player.getPrimaryRole() != null && player.getSecondaryRole() != null)) {
			if (queueHelper.isQueued(player, "SOLOQ")) {
				ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " you're already queued for SoloQ!");
			} else if (queueHelper.getPlayers().size() < 12){
				queueHelper.addPrimaryRole(player, queueHelper);
				ctx.getMessage().addReaction(":white_check_mark:");
				if (queueHelper.getPlayers().size() == 12) {
					MatchObject match = queueHelper.makeTeams(temp_team_red, temp_team_blue, queueHelper);
					MatchDAO matchDAO = new MatchDAOImp();
					matchDAO.insertMatch(match);
					match.setId(matchDAO.getLastMatchID());

					EmbedBuilder builder = new EmbedBuilder();

					builder.appendField("RED TEAM MMR: " + match.getTeam_red().getAvgRating(), "Captain: " +
							ctx.getGuild().getUserByID(
									Long.valueOf(match.getTeam_red().getCaptain().getId())).getName(), true);

					builder.appendField("BLUE TEAM MMR: " + match.getTeam_blue().getAvgRating(), "Captain: " +
							ctx.getGuild().getUserByID(
									Long.valueOf(match.getTeam_blue().getCaptain().getId())).getName(), true);

					builder.withColor(0, 153, 255);
					builder.withDescription("MAP - " + match.getMap());
					builder.withTitle("MATCH ID: " + matchDAO.getLastMatchID());

					builder.withFooterText("EACH CAPTAIN MUST REPORT RESULTS AFTER THE MATCH, \"!Result match_id team_color\"");
					builder.withThumbnail(queueHelper.getMapImg(match.getMap()));
					ctx.getChannel().sendMessage(builder.build());

					// create new VerificationObject for the match
					VerificationDAO verificationDAO = new VerificationDAOImp();

					// create verification for team red, false by default
					verificationDAO.insertVerification(match.getId(), match.getTeam_red().getCaptain().getId(), false);

					// create verification for team blue, false by default
					verificationDAO.insertVerification(match.getId(), match.getTeam_blue().getCaptain().getId(), false);

					queueHelper.resetPools("SOLOQ");
				}
			}
		}
	}

	private void rankSMode(QueueHelper queueHelper) {
		String id = ctx.getAuthor().getStringID();
		PlayerDAO playerDAO = new PlayerDAOImp();
		PlayerObject player = playerDAO.getPlayer(id);

		if(queueHelper.getRankSplayers().size() < 14) {
			if(queueHelper.getRankSplayers().contains(player)) {
				ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " you're already queued for Rank S");
			} else {
				if(!queueHelper.restrictQueue) {
					queueHelper.getRankSplayers().add(player);
					queueHelper.pickedUsers.add(ctx.getMessage().getGuild().getUserByID(Long.valueOf(player.getId())));
					ctx.getMessage().addReaction(":white_check_mark:");
				} else {
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " wait until pick phase is over");
				}
			}
		} else {
			queueHelper.startRankS(ctx);
			queueHelper.resetPools("RANKS");
		}
	}
}