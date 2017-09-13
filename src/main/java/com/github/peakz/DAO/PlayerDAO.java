package com.github.peakz.DAO;

import java.util.ArrayList;

public interface PlayerDAO {
	PlayerObject getPlayer(String player_id);
	ArrayList<PlayerObject> getPlayersTeam(int team_id);
	void insertPlayer(PlayerObject player);
	void updatePlayer(PlayerObject player);
	void deletePlayer(PlayerObject player);
	boolean checkId(String player_id);
	boolean checkRoles(String player_id, String primaryRole, String secondaryRole);
	int checkMMR(String player_id);
}
