package com.github.peakz.queues;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.*;
import sx.blah.discord.util.EmbedBuilder;

import java.io.Serializable;
import java.util.*;

public class QueuePug implements Serializable {

	private boolean aboutToPop = false;
	private ArrayList<PlayerObject> queuedPlayers;
	private MatchObject match;
	private GameMaps gameMaps;

	private int mtank;
	private int ftank;
	private int hitscan;
	private int projectile;
	private int fsupp;
	private int msupp;

	private HashMap<PlayerObject, String> roleCount1;
	private HashMap<PlayerObject, String> roleCount2;

	private ArrayList<PlayerObject> ranks;

	private CommandContext ctx;

	QueuePug() {
		this.match = new MatchObject();
		this.queuedPlayers = new ArrayList<>();
		this.roleCount1 = new HashMap<>();
		this.roleCount2 = new HashMap<>();

		this.mtank = 0;
		this.ftank = 0;
		this.hitscan = 0;
		this.projectile = 0;
		this.fsupp = 0;
		this.msupp = 0;

		this.gameMaps = new GameMaps();
	}

	public void addPlayer(String mode, PlayerObject player, CommandContext ctx) {
		this.ctx = ctx;
		switch (mode) {
			case "SOLOQ":
				if (!aboutToPop) {
					addRole(mode, player);
					/*
					 * check if soloq can start with 2/2/2 possible
					 * and if it isn't, it won't start and will wait until
					 * there's enough players in each role to start.
					 */
					if (aboutToPop) {
						aboutToPop = !balanceComps(mode);
					} else {
						aboutToPop = false;
					}
					break;
				} else {
					ctx.getMessage().getChannel().sendMessage("Queue popped and match is being made, please try again in a sec");
				}
				break;

			case "RANKS":
				if (!aboutToPop) {
					addRole(mode, player);
				/*
				 * 	check if ranks can start with 2/2/2 possible
				 * 	and if there's 1 player to fatkid, otherwise
				 * 	it will wait.
				 */
					if (aboutToPop) {
						aboutToPop = !balanceComps(mode);
					} else {
						aboutToPop = false;
					}
					break;
				} else {
					ctx.getMessage().getChannel().sendMessage("Please wait until pick phase is over!");
				}
				break;

			default:
				break;
		}
	}

	public void removePlayer(String mode, PlayerObject player) {
		switch (mode) {
			case "SOLOQ":
				if (minusPlayer(player)) {
					player.scheduleNotification(ctx, "", 1, this);
					break;
				}
				break;

			case "RANKS":
				if (minusPlayer(player)) {
					player.scheduleNotification(ctx, "", 1, this);
					break;
				}
				break;

			default:
				break;
		}
	}

