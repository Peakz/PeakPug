package com.github.peakz.DAO;

import java.util.ArrayList;

public interface VerificationDAO {
	ArrayList<VerificationObject> getVerifications(int match_id);
	void insertVerification(int match_id, String captain_id, boolean verified);
	void updateVerification(VerificationObject verification);
	boolean checkCaptain(String captain_id);
}
