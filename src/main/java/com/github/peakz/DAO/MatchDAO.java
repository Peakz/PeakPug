package com.github.peakz.DAO;

import java.util.ArrayList;

public interface MatchDAO {
	ArrayList<MatchObject> getAllMatches(ArrayList<MatchObject> matches);
	MatchObject getLastMatchID();
	MatchObject getMatch(String id);
	void insertMatch(MatchObject match);
	void updateMatch(MatchObject match);
	void deleteMatch(MatchObject match);
}
