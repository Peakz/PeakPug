package com.github.peakz;

import com.darichey.discord.Command;
import com.darichey.discord.CommandListener;
import com.darichey.discord.CommandRegistry;
import com.github.peakz.DAO.PlayerDAO;
import com.github.peakz.DAO.PlayerDAOImp;
import com.github.peakz.DAO.PlayerObject;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.ArrayList;
import java.util.Random;

public class PugBot {
	public IDiscordClient client; // The instance of the discord client.

	/**
	 * String arrays of each game mode and its respective maps.
	 */
	private static String [] ESCORT = {"Dorado", "Route 66", "Watchpoint: Gibraltar"};
	private static String [] HYBRID = {"Eichenwalde", "Hollywood", "King's Row", "Numbani"};
	private static String [] CONTROL = {"Ilios", "Lijiang Tower", "Nepal", "Oasis"};
	private static String [] ASSAULT = {"Hanamura", "Horizon Lunar Colony", "Temple of Anubis", "Volskaya Industries"};
	private static String[][] MODES = {ESCORT, HYBRID, CONTROL, ASSAULT};
	private static String selectedMap = "";

	//for future feature
	//private static String [] ARENA = {"Black Forest", "Castillo", "Ecopoint: Antarctica", "Necropolis"};

	private static PlayerDAO playerDAO = new PlayerDAOImp();
	private static boolean allowRegister = true;
	private static int antiRNG = 0;

	private static PlayerObject[] red = new PlayerObject[5];
	private static PlayerObject[] blue = new PlayerObject[5];
	private static int turnToPick = 0;

	public static ArrayList<PlayerObject> allPlayers = new ArrayList<>();

	public PugBot(IDiscordClient client) {
		this.client = client; // Sets the client instance to the one provided
	}

	/**
	 * A custom login() method to handle all of the possible exceptions and set the bot instance.
	 */
	public static IDiscordClient createClient(String token, boolean login) { // Returns a new instance of the Discord client
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

	public static void createCommands(IDiscordClient client) {
		Command register = Command.builder()
				.onCalled(ctx -> {
					IUser user = ctx.getAuthor();
					String id = user.getStringID();

					if(playerDAO.checkId(id)){
						ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " you're already registered.");
					} else {

						/** Split the string into 3 parts
						 *  1. The command
						 *  2. Primary role
						 *  3. Secondary role
						 */
						String[] strArr = new String[3];
						int i = 0;

						/**
						 * Store the split strings
						 */
						for(String val : ctx.getMessage().getContent().split(" ", 3)) {
							strArr[i] = val;
							i++;
						}

						/**
						 * Switch statement for different cases
						 */
						switch((strArr[1].toUpperCase())) {
							case "TANK":
								if (strArr[2].toUpperCase().equals("DPS") || strArr[2].toUpperCase().equals("FLEX") || strArr[2].toUpperCase().equals("SUPP")) {
									registerPlayer(id, strArr[1], strArr[2], user.getLongID());
									ctx.getMessage().addReaction(":white_check_mark:");
								}
								break;
							case "SUPP":
								if (strArr[2].toUpperCase().equals("DPS") || strArr[2].toUpperCase().equals("FLEX") || strArr[2].toUpperCase().equals("TANK")) {
									registerPlayer(id, strArr[1], strArr[2], user.getLongID());
									ctx.getMessage().addReaction(":white_check_mark:");
								}
								break;
							case "FLEX":
								if (strArr[2].toUpperCase().equals("DPS") || strArr[2].toUpperCase().equals("TANK") || strArr[2].toUpperCase().equals("SUPP")) {
									registerPlayer(id, strArr[1], strArr[2], user.getLongID());
									ctx.getMessage().addReaction(":white_check_mark:");
								}
								break;
							case "DPS":
								if (strArr[2].toUpperCase().equals("TANK") || strArr[2].toUpperCase().equals("FLEX") || strArr[2].toUpperCase().equals("SUPP")) {
									registerPlayer(id, strArr[1], strArr[2], user.getLongID());
									ctx.getMessage().addReaction(":white_check_mark:");
								}
								break;
							default:
								ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " try again.");
								break;
						}
					}
				})
				.build();

