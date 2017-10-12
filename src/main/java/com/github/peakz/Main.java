package com.github.peakz;

import com.github.peakz.queues.QueueManager;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.DiscordException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
	public static Main INSTANCE; // Singleton instance of the bot.
	public IDiscordClient client; // The instance of the discord client.

	public static Map<IGuild, QueueManager> pugInstances = new ConcurrentHashMap<>();

	public static void main(String[] args) {
		INSTANCE = login(args[0]); // Gets the client object (from the first example)
		EventDispatcher dispatcher = INSTANCE.client.getDispatcher(); // Gets the EventDispatcher instance for this client instance
		dispatcher.registerListener(new InterfaceListener()); // Registers the IListener example class from above
		dispatcher.registerListener(new AnnotationListener()); // Registers the @EventSubscriber example class from above
		PugBot.createCommands(INSTANCE.client);
	}

	public Main(IDiscordClient client) {
		this.client = client;
	}

	public static Main login(String token) {
		Main main;

		ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
		clientBuilder.withToken(token); // Adds the login info to the builder
		try {
			IDiscordClient client = clientBuilder.login();
			main = new PugBot(client);
		} catch (DiscordException e) { // This is thrown if there was a problem building the client
			System.err.println("Error occurred while logging in!");
			e.printStackTrace();
			return null;
		}

		return main;
	}
}