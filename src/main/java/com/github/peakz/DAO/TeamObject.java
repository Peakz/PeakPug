package com.github.peakz.DAO;

public class TeamObject {

	private int team_id;
	private String color;
	private PlayerObject captain;
	private PlayerObject player_1;
	private PlayerObject player_2;
	private PlayerObject player_3;
	private PlayerObject player_4;
	private PlayerObject player_5;

	public TeamObject() {
	}

	public TeamObject(int team_id, String color, PlayerObject captain) {
		this.team_id = team_id;
		this.color = color;
		this.captain = captain;
	}

	public TeamObject(PlayerObject captain, PlayerObject player_1, PlayerObject player_2, PlayerObject player_3, PlayerObject player_4, PlayerObject player_5) {
		this.captain = captain;
		this.player_1 = player_1;
		this.player_2 = player_2;
		this.player_3 = player_3;
		this.player_4 = player_4;
		this.player_5 = player_5;
	}

	public int getTeam_id() {
		return team_id;
	}

	public void setTeam_id(int team_id) {
		this.team_id = team_id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public PlayerObject getCaptain() {
		return captain;
	}

	public void setCaptain(PlayerObject captain) {
		this.captain = captain;
	}

	public PlayerObject getPlayer_1() {
		return player_1;
	}

	public void setPlayer_1(PlayerObject player_1) {
		this.player_1 = player_1;
	}

	public PlayerObject getPlayer_2() {
		return player_2;
	}

	public void setPlayer_2(PlayerObject player_2) {
		this.player_2 = player_2;
	}

	public PlayerObject getPlayer_3() {
		return player_3;
	}

	public void setPlayer_3(PlayerObject player_3) {
		this.player_3 = player_3;
	}

	public PlayerObject getPlayer_4() {
		return player_4;
	}

	public void setPlayer_4(PlayerObject player_4) {
		this.player_4 = player_4;
	}

	public PlayerObject getPlayer_5() {
		return player_5;
	}

	public void setPlayer_5(PlayerObject player_5) {
		this.player_5 = player_5;
	}

	public int getAvgRating() {
		return ((captain.getRating() + player_1.getRating() + player_2.getRating() + player_3.getRating() + player_4.getRating() + player_5.getRating()) / 6);
	}
}
