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
			if(ctx.getChannel().getName().equals("propug")) {
				for (Map.Entry<IGuild, QueueManager> entry : queueInstances.entrySet()) {
					if (entry.getKey().equals(ctx.getGuild())) {
						new UpdateCommand(ctx);
					}
				}
			}
		}).build();

		Command add = Command.builder().onCalled(ctx -> {
			if(ctx.getChannel().getName().equals("propug")) {
				// Get the mode by splitting strings
				String[] strArr = new String[2];
				int i = 0;

				// Store the split strings
				for (String val : ctx.getMessage().getContent().split(" ", 2)) {
					strArr[i] = val;
					i++;
				}

				QueueManager queueManager = queueInstances.get(ctx.getGuild());
				AddCommand addInstance = new AddCommand(ctx, queueManager);

				switch (strArr[1].toUpperCase()) {
					case "SOLOQ":
						addInstance.addToMode("SOLOQ");
						break;

					case "RANKS":
						addInstance.addToMode("RANKS");
						break;

					case "DUOQ":
						ctx.getChannel().sendMessage("DuoQ is not yet implemented. Check back later!");
						break;

					default:
						ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " wrong mode");
						break;
				}
			}
		}).build();

		Command remove = Command.builder().onCalled(ctx -> {
			if(ctx.getChannel().getName().equals("propug")) {
				// Get the mode by splitting strings
				String[] strArr = new String[2];
				int i = 0;

				// Store the split strings
				for (String val : ctx.getMessage().getContent().split(" ", 2)) {
					strArr[i] = val;
					i++;
				}

				QueueManager queueManager = queueInstances.get(ctx.getGuild());
				RemoveCommand removeCommand = new RemoveCommand(ctx, queueManager);

				switch (strArr[1].toUpperCase()) {
					case "SOLOQ":
						if(removeCommand.removeFromMode("SOLOQ")) {
							ctx.getMessage().addReaction(":white_check_mark:");
						}
						break;

					case "RANKS":
						if(removeCommand.removeFromMode("RANKS")) {
							ctx.getMessage().addReaction(":white_check_mark:");
						}
						break;

					case "DUOQ":
						ctx.getChannel().sendMessage("DuoQ is not yet implemented. Check back later!");
						break;

					default:
						ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " wrong mode");
						break;
				}
			}
		}).build();

		Command result = Command.builder().onCalled(ctx -> {
			if(ctx.getChannel().getName().equals("propug")) {
				QueueManager queueManager = queueInstances.get(ctx.getGuild());
				new ResultCommand(ctx, queueManager);
			}
		}).build();

		Command rank = Command.builder().onCalled(ctx -> {
			if(ctx.getChannel().getName().equals("propug")) {
				ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " you rating is " + playerDAO.getPlayer(ctx.getAuthor().getStringID()).getRating());
			}
		}).build();

		Command help = Command.builder().onCalled(HelpCommand::new).build();

		Command status = Command.builder().onCalled(ctx -> {
			if(ctx.getChannel().getName().equals("propug")) {
				// Get the mode by splitting strings
				String[] strArr = new String[2];
				int i = 0;

				// Store the split strings
				for (String val : ctx.getMessage().getContent().split(" ", 2)) {
					strArr[i] = val;
					i++;
				}

				QueueManager queueManager = queueInstances.get(ctx.getGuild());
				QueueHelper queueHelper = queueManager.getQueueHelper("SOLOQ");
				String str = "";
				str += "[SoloQ: " + queueHelper.getPlayers().size() + "] ";
				queueHelper = queueManager.getQueueHelper("RANKS");
				str += "[RankS: " + queueHelper.getPlayers().size() + "]";
				ctx.getMessage().getChannel().sendMessage(str);
			}
		}).build();

		Command pick = Command.builder()
				.onCalled(ctx -> {
					if(ctx.getChannel().getName().equals("propug")) {
						// Get the mode by splitting strings
						String[] strArr = new String[2];
						int i = 0;

						// Store the split strings
						for (String val : ctx.getMessage().getContent().split(" ", 2)) {
							strArr[i] = val;
							i++;
						}

						QueueManager queueManager = queueInstances.get(ctx.getGuild());
						PickCommand pickInstance = new PickCommand(ctx, queueManager);
						pickInstance.pickPlayer(strArr[1]);
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

		registry.register(pick, "Pick");
		registry.register(pick, "pick");
		client.getDispatcher().registerListener(new CommandListener(registry));
	}
}