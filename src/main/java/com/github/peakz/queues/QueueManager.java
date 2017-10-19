package com.github.peakz.queues;

import java.util.HashMap;
import java.util.Map;

public class QueueManager {
	public Map<String, QueueHelper> queueInstances;

	public QueueManager(){
		this.queueInstances = new HashMap<>();
	}

	public void addQueueHelperInstances() {
		queueInstances.put("SOLOQ", new QueueHelper());
		queueInstances.put("RANKS", new QueueHelper());
		queueInstances.put("DUOQ", new QueueHelper());
		createQueueHelpers();
	}

	private void createQueueHelpers() {
		String[] modes = new String[] {"SOLOQ", "RANKS", "DUOQ"};
		for(String mode : modes) {
			getQueueHelper(mode).setQueueManager(this);
		}
	}

	public QueueHelper getQueueHelper(String mode) {
		return queueInstances.get(mode);
	}

	public void setQueueHelper(String mode) {
		queueInstances.remove(mode);
		queueInstances.put(mode, new QueueHelper());
	}
}
