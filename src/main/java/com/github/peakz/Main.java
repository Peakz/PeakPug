package com.github.peakz;

import com.github.peakz.DAO.ConnectionFactory;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;

import java.sql.Connection;

public class Main {
	private static Connection connection = ConnectionFactory.getConnection();

	public static void main(String[] args) {
		IDiscordClient client = PugBot.createClient(args[0], true); // Gets the client object (from the first example)
		EventDispatcher dispatcher = client.getDispatcher(); // Gets the EventDispatcher instance for this client instance
		dispatcher.registerListener(new InterfaceListener()); // Registers the IListener example class from above
		dispatcher.registerListener(new AnnotationListener()); // Registers the @EventSubscriber example class from above

		PugBot.createCommands(client);
	}
}