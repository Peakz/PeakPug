package com.github.peakz.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class VerificationDAOImp implements VerificationDAO {

	/**
	 * Get a verification for a specific match_id and captain_id.
	 *
	 * @see VerificationObject#getMatch_id()
	 * @see VerificationObject#getCaptain_id()
	 *
	 * @param match_id
	 * @return
	 */
	@Override
	public ArrayList<VerificationObject> getVerifications(int match_id) {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT * FROM match_verification WHERE matchID=" + match_id);

			ArrayList<VerificationObject> verificationObjects = new ArrayList<>();
			while (rs.next()) {
				VerificationObject verification = new VerificationObject();

				verification.setVerification_id(rs.getInt("verification_id"));
				verification.setMatch_id(match_id);
				verification.setCaptain_id(rs.getString("captain_id"));
				verification.setVerified(rs.getBoolean("verified"));

				verificationObjects.add(verification);
			}
			con.close();
			return verificationObjects;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public VerificationObject getVerification(int match_id, String captain_id) {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT * FROM match_verification WHERE matchID=" + match_id + " AND captain_id =" + captain_id);

			if(rs.next()) {
				VerificationObject verification = new VerificationObject();

				verification.setVerification_id(rs.getInt("verification_id"));
				verification.setMatch_id(match_id);
				verification.setCaptain_id(rs.getString("captain_id"));
				verification.setVerified(rs.getBoolean("verified"));

				con.close();
				return verification;
			} else {
				con.close();
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates a new verification that belongs to a specific match_id
	 * but is bound to the captains for each team in a match.
	 *
	 * Each verification has a unique id and only one captain belonging to it.
	 *
	 * @see VerificationObject#getMatch_id()
	 * @see VerificationObject#getCaptain_id()
	 *
	 * @param match_id
	 * @param captain_id
	 * @param verified
	 */
	@Override
	public void insertVerification(int match_id, String captain_id, boolean verified) {
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement pst = con.prepareStatement(
					"INSERT INTO match_verification "
							+ "(matchID, "
							+ "captain_id, "
							+ "verified)"
							+ "VALUES (?, ?, ?)");

			pst.setInt(1, match_id);
			pst.setString(2, captain_id);
			pst.setBoolean(3, verified);
			pst.executeUpdate();

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update a verification from a match through a captain from one of the teams in that match.
	 * The method updates the verification since a new verification is created one a match has started.
	 *
	 * @see VerificationObject#getMatch_id()
	 * @see VerificationObject#getCaptain_id()
	 * @see VerificationObject#isVerified()
	 *
	 * @param verification
	 */
	@Override
	public void updateVerification(VerificationObject verification) {
		Connection con = ConnectionFactory.getConnection();
		try {
			PreparedStatement pst = con.prepareStatement("UPDATE match_verification SET verified = ? WHERE match_verification.matchID=" + verification.getMatch_id() + " AND match_verification.captain_id=" + verification.getCaptain_id());

			pst.setBoolean(1, verification.isVerified());
			pst.executeUpdate();

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if a captain has verified the results from a match.
	 *
	 * @param captain_id
	 * @return
	 */
	@Override
	public boolean checkCaptain(int match_id, String captain_id) {
		Connection con = ConnectionFactory.getConnection();
		boolean verified;
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT verified FROM match_verification WHERE captain_id = " + captain_id + " AND matchID =" + match_id);

			if(rs.next()) {
				if(rs.getBoolean("verified") == false) {
					con.close();
					return true;
				}
				con.close();
				return false;
			} else {
				con.close();
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean checkBothVerifications(int match_id) {
		Connection con = ConnectionFactory.getConnection();
		Boolean[] verified = new Boolean[2];
		int i = 0;
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT verified FROM match_verification WHERE matchID = " + match_id);

			while(rs.next()) {
				verified[i] = rs.getBoolean("verified");
				i++;
			}
			con.close();

			return verified[0] != false && verified[1] != false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String getWinner(int match_id) {
		Connection con = ConnectionFactory.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT winner FROM pug_match WHERE matchID = " + match_id);

			if(rs.next()){
				String winner = rs.getString("winner");
				con.close();
				return winner;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
