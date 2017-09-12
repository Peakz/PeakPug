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
	public MatchObject getLastMatchID() {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT pug_id FROM pug_match ORDER BY pug_id DESC LIMIT 1");

			if (rs.next()) {
				MatchObject match = new MatchObject();
				match.setId(rs.getInt("pug_id"));
				con.close();
				return match;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public MatchObject getMatch(String id) {
		Connection con = ConnectionFactory.getConnection();

		ArrayList<String> player_ids = new ArrayList<>();

		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT pug_id, winner FROM pug_match WHERE pug_id=" + id);

			if (rs.next()) {
				MatchObject match = new MatchObject();
				match.setId(rs.getInt("pug_id"));
				match.setWinner(rs.getBoolean("winner"));

				player_ids.add(rs.getString("blue_1"));
				player_ids.add(rs.getString("blue_2"));
				player_ids.add(rs.getString("blue_3"));
				player_ids.add(rs.getString("blue_4"));
				player_ids.add(rs.getString("blue_5"));
				player_ids.add(rs.getString("blue_6"));
				player_ids.add(rs.getString("red_1"));
				player_ids.add(rs.getString("red_2"));
				player_ids.add(rs.getString("red_3"));
				player_ids.add(rs.getString("red_4"));
				player_ids.add(rs.getString("red_5"));
				player_ids.add(rs.getString("red_6"));

				match.setPlayer_ids(player_ids);

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
							+ "(blue_1, "
							+ "blue_2,"
							+ "blue_3,"
							+ "blue_4,"
							+ "blue_5,"
							+ "blue_6,"
							+ "red_1,"
							+ "red_2,"
							+ "red_3,"
							+ "red_4,"
							+ "red_5,"
							+ "red_6,"
							+ "map,"
							+ "recorded)"
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			pst.setString(1, match.getTeam_blue().getCaptain().getId());
			pst.setString(2, match.getTeam_blue().getPlayer_1().getId());
			pst.setString(3, match.getTeam_blue().getPlayer_2().getId());
			pst.setString(4, match.getTeam_blue().getPlayer_3().getId());
			pst.setString(5, match.getTeam_blue().getPlayer_4().getId());
			pst.setString(6, match.getTeam_blue().getPlayer_5().getId());

			pst.setString(7, match.getTeam_red().getCaptain().getId());
			pst.setString(8, match.getTeam_red().getPlayer_1().getId());
			pst.setString(9, match.getTeam_red().getPlayer_2().getId());
			pst.setString(10, match.getTeam_red().getPlayer_3().getId());
			pst.setString(11, match.getTeam_red().getPlayer_4().getId());
			pst.setString(12, match.getTeam_red().getPlayer_5().getId());

			pst.setString(13, match.getMap());
			pst.setBoolean(14, match.isRecorded());

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
			PreparedStatement pst = con.prepareStatement("UPDATE pug_match SET winner = ?, recorded = ? WHERE pug_id =" + match.getId());
			pst.setBoolean(1, match.getWinner());
			pst.setBoolean(2, match.isRecorded());
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
