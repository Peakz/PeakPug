package com.github.peakz;

import com.darichey.discord.Command;
import com.darichey.discord.CommandListener;
import com.darichey.discord.CommandRegistry;
import com.github.peakz.DAO.*;
import com.github.peakz.commands.*;
import com.github.peakz.queues.QueueHelper;
import com.github.peakz.queues.QueueManager;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

import java.util.HashMap;
import java.util.Map;

public class PugBot extends Main {
	public static Map<IGuild, QueueManager> queueManagers = new HashMap<>();
	public static Map<IChannel, QueueManager> channelQueue = new HashMap<>();

	private static PlayerDAO playerDAO = new PlayerDAOImp();
	private static int turn = 0;
	// private static int antiRNG = 0;

	PugBot(IDiscordClient client) {
		super(client);
	}

	static void createCommands(IDiscordClient client) {
		Command register = Command.builder().onCalled(RegisterCommand::new).build();

		// Create command for updating roles in a player profile
		Command update = Command.builder().onCalled(ctx -> {
			for(Map.Entry<IGuild, QueueManager> entry : queueManagers.entrySet()) {
				if(entry.getKey().equals(ctx.getGuild())) {
					new UpdateCommand(ctx);
				} else {
					ctx.getMessage().addReaction(":exclamation:");
				}
			}
		}).build();

		Command add = Command.builder().onCalled(ctx -> {
			// Get the mode by splitting strings
			String[] strArr = new String[2];
			int i = 0;

			// Store the split strings
			for(String val : ctx.getMessage().getContent().split(" ", 2)) {
				strArr[i] = val;
				i++;
			}

			PlayerObject player = playerDAO.getPlayer(ctx.getAuthor().getStringID());
			if(strArr[1] != null) {
				if (strArr[1].toUpperCase().equals("SOLOQ")) {
					for (Map.Entry<IGuild, QueueManager> entry : queueManagers.entrySet()) {
						if (entry.getKey().equals(ctx.getGuild())) {
							new AddCommand(ctx, entry.getValue());
						}
					}
				} else {
					ctx.getMessage().addReaction(":exclamation:");
				}
				/**

				 else if (strArr[1].toUpperCase().equals("RANKS") && (!entry.getValue().getQueueHelper().getRankSplayers().contains(player))) {
				 // check if we have enough ppl for rankS
				 if(entry.getValue().getQueueHelper().getRankSplayers().size() < 14) {
				 entry.getValue().getQueueHelper().getRankSplayers().add(player);
				 if(entry.getValue().getQueueHelper().getRankSplayers().size() == 1) {
				 MatchObject match = entry.getValue().getQueueHelper().startRankS(entry.getValue().getQueueHelper().getRankSplayers());
				 entry.getValue().getQueueHelper().setMatch(match);
				 }
				 ctx.getMessage().addReaction(":white_check_mark:");
				 } else {
				 ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " wait until pick phase is over!");
				 }
				 }
				 */
			}
		}).build();

		Command remove = Command.builder().onCalled(ctx -> {
			// Get the mode by splitting strings
			String[] strArr = new String[2];
			int i = 0;

			// Store the split strings
			for(String val : ctx.getMessage().getContent().split(" ", 2)) {
				strArr[i] = val;
				i++;
			}

			if(strArr[1] != null) {
				if (strArr[1].toUpperCase().equals("SOLOQ")) {
					for (Map.Entry<IGuild, QueueManager> entry : queueManagers.entrySet()) {
						if (entry.getKey().equals(ctx.getGuild())) {
							new RemoveCommand(ctx, entry.getValue());
						}
					}
				} else {
					ctx.getMessage().addReaction(":exclamation:");
				}
			} else {
				ctx.getMessage().addReaction(":exclamation:");
			}
		}).build();

		Command result = Command.builder().onCalled(ctx -> {
			for(Map.Entry<IGuild, QueueManager> entry : queueManagers.entrySet()) {
				if(entry.getKey().equals(ctx.getGuild())) {
					new ResultCommand(ctx, entry.getValue());
				} else {
					ctx.getMessage().addReaction(":exclamation:");
				}
			}
		}).build();

		Command rank = Command.builder()
				.onCalled(ctx -> ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " you rating is " + playerDAO.getPlayer(ctx.getAuthor().getStringID()).getRating()))
				.build();

		Command help = Command.builder().onCalled(HelpCommand::new).build();

