package com.github.peakz.DAO;

public class PlayerObject {

	private String id;
	private String primaryRole;
	private String secondaryRole;
	private int rating;

	public PlayerObject() {
	}

	public PlayerObject(String id) {
		this.id = id;
	}

	public PlayerObject(String id, String primaryRole, String secondaryRole) {
		this.id = id;
		this.primaryRole = primaryRole;
		this.secondaryRole = secondaryRole;
	}

	public PlayerObject(String id, String primaryRole, String secondaryRole, int rating) {
		this.id = id;
		this.primaryRole = primaryRole;
		this.secondaryRole = secondaryRole;
		this.rating = rating;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrimaryRole() {
		return primaryRole;
	}

	public void setPrimaryRole(String primaryRole) {
		this.primaryRole = primaryRole;
	}

	public String getSecondaryRole() {
		return secondaryRole;
	}

	public void setSecondaryRole(String secondaryRole) {
		this.secondaryRole = secondaryRole;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
}
