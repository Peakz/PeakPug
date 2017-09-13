package com.github.peakz.DAO;

public interface TeamDAO {
	TeamObject getTeam(int match_id, String color);
	void insertTeam(TeamObject team);
}
