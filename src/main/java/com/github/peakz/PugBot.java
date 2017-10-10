package com.github.peakz;

import com.darichey.discord.Command;
import com.darichey.discord.CommandContext;
import com.darichey.discord.CommandListener;
import com.darichey.discord.CommandRegistry;
import com.github.peakz.DAO.*;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.ArrayList;
import java.util.Random;

public class PugBot {
	private IDiscordClient client; // The instance of the discord client.

	// String arrays of each game mode and its respective maps.
	private static String [] ESCORT = {"Dorado", "Route 66", "Watchpoint: Gibraltar"};
	private static String [] HYBRID = {"Eichenwalde", "Hollywood", "King's Row", "Numbani"};
	private static String [] CONTROL = {"Ilios", "Lijiang Tower", "Nepal", "Oasis"};
	private static String [] ASSAULT = {"Hanamura", "Horizon Lunar Colony", "Temple of Anubis", "Volskaya Industries"};
	private static String[][] MODES = {ESCORT, HYBRID, CONTROL, ASSAULT};

	private static PlayerDAO playerDAO = new PlayerDAOImp();
	private static int antiRNG = 0;

	private static QueueHelper queueHelper = new QueueHelper();
	private static ArrayList<PlayerObject> temp_team_red = new ArrayList<>();
	private static ArrayList<PlayerObject> temp_team_blue = new ArrayList<>();

	public PugBot(IDiscordClient client) {
		this.client = client; // Sets the client instance to the one provided
	}

	// A custom login() method to handle all of the possible exceptions and set the bot instance.
	static IDiscordClient createClient(String token, boolean login) { // Returns a new instance of the Discord client
		ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
		clientBuilder.withToken(token); // Adds the login info to the builder
		try {
			if (login) {
				return clientBuilder.login(); // Creates the client instance and logs the client in
			} else {
				return clientBuilder.build(); // Creates the client instance but it doesn't log the client in yet, you would have to call client.login() yourself
			}
		} catch (DiscordException e) { // This is thrown if there was a problem building the client
			e.printStackTrace();
			return null;
		}
	}

	static void createCommands(IDiscordClient client) {
		Command register = Command.builder().onCalled(PugBot::RegisterCommand).build();

		// Create command for updating roles in a player profile
		Command update = Command.builder().onCalled(PugBot::UpdateCommand).build();

		Command add = Command.builder().onCalled(PugBot::AddCommand).build();

		Command remove = Command.builder().onCalled(PugBot::RemoveCommand).build();

		Command result = Command.builder().onCalled(PugBot::ResultCommand).build();

		Command rank = Command.builder()
				.onCalled(ctx -> ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " you rating is " + playerDAO.getPlayer(ctx.getAuthor().getStringID()).getRating()))
				.build();

		Command help = Command.builder().onCalled(PugBot::HelpCommand).build();

		Command status = Command.builder().onCalled(ctx -> {
					if(queueHelper.getPlayers().size() == 1) {
						ctx.getMessage().getChannel().sendMessage("" + queueHelper.getPlayers().size() + " player in queue");
					} else {
						ctx.getMessage().getChannel().sendMessage("" + queueHelper.getPlayers().size() + " players in queue");
					}
				}).build();

		// Register the commands
		CommandRegistry registry = new CommandRegistry("!");
		registry.register(register, "Register");
		registry.register(register, "register");

		registry.register(update, "Update");
		registry.register(update, "update");

		registry.register(result, "Result");
		registry.register(result, "result");

		registry.register(rank, "Rating");
		registry.register(rank, "rating");

		registry.register(add, "Add");
		registry.register(add, "add");

		registry.register(remove, "Remove");
		registry.register(remove,"remove");

		registry.register(status, "Status");
		registry.register(status, "status");

