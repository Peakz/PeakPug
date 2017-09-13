package com.github.peakz.DAO;

public interface TeamDAO {
	TeamObject getTeam(String match_id, String color);
	void insertTeam(String id, String color);
	void updateTeam(String id, String color);
}
