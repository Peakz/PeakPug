package com.github.peakz.DAO;

import java.util.ArrayList;

public interface PlayerDAO {
	ArrayList<PlayerObject> getPlayersSorted(ArrayList<PlayerObject> players);
	PlayerObject getPlayer(String id);
	void insertPlayer(PlayerObject player);
	void updatePlayer(PlayerObject player);
	void updateMMR(String id, int mmr, String status);
	void deletePlayer(PlayerObject player);
	boolean checkId(String id);
	boolean checkPrimaryRole(PlayerObject player);
	boolean checkSecondaryRole(PlayerObject player);
}
