package com.github.peakz.DAO;

public class MatchObject {

	private TeamObject team_blue;
	private TeamObject team_red;
	private int id;
	// true for red win, false for blue win
	private String winner;
	private String map;

	public MatchObject() {
	}

	public MatchObject(TeamObject team_blue, TeamObject team_red, String map) {
		this.team_blue = team_blue;
		this.team_red = team_red;
		this.map = map;
	}

	public TeamObject getTeam_blue() {
		return team_blue;
	}

	public void setTeam_blue(TeamObject team_blue) {
		this.team_blue = team_blue;
	}

	public TeamObject getTeam_red() {
		return team_red;
	}

	public void setTeam_red(TeamObject team_red) {
		this.team_red = team_red;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}
}
