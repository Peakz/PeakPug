package com.github.peakz.DAO;

public class VerificationObject {

	private int verification_id;
	private int match_id;
	private String captain_id;

	private boolean verified = false;

	public VerificationObject() {
	}

	public VerificationObject(int match_id, String captain_id) {
		this.match_id = match_id;
		this.captain_id = captain_id;
	}

	public VerificationObject(int verification_id, int match_id, String captain_id) {
		this.verification_id = verification_id;
		this.match_id = match_id;
		this.captain_id = captain_id;
	}

	public VerificationObject(int verification_id, int match_id, String captain_id, boolean verified) {
		this.verification_id = verification_id;
		this.match_id = match_id;
		this.captain_id = captain_id;
		this.verified = verified;
	}

	public int getVerification_id() {
		return verification_id;
	}

	public void setVerification_id(int verification_id) {
		this.verification_id = verification_id;
	}

	public int getMatch_id() {
		return match_id;
	}

	public void setMatch_id(int match_id) {
		this.match_id = match_id;
	}

	public String getCaptain_id() {
		return captain_id;
	}

	public void setCaptain_id(String captain_id) {
		this.captain_id = captain_id;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}
}