		Command status = Command.builder().onCalled(ctx -> {
			// Get the mode by splitting strings
			String[] strArr = new String[2];
			int i = 0;

			// Store the split strings
			for(String val : ctx.getMessage().getContent().split(" ", 2)) {
				strArr[i] = val;
				i++;
			}
			if(strArr[1] != null) {
				if(strArr[1].toUpperCase().equals("SOLOQ")) {
					for (Map.Entry<IGuild, QueueManager> entry : queueManagers.entrySet()) {
						if (entry.getKey().equals(ctx.getGuild())) {
							QueueHelper queueHelper = entry.getValue().getQueueHelper();
							if (queueHelper.getPlayers().size() == 1) {
								ctx.getMessage().getChannel().sendMessage("" + queueHelper.getPlayers().size() + " player in queue");
							} else {
								ctx.getMessage().getChannel().sendMessage("" + queueHelper.getPlayers().size() + " players in queue");
							}
						}
					}
				} else {
					ctx.getMessage().addReaction(":exclamation:");
				}
			} else {
				ctx.getMessage().addReaction(":exclamation:");
			}
				/**
				else if (strArr[1].toUpperCase().equals("RANKS")) {
					if (entry.getKey().equals(ctx.getGuild())) {
						QueueHelper queueHelper = entry.getValue().getQueueHelper();
						if (queueHelper.getRankSplayers().size() == 1) {
							ctx.getMessage().getChannel().sendMessage("" + queueHelper.getRankSplayers().size() + " player in queue");
						} else {
							ctx.getMessage().getChannel().sendMessage("" + queueHelper.getRankSplayers().size() + " players in queue");
						}
					}
				}
				 */
		}).build();

		Command pick = Command.builder()
				.onCalled(ctx -> {
					// Get the mode by splitting strings
					String[] strArr = new String[2];
					int i = 0;

					// Store the split strings
					for(String val : ctx.getMessage().getContent().split(" ", 2)) {
						strArr[i] = val;
						i++;
					}

					for(Map.Entry<IGuild, QueueManager> entry : queueManagers.entrySet()) {
						if(entry.getKey().equals(ctx.getGuild())) {
							for(PlayerObject player : entry.getValue().getQueueHelper().getRankSplayers()) {
								if(ctx.getGuild().getUserByID(Long.valueOf(player.getId())).getName().equals(strArr[2])) {
									MatchObject tempMatch = entry.getValue().getQueueHelper().getMatch();

									// if it's red's turn to pick
									if(tempMatch.getTeam_red().getCaptain().getId().equals(ctx.getAuthor().getStringID()) && turn == 0) {
										if(entry.getValue().getQueueHelper().pickRankS(ctx, player, turn, tempMatch)) {
											turn++;
										} else {
											ctx.getMessage().addReaction(":exclamation:");
										}

									// if it's blue's turn to pick
									} else if (tempMatch.getTeam_blue().getCaptain().getId().equals(ctx.getAuthor().getStringID()) && turn == 1) {
										if(entry.getValue().getQueueHelper().pickRankS(ctx, player, turn, tempMatch)) {
											turn--;
											if(entry.getValue().getQueueHelper().getRankSplayers().size() == 0) {
												MatchDAO matchDAO = new MatchDAOImp();
												matchDAO.insertMatch(tempMatch);
											}
										} else {
											ctx.getMessage().addReaction(":exclamation:");
										}

									// if the 2nd part of the !Pick command is invalid
									} else {
										ctx.getMessage().addReaction(":exclamation:");
									}
								}
							}
						} else {
							ctx.getMessage().addReaction(":exclamation:");
						}
					}
				}).build();

		// Register the commands
		CommandRegistry registry = new CommandRegistry("!");
		registry.register(register, "Register");
		registry.register(register, "register");

		registry.register(update, "Update");
		registry.register(update, "update");

		registry.register(result, "Result");
		registry.register(result, "result");

		registry.register(rank, "Rating");
		registry.register(rank, "rating");

		registry.register(add, "Add");
		registry.register(add, "add");

		registry.register(remove, "Remove");
		registry.register(remove,"remove");

		registry.register(status, "Status");
		registry.register(status, "status");

		registry.register(help, "Help");
		registry.register(help, "help");

		//registry.register(pick, "Pick");
		//registry.register(pick, "pick");
		client.getDispatcher().registerListener(new CommandListener(registry));
	}
}