package com.github.peakz;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

public class Client {
	public static IDiscordClient getClient(String token, boolean login) throws DiscordException { // Returns an instance of the Discord client
		ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
		clientBuilder.withToken(token); // Adds the login info to the builder
		if (login) {
			return clientBuilder.login(); // Creates the client instance and logs the client in
		} else {
			return clientBuilder.build(); // Creates the client instance but it doesn't log the client in yet, you would have to call client.login() yourself
		}
	}

}
