package com.github.peakz.DAO;

import java.util.ArrayList;

public interface MatchDAO {
	ArrayList<MatchObject> getAllMatches(ArrayList<MatchObject> matches);
	int getLastMatchID();
	MatchObject getMatch(int match_id);
	void insertMatch(MatchObject match);
	void updateMatch(MatchObject match);
	void deleteMatch(MatchObject match);
	boolean checkMatchID(int match_id);
}
