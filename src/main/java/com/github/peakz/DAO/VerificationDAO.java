package com.github.peakz.DAO;

public interface VerificationDAO {
	VerificationObject getVerification(int match_id, String captain_id);
	void insertVerification(VerificationObject verification);
	void updateVerification(VerificationObject verification);
	boolean checkCaptain(String captain_id);
}
