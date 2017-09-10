package com.github.peakz;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;

public class InterfaceListener {

	/**
	 * This class will have its handle() method called when a ReadyEvent is dispatched by Discord4J.
	 */
	public static class IListenerReady implements IListener<ReadyEvent> {

		@Override
		public void handle(ReadyEvent event) { // This is called when the ReadyEvent is dispatched

			event.getClient().changePlayingText("with nothing");
		}
	}
}