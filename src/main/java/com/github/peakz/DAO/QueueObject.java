package com.github.peakz.DAO;

import java.util.ArrayList;

public class QueueObject {

	private ArrayList<PlayerObject> PlayerPool = new ArrayList<>();

	public QueueObject() {
	}

	public QueueObject(ArrayList PlayerPool) {
		this.PlayerPool = PlayerPool;
	}

	public ArrayList<PlayerObject> getPlayerPool() {
		return PlayerPool;
	}

	public void setPlayerPool(ArrayList<PlayerObject> playerPool) {
		PlayerPool = playerPool;
	}
}
