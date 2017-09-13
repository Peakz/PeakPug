package com.github.peakz;

import com.github.peakz.DAO.*;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelJoinEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.ArrayList;
import java.util.Collections;

public class QueueHelper {
	private ArrayList<PlayerObject> players = new ArrayList<>();
	private ArrayList<PlayerObject> tanks = new ArrayList<>();
	private ArrayList<PlayerObject> dps = new ArrayList<>();
	private ArrayList<PlayerObject> supps = new ArrayList<>();
	private ArrayList<PlayerObject> flexes = new ArrayList<>();

	public ArrayList<PlayerObject> getPlayers() {
		return players;
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

	public static boolean checkRolesAvailable(QueueHelper queueHelper, UserVoiceChannelJoinEvent event) {
		return checkSecondaryRoleFill(queueHelper);
	}

	private static boolean checkSecondaryRoleFill(QueueHelper queueHelper) {
		if(queueHelper.getTanks().size() > 1){
			if(queueHelper.getSupps().size() > 1){
				if(queueHelper.getFlexes().size() > 1){
					if(queueHelper.getDps().size() > 1){
						return true;
					}
				} else if(queueHelper.getSupps().size() == 1 && (iterateSecondaryRole(queueHelper, 3) || iterateSecondaryRole(queueHelper, 4))){
					return true;
				}
				return false;
			} else if(queueHelper.getSupps().size() == 1 && (iterateSecondaryRole(queueHelper, 2) || iterateSecondaryRole(queueHelper, 3))){
				return true;
			}
			return false;
		} else if(queueHelper.getTanks().size() == 1 && (iterateSecondaryRole(queueHelper, 1) || iterateSecondaryRole(queueHelper, 2))){
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
				break;

			default:
				break;
		}
		return false;
	}

	public static MatchObject makeTeams(ArrayList<PlayerObject> temp_team_red, ArrayList<PlayerObject> temp_team_blue, QueueHelper queueHelper, UserVoiceChannelJoinEvent event) {
		MatchObject match = balanceTeams(temp_team_red, temp_team_blue, queueHelper, event);
		return match;
	}

	public static MatchObject balanceTeams(ArrayList<PlayerObject> temp_team_red, ArrayList<PlayerObject> temp_team_blue, QueueHelper queueHelper, UserVoiceChannelJoinEvent event) {
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
					movePlayersVoice(event, queueHelper.getPlayers().get(i), true);
					tank++;
				}

			} else if (supp < 2) {
				if(queueHelper.getSupps().equals(queueHelper.getPlayers().get(i))) {
					temp_team_red.add(queueHelper.getPlayers().get(i));
					movePlayersVoice(event, queueHelper.getPlayers().get(i), true);
					supp++;
				}

			} else if (flex < 2) {
				if(queueHelper.getFlexes().equals(queueHelper.getPlayers().get(i))) {
					temp_team_red.add(queueHelper.getPlayers().get(i));
					movePlayersVoice(event, queueHelper.getPlayers().get(i), true);
					flex++;
				}
			} else if (dps < 2) {
				if(dps < 1) {
					if (queueHelper.getDps().equals(queueHelper.getPlayers().get(i))) {
						temp_team_red.add(queueHelper.getPlayers().get(i));
						movePlayersVoice(event, queueHelper.getPlayers().get(i), true);
						dps++;
					}
				} else {
					if (queueHelper.getDps().equals(queueHelper.getPlayers().get(i))) {
						temp_team_red.add(queueHelper.getPlayers().get(i));
						movePlayersVoice(event, queueHelper.getPlayers().get(i), true);
						dps++;
					}
				}
			}
		}

		for(int i = 1; i < queueHelper.getPlayers().size(); i++){
			// add players to temp red arraylist
			if(i == 1 || i == 3 || i == 5 || i == 7 || i == 9 || i == 11){
				temp_team_red.add(queueHelper.getPlayers().get(i));
				movePlayersVoice(event, queueHelper.getPlayers().get(i), true);
			} else {
				// add players to temp blue arraylist
				temp_team_blue.add(queueHelper.getPlayers().get(i));
				movePlayersVoice(event, queueHelper.getPlayers().get(i), false);
			}
		}

		red.setCaptain(temp_team_red.get(1));
		red.setPlayer_1(temp_team_red.get(2));
		red.setPlayer_2(temp_team_red.get(3));
		red.setPlayer_3(temp_team_red.get(4));
		red.setPlayer_4(temp_team_red.get(5));
		red.setPlayer_5(temp_team_red.get(6));

		blue.setCaptain(temp_team_blue.get(1));
		blue.setPlayer_1(temp_team_blue.get(2));
		blue.setPlayer_2(temp_team_blue.get(3));
		blue.setPlayer_3(temp_team_blue.get(4));
		blue.setPlayer_4(temp_team_blue.get(5));
		blue.setPlayer_5(temp_team_blue.get(6));

		TeamDAO teamDAO = new TeamDAOImp();
		teamDAO.insertTeam(red);
		teamDAO.insertTeam(blue);
		return new MatchObject(red, blue);
	}

	public static void movePlayersVoice(UserVoiceChannelJoinEvent event, PlayerObject player, boolean redOrBlue){
		IVoiceChannel voice_1 = event.getGuild().getVoiceChannelsByName("propugsRed1").get(0).copy();
		IVoiceChannel voice_2 = event.getGuild().getVoiceChannelsByName("propugsBlue1").get(0).copy();

		if(redOrBlue){
			event.getGuild().getUserByID(Long.valueOf(player.getId())).moveToVoiceChannel(voice_1);
		} else {
			event.getGuild().getUserByID(Long.valueOf(player.getId())).moveToVoiceChannel(voice_2);
		}
	}

	public static void addPrimaryRole(PlayerObject player, QueueHelper queueHelper) {
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

	public static void addTank(PlayerObject player, QueueHelper queueHelper) {
		queueHelper.getPlayers().add(player);
		queueHelper.getTanks().add(player);
	}

	public static void addDPS(PlayerObject player, QueueHelper queueHelper) {
		queueHelper.getPlayers().add(player);
		queueHelper.getDps().add(player);
	}

	public static void addFlex(PlayerObject player, QueueHelper queueHelper) {
		queueHelper.getPlayers().add(player);
		queueHelper.getFlexes().add(player);
	}

	public static void addSupp(PlayerObject player, QueueHelper queueHelper) {
		queueHelper.getPlayers().add(player);
		queueHelper.getFlexes().add(player);
	}

	public static void newMatchMessage(IGuild guild, MatchObject match) {
		MatchDAO matchDAO = new MatchDAOImp();

		EmbedBuilder builder = new EmbedBuilder();

		builder.appendField("RED TEAM MMR: " + match.getTeam_red().getAvgRating(),"Captain: " +
				guild.getUserByID(
						Long.valueOf(match.getTeam_red().getCaptain().getId())).getName(), true);

		builder.appendField("BLUE TEAM MMR: " + match.getTeam_blue().getAvgRating(), "Captain: " +
				guild.getUserByID(
						Long.valueOf(match.getTeam_blue().getCaptain().getId())).getName(), true);

		builder.withColor(0, 153, 255);
		builder.withDescription("MAP - " + match.getMap());
		builder.withTitle("MATCH ID: " + matchDAO.getLastMatchID());

		builder.withFooterText("EACH CAPTAIN MUST REPORT RESULTS AFTER THE MATCH, \"!Result match_id team_color\"");
		builder.withThumbnail(PugBot.getMapImg(match.getMap()));
		RequestBuffer.request(() -> guild.getChannelsByName("propugs").get(0).copy().sendMessage(builder.build()));
	}

	public static void updateMMR(VerificationObject v_red, VerificationObject v_blue){

	}
}