		/**
		 * Create command for updating roles in a player profile
		 */
		Command update = Command.builder()
				.onCalled(ctx -> {
					IUser user = ctx.getAuthor();
					String id = user.getStringID();

					/** Split the string into 3 parts
					 *  1. The command
					 *  2. Primary role
					 *  3. Secondary role
					 */
					String[] strArr = new String[3];
					int i = 0;

					/**
					 * Store the split strings
					 */
					for(String val : ctx.getMessage().getContent().split(" ", 3)) {
						strArr[i] = val;
						i++;
					}

					/**
					 * Check if player exists and then switch statement for different cases
					 */
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
								ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " try again.");
								break;
						}
					} else {
						ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " you're not registered. Try again.");
					}
				})
				.build();

		Command queue = Command.builder()
				.onCalled(ctx -> {
					String id = ctx.getAuthor().getStringID();
					if  (allowRegister) {
						if (allPlayers.size() < 14) {
							if (playerDAO.checkId(id)) {
								PlayerObject player;
								player = playerDAO.getPlayer(id);
								if (player.getPrimaryRole().equals(null) || player.getSecondaryRole().equals(null)) {
									ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " you cannot queue until you've specified roles.");
								} else {
									player.setLong_id(ctx.getAuthor().getLongID());
									queuePlayer(player);
									ctx.getMessage().addReaction(":white_check_mark:");
								}
							} else {
								ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " you haven't registered yet. Type !Help for help.");
							}
						}

						if (allPlayers.size() == 14) {
							allowRegister = false;
							selectedMap = selectMap();
							Random random = new Random();

							PlayerObject captain_1 = allPlayers.get(random.nextInt(13));
							allPlayers.remove(captain_1);
							red[0] = captain_1;

							PlayerObject captain_2 = allPlayers.get(random.nextInt(12));
							allPlayers.remove(captain_2);
							blue[0] = captain_2;

							poolBuilder(ctx.getChannel());
						}
					} else {
						ctx.getChannel().sendMessage("Pick phase already in progress.");
					}
				})
				.build();

		Command pick = Command.builder()
				.onCalled(ctx -> {
					IGuild guild = ctx.getGuild();
					IUser user = guild.getUserByID(ctx.getMessage().getMentions().get(0).getLongID());

					switch (turnToPick) {
						case 0:
						case 2:
						case 4:
						case 6:
						case 8:
							if (ctx.getAuthor().getLongID() == red[0].getLong_id()) {
								String[] strArr = new String[2];
								int i = 0;

								for(String val : ctx.getMessage().getContent().split(" @", 2)) {
									strArr[i] = val;
									i++;
								}

								int j = 1;

								for (PlayerObject player : allPlayers) {
									if (player.getId().equals(strArr[1])) {
										allPlayers.remove(player);
										red[j] = player;
										ctx.getMessage().addReaction(":white_check_mark:");
										turnToPick++;
										break;
									} else {
										ctx.getChannel().sendMessage((strArr[1]) + " is already picked! Try agian.");
									}
									j++;
								}
							}
							break;

						case 1:
						case 3:
						case 5:
						case 7:
						case 9:
							if (ctx.getAuthor().getLongID() == blue[0].getLong_id()) {
								String[] strArr = new String[2];
								int i = 0;

								for(String val : ctx.getMessage().getContent().split(" @", 2)) {
									strArr[i] = val;
									i++;
								}

								int j = 1;

								for (PlayerObject player : allPlayers) {
									if (player.getId().equals(user.getStringID())) {
										allPlayers.remove(player);
										blue[j] = player;
										ctx.getMessage().addReaction(":white_check_mark:");
										turnToPick++;
										break;
									} else {
										ctx.getChannel().sendMessage((strArr[1]) + " is already picked! Try agian.");
									}
									j++;
								}
							}
							break;
						case 10:
							ctx.getChannel().sendMessage("**TEAMS READY, GL HF**");
							allowRegister = true;
							allPlayers.clear();
							red = new PlayerObject[5];
							blue = new PlayerObject[5];
							break;
					}
				})
				.build();

		Command exit = Command.builder()
				.onCalled(ctx -> {
					PlayerObject player = playerDAO.getPlayer(ctx.getAuthor().getStringID());
					if (allPlayers.contains(player)) {
						allPlayers.remove(player);
						ctx.getMessage().addReaction(":white_check_mark:");
					}

					ctx.getChannel().sendMessage(ctx.getAuthor().mention() + " you're not queued.");
				})
				.build();

		Command help = Command.builder()
				.onCalled(ctx -> {
					replyHelp(ctx.getAuthor());
					ctx.getMessage().addReaction(":white_check_mark:");
				})
				.build();

		/**
		 * Register the commands
		 */
		CommandRegistry registry = new CommandRegistry("!");
		registry.register(register, "Register");
		registry.register(register, "register");
		registry.register(update, "Update");
		registry.register(update, "update");
		registry.register(queue, "Queue");
		registry.register(queue, "queue");
		registry.register(pick, "Pick");
		registry.register(pick, "pick");
		registry.register(exit, "Exit");
		registry.register(exit, "exit");
		registry.register(help, "Help");
		registry.register(help, "help");
		client.getDispatcher().registerListener(new CommandListener(registry));
	}

	private static boolean checkRoles(String id) {
		return playerDAO.checkPrimaryRole(playerDAO.getPlayer(id));
	}

	private static void replyHelp(IUser user) {
		EmbedBuilder builder = new EmbedBuilder();

		builder.appendField("!Register <role_1> <role_2>", "Choose a primary and secondary role to register with.", true);
		builder.appendField("!Update <role_1> <role_2>", "Update your current primary and secondary roles.", true);
		builder.appendField("!Queue", "Puts you in the queue.", true);
		builder.appendField("!Exit", "Exits the queue.", true);
		builder.appendField("!Pick @name", "Command for captains to pick players with during pick phase.", true);

		builder.withColor(185, 255, 173);
		builder.withDescription("Roles accepted for role commands: Tank, Dps, Supp, Flex.");
		builder.withTitle("!Help - Explained commands");
		builder.withTimestamp(100);

		RequestBuffer.request(() -> user.getOrCreatePMChannel().sendMessage(builder.build()));

	}

	private static void replyUpdateRoles(IChannel channel, IUser user, String role_1, String role_2) {
		EmbedBuilder builder = new EmbedBuilder();

		builder.appendField("Primary role", role_1, true);
		builder.appendField("Secondary role", role_2, true);

		builder.withAuthorName(user.getName());
		builder.withColor(0, 255, 0);
		builder.withTimestamp(100);

		RequestBuffer.request(() -> channel.sendMessage(builder.build()));
	}

	private static void queuePlayer(PlayerObject player) {
		allPlayers.add(player);
	}

	private static void registerPlayer(String id, String primaryRole, String secondaryRole, Long long_id) {
		playerDAO.insertPlayer(new PlayerObject(id, primaryRole, secondaryRole,1000, long_id));
	}

	private static void updatePlayer(String id, String primaryRole, String secondaryRole) {
		PlayerObject player = playerDAO.getPlayer(id);
		player.setPrimaryRole(primaryRole);
		player.setSecondaryRole(secondaryRole);
		playerDAO.updatePlayer(player);
	}

	private static String selectMap(){
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

	private static void poolBuilder(IChannel channel) {
		EmbedBuilder builder = new EmbedBuilder();

		selectedMap = selectMap();
		builder.appendField("**RED TEAM**", "Captain: " + red[0], true);
		builder.appendField("**BLUE TEAM**", "Captain: " + blue[0], true);

		builder.withColor(0, 153, 255);
		builder.withDescription("MAP - " + selectedMap);
		builder.withTitle("PICK PHASE");
		String playersLeft = "Players left: ";

		for(PlayerObject player : allPlayers) {
			playersLeft += (channel.getGuild().getUserByID(player.getLong_id()).getName() + ", ");
		}

		builder.withFooterText(playersLeft);
		builder.withThumbnail(getMapImg(selectedMap));
		RequestBuffer.request(() -> channel.sendMessage(builder.build()));
	}

	public static String getMapImg(String map) {

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