package com.github.peakz.stateHandler;

import sx.blah.discord.api.IDiscordClient;

public interface WrapperInterface {
	void wrapGuilds(IDiscordClient client);
	void wrapChannels();
}