	@SuppressWarnings("Duplicates")
	public void pickPlayer(String color, String number) {
		PlayerObject p = ranks.get(Integer.parseInt(number));
		if (ranks.contains(p)) {
			if (color.equals("RED")) {
				TeamObject red = match.getTeam_red();
				if (red.getCaptain().getId().equals(ctx.getAuthor().getStringID())) {
					if (red.checkEmptyRole(p).equals(ranks.get(Integer.parseInt(number)).getRoleFlag())) {
						red.setPlayersNumber(ranks.get(Integer.parseInt(number)));
						minusPlayer(ranks.get(Integer.parseInt(number)));
						makePickPhaseMessage(color);
					} else {
						ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " Your team already has that role!");
					}
				} else {
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You're not captain of the red team!");
				}
			} else {
				TeamObject blue = match.getTeam_blue();
				if (blue.getCaptain().getId().equals(ctx.getAuthor().getStringID())) {
					if (blue.checkEmptyRole(p).equals(ranks.get(Integer.parseInt(number)).getRoleFlag())) {
						blue.setPlayersNumber(ranks.get(Integer.parseInt(number)));
						minusPlayer(ranks.get(Integer.parseInt(number)));
						makePickPhaseMessage(color);
					} else {
						ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " Your team already has that role!");
					}
				} else {
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " You're not captain of the blue team!");
				}
			}
		}
	}

	private boolean minusPlayer(PlayerObject player) {
		queuedPlayers.remove(player);
		if (roleCount1.containsKey(player)) {
			roleCount1.remove(player, player.getRoleFlag());
		} else if (roleCount2.containsKey(player)) {
			roleCount2.remove(player, player.getRoleFlag());
		}

		switch (player.getRoleFlag()) {
			case "mtank":
				mtank--;
				return true;

			case "ftank":
				ftank--;
				return true;

			case "hitscan":
				hitscan--;
				return true;

			case "projectile":
				projectile--;
				return true;

			case "fsupp":
				fsupp--;
				return true;

			case "msupp":
				msupp--;
				return true;

			default:
				return true;
		}
	}

	// Added when queueing for soloq
	private void addRole(String mode, PlayerObject player) {
		if (addRoleSwitch(player, "first")) {
			queuedPlayers.add(player);
			player.scheduleNotification(ctx, mode, 0, this);
		} else if (addRoleSwitch(player, "second")) {
			queuedPlayers.add(player);
			player.scheduleNotification(ctx, mode, 0, this);
		}

		if (mode.equals("SOLOQ")) {
			// lock queue
			if (queuedPlayers.size() == 12 || queuedPlayers.size() > 12) {
				aboutToPop = true;
			}
		} else if (mode.equals("RANKS")) {
			// lock queue
			if (queuedPlayers.size() == 13 || queuedPlayers.size() > 13) {
				aboutToPop = true;
			}
		}
	}

	@SuppressWarnings("Duplicates")
	private boolean addRoleSwitch(PlayerObject player, String whichRole) {
		String primaryRole = player.getPrimaryRole();
		String secondaryRole = player.getSecondaryRole();
		String roleFlag;

		if (primaryRole.equals("mtank") && whichRole.equals("first") && mtank < 2) {
			player.setRoleFlag("mtank");
			roleFlag = player.getRoleFlag();
			roleCount1.put(player, roleFlag);
			mtank++;
			return true;
		} else if (secondaryRole.equals("mtank") && whichRole.equals("second")) {
			player.setRoleFlag("mtank");
			roleFlag = player.getRoleFlag();
			roleCount2.put(player, roleFlag);
			mtank++;
			return true;
		} else if ((primaryRole.equals("ftank")) && whichRole.equals("first") && ftank < 2) {
			player.setRoleFlag("ftank");
			roleFlag = player.getRoleFlag();
			roleCount1.put(player, roleFlag);
			ftank++;
			return true;
		} else if (secondaryRole.equals("ftank") && whichRole.equals("second")) {
			player.setRoleFlag("ftank");
			roleFlag = player.getRoleFlag();
			roleCount2.put(player, roleFlag);
			ftank++;
			return true;
		} else if (primaryRole.equals("hitscan") && whichRole.equals("first") && hitscan < 2) {
			player.setRoleFlag("hitscan");
			roleFlag = player.getRoleFlag();
			roleCount1.put(player, roleFlag);
			hitscan++;
			return true;
		} else if (secondaryRole.equals("hitscan") && whichRole.equals("second")) {
			player.setRoleFlag("hitscan");
			roleFlag = player.getRoleFlag();
			roleCount2.put(player, roleFlag);
			hitscan++;
			return true;
		} else if (primaryRole.equals("projectile") && whichRole.equals("first") && projectile < 2) {
			player.setRoleFlag("projectile");
			roleFlag = player.getRoleFlag();
			roleCount1.put(player, roleFlag);
			projectile++;
			return true;
		} else if (secondaryRole.equals("projectile") && whichRole.equals("second")) {
			player.setRoleFlag("projectile");
			roleFlag = player.getRoleFlag();
			roleCount2.put(player, roleFlag);
			projectile++;
			return true;
		} else if (primaryRole.equals("fsupp") && whichRole.equals("first") && fsupp < 2) {
			player.setRoleFlag("fsupp");
			roleFlag = player.getRoleFlag();
			roleCount1.put(player, roleFlag);
			fsupp++;
			return true;
		} else if (secondaryRole.equals("fsupp") && whichRole.equals("second")) {
			player.setRoleFlag("fsupp");
			roleFlag = player.getRoleFlag();
			roleCount2.put(player, roleFlag);
			fsupp++;
			return true;
		} else if (primaryRole.equals("msupp") && whichRole.equals("first") && msupp < 2) {
			player.setRoleFlag("msupp");
			roleFlag = player.getRoleFlag();
			roleCount1.put(player, roleFlag);
			msupp++;
			return true;
		} else if (secondaryRole.equals("msupp") && whichRole.equals("second")) {
			System.out.println("test");
			player.setRoleFlag("msupp");
			roleFlag = player.getRoleFlag();
			roleCount2.put(player, roleFlag);
			msupp++;
			return true;
		}
		return false;
	}

	private void sortRating(ArrayList<PlayerObject> tempList) {
		for (PlayerObject p : tempList) {
			if ((tempList.indexOf(p) + 1) != 12) {
				PlayerObject nextPlayer = tempList.get(tempList.indexOf(p) + 1);
				if ((nextPlayer != null) && (p.getRating() > nextPlayer.getRating())) {
					Collections.swap(tempList, tempList.indexOf(p), tempList.indexOf(nextPlayer));
				}
			}
		}
	}

	@SuppressWarnings("Duplicates")
	private boolean balanceComps(String mode) {
		if (mtank > 1 && ftank > 1 && hitscan > 1 && projectile > 1 && fsupp > 1 && msupp > 1) {

			// Array with red players
			PlayerObject[] rArr = new PlayerObject[6];
			int r = 0;

			// Array with blue players
			PlayerObject[] bArr = new PlayerObject[6];
			int b = 0;

			int turn = 12;

			// Copy the queued players
			ArrayList<PlayerObject> tempList = new ArrayList<>(queuedPlayers);

			// If it's SoloQ
			if (mode.equals("SOLOQ")) {

				// Sort by rating
				sortRating(tempList);

				// Add players to teams & start
				return startSoloQ(tempList, rArr, r, bArr, b, turn);
				// If it's Rank S
			} else if (mode.equals("RANKS")) {
				return startRankS(tempList);
			}
		}
		return false;
	}

	@SuppressWarnings("Duplicates")
	private boolean startSoloQ(ArrayList<PlayerObject> players, PlayerObject[] rArr, int r, PlayerObject[] bArr, int b, int turn) {
		// Create empty teams
		TeamObject red = new TeamObject(rArr);
		TeamObject blue = new TeamObject(bArr);

		// Add players to teams
		for (PlayerObject p : players) {
			boolean r1Found = roleCount1.containsKey(p);
			boolean r2Found = roleCount2.containsKey(p);

			// checker for what turn it is
			if (turn != 0) {
				// if it's an even number, add to red
				if (turn % 2 == 0) {
					if (r < 6 && p.getRoleFlag().equals(red.checkEmptyRole(p))) {
						if (r1Found) {
							rArr[r] = p;
							r++;
							turn--;
							minusPlayer(p);
						} else if (r2Found) {
							rArr[r] = p;
							r++;
							turn--;
							minusPlayer(p);
						}
					}

					// if it's an uneven number, add to blue
				} else if (turn % 2 == 1) {
					if (b < 6 && p.getRoleFlag().equals(blue.checkEmptyRole(p))) {
						if (r1Found) {
							bArr[b] = p;
							b++;
							turn--;
							minusPlayer(p);
						} else if (r2Found) {
							bArr[b] = p;
							b++;
							turn--;
							minusPlayer(p);
						}
					}

					if ((r == 6) && (b == 6)) {
						red = new TeamObject(rArr);
						blue = new TeamObject(bArr);

						match = new MatchObject();
						match.setMap(gameMaps.selectMap());

						match.setTeam_red(red);
						match.setTeam_blue(blue);

						MatchDAO matchDAO = new MatchDAOImp();
						matchDAO.insertMatch(match);
						match.setId(matchDAO.getLastMatchID());
						makeMatchMessage("SOLOQ");

						queuedPlayers.removeAll(Arrays.asList(rArr));
						queuedPlayers.removeAll(Arrays.asList(bArr));
						createVerifications(match);
						return true;
					}
				}
			}
		}
		return false;
	}

	private void createVerifications(MatchObject match) {
		VerificationDAO vDAO = new VerificationDAOImp();

		vDAO.insertVerification(match.getId(), "" + match.getTeam_red().getCaptain().getId(), false);
		vDAO.insertVerification(match.getId(), "" + match.getTeam_blue().getCaptain().getId(), false);
	}

	private boolean startRankS(ArrayList<PlayerObject> players) {
		TeamObject red = new TeamObject(new PlayerObject[6]);
		TeamObject blue = new TeamObject(new PlayerObject[6]);

		Random rand = new Random();
		red.setCaptain(queuedPlayers.get(rand.nextInt(queuedPlayers.size())));
		blue.setCaptain(queuedPlayers.get(rand.nextInt(queuedPlayers.size())));

		if (red.getCaptain() != null && blue.getCaptain() != null) {
			match.setTeam_red(red);
			match.setTeam_blue(blue);
			ranks = new ArrayList<>(players);

			GameMaps gameMaps = new GameMaps();
			match.setMap(gameMaps.selectMap());

			MatchDAO matchDAO = new MatchDAOImp();
			matchDAO.insertMatch(match);
			match.setId(matchDAO.getLastMatchID());
			makeMatchMessage("RANKS");
			return true;
		}
		return false;
	}

	public void setCtx(CommandContext ctx) {
		this.ctx = ctx;
	}

	public int getPlayersQueued() {
		return queuedPlayers.size();
	}

	public int getRanksPool() {
		return ranks.size();
	}

	public int getMTQueud() {
		return mtank;
	}

	public int getFTQueued() {
		return ftank;
	}

	public int getHSQueued() {
		return hitscan;
	}

	public int getPJQueued() {
		return projectile;
	}

	public int getFSQueued() {
		return fsupp;
	}

	public int getMSQueued() {
		return msupp;
	}

	public boolean containsInstance(PlayerObject c) {
		return queuedPlayers.stream().anyMatch(x -> x.getId().equals(c.getId()));
	}

	public boolean isAboutToPop() {
		return aboutToPop;
	}

	public void setAboutToPop(boolean aboutToPop) {
		this.aboutToPop = aboutToPop;
	}

	public PlayerObject getPlayer(String id) {
		for (PlayerObject p : queuedPlayers) {
			if (p.getId().equals(id)) {
				return p;
			}
		}
		return null;
	}

	private void makeMatchMessage(String mode) {
		EmbedBuilder builder = new EmbedBuilder();
		if (mode.equals("SOLOQ")) {
			// Average mmr + captains
			builder.appendField("RED TEAM MMR", "" + match.getTeam_red().getAvgRating(), true);
			builder.appendField("BLUE TEAM MMR", "" + match.getTeam_blue().getAvgRating(), true);

			builder.appendField("Captain", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_red().getCaptain().getId())).mention(), true);
			builder.appendField("Captain", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_blue().getCaptain().getId())).mention(), true);

			builder.appendField("Main Tank", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_red().withRole("mtank").getId())).mention(), true);
			builder.appendField("Main Tank", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_blue().withRole("mtank").getId())).mention(), true);

			builder.appendField("Flex Tank", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_red().withRole("ftank").getId())).mention(), true);
			builder.appendField("Flex Tank", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_blue().withRole("ftank").getId())).mention(), true);

			builder.appendField("Hitscan", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_red().withRole("hitscan").getId())).mention(), true);
			builder.appendField("Hitscan", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_blue().withRole("hitscan").getId())).mention(), true);

			builder.appendField("Projectile", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_red().withRole("projectile").getId())).mention(), true);
			builder.appendField("Projectile", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_blue().withRole("projectile").getId())).mention(), true);

			builder.appendField("Flex Support", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_red().withRole("fsupp").getId())).mention(), true);
			builder.appendField("Flex Support", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_blue().withRole("fsupp").getId())).mention(), true);

			builder.appendField("Main Support", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_red().withRole("msupp").getId())).mention(), true);
			builder.appendField("Main Support", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_blue().withRole("msupp").getId())).mention(), true);
		} else {
			builder.appendField("Captain", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_red().getCaptain().getId())).mention(), true);
			builder.appendField("Captain", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_blue().getCaptain().getId())).mention(), true);

			builder.appendField("Available Main Tanks", "" + rolePoolMsg("mtank"), true);
			builder.appendField("Available Flex Tanks", "" + rolePoolMsg("ftank"), true);
			builder.appendField("Available Hitscans", "" + rolePoolMsg("hitscan"), true);
			builder.appendField("Available Projectiles", "" + rolePoolMsg("projectile"), true);
			builder.appendField("Available Flex Supports", "" + rolePoolMsg("fsupp"), true);
			builder.appendField("Available Main Supports", "" + rolePoolMsg("msupp"), true);
		}

		builder.withDescription("MAP - " + match.getMap());

		MatchDAO matchDAO = new MatchDAOImp();
		if (mode.equals("SOLOQ")) {
			builder.withColor(55, 240, 27);
			builder.withTitle("MATCH ID: " + match.getId());
			builder.withFooterText("Don't forget to report the result, \"!Result match_id winning_color\", GL HF!");
		} else {
			builder.withColor(228, 38, 38);
			builder.withTitle("RANK S - PICK PHASE - TURN TO PICK: " + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_red().getCaptain().getId())).getName() + " - MATCH ID: " + matchDAO.getLastMatchID());
		}
		GameMaps gameMaps = new GameMaps();
		builder.withThumbnail(gameMaps.getMapImgUrl(match.getMap()));
		ctx.getMessage().getChannel().sendMessage(builder.build());
	}

	private String rolePoolMsg(String role) {
		String str = "";
		switch (role) {
			case "mtank":
			case "ftank":
			case "hitscan":
			case "projectile":
			case "fsupp":
			case "msupp":
				for (int i = 0; i < ranks.size(); i++) {
					if (ranks.get(i).getRoleFlag().equals(role)) {
						str += (i + 1) + ". " + ctx.getGuild().getUserByID(Long.valueOf(ranks.get(i).getId())).getName() + "\n";
						//str += (i + 1) + ". " + ctx.getAuthor().getName() + " " + role + "\n";
					}
				}
				break;
		}
		if (!str.equals("")) {
			return str;
		}
		return "None";
	}

	private void makePickPhaseMessage(String color) {
		EmbedBuilder builder = new EmbedBuilder();

		builder.appendField("Captain", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_red().getCaptain().getId())).getName(), true);
		builder.appendField("Captain", "" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_blue().getCaptain().getId())).getName(), true);

		builder.appendField("Available Main Tanks", "" + rolePoolMsg("mtank"), true);
		builder.appendField("Available Flex Tanks", "" + rolePoolMsg("ftank"), true);
		builder.appendField("Available Hitscans", "" + rolePoolMsg("hitscan"), true);
		builder.appendField("Available Projectiles", "" + rolePoolMsg("projectile"), true);
		builder.appendField("Available Flex Supports", "" + rolePoolMsg("fsupp"), true);
		builder.appendField("Available Main Supports", "" + rolePoolMsg("msupp"), true);

		switch (color) {
			case "RED":
				builder.withColor(228, 38, 38);
				builder.withTitle("RANK S - PICK PHASE - TURN TO PICK: " + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_red().getCaptain().getId())).mention() + " - MATCH ID: " + match.getId());
				break;
			case "BLUE":
				builder.withColor(24, 109, 238);
				builder.withTitle("RANK S - PICK PHASE - TURN TO PICK: " + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_blue().getCaptain().getId())).mention() + " - MATCH ID: " + match.getId());
				break;
			default:
				builder.withColor(55, 240, 27);
				builder.withTitle("RANK S - PICK PHASE - FINISHED - MATCH ID: " + match.getId());
				break;
		}
		builder.withDescription("MAP - " + match.getMap());

		GameMaps gameMaps = new GameMaps();
		builder.withThumbnail(gameMaps.getMapImgUrl(match.getMap()));
		ctx.getMessage().getChannel().sendMessage(builder.build());
	}

	public void finalRanksMessage() {
		EmbedBuilder builder = new EmbedBuilder();

		builder.appendField("Red Team Captain" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_red().getCaptain().getId())).mention(), "" + match.getTeam_red().getCaptain().getRoleFlag(), true);
		builder.appendField("Blue Team Captain" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_blue().getCaptain().getId())).mention(), "" + match.getTeam_blue().getCaptain().getRoleFlag(), true);

		builder.appendField("" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_red().getPlayer_1().getId())).mention(), "" + match.getTeam_red().getPlayer_1().getRoleFlag(), true);
		builder.appendField("" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_blue().getPlayer_1().getId())).mention(), "" + match.getTeam_blue().getPlayer_1().getRoleFlag(), true);

		builder.appendField("" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_red().getPlayer_2().getId())).mention(), "" + match.getTeam_red().getPlayer_1().getRoleFlag(), true);
		builder.appendField("" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_blue().getPlayer_2().getId())).mention(), "" + match.getTeam_blue().getPlayer_1().getRoleFlag(), true);

		builder.appendField("" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_red().getPlayer_3().getId())).mention(), "" + match.getTeam_red().getPlayer_1().getRoleFlag(), true);
		builder.appendField("" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_blue().getPlayer_3().getId())).mention(), "" + match.getTeam_blue().getPlayer_1().getRoleFlag(), true);

		builder.appendField("" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_red().getPlayer_4().getId())).mention(), "" + match.getTeam_red().getPlayer_1().getRoleFlag(), true);
		builder.appendField("" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_blue().getPlayer_4().getId())).mention(), "" + match.getTeam_blue().getPlayer_1().getRoleFlag(), true);

		builder.appendField("" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_red().getPlayer_5().getId())).mention(), "" + match.getTeam_red().getPlayer_1().getRoleFlag(), true);
		builder.appendField("" + ctx.getGuild().getUserByID(Long.valueOf(match.getTeam_blue().getPlayer_5().getId())).mention(), "" + match.getTeam_blue().getPlayer_1().getRoleFlag(), true);


		builder.withColor(228, 38, 38);
		builder.withTitle("RANK S - STARTING" + " - MATCH ID: " + match.getId());
		builder.withDescription("MAP - " + match.getMap());

		builder.withFooterText("GL HF!");
		GameMaps gameMaps = new GameMaps();
		builder.withThumbnail(gameMaps.getMapImgUrl(match.getMap()));
		ctx.getMessage().getChannel().sendMessage(builder.build());
	}
}
