package com.github.peakz;

import com.darichey.discord.Command;
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
	static Map<IGuild, QueueManager> queueManagers = new HashMap<>();

	private static PlayerDAO playerDAO = new PlayerDAOImp();
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
					ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " something went wrong");
				}
			}
		}).build();

		Command add = Command.builder().onCalled(ctx -> {
			for(Map.Entry<IGuild, QueueManager> entry : queueManagers.entrySet()) {
				if(entry.getKey().equals(ctx.getGuild())) {
					new AddCommand(ctx, entry.getValue());
				} else {
					ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " something went wrong");
				}
			}
		}).build();

		Command remove = Command.builder().onCalled(ctx -> {
			for(Map.Entry<IGuild, QueueManager> entry : queueManagers.entrySet()) {
				if(entry.getKey().equals(ctx.getGuild())) {
					new RemoveCommand(ctx, entry.getValue());
				} else {
					ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " something went wrong");
				}
			}
		}).build();

		Command result = Command.builder().onCalled(ctx -> {
			for(Map.Entry<IGuild, QueueManager> entry : queueManagers.entrySet()) {
				if(entry.getKey().equals(ctx.getGuild())) {
					new ResultCommand(ctx, entry.getValue());
				} else {
					ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " something went wrong");
				}
			}
		}).build();

		Command rank = Command.builder()
				.onCalled(ctx -> ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " you rating is " + playerDAO.getPlayer(ctx.getAuthor().getStringID()).getRating()))
				.build();

		Command help = Command.builder().onCalled(HelpCommand::new).build();

		Command status = Command.builder().onCalled(ctx -> {
			for(Map.Entry<IGuild, QueueManager> entry : queueManagers.entrySet()) {
				if(entry.getKey().equals(ctx.getGuild())) {
					QueueHelper queueHelper = entry.getValue().getQueueHelper();
					if(queueHelper.getPlayers().size() == 1) {
						ctx.getMessage().getChannel().sendMessage("" + queueHelper.getPlayers().size() + " player in queue");
					} else {
						ctx.getMessage().getChannel().sendMessage("" + queueHelper.getPlayers().size() + " players in queue");
					}
				} else {
					ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " something went wrong");
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
		client.getDispatcher().registerListener(new CommandListener(registry));
	}

}