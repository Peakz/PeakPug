package com.github.peakz.DAO;

public class TeamObject {

	private int team_id;
	private String color;
	private PlayerObject[] players;
	private PlayerObject captain;
	private PlayerObject player_1;
	private PlayerObject player_2;
	private PlayerObject player_3;
	private PlayerObject player_4;
	private PlayerObject player_5;

	private boolean gotMT = false;
	private boolean gotFT = false;
	private boolean gotHS = false;
	private boolean gotPJ = false;
	private boolean gotFS = false;
	private boolean gotMS = false;

	/**
	 * Empty constructor
	 */
	public TeamObject() {
	}

	public TeamObject(PlayerObject[] players) {
		this.players = players;
		this.captain = players[0];
		this.player_1 = players[1];
		this.player_2 = players[2];
		this.player_3 = players[3];
		this.player_4 = players[4];
		this.player_5 = players[5];
	}

	/**
	 * Constructor with parameters, only team_id, team color and captain
	 *
	 * @param team_id
	 * @param color
	 * @param captain
	 */
	public TeamObject(int team_id, String color, PlayerObject captain) {
		this.team_id = team_id;
		this.color = color;
		this.captain = captain;
	}

	/**
	 * Constructor for a colored team with captain and players
	 *
	 * @param captain
	 * @param player_1
	 * @param player_2
	 * @param player_3
	 * @param player_4
	 * @param player_5
	 */
	public TeamObject(String color, PlayerObject captain, PlayerObject player_1, PlayerObject player_2, PlayerObject player_3, PlayerObject player_4, PlayerObject player_5) {
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

	public PlayerObject[] getPlayers() {
		return players;
	}

	public void setPlayersNumber(PlayerObject player) {
		if (player_1 == null) {
			player_1 = player;
		} else if (player_2 == null) {
			player_2 = player;
		} else if (player_3 == null) {
			player_3 = player;
		} else if (player_4 == null) {
			player_4 = player;
		} else if (player_5 == null) {
			player_5 = player;
		}
	}

	public int getAvgRating() {
		return ((captain.getRating() + player_1.getRating() + player_2.getRating() + player_3.getRating() + player_4.getRating() + player_5.getRating()) / 6);
	}

	public int checkEmptySlot() {
		int i = -1;

		if (captain == null) {
			i = 0;
			return i;
		} else if (player_1 == null) {
			i = 1;
			return i;
		} else if (player_2 == null) {
			i = 2;
			return i;
		} else if (player_3 == null) {
			i = 3;
			return i;
		} else if (player_4 == null) {
			i = 4;
			return i;
		} else if (player_5 == null) {
			i = 5;
			return i;
		}
		return i;
	}

	public String checkEmptyRole(PlayerObject p) {
		if (!gotMT && p.getRoleFlag().equals("mtank")) {
			gotMT = true;
			return "mtank";
		} else if (!gotFT && p.getRoleFlag().equals("ftank")) {
			gotFT = true;
			return "ftank";
		} else if (!gotHS && p.getRoleFlag().equals("hitscan")) {
			gotHS = true;
			return "hitscan";
		} else if (!gotPJ && p.getRoleFlag().equals("projectile")) {
			gotPJ = true;
			return "projectile";
		} else if (!gotFS && p.getRoleFlag().equals("fsupp")) {
			gotFS = true;
			return "fsupp";
		} else if (!gotMS && p.getRoleFlag().equals("msupp")) {
			gotMS = true;
			return "msupp";
		}
		return null;
	}

	public PlayerObject withRole(String role) {
		for (PlayerObject p : players) {
			if (p.getRoleFlag().equals(role)) {
				return p;
			}
		}
		return null;
	}
}
