package com.github.peakz;

import com.darichey.discord.Command;
import com.darichey.discord.CommandListener;
import com.darichey.discord.CommandRegistry;
import com.github.peakz.DAO.PlayerDAO;
import com.github.peakz.DAO.PlayerDAOImp;
import com.github.peakz.commands.*;
import com.github.peakz.queues.QueueHelper;
import com.github.peakz.queues.QueueManager;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;

import java.util.HashMap;
import java.util.Map;

public class PugBot extends Main {
	public static Map<IGuild, QueueManager> queueInstances = new HashMap<>();
	// public static Map<IChannel, QueueManager> channelInstances = new HashMap<>();

	private static PlayerDAO playerDAO = new PlayerDAOImp();

	PugBot(IDiscordClient client) {
		super(client);
	}

	static void createCommands(IDiscordClient client) {
		Command register = Command.builder().onCalled(RegisterCommand::new).build();

		// Create command for updating roles in a player profile
		Command update = Command.builder().onCalled(ctx -> {
			for(Map.Entry<IGuild, QueueManager> entry : queueInstances.entrySet()) {
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

			QueueManager queueManager = queueInstances.get(ctx.getGuild());
			AddCommand addInstance = new AddCommand(ctx, queueManager);

			switch(strArr[1].toUpperCase()) {
				case "SOLOQ":
					addInstance.addToMode(strArr[1].toUpperCase());
					break;

				case "RANKS":
					addInstance.addToMode(strArr[1].toUpperCase());
					break;

				case "DUOQ":
					ctx.getChannel().sendMessage("DuoQ is not yet implemented. Check back later!");
					break;

				default:
					ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " wrong mode");
					break;
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

			QueueManager queueManager = queueInstances.get(ctx.getGuild());
			RemoveCommand removeCommand = new RemoveCommand(ctx, queueManager);

			switch(strArr[1].toUpperCase()) {
				case "SOLOQ":
					removeCommand.removeFromMode(strArr[1].toUpperCase());
					break;

				case "RANKS":
					removeCommand.removeFromMode(strArr[1].toUpperCase());
					break;

				case "DUOQ":
					ctx.getChannel().sendMessage("DuoQ is not yet implemented. Check back later!");
					break;

				default:
					ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " wrong mode");
					break;
			}
		}).build();

		Command result = Command.builder().onCalled(ctx -> {
			QueueManager queueManager = queueInstances.get(ctx.getGuild());
			new ResultCommand(ctx, queueManager);
		}).build();

		Command rank = Command.builder().onCalled(ctx -> {
			ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " you rating is " + playerDAO.getPlayer(ctx.getAuthor().getStringID()).getRating());
		}).build();

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

			QueueManager queueManager = queueInstances.get(ctx.getGuild());
			switch(strArr[1].toUpperCase()) {
				case "SOLOQ":
					QueueHelper queueHelper = queueManager.getQueueHelper("SOLOQ");
					if (queueHelper.getPlayers().size() == 1) {
						ctx.getMessage().getChannel().sendMessage("" + queueHelper.getPlayers().size() + " player in queue");
					} else {
						ctx.getMessage().getChannel().sendMessage("" + queueHelper.getPlayers().size() + " players in queue");
					}
					break;

				case "RANKS":
					queueHelper = queueManager.getQueueHelper("RANKS");
					if(queueHelper.pickedUsers.size() > 1) {

					}
					if (queueHelper.getPlayers().size() == 1) {
						ctx.getMessage().getChannel().sendMessage("" + queueHelper.getRankSplayers().size() + " player in queue");
					} else {
						ctx.getMessage().getChannel().sendMessage("" + queueHelper.getRankSplayers().size() + " players in queue");
					}
					break;

				default:
					ctx.getMessage().getChannel().sendMessage("wrong mode");
			}
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

					PickCommand pickInstance = new PickCommand(ctx, queueInstances.get(ctx.getGuild()));
					pickInstance.pickPlayer();
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

		registry.register(pick, "Pick");
		registry.register(pick, "pick");
		client.getDispatcher().registerListener(new CommandListener(registry));
	}
}