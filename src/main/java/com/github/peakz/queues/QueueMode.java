package com.github.peakz.queues;

public class QueueMode {
	public enum Mode {
		SOLOQ,
		RANKS,
		DUOQ
	}

	private Mode mode;

	public QueueMode(Mode mode) {
		this.mode = mode;
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

}
