package com.github.peakz.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAOImp implements PlayerDAO {

	/**
	 * Get all the Players in the database in a ArraySet
	 *
	 * @return allPlayers
	 */
	@Override
	public List<PlayerObject> getAllPlayers() {
		try (Connection con = ConnectionFactory.getConnection()) {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM player");

			List<PlayerObject> allPlayers = new ArrayList<>();

			while (rs.next()) {
				PlayerObject player = new PlayerObject();

				player.setId(rs.getString("id"));
				player.setPrimaryRole(rs.getString("primary_role"));
				player.setSecondaryRole(rs.getString("secondary_role"));
				player.setRating(rs.getInt("rating"));
				player.setLong_id(rs.getLong("long_id"));

				allPlayers.add(player);
			}
			con.close();
			return allPlayers;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get the customer for the specified name
	 *
	 * @param id
	 * @return player
	 */
	@Override
	public PlayerObject getPlayer(String id) {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT * FROM player WHERE id='" + id + "'");

			if (rs.next()) {
				PlayerObject player = new PlayerObject();

				player.setId(rs.getString("id"));
				player.setPrimaryRole(rs.getString("primary_role"));
				player.setSecondaryRole(rs.getString("secondary_role"));
				player.setRating(rs.getInt("rating"));
				player.setLong_id(rs.getLong("long_id"));

				con.close();
				return player;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Insert a new player
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
							+ "rating,"
							+ "long_id)"
							+ "VALUES (?, ?, ?, ?, ?)");

			pst.setString(1, player.getId());
			pst.setString(2, player.getPrimaryRole().toUpperCase());
			pst.setString(3, player.getSecondaryRole().toUpperCase());
			pst.setInt(4, player.getRating());
			pst.setLong(5, player.getLong_id());
			pst.executeUpdate();

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update the player
	 *
	 * @param player
	 */
	@Override
	public void updatePlayer(PlayerObject player) {
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement pst = con.prepareStatement("UPDATE player SET primary_role = ?, secondary_role = ? WHERE id =" + player.getId());

			pst.setString(1, player.getPrimaryRole().toUpperCase());
			pst.setString(2, player.getSecondaryRole().toUpperCase());
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
	 * Check if a player is already registered
	 *
	 * @param id
	 */
	@Override
	public boolean checkId(String id) {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT id FROM player WHERE id= " + id);

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
	 * Check if a player's primary role is registered
	 *
	 * @param player
	 */
	@Override
	public boolean checkPrimaryRole(PlayerObject player) {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT primary_role FROM player WHERE primary_role= " + player.getPrimaryRole());

			if (rs.next()) {
				con.close();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Check if a player's secondary role is registered
	 *
	 * @param player
	 */
	@Override
	public boolean checkSecondaryRole(PlayerObject player) {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT secondary_role FROM player WHERE secondary_role= " + player.getSecondaryRole());
			if (rs.next()) {
				con.close();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