		registry.register(help, "Help");
		registry.register(help, "help");
		client.getDispatcher().registerListener(new CommandListener(registry));
	}

	private static void RegisterCommand(CommandContext ctx) {
		String id = ctx.getAuthor().getStringID();

		if(playerDAO.checkId(id)){
			ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " you're already registered.");
		} else {

			/*
			Split the string into 3 parts
			1. The command
			2. Primary role
			3. Secondary role
			 */
			String[] strArr = new String[3];
			int i = 0;

			// Store the split strings
			for(String val : ctx.getMessage().getContent().split(" ", 3)) {
				strArr[i] = val;
				i++;
			}

			// if index 1 is null, we set the value to a new arbitrary value so trigger the switch default case
			if(strArr[1] == null) {
				strArr[1] = "nope";
			}

			// if index 2 is null, we set the value to a new arbitrary value so trigger the switch default case
			if(strArr[2] == null) {
				strArr[2] = "nope";
			}

			// Switch statement for different cases
			switch((strArr[1].toUpperCase())) {
				case "TANK":
					if (strArr[2].toUpperCase().equals("DPS") || strArr[2].toUpperCase().equals("FLEX") || strArr[2].toUpperCase().equals("SUPP")) {
						registerPlayer(id, strArr[1], strArr[2]);
						ctx.getMessage().addReaction(":white_check_mark:");
					}
					break;

				case "SUPP":
					if (strArr[2].toUpperCase().equals("DPS") || strArr[2].toUpperCase().equals("FLEX") || strArr[2].toUpperCase().equals("TANK")) {
						registerPlayer(id, strArr[1], strArr[2]);
						ctx.getMessage().addReaction(":white_check_mark:");
					}
					break;

				case "FLEX":
					if (strArr[2].toUpperCase().equals("DPS") || strArr[2].toUpperCase().equals("TANK") || strArr[2].toUpperCase().equals("SUPP")) {
						registerPlayer(id, strArr[1], strArr[2]);
						ctx.getMessage().addReaction(":white_check_mark:");
					}
					break;

				case "DPS":
					if (strArr[2].toUpperCase().equals("TANK") || strArr[2].toUpperCase().equals("FLEX") || strArr[2].toUpperCase().equals("SUPP")) {
						registerPlayer(id, strArr[1], strArr[2]);
						ctx.getMessage().addReaction(":white_check_mark:");
					}
					break;

				default:
					ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " something went wrong, try again.");
					break;
			}
		}
	}

	private static void UpdateCommand(CommandContext ctx) {
		String id = ctx.getAuthor().getStringID();

		/*
		Split the string into 3 parts
		1. The command
		2. Primary role
		3. Secondary role
		 */
		String[] strArr = new String[3];
		int i = 0;

		// Store the split strings
		for(String val : ctx.getMessage().getContent().split(" ", 3)) {
			strArr[i] = val;
			i++;
		}

		// if index 1 is null, we set the value to a new arbitrary value so trigger the switch default case
		if(strArr[1] == null) {
			strArr[1] = "nope";
		}

		// if index 2 is null, we set the value to a new arbitrary value so trigger the switch default case
		if(strArr[2] == null) {
			strArr[2] = "nope";
		}

		// Check if player exists and then switch statement for different cases
		if(playerDAO.checkId(id)) {
			switch (strArr[1].toUpperCase()) {
				case "TANK":
					if (strArr[2].toUpperCase().equals("DPS") || strArr[2].toUpperCase().equals("FLEX") || strArr[2].toUpperCase().equals("SUPP")) {
						updatePlayer(id, strArr[1], strArr[2]);
						ctx.getMessage().addReaction(":white_check_mark:");
					}
					break;

				case "DPS":
					if (strArr[2].toUpperCase().equals("TANK") || strArr[2].toUpperCase().equals("FLEX") || strArr[2].toUpperCase().equals("SUPP")) {
						updatePlayer(id, strArr[1], strArr[2]);
						ctx.getMessage().addReaction(":white_check_mark:");
					}
					break;

				case "FLEX":
					if (strArr[2].toUpperCase().equals("TANK") || strArr[2].toUpperCase().equals("DPS") || strArr[2].toUpperCase().equals("SUPP")) {
						updatePlayer(id, strArr[1], strArr[2]);
						ctx.getMessage().addReaction(":white_check_mark:");
					}
					break;

				case "SUPP":
					if (strArr[2].toUpperCase().equals("TANK") || strArr[2].toUpperCase().equals("FLEX") || strArr[2].toUpperCase().equals("FLEX")) {
						updatePlayer(id, strArr[1], strArr[2]);
						ctx.getMessage().addReaction(":white_check_mark:");
					}
					break;

				default:
					ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " something went wrong, try again.");
					break;
			}
		} else {
			ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " you're not registered. Try again.");
		}
	}

	private static void AddCommand(CommandContext ctx) {
		String id = ctx.getAuthor().getStringID();
		PlayerObject player = playerDAO.getPlayer(id);

		if(player != null && (player.getPrimaryRole() != null && player.getSecondaryRole() != null)) {
			if (QueueHelper.isQueued(player, queueHelper)) {
				ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " you're already added!");
			} else if (queueHelper.getPlayers().size() < 12){
				QueueHelper.addPrimaryRole(player, queueHelper);
				ctx.getMessage().addReaction(":white_check_mark:");
				if (QueueHelper.checkRolesAvailable(queueHelper) && queueHelper.getPlayers().size() > 12) {
					MatchObject match = QueueHelper.makeTeams(temp_team_red, temp_team_blue, queueHelper);
					MatchDAO matchDAO = new MatchDAOImp();
					matchDAO.insertMatch(match);
					match.setId(matchDAO.getLastMatchID());

					EmbedBuilder builder = new EmbedBuilder();

					builder.appendField("RED TEAM MMR: " + match.getTeam_red().getAvgRating(), "Captain: " +
							ctx.getGuild().getUserByID(
									Long.valueOf(match.getTeam_red().getCaptain().getId())).getName(), true);

					builder.appendField("BLUE TEAM MMR: " + match.getTeam_blue().getAvgRating(), "Captain: " +
							ctx.getGuild().getUserByID(
									Long.valueOf(match.getTeam_blue().getCaptain().getId())).getName(), true);

					builder.withColor(0, 153, 255);
					builder.withDescription("MAP - " + match.getMap());
					builder.withTitle("MATCH ID: " + matchDAO.getLastMatchID());

					builder.withFooterText("EACH CAPTAIN MUST REPORT RESULTS AFTER THE MATCH, \"!Result match_id team_color\"");
					builder.withThumbnail(PugBot.getMapImg(match.getMap()));
					RequestBuffer.request(() -> ctx.getGuild().getChannelsByName("pugs").get(0).copy().sendMessage(builder.build()));

					// create new VerificationObject for the match
					VerificationDAO verificationDAO = new VerificationDAOImp();

					// create verification for team red, false by default
					verificationDAO.insertVerification(match.getId(), match.getTeam_red().getCaptain().getId(), false);

					// create verification for team blue, false by default
					verificationDAO.insertVerification(match.getId(), match.getTeam_blue().getCaptain().getId(), false);

					queueHelper = new QueueHelper();
				}
			}
		}
	}

	private static void ResultCommand(CommandContext ctx) {
		String[] strArr = new String[3];
		int i = 0;

		// Store the split strings
		for(String val : ctx.getMessage().getContent().split(" ", 3)) {
			strArr[i] = val;
			i++;
		}

		// check team color in the command
		if(strArr[2].toUpperCase().equals("RED") || strArr[2].toUpperCase().equals("BLUE")) {
			// create a MatchObject and get the match for the match id that's typed in.
			MatchDAO matchDAO = new MatchDAOImp();
			MatchObject match = matchDAO.getMatch(Integer.parseInt(strArr[1]));

			// check if the match exists
			if (match.getId() == Integer.parseInt(strArr[1]) && match.getId() != 0) {
				VerificationDAO verificationDAO = new VerificationDAOImp();

				// check if there's a verification linked to this user's id
				if (verificationDAO.checkCaptain(ctx.getAuthor().getStringID())) {
					ArrayList<VerificationObject> vos = verificationDAO.getVerifications(match.getId());

					// check if both verification requests are already handled
					if ((vos.get(1).isVerified() && vos.get(2).isVerified())) {
						ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " match is already verified");
					} else {
						// find and verify a match for the captain that sent the message
						for (VerificationObject vo : vos) {
							if (vo.getCaptain_id().equals(ctx.getAuthor().getStringID())) {
								// check first if there's a mismatch in verifications or if there's no verification at all
								if ((match.getWinner() != null && strArr[2].toUpperCase().equals(match.getWinner())) || (match.getWinner() == null)) {
									vo.setVerified(true);
									verificationDAO.updateVerification(vo);
									ctx.getMessage().addReaction(":white_check_mark:");
								} else {
									// reply if there's a mismatch in team colors reported by the two captains
									ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " Your opponent has verified a different winner already. If the winning team color was typed wrong, please contact an admin");
								}
							}
						}
						// check if both verifications are valid once again and if so, proceed to update mmr accordingly
						if ((vos.get(1).isVerified() && vos.get(2).isVerified())) {
							if(QueueHelper.updateMMR(vos.get(1), vos.get(2), match.getWinner())) {
								ctx.getMessage().getChannel().sendMessage("Updated the match: " + match.getId() + " and each player's MMR");
							}
						}
					}
					// reply if match is already reported by a captain
				} else {
					ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " you're not captain for the match id: " + match.getId());
				}
				// reply if the match doesn't exist
			} else {
				ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " this match doesn't exist");
			}
			// reply if there's an attempt to report a team color other than red or blue
		} else {
			ctx.getMessage().getChannel().sendMessage(ctx.getAuthor().mention() + " try typing in the winning team color again. Either \"Red\" or \"Blue\"");
		}
	}

	private static void HelpCommand(CommandContext ctx) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.appendField("!Update role_1 role_2", "Update current primary and secondary role", false);
		builder.appendField("!Add", "Queue up", true);
		builder.appendField("!Remove", "Exit queue", true);
		builder.appendField("!Result match_id winner_color (red or blue)", "Captain command to record result from a match", false);
		builder.appendField("!Rating", "Your rating", true);
		builder.appendField("!Status", "Queue status", true);

		builder.withColor(185, 255, 173);
		builder.withDescription("Register primary and secondary role");
		builder.withTitle("!Register role_1 role_2");
		ctx.getMessage().getChannel().sendMessage(builder.build());
	}

	private static void RemoveCommand(CommandContext ctx) {
		PlayerObject player = playerDAO.getPlayer(ctx.getAuthor().getStringID());
		if(queueHelper.getPlayers().size() == 1){
			// If the queue becomes empty, the whole queue helper is renewed
			queueHelper.setPlayers(new ArrayList<>());
			ctx.getMessage().addReaction(":white_check_mark:");

		} else if (QueueHelper.isQueued(player, queueHelper)){
			// Removes the player from the queue if it contains at least 1 player
			queueHelper.getPlayers().remove(player);
			ctx.getMessage().addReaction(":white_check_mark:");
			// Checks primary role queue and removes the player from the list they're in
			if(queueHelper.getTanks().contains(player)){
				queueHelper.getTanks().remove(player);
				ctx.getMessage().addReaction(":white_check_mark:");
			} else if (queueHelper.getDps().contains(player)){
				queueHelper.getDps().remove(player);
				ctx.getMessage().addReaction(":white_check_mark:");
			} else if (queueHelper.getSupps().contains(player)){
				queueHelper.getSupps().remove(player);
				ctx.getMessage().addReaction(":white_check_mark:");
			} else if (queueHelper.getFlexes().contains(player)){
				queueHelper.getFlexes().remove(player);
				ctx.getMessage().addReaction(":white_check_mark:");
			}
		}
	}

	private static void registerPlayer(String id, String primaryRole, String secondaryRole) {
		playerDAO.insertPlayer(new PlayerObject(id, primaryRole, secondaryRole,1000));
	}

	private static void updatePlayer(String id, String primaryRole, String secondaryRole) {
		PlayerObject player = playerDAO.getPlayer(id);
		player.setPrimaryRole(primaryRole);
		player.setSecondaryRole(secondaryRole);
		playerDAO.updatePlayer(player);
	}

	public static String selectMap(){
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

	private static String getMapImg(String map) {

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