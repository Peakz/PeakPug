package com.github.peakz.DAO;

import java.util.List;

public interface PlayerDAO {
	List<PlayerObject> getAllPlayers();
	PlayerObject getPlayer(String id);
	void insertPlayer(PlayerObject player);
	void updatePlayer(PlayerObject player);
	void deletePlayer(PlayerObject player);
	boolean checkId(String id);
	boolean checkPrimaryRole(PlayerObject player);
	boolean checkSecondaryRole(PlayerObject player);
}
