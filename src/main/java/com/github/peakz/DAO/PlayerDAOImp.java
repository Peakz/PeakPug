package com.github.peakz.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PlayerDAOImp implements PlayerDAO {

	/**
	 * Get PlayerObject for a player and the id they belong to
	 *
	 * @param player_id
	 * @return player
	 */
	@Override
	public PlayerObject getPlayer(String player_id) {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT * FROM player WHERE id=" + player_id);

			if (rs.next()) {
				PlayerObject player = new PlayerObject();

				player.setId(rs.getString("id"));
				player.setPrimaryRole(rs.getString("primary_role"));
				player.setSecondaryRole(rs.getString("secondary_role"));
				player.setRating(rs.getInt("rating"));

				con.close();
				return player;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get the players from a team based on the team id
	 *
	 * @param team_id
	 * @return player
	 */
	@Override
	public ArrayList<PlayerObject> getPlayersTeam(int team_id) {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT * FROM player WHERE team.team_id =" + team_id);

			ArrayList<PlayerObject> players = new ArrayList<>();

			while (rs.next()) {
				PlayerObject player = new PlayerObject();

				player.setId(rs.getString("id"));
				player.setPrimaryRole(rs.getString("primary_role"));
				player.setSecondaryRole(rs.getString("secondary_role"));
				player.setRating(rs.getInt("rating"));

				players.add(player);
			}
			con.close();
			return players;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Insert a new player
	 *
	 * @see PlayerObject#setId(String)
	 * @see PlayerObject#setPrimaryRole(String)
	 * @see PlayerObject#setSecondaryRole(String)
	 * @see PlayerObject#setRating(int)
	 *
	 * @param player
	 */
	@Override
	public void insertPlayer(PlayerObject player) {
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement pst = con.prepareStatement(
					"INSERT INTO player "
							+ "(id, "
							+ "primary_role, "
							+ "secondary_role, "
							+ "rating)"
							+ "VALUES (?, ?, ?, ?)");

			pst.setString(1, player.getId());
			pst.setString(2, player.getPrimaryRole().toUpperCase());
			pst.setString(3, player.getSecondaryRole().toUpperCase());
			pst.setInt(4, player.getRating());
			pst.executeUpdate();

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update the player
	 *
	 * @see PlayerObject#setPrimaryRole(String)
	 * @see PlayerObject#setSecondaryRole(String)
	 * @see PlayerObject#setRating(int)
	 *
	 * @param player
	 */
	@Override
	public void updatePlayer(PlayerObject player) {
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement pst = con.prepareStatement("UPDATE player SET primary_role = ?, secondary_role = ?, rating = ? WHERE id =" + player.getId());

			pst.setString(1, player.getPrimaryRole().toUpperCase());
			pst.setString(2, player.getSecondaryRole().toUpperCase());
			pst.setInt(3, player.getRating());
			pst.executeUpdate();

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete the player
	 *
	 * @param player
	 */
	@Override
	public void deletePlayer(PlayerObject player) {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			st.executeUpdate("DELETE FROM player WHERE id=" + player.getId());
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check if a player is registered in the db
	 *
	 * @param player_id
	 * @return 		true if registered, false if not
	 */
	@Override
	public boolean checkId(String player_id) {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT id FROM player WHERE id= " + player_id);

			if (rs.next()) {
				con.close();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Check if a player is registered with parameter roles
	 *
	 * @param player_id
	 * @param primaryRole
	 * @param secondaryRole
	 * @return
	 */
	@Override
	public boolean checkRoles(String player_id, String primaryRole, String secondaryRole) {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT primary_role, secondary_role FROM player WHERE id= " + player_id);

			if(rs.next() && (rs.getString("primary_role").equals(primaryRole) && (rs.getString("secondary_role").equals(secondaryRole)))) {
				con.close();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int checkMMR(String player_id) {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT rating FROM player WHERE id= " + player_id);
			if (rs.next()) {
				int rating = rs.getInt("rating");
				con.close();
				return rating;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Update the player's mmr
	 *
	 * @see PlayerObject#setRating(int)
	 *
	 * @param player
	 */
	@Override
	public void updatePlayerMMR(PlayerObject player) {
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement pst = con.prepareStatement("UPDATE player SET rating = ? WHERE id =" + player.getId());

			pst.setInt(1, player.getRating());
			pst.executeUpdate();

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
