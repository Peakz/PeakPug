package com.github.peakz.DAO;

public class MatchObject {

	private TeamObject team_blue;
	private TeamObject team_red;

	public MatchObject() {
	}

	public MatchObject(TeamObject team_blue, TeamObject team_red) {
		this.team_blue = team_blue;
		this.team_red = team_red;
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
}
