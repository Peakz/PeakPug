package com.github.peakz.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class TeamDAOImp implements TeamDAO {

	/**
	 * Get a team from the match that belongs to the match_id
	 * The team's color will be the one put in the parameter
	 *
	 * @param match_id
	 * @param color
	 * @return
	 */
	@Override
	public TeamObject getTeam(int match_id, String color) {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT " +
							"team.team_id, " +
							"team.color, " +
							"team.captain_id," +
							"team.player_1_id," +
							"team.player_2_id," +
							"team.player_3_id," +
							"team.player_4_id," +
							"team.player_5_id FROM team, pug_match WHERE team.team_id = pug_match.team_id_" + color + " AND pug_match.match_id =" + match_id);

			if (rs.next()) {
				TeamObject team = new TeamObject();
				PlayerDAO playerDAO = new PlayerDAOImp();

				team.setTeam_id(rs.getInt("team_id"));
				team.setColor(rs.getString("color"));
				team.setCaptain(playerDAO.getPlayer(rs.getString("captain_id")));
				team.setPlayer_1(playerDAO.getPlayer(rs.getString("player_1_id")));
				team.setPlayer_2(playerDAO.getPlayer(rs.getString("player_2_id")));
				team.setPlayer_3(playerDAO.getPlayer(rs.getString("player_3_id")));
				team.setPlayer_4(playerDAO.getPlayer(rs.getString("player_4_id")));
				team.setPlayer_5(playerDAO.getPlayer(rs.getString("player_5_id")));

				con.close();
				return team;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getLastTeamID() {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT team_id FROM team ORDER BY team_id DESC LIMIT 1");

			if (rs.next()) {
				int teamID = rs.getInt("team_id");
				con.close();
				return teamID;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Insert a TeamObject into the db, doesn't take whole object, just the string ids.
	 *
	 * @param team
	 */
	@Override
	public void insertTeam(TeamObject team) {
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement pst = con.prepareStatement("INSERT INTO team "
					+ "(color, "
					+ "captain_id,"
					+ "player_1_id,"
					+ "player_2_id,"
					+ "player_3_id,"
					+ "player_4_id,"
					+ "player_5_id)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)");

			pst.setString(1, team.getColor());
			pst.setString(2, team.getCaptain().getId());
			pst.setString(3, team.getPlayer_1().getId());
			pst.setString(4, team.getPlayer_2().getId());
			pst.setString(5, team.getPlayer_3().getId());
			pst.setString(6, team.getPlayer_4().getId());
			pst.setString(7, team.getPlayer_5().getId());

			pst.executeUpdate();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
