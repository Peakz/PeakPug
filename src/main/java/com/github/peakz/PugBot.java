package com.github.peakz;

import com.darichey.discord.Command;
import com.darichey.discord.CommandContext;
import com.darichey.discord.CommandListener;
import com.darichey.discord.CommandRegistry;
import com.github.peakz.DAO.PlayerDAO;
import com.github.peakz.DAO.PlayerDAOImp;
import com.github.peakz.DAO.PlayerObject;
import com.github.peakz.commands.*;
import com.github.peakz.queues.QueueManager;
import com.github.peakz.queues.QueuePug;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;

import java.util.concurrent.ConcurrentHashMap;

public class PugBot extends Main {
	public static ConcurrentHashMap<IChannel, QueueManager> queueInstances = new ConcurrentHashMap<>();

	PugBot(IDiscordClient client) {
		super(client);
	}

	static void createCommands(IDiscordClient client) {
		PlayerDAO playerDAO = new PlayerDAOImp();

		Command register = Command.builder().onCalled(ctx -> {
			if (queueInstances.containsKey(ctx.getChannel())) {
				String[] strArr = getParts(ctx, 3);
				if (!playerDAO.checkId(ctx.getAuthor().getStringID())) {
					RegisterCommand registerCommand = new RegisterCommand(ctx);
					registerCommand.registerPlayer(strArr[1], strArr[2]);
				} else {
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You're already registered!");
				}
			} else {
				ctx.getMessage().getChannel().sendMessage("This channel's name doesn't contain \"pug \"");
			}
		}).build();

		// Create command for updating roles in a player profile
		Command update = Command.builder().onCalled(ctx -> {
			if (queueInstances.containsKey(ctx.getChannel())) {
				if(playerDAO.checkId(ctx.getAuthor().getStringID())) {
					String strArr[] = getParts(ctx, 3);
					if(strArr[1] == null || strArr[2] == null) {
						ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You forgot something wrong in that command");
					} else {
						UpdateCommand updateCommand = new UpdateCommand(ctx);
						updateCommand.updatePlayer(strArr[1], strArr[2]);
					}
				} else {
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You're not registered yet! Type !Help");
				}
			} else {
				ctx.getMessage().getChannel().sendMessage("This channel's name doesn't contain \"pug \"");
			}
		}).build();

		Command add = Command.builder().onCalled(ctx -> {
			if (queueInstances.containsKey(ctx.getChannel())) {
				String[] strArr = getParts(ctx, 2);
				if(strArr[1] == null) {
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You forgot something wrong in that command");
				} else {
					String id = ctx.getAuthor().getStringID();
					if (playerDAO.checkId(id)) {
						QueueManager queueManager = queueInstances.get(ctx.getChannel());
						PlayerObject player = playerDAO.getPlayer(id);

						AddCommand addInstance = new AddCommand(ctx, queueManager);
						addInstance.addToMode(strArr[1].toUpperCase(), player);
					} else {
						ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " you're not registered. Type !help");
					}
				}
			}
		}).build();

		Command remove = Command.builder().onCalled(ctx -> {
			if (queueInstances.containsKey(ctx.getChannel())) {
				String[] strArr = getParts(ctx, 2);
				if(strArr[1] == null) {
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You forgot something wrong in that command");
				} else {
					QueueManager queueManager = queueInstances.get(ctx.getChannel());
					RemoveCommand removeCommand = new RemoveCommand(ctx, queueManager);
					removeCommand.removeFromMode(strArr[1].toUpperCase());
				}
			} else {
				ctx.getMessage().getChannel().sendMessage("This channel's name doesn't contain \"pug \"");
			}
		}).build();

		Command result = Command.builder().onCalled(ctx -> {
			if (queueInstances.containsKey(ctx.getChannel())) {
				String[] strArr = getParts(ctx, 3);
				QueueManager queueManager = queueInstances.get(ctx.getChannel());
				ResultCommand resultCommand = new ResultCommand(ctx, queueManager);
				resultCommand.checkWinner(Integer.parseInt(strArr[1]), strArr[2]);
			} else {
				ctx.getMessage().getChannel().sendMessage("This channel's name doesn't contain \"pug \"");
			}
		}).build();

