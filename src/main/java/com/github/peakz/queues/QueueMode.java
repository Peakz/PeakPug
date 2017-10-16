package com.github.peakz.queues;

import com.darichey.discord.CommandContext;

public class QueueMode {
	private CommandContext ctx;
	private QueueManager queueManager;
	private Mode mode;

	public enum Mode {
		SOLOQ,
		RANKS,
		DUOQ
	}



	public QueueMode(Mode mode, CommandContext ctx, QueueManager queueManager) {
		this.mode = mode;
		this.ctx = ctx;
		this.queueManager = queueManager;
	}

	public void ChooseMode() {
		switch(mode) {
			case SOLOQ:

				break;

			case RANKS:
				break;

			case DUOQ:
				break;

			default:
				break;
		}
	}

	private void addToQueue() {

	}
}
