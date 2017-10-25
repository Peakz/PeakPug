package com.github.peakz;

import com.darichey.discord.Command;
import com.darichey.discord.CommandContext;
import com.darichey.discord.CommandListener;
import com.darichey.discord.CommandRegistry;
import com.github.peakz.DAO.PlayerDAO;
import com.github.peakz.DAO.PlayerDAOImp;
import com.github.peakz.commands.*;
import com.github.peakz.queues.QueueManager;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;

import java.util.HashMap;
import java.util.Map;

public class PugBot extends Main {
	public static Map<IGuild, QueueManager> queueInstances = new HashMap<>();
	// public static Map<IChannel, QueueManager> channelInstances = new HashMap<>();

	private static PlayerDAO playerDAO = new PlayerDAOImp();
	private static int k = 1;

	PugBot(IDiscordClient client) {
		super(client);
	}

	static void createCommands(IDiscordClient client) {
		Command register = Command.builder().onCalled(ctx -> {
			RegisterCommand registerCommand = new RegisterCommand(ctx);
			/*
			for(int i = 0; i < 12; i++) {
				PlayerObject player = playerDAO.getPlayer("" + (k));
				k++;
				System.out.println("id :" + player.getId() + "\nPrimary Role: " + player.getPrimaryRole().toLowerCase() + "\nSecondary Role: " + player.getSecondaryRole().toLowerCase());
				AddCommand add = new AddCommand(ctx, queueInstances.get(ctx.getGuild()));
				add.addToMode("RANKS", player);
			}
			*/
		}).build();

		// Create command for updating roles in a player profile
		Command update = Command.builder().onCalled(ctx -> {
			if(ctx.getChannel().getName().equals("propug")) {
				String strArr[] = getParts(ctx, 3);
				UpdateCommand updateCommand = new UpdateCommand(ctx);
				updateCommand.updatePlayer(strArr[1], strArr[2]);
			}
		}).build();

		Command add = Command.builder().onCalled(ctx -> {
			if(ctx.getChannel().getName().equals("propug") || ctx.getChannel().getName().equals("testing")) {
				String[] strArr = getParts(ctx, 2);
				String id = ctx.getAuthor().getStringID();

				if(playerDAO.checkId(id)) {
					QueueManager queueManager = queueInstances.get(ctx.getGuild());
					AddCommand addInstance = new AddCommand(ctx, queueManager);
					addInstance.addToMode(strArr[1].toUpperCase(), playerDAO.getPlayer(id));
				} else {
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " you're not registered. Type !help");
				}
			}
		}).build();

		Command remove = Command.builder().onCalled(ctx -> {
			if(ctx.getChannel().getName().equals("propug") || ctx.getChannel().getName().equals("testing")) {
				String[] strArr = getParts(ctx, 2);

				QueueManager queueManager = queueInstances.get(ctx.getGuild());
				RemoveCommand removeCommand = new RemoveCommand(ctx, queueManager);
				removeCommand.removeFromMode(strArr[1].toUpperCase());
			}
		}).build();

		Command result = Command.builder().onCalled(ctx -> {
			if(ctx.getChannel().getName().equals("propug") || ctx.getChannel().getName().equals("testing")) {
				String[] strArr = getParts(ctx, 3);
				QueueManager queueManager = queueInstances.get(ctx.getGuild());
				ResultCommand resultCommand = new ResultCommand(ctx, queueManager);
				resultCommand.checkWinner(Integer.parseInt(strArr[1]), strArr[2]);
			}
		}).build();

		Command rank = Command.builder().onCalled(ctx -> {
			if(ctx.getChannel().getName().equals("propug") || ctx.getChannel().getName().equals("testing")) {
				ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " you rating is " + playerDAO.getPlayer(ctx.getAuthor().getStringID()).getRating());
			}
		}).build();

		Command ranks = Command.builder().onCalled(ctx -> {
			if(ctx.getChannel().getName().equals("propug") || ctx.getChannel().getName().equals("testing")) {
				HelpCommand.rolesRankS(ctx);
			}
		}).build();

		Command help = Command.builder().onCalled(HelpCommand::new).build();

		Command status = Command.builder().onCalled(ctx -> {
			if(ctx.getChannel().getName().equals("propug") || ctx.getChannel().getName().equals("testing")) {
				String[] strArr = getParts(ctx, 2);
				StatusCommand statusCommand = new StatusCommand(ctx, queueInstances.get(ctx.getGuild()));
				statusCommand.showStatus("BOTH");
			}
		}).build();

		Command pick = Command.builder()
				.onCalled(ctx -> {
					if(ctx.getChannel().getName().equals("propug") || ctx.getChannel().getName().equals("testing")) {
						String[] strArr = getParts(ctx, 2);
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

		registry.register(ranks, "Role");
		registry.register(ranks, "role");
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