package com.github.peakz.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class TeamDAOImp implements TeamDAO {

	@Override
	public TeamObject getTeam(String match_id, String color) {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT * FROM team WHERE match.match_id=" + match_id + " AND team.team_color=" + color);

			if(rs.next()) {
				TeamObject team = new TeamObject();
				PlayerDAO playerDAO = new PlayerDAOImp();

				team.setTeam_id(rs.getInt("team_id"));
				team.setCaptain(playerDAO.getPlayer(rs.getString("captain")));
				team.setColor(rs.getString("color"));

				con.close();
				return team;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void insertTeam(String id, String color) {
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement pst = con.prepareStatement(
					"INSERT INTO team "
							+ "(color, "
							+ "captain)"
							+ "VALUES (?, ?)");

			pst.setString(1, id);
			pst.setString(2, color);
			pst.executeUpdate();

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateTeam(String match_id, String color) {
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement pst = con.prepareStatement("UPDATE team SET captain = ?, color = ? WHERE match_id =" + match_id + " AND color=" + color);

			pst.setString(1, match_id);
			pst.setString(2, color);
			pst.executeUpdate();

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
