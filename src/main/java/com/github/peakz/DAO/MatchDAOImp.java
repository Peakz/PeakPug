package com.github.peakz.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MatchDAOImp implements MatchDAO{

	@Override
	public ArrayList<MatchObject> getAllMatches(ArrayList<MatchObject> matches) {
		return null;
	}

	@Override
	public int getLastMatchID() {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT match_id FROM pug_match ORDER BY match_id DESC LIMIT 1");

			if (rs.next()) {
				int matchid = rs.getInt("match_id");
				con.close();
				return matchid;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public MatchObject getMatch(int match_id) {
		Connection con = ConnectionFactory.getConnection();

		TeamDAO teamDAO = new TeamDAOImp();

		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT * FROM pug_match WHERE match_id=" + match_id);

			if (rs.next()) {
				MatchObject match = new MatchObject();
				match.setId(rs.getInt("match_id"));
				match.setMap(rs.getString("map"));
				match.setTeam_blue(teamDAO.getTeam(match_id, "blue"));
				match.setTeam_blue(teamDAO.getTeam(match_id, "red"));
				match.setWinner(rs.getString("winner"));

				con.close();
				return match;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void insertMatch(MatchObject match) {
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement pst = con.prepareStatement(
					"INSERT INTO pug_match "
							+ "(match_id, "
							+ "map)"
							+ "VALUES (?, ?)");

			if(match.getId() == 0)
			pst.setInt(1, match.getId());
			pst.setString(2, match.getMap());
			pst.executeUpdate();

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateMatch(MatchObject match) {
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement pst = con.prepareStatement("UPDATE pug_match SET winner = ? WHERE pug_id =" + match.getId() + " AND match_verification.matchID = " + match.getId());
			pst.setString(1, match.getWinner());
			pst.executeUpdate();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete a match
	 *
	 * @param match
	 */
	@Override
	public void deleteMatch(MatchObject match) {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			st.executeUpdate("DELETE FROM pug_match WHERE pug_id=" + match.getId());
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
