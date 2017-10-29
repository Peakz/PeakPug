package com.github.peakz;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;

public class Main {
	public IDiscordClient client; // The instance of the discord client.

	public Main(IDiscordClient client) {
		this.client = client;
	}

	public static void main(String[] args) {
		Main INSTANCE = login(args[0]);
		EventDispatcher dispatcher = INSTANCE.client.getDispatcher(); // Gets the EventDispatcher instance for this client instance
		dispatcher.registerListener(new AnnotationListener()); // Registers the @EventSubscriber example class from above
		PugBot.createCommands(INSTANCE.client);
	}

	public static Main login(String token) {
		Main main;

		ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
		clientBuilder.withToken(token); // Adds the login info to the builder
		try {
			IDiscordClient client = clientBuilder.login();
			main = new PugBot(client);
		} catch (DiscordException e) { // This is thrown if there was a problem building the client
			e.printStackTrace();
			return null;
		}

		return main;
	}
}