		Command rank = Command.builder().onCalled(ctx -> {
			ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " you rating is " + playerDAO.getPlayer(ctx.getAuthor().getStringID()).getRating());
		}).build();

		Command ranks = Command.builder().onCalled(HelpCommand::rolesRankS).build();

		Command help = Command.builder().onCalled(HelpCommand::new).build();

		Command status = Command.builder().onCalled(ctx -> {
			if (queueInstances.containsKey(ctx.getChannel())) {
				StatusCommand statusCommand = new StatusCommand(ctx, queueInstances.get(ctx.getChannel()));
				statusCommand.showStatus("BOTH");
			} else {
				ctx.getMessage().getChannel().sendMessage("There's no channel name that contains \"pug \"");
			}
		}).build();

		Command pick = Command.builder()
				.onCalled(ctx -> {
					if (queueInstances.containsKey(ctx.getChannel())) {
						String[] strArr = getParts(ctx, 2);
						if(strArr[1] == null) {
							ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You forgot something wrong in that command");
						} else {
							QueueManager queueManager = queueInstances.get(ctx.getChannel());
							PickCommand pickInstance = new PickCommand(ctx, queueManager);
							queueManager.getQueuePug(ctx.getChannel(), "RANKS");
							pickInstance.pickPlayer(strArr[1]);
						}
					} else {
						ctx.getMessage().getChannel().sendMessage("There's no channel name that contains \"pug \"");
					}
				}).build();

		Command wiki = Command.builder().onCalled(ctx -> ctx.getMessage().getChannel().sendMessage(new EmbedBuilder()
				.withTitle("Wiki")
				.withUrl("https://github.com/Peakz/PeakPug/wiki")
				.withDesc("A wiki page about the bot on GitHub.com")
				.withColor(250, 117, 255)
				.build())).build();

		Command queued = Command.builder().onCalled(ctx -> {
			if (queueInstances.containsKey(ctx.getChannel())) {

				QueuePug qpugSolo = queueInstances.get(ctx.getChannel()).getQueuePug(ctx.getChannel(), "SOLOQ");
				String strSolo = "";
				if(qpugSolo.queuedPlayers.size() > 0) {
					for (PlayerObject p : qpugSolo.queuedPlayers) {
						strSolo += "`" + client.getUserByID(Long.valueOf(p.getId())).getName() + " - " + p.getPrimaryRole() + " & " + p.getSecondaryRole() + "`\n";
					}
				} else strSolo = "None";

				QueuePug qpugRanks = queueInstances.get(ctx.getChannel()).getQueuePug(ctx.getChannel(), "RANKS");
				String strRanks = "";
				if(qpugRanks.queuedPlayers.size() > 0) {
					for (PlayerObject p : qpugRanks.queuedPlayers) {
						strRanks += "`" + client.getUserByID(Long.valueOf(p.getId())).getName() + " - " + p.getPrimaryRole() + " & " + p.getSecondaryRole() + "`\n";
					}
				} else strRanks = "None";

				ctx.getMessage().getChannel().sendMessage(new EmbedBuilder()
						.withTitle("Queued Players")
						.withDesc("SoloQ & Rank S")
						.withColor(239, 225, 28)
						.appendField("SoloQ", strSolo, false)
						.appendField("Rank S", strRanks, false)
						.build());

			} else {
				ctx.getMessage().getChannel().sendMessage("This channel's name doesn't contain \"pug \"");
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
		registry.register(remove, "remove");

		registry.register(status, "Status");
		registry.register(status, "status");

		registry.register(help, "Help");
		registry.register(help, "help");

		registry.register(pick, "Pick");
		registry.register(pick, "pick");

		registry.register(ranks, "Role");
		registry.register(ranks, "role");

		registry.register(wiki, "Wiki");
		registry.register(wiki, "wiki");

		registry.register(queued, "Queued");
		registry.register(queued, "queued");
		client.getDispatcher().registerListener(new CommandListener(registry));
	}

	private static String[] getParts(CommandContext ctx, int parts) {
		// Get the mode by splitting strings
		String[] strArr = new String[parts];
		int i = 0;

		// Store the split strings
		for (String val : ctx.getMessage().getContent().split(" ", parts)) {
			strArr[i] = val;
			i++;
		}
		return strArr;
	}
}