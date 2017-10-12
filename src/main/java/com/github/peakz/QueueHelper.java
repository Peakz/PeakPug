package com.github.peakz;

import com.github.peakz.DAO.*;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelJoinEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QueueHelper {
	// String arrays of each game mode and its respective maps.
	private static String [] ESCORT = {"Dorado", "Route 66", "Watchpoint: Gibraltar"};
	private static String [] HYBRID = {"Eichenwalde", "Hollywood", "King's Row", "Numbani"};
	private static String [] CONTROL = {"Ilios", "Lijiang Tower", "Nepal", "Oasis"};
	private static String [] ASSAULT = {"Hanamura", "Horizon Lunar Colony", "Temple of Anubis", "Volskaya Industries"};
	private static String[][] MODES = {ESCORT, HYBRID, CONTROL, ASSAULT};

	private ArrayList<PlayerObject> players = new ArrayList<>();
	private ArrayList<PlayerObject> tanks = new ArrayList<>();
	private ArrayList<PlayerObject> dps = new ArrayList<>();
	private ArrayList<PlayerObject> supps = new ArrayList<>();
	private ArrayList<PlayerObject> flexes = new ArrayList<>();

	private int antiRNG = 0;

	public ArrayList<PlayerObject> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<PlayerObject> players) {
		this.players = players;
	}

	public ArrayList<PlayerObject> getTanks() {
		return tanks;
	}

	public ArrayList<PlayerObject> getDps() {
		return dps;
	}

	public ArrayList<PlayerObject> getSupps() {
		return supps;
	}

	public ArrayList<PlayerObject> getFlexes() {
		return flexes;
	}

	public static boolean checkRolesAvailable(QueueHelper queueHelper) {
		return checkSecondaryRoleFill(queueHelper);
	}

	private static boolean checkSecondaryRoleFill(QueueHelper queueHelper) {
			if (queueHelper.getTanks().size() > 1) {
				if (queueHelper.getSupps().size() > 1) {
					if (queueHelper.getFlexes().size() > 1) {
						if (queueHelper.getDps().size() > 1) {
							return true;
						}
					} else if (queueHelper.getSupps().size() == 0 && (iterateSecondaryRole(queueHelper, 3) || iterateSecondaryRole(queueHelper, 4))) {
						return true;
					}
					return false;
				} else if (queueHelper.getSupps().size() == 0 && (iterateSecondaryRole(queueHelper, 2) || iterateSecondaryRole(queueHelper, 3))) {
					return true;
				}
				return false;
			} else if (queueHelper.getTanks().size() == 0 && (iterateSecondaryRole(queueHelper, 1) || iterateSecondaryRole(queueHelper, 2))) {
				return true;
			}
		return false;
	}

	private static boolean iterateSecondaryRole(QueueHelper queueHelper, int i) {
		switch(i){
			// Search for tanks
			case 1:
				for(PlayerObject player : queueHelper.getPlayers()){
					if(player.getSecondaryRole().equals("TANK")){

						/**
						 * If there exists more than 2 support mains with tank as 2nd role
						 * in the queue, we will put one of the supp mains to tanks since we need tanks
						 * in this scenario.
						 */
						if(queueHelper.getSupps().size() > 2 && player.getPrimaryRole().equals("SUPP")){
							queueHelper.getSupps().remove(player);
							queueHelper.getTanks().add(player);
							return true;

							/**
							 * If there exists more than 2 flex mains with tank as 2nd role
							 * in the queue, we will put one of the flex mains to tanks since we need tanks
							 * in this scenario.
							 */
						} else if(queueHelper.getFlexes().size() > 2 && player.getPrimaryRole().equals("FLEX")){
							queueHelper.getFlexes().remove(player);
							queueHelper.getTanks().add(player);
							return true;

							/**
							 * If there exists more than 2 DPS mains with tank as 2nd role
							 * in the queue, we will put one of the DPS mains to tanks since we need tanks
							 * in this scenario.
							 */
						} else if(queueHelper.getDps().size() > 2 && player.getPrimaryRole().equals("DPS")){
							queueHelper.getDps().remove(player);
							queueHelper.getTanks().add(player);
							return true;
						}
					}
				}

				/**
				 * If we cannot find any tank to replace with, the method will return false
				 */
				break;

			// Search for supports
			case 2:
				for(PlayerObject player : queueHelper.getPlayers()){
					if(player.getSecondaryRole().equals("SUPP")){

						/**
						 * If there exists more than 2 tank mains with supp as 2nd role
						 * in the queue, we will put one of the supp mains to supps since we need supps
						 * in this scenario.
						 */
						if(queueHelper.getTanks().size() > 2 && player.getPrimaryRole().equals("TANK")){
							queueHelper.getTanks().remove(player);
							queueHelper.getSupps().add(player);
							return true;

							/**
							 * If there exists more than 2 flex mains with supp as 2nd role
							 * in the queue, we will put one of the flex mains to supps since we need supps
							 * in this scenario.
							 */
						} else if(queueHelper.getFlexes().size() > 2 && player.getPrimaryRole().equals("FLEX")){
							queueHelper.getFlexes().remove(player);
							queueHelper.getSupps().add(player);
							return true;

							/**
							 * If there exists more than 2 DPS mains with supp as 2nd role
							 * in the queue, we will put one of the DPS mains to supps since we need supps
							 * in this scenario.
							 */
						} else if(queueHelper.getDps().size() > 2 && player.getPrimaryRole().equals("DPS")){
							queueHelper.getDps().remove(player);
							queueHelper.getSupps().add(player);
							return true;
						}
					}
				}

				/**
				 * If we cannot find any supp to replace with, the method will return false
				 */
				break;

			// Search for flexes
			case 3:
				for(PlayerObject player : queueHelper.getPlayers()){
					if(player.getSecondaryRole().equals("FLEX")){

						/**
						 * If there exists more than 2 tank mains with flex as 2nd role
						 * in the queue, we will put one of the supp mains to flexes since we need flexes
						 * in this scenario.
						 */
						if(queueHelper.getTanks().size() > 2 && player.getPrimaryRole().equals("TANK")){
							queueHelper.getTanks().remove(player);
							queueHelper.getTanks().add(player);
							return true;

							/**
							 * If there exists more than 2 supp mains with flex as 2nd role
							 * in the queue, we will put one of the flex mains to flexes since we need flexes
							 * in this scenario.
							 */
						} else if(queueHelper.getSupps().size() > 2 && player.getPrimaryRole().equals("SUPP")){
							queueHelper.getSupps().remove(player);
							queueHelper.getTanks().add(player);
							return true;

							/**
							 * If there exists more than 2 DPS mains with flex as 2nd role
							 * in the queue, we will put one of the DPS mains to flexes since we need flexes
							 * in this scenario.
							 */
						} else if(queueHelper.getDps().size() > 2 && player.getPrimaryRole().equals("DPS")){
							queueHelper.getDps().remove(player);
							queueHelper.getTanks().add(player);
							return true;
						}
					}
				}

				/**
				 * If we cannot find any supp to replace with, the method will return false
				 */
				break;

			// Seach for DPS
			case 4:
				for(PlayerObject player : queueHelper.getPlayers()){
					if(player.getSecondaryRole().equals("DPS")){

						/**
						 * If there exists more than 2 tank mains with dps as 2nd role
						 * in the queue, we will put one of the supp mains to dps since we need dps
						 * in this scenario.
						 */
						if(queueHelper.getTanks().size() > 2 && player.getPrimaryRole().equals("TANK")){
							queueHelper.getTanks().remove(player);
							queueHelper.getDps().add(player);
							return true;

							/**
							 * If there exists more than 2 supp mains with dps as 2nd role
							 * in the queue, we will put one of the flex mains to dps since we need dps
							 * in this scenario.
							 */
						} else if(queueHelper.getSupps().size() > 2 && player.getPrimaryRole().equals("SUPP")){
							queueHelper.getSupps().remove(player);
							queueHelper.getDps().add(player);
							return true;

							/**
							 * If there exists more than 2 flex mains with flex as 2nd role
							 * in the queue, we will put one of the DPS mains to dps since we need dps
							 * in this scenario.
							 */
						} else if(queueHelper.getDps().size() > 2 && player.getPrimaryRole().equals("FLEX")){
							queueHelper.getFlexes().remove(player);
							queueHelper.getDps().add(player);
							return true;
						}
					}
				}

				/**
				 * If we cannot find any supp to replace with, the method will return false
				 */
				return false;

			default:
				break;
		}
		return false;
	}

	public MatchObject makeTeams(ArrayList<PlayerObject> temp_team_red, ArrayList<PlayerObject> temp_team_blue, QueueHelper queueHelper) {
		return balanceTeams(temp_team_red, temp_team_blue, queueHelper);
	}

	private MatchObject balanceTeams(ArrayList<PlayerObject> temp_team_red, ArrayList<PlayerObject> temp_team_blue, QueueHelper queueHelper) {
		TeamObject red = new TeamObject();
		TeamObject blue = new TeamObject();

		int tank = 0;
		int supp = 0;
		int flex = 0;
		int dps = 0;

		// Sort players by highest rating
		for(int i = 1; i < queueHelper.getPlayers().size(); i++) {
			if((queueHelper.getPlayers().get(i - 1).getRating() > queueHelper.getPlayers().get(i).getRating())) {
				Collections.swap(queueHelper.getPlayers(), i, i - 1);
			}

			// Sort players based on roles into red and blue team
			if(tank < 2) {
				if(queueHelper.getTanks().equals(queueHelper.getPlayers().get(i))) {
					temp_team_red.add(queueHelper.getPlayers().get(i));
					//movePlayersVoice(event, queueHelper.getPlayers().get(i), true);
					tank++;
				}

			} else if (supp < 2) {
				if(queueHelper.getSupps().equals(queueHelper.getPlayers().get(i))) {
					temp_team_red.add(queueHelper.getPlayers().get(i));
					//movePlayersVoice(event, queueHelper.getPlayers().get(i), true);
					supp++;
				}

			} else if (flex < 2) {
				if(queueHelper.getFlexes().equals(queueHelper.getPlayers().get(i))) {
					temp_team_red.add(queueHelper.getPlayers().get(i));
					//movePlayersVoice(event, queueHelper.getPlayers().get(i), true);
					flex++;
				}
			} else if (dps < 2) {
				if(dps < 1) {
					if (queueHelper.getDps().equals(queueHelper.getPlayers().get(i))) {
						temp_team_red.add(queueHelper.getPlayers().get(i));
						//movePlayersVoice(event, queueHelper.getPlayers().get(i), true);
						dps++;
					}
				} else {
					if (queueHelper.getDps().equals(queueHelper.getPlayers().get(i))) {
						temp_team_red.add(queueHelper.getPlayers().get(i));
						//movePlayersVoice(event, queueHelper.getPlayers().get(i), true);
						dps++;
					}
				}
			}
		}

		for(int i = 1; i < queueHelper.getPlayers().size(); i++){
			// add players to temp red arraylist
			if(i == 1 || i == 3 || i == 5 || i == 7 || i == 9 || i == 11){
				temp_team_red.add(queueHelper.getPlayers().get(i));
				//movePlayersVoice(event, queueHelper.getPlayers().get(i), true);
			} else {
				// add players to temp blue arraylist
				temp_team_blue.add(queueHelper.getPlayers().get(i));
				//movePlayersVoice(event, queueHelper.getPlayers().get(i), false);
			}
		}

		red.setCaptain(temp_team_red.get(0));
		red.setPlayer_1(temp_team_red.get(1));
		red.setPlayer_2(temp_team_red.get(2));
		red.setPlayer_3(temp_team_red.get(3));
		red.setPlayer_4(temp_team_red.get(4));
		red.setPlayer_5(temp_team_red.get(5));

		blue.setCaptain(temp_team_blue.get(0));
		blue.setPlayer_1(temp_team_blue.get(1));
		blue.setPlayer_2(temp_team_blue.get(2));
		blue.setPlayer_3(temp_team_blue.get(3));
		blue.setPlayer_4(temp_team_blue.get(4));
		blue.setPlayer_5(temp_team_blue.get(5));

		TeamDAO teamDAO = new TeamDAOImp();
		red.setColor("RED");
		blue.setColor("BLUE");
		teamDAO.insertTeam(red);
		teamDAO.insertTeam(blue);
		return new MatchObject(red, blue, selectMap());
	}

	private void movePlayersVoice(UserVoiceChannelJoinEvent event, PlayerObject player, boolean redOrBlue){
		IVoiceChannel voice_1 = event.getGuild().getVoiceChannelsByName("propugsRed1").get(0).copy();
		IVoiceChannel voice_2 = event.getGuild().getVoiceChannelsByName("propugsBlue1").get(0).copy();

		if(redOrBlue){
			event.getGuild().getUserByID(Long.valueOf(player.getId())).moveToVoiceChannel(voice_1);
		} else {
			event.getGuild().getUserByID(Long.valueOf(player.getId())).moveToVoiceChannel(voice_2);
		}
	}

	public void addPrimaryRole(PlayerObject player, QueueHelper queueHelper) {
		switch(player.getPrimaryRole().toUpperCase()){
			case "TANK":
				addTank(player, queueHelper);
				break;

			case "DPS":
				addDPS(player, queueHelper);
				break;

			case "FLEX":
				addFlex(player, queueHelper);
				break;

			case "SUPP":
				addSupp(player, queueHelper);
				break;

			default:
				break;
		}
	}

	private void addTank(PlayerObject player, QueueHelper queueHelper) {
		queueHelper.getPlayers().add(player);
		queueHelper.getTanks().add(player);
	}

	private void addDPS(PlayerObject player, QueueHelper queueHelper) {
		queueHelper.getPlayers().add(player);
		queueHelper.getDps().add(player);
	}

	private void addFlex(PlayerObject player, QueueHelper queueHelper) {
		queueHelper.getPlayers().add(player);
		queueHelper.getFlexes().add(player);
	}

	private void addSupp(PlayerObject player, QueueHelper queueHelper) {
		queueHelper.getPlayers().add(player);
		queueHelper.getFlexes().add(player);
	}

	public boolean updateMMR(VerificationObject v_red, VerificationObject v_blue, String winner){
		if(v_red.isVerified() && v_blue.isVerified()) {
			MatchDAO matchDAOImp = new MatchDAOImp();
			MatchObject mo = matchDAOImp.getMatch(v_blue.getMatch_id());

			TeamDAO teamDAO = new TeamDAOImp();
			TeamObject t_red = teamDAO.getTeam(v_red.getMatch_id(), "RED");
			TeamObject t_blue = teamDAO.getTeam(v_blue.getMatch_id(), "BLUE");

			PlayerDAO playerDAO = new PlayerDAOImp();

			PlayerObject[] players = new PlayerObject[] {
					t_red.getCaptain(),
					t_red.getPlayer_1(),
					t_red.getPlayer_2(),
					t_red.getPlayer_3(),
					t_red.getPlayer_4(),
					t_red.getPlayer_5(),

					t_blue.getCaptain(),
					t_blue.getPlayer_1(),
					t_blue.getPlayer_2(),
					t_blue.getPlayer_3(),
					t_blue.getPlayer_4(),
					t_blue.getPlayer_5()
			};

			switch(winner.toUpperCase()) {
				case "RED":
					for(int i = 0; i < players.length; i++) {
						if(i < 5) {
							players[i].setRating(players[i].getRating() + 20);
							playerDAO.updatePlayer(players[i]);
						} else {
							players[i].setRating(players[i].getRating() - 20);
							playerDAO.updatePlayer(players[i]);
						}
					}
					return true;
				case "BLUE":
					for(int i = 0; i < players.length; i++) {
						if(i < 5) {
							players[i].setRating(players[i].getRating() - 20);
							playerDAO.updatePlayer(players[i]);
						} else {
							players[i].setRating(players[i].getRating() + 20);
							playerDAO.updatePlayer(players[i]);
						}
					}
					return true;
				default:
					return false;
			}
		}
		return false;
	}

	public boolean isQueued(PlayerObject player, QueueHelper queueHelper){
		for(PlayerObject po : queueHelper.getPlayers()){
			if(po.getId().equals(player.getId()))
				return true;
		}
		return false;
	}

	public String selectMap(){
		Random random = new Random();
		int n = random.nextInt(3);
		int m;

		switch (n) {
			case 0:
				m = random.nextInt(2);
				return MODES[n][m];
			case 1:
				m = random.nextInt(3);
				return MODES[n][m];
			case 2:
				m = random.nextInt(3);
				return MODES[n][m];
			case 3:
				if (antiRNG < 2) {
					m = random.nextInt(3);
					antiRNG++;
					return MODES[n][m];
				} else {
					antiRNG = 0;
					return MODES[random.nextInt(2)][1];
				}
		}
		return null;
	}

	public String getMapImg(String map) {

		switch (map) {
			case "Dorado":
				return "https://vignette.wikia.nocookie.net/overwatch/images/5/51/Dorado_screenshot_9.png/revision/latest/scale-to-width-down/165?cb=20160630045807";

			case "Route 66":
				return "https://vignette.wikia.nocookie.net/overwatch/images/8/87/Route66_screenshot_1.png/revision/latest/scale-to-width-down/165?cb=20160708033615";

			case "Watchpoint: Gibraltar":
				return "https://vignette.wikia.nocookie.net/overwatch/images/7/73/Gibraltar_screenshot_1.png/revision/latest/scale-to-width-down/165?cb=20160710225230";

			case "Eichenwalde":
				return "https://vignette.wikia.nocookie.net/overwatch/images/5/5e/Eichenwalde_sreenshot_1.png/revision/latest/scale-to-width-down/165?cb=20160823041053";

			case "Hollywood":
				return "https://vignette.wikia.nocookie.net/overwatch/images/9/98/Hollywood_screenshot_4.jpg/revision/latest/scale-to-width-down/165?cb=20160605011629";

			case "King's Row":
				return "https://vignette.wikia.nocookie.net/overwatch/images/2/21/Kingsrow_screenshot_8.png/revision/latest/scale-to-width-down/165?cb=20160711195544";

			case "Numbani":
				return "https://vignette.wikia.nocookie.net/overwatch/images/e/eb/Numbani_screenshot_5.jpg/revision/latest/scale-to-width-down/165?cb=20160504022045";

			case "Ilios":
				return "https://vignette.wikia.nocookie.net/overwatch/images/7/7d/Ilios_screenshot_1.png/revision/latest/scale-to-width-down/165?cb=20160712060605";

			case "Lijiang Tower":
				return "https://vignette.wikia.nocookie.net/overwatch/images/e/ed/Lijiang_screenshot_34.jpg/revision/latest/scale-to-width-down/165?cb=20160711182404";

			case "Nepal":
				return "https://vignette.wikia.nocookie.net/overwatch/images/7/7a/Nepal_screenshot_1.png/revision/latest/scale-to-width-down/165?cb=20160711235021";

			case "Oasis":
				return "https://vignette.wikia.nocookie.net/overwatch/images/7/70/Oasis_screenshot_first.png/revision/latest/scale-to-width-down/165?cb=20161203031638";

			case "Hanamura":
				return "https://vignette.wikia.nocookie.net/overwatch/images/d/dd/Hanamura_screenshot_21.png/revision/latest/scale-to-width-down/165?cb=20160626193608";

			case "Horizon Lunar Colony":
				return "https://vignette.wikia.nocookie.net/overwatch/images/b/b8/Horizon_screenshot_1.png/revision/latest/scale-to-width-down/165?cb=20170620220601";

			case "Temple of Anubis":
				return "https://vignette.wikia.nocookie.net/overwatch/images/1/12/Anubis_screenshot_4.png/revision/latest/scale-to-width-down/165?cb=20160628005051";

			case "Volskaya Industries":
				return "https://vignette.wikia.nocookie.net/overwatch/images/9/93/Volskaya_screenshot_13.png/revision/latest/scale-to-width-down/165?cb=20160704000922";

			default:
				return null;
		}
	}
}