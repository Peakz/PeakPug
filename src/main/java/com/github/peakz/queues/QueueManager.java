package com.github.peakz.queues;

import java.util.HashMap;
import java.util.Map;

public class QueueManager {
	public Map<String, QueueHelper> queueInstances = new HashMap<>();

	public QueueManager(){
	}

	public void addQueueHelperInstances() {
		queueInstances.put("SOLOQ", new QueueHelper());
		queueInstances.put("RANKS", new QueueHelper());
		queueInstances.put("DUOQ", new QueueHelper());
	}

	public void createQueueHelpers() {
		String[] modes = new String[] {"SOLOQ", "RANKS", "DUOQ"};
		for(String mode : modes) {
			QueueHelper queueHelper = new QueueHelper();
			queueInstances.put(mode, queueHelper);
			//getQueueHelper(mode).setQueueManager(this);
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
