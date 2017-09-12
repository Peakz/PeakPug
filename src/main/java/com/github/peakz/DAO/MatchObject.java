package com.github.peakz.DAO;

import com.github.peakz.PugBot;

import java.util.ArrayList;

public class MatchObject {

	private TeamObject team_blue;
	private TeamObject team_red;
	private int id;
	// true for red win, false for blue win
	private boolean winner;
	private String map;
	private boolean recorded;

	private ArrayList<String> player_ids = new ArrayList<>();

	public MatchObject() {
	}

	public MatchObject(TeamObject team_blue, TeamObject team_red) {
		this.team_blue = team_blue;
		this.team_red = team_red;
		this.map = PugBot.selectMap();
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

	public boolean getWinner() {
		return winner;
	}

	public void setWinner(boolean winner) {
		this.winner = winner;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public ArrayList<String> getPlayer_ids() {
		return player_ids;
	}

	public void setPlayer_ids(ArrayList<String> player_ids) {
		this.player_ids = player_ids;
	}

	public boolean isRecorded() {
		return recorded;
	}

	public void setRecorded(boolean recorded) {
		this.recorded = recorded;
	}
}
