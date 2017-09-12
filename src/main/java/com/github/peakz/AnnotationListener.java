package com.github.peakz;

import com.github.peakz.DAO.*;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelJoinEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelLeaveEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.ArrayList;
import java.util.Collections;

public class AnnotationListener {
	private MatchObject match;

	private static PlayerObject[] pool = new PlayerObject[12];
	private static ArrayList<PlayerObject> players = new ArrayList<>();
	private int counter = 0;

	private static PlayerDAO playerDAO = new PlayerDAOImp();

	@EventSubscriber
	public void onReadyEvent(ReadyEvent event) {
		event.getClient().changePlayingText("propugs");
	}

	@EventSubscriber
	public void onUserJoinVoiceChannelEvent(UserVoiceChannelJoinEvent event) {
		IVoiceChannel channel = event.getVoiceChannel();
		switch (channel.getName()) {
			// Using switch for future features and multiple queues
			case "propugsQueue1":
				PlayerObject player = playerDAO.getPlayer(event.getUser().getStringID());

				if(players.size() == 10 && player != null) {
					players.add(player);
					TeamObject red = new TeamObject();
					TeamObject blue = new TeamObject();

					match = balanceTeams(red, blue);
					movePlayersVoice(event.getGuild());

					newMatchMessage(event.getVoiceChannel().getGuild(), match);

					MatchDAO matchDAO = new MatchDAOImp();
					matchDAO.insertMatch(match);
					break;
				} else if((players.size() < 10) && (player != null)) {
					players.add(player);
					break;
				}
			default:
				break;
		}
	}

	@EventSubscriber
	public void onUserLeaveVoiceChannelEvent(UserVoiceChannelLeaveEvent event) {
		IVoiceChannel channel = event.getVoiceChannel();
		PlayerObject player = playerDAO.getPlayer(event.getUser().getStringID());
		switch(channel.getName()){
			case "propugsQueue1":
				players.remove(player);
				IChannel txt_channel = event.getGuild().getChannelsByName("propugs").get(0).copy();
				txt_channel.sendMessage(event.getUser().mention() + " you left");
				break;
			default:
				break;
		}
	}

	public static void movePlayersVoice(IGuild guild){
		IVoiceChannel voice_1 = guild.getVoiceChannelsByName("propugsRed1").get(0).copy();
		IVoiceChannel voice_2 = guild.getVoiceChannelsByName("propugsBlue1").get(0).copy();
		for(PlayerObject player : players){
			if((players.get(0).equals(player) || players.get(2).equals(player) || players.get(4).equals(player) || players.get(6).equals(player) || players.get(8).equals(player) || players.get(10).equals(player))){
				guild.getUserByID(player.getLong_id()).moveToVoiceChannel(voice_1);
			} else {
				guild.getUserByID(player.getLong_id()).moveToVoiceChannel(voice_2);
			}
		}
	}

	public static MatchObject balanceTeams(TeamObject red, TeamObject blue) {
		for(int i = 0; i < players.size(); i++) {
			if(i > 0) {
				if ((players.get(i - 1).getRating() > players.get(i).getRating())) {
					Collections.swap(players, i, i - 1);
				}
			}
		}


		red.setCaptain(players.get(0));
		red.setPlayer_1(players.get(2));
		red.setPlayer_2(players.get(4));
		red.setPlayer_3(players.get(6));
		red.setPlayer_4(players.get(8));
		red.setPlayer_5(players.get(10));

		blue.setCaptain(players.get(1));
		blue.setPlayer_1(players.get(3));
		blue.setPlayer_2(players.get(5));
		blue.setPlayer_3(players.get(7));
		blue.setPlayer_4(players.get(9));
		blue.setPlayer_5(players.get(11));

		return new MatchObject(red, blue);
	}

	private static void newMatchMessage(IGuild guild, MatchObject match) {
		MatchDAO matchDAO = new MatchDAOImp();

		EmbedBuilder builder = new EmbedBuilder();

		builder.appendField("RED TEAM MMR: " + match.getTeam_red().getAvgRating(), "Captain: " + guild.getUserByID(match.getTeam_red().getCaptain().getLong_id()).getName(), true);
		builder.appendField("BLUE TEAM MMR: " + match.getTeam_blue().getAvgRating(), "Captain: " + guild.getUserByID(match.getTeam_blue().getCaptain().getLong_id()).getName(), true);

		builder.withColor(0, 153, 255);
		builder.withDescription("MAP - " + match.getMap());
		builder.withTitle("MATCH ID: " + matchDAO.getLastMatchID());

		builder.withFooterText("EACH CAPTAIN MUST REPORT RESULTS AFTER THE MATCH, \"!Result match_id team_color\"");
		builder.withThumbnail(PugBot.getMapImg(match.getMap()));
		RequestBuffer.request(() -> guild.getChannelsByName("propugs").get(0).copy().sendMessage(builder.build()));
	}

	public MatchObject getMatch() {
		return match;
	}

	public void setMatch(MatchObject match) {
		this.match = match;
	}
}