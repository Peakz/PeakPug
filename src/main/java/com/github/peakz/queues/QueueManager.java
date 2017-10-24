package com.github.peakz.queues;

import java.util.HashMap;

public class QueueManager {
	public HashMap<String, QueuePug> queueInstances;

	public QueueManager() {
		this.queueInstances = new HashMap<>();
	}

	public void addQueueHelperInstances() {
		queueInstances.put("SOLOQ", new QueuePug());
		queueInstances.put("RANKS", new QueuePug());
		//queueInstances.put("DUOQ", new QueuePug());
	}

	public QueuePug getQueuePug(String mode) {
		return queueInstances.get(mode);
	}
}
