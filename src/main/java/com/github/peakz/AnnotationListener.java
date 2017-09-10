package com.github.peakz;

import com.github.peakz.DAO.PlayerDAO;
import com.github.peakz.DAO.PlayerDAOImp;
import com.github.peakz.DAO.PlayerObject;
import com.github.peakz.DAO.TeamObject;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelJoinEvent;
import sx.blah.discord.handle.impl.events.guild.voice.user.UserVoiceChannelLeaveEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;

import java.util.ArrayList;

public class AnnotationListener {

	private static int counter = 0;
	private static ArrayList<PlayerObject> allPlayers = new ArrayList<>();
	private static PlayerObject[] red = new PlayerObject[5];
	private static PlayerObject[] blue = new PlayerObject[5];

	@EventSubscriber
	public void onReadyEvent(ReadyEvent event) { // This method is called when the ReadyEvent is dispatched
		event.getClient().changePlayingText("dividing by zero");
	}

	@EventSubscriber
	public void onUserJoinVoiceChannelEvent(UserVoiceChannelJoinEvent event) {
		IVoiceChannel channel = event.getVoiceChannel();
		IUser user = event.getUser();

		switch (channel.getName()) {
			case "PRO PUG LOBBY":

				PlayerDAO playerDao = new PlayerDAOImp();
				if (playerDao.checkId(user.getStringID())) {
					PlayerObject player = playerDao.getPlayer(user.getStringID());
					allPlayers.add(player);

					if(allPlayers.size() == 14) {
						TeamObject red = new TeamObject();
						TeamObject blue = new TeamObject();


					}

					break;
				} else {
					IChannel txt_channel = event.getGuild().getChannelsByName("propug").get(0).copy();
					txt_channel.sendMessage(event.getUser().mention() + " you're not registered.");
					break;
				}
			case "test":
				IChannel txt_channel = event.getGuild().getChannelsByName("propug").get(0).copy();
				txt_channel.sendMessage(event.getUser().mention() + " test");
				break;
			default:
				break;
		}
	}

	@EventSubscriber
	public void onUserLeaveVoiceChannelEvent(UserVoiceChannelLeaveEvent event) {
		IVoiceChannel channel = event.getVoiceChannel();
		IUser user = event.getUser();
	}

	public static void balanceTeams() {
		PlayerObject[] pool = new PlayerObject[14];

		for(int i = 0; i < 14; i++) {
			pool[i] = allPlayers.get(i);
			allPlayers.remove(pool[i]);

			if(pool[i].getRating() > pool[i - 1].getRating() && pool[i] != pool[0]) {
				PlayerObject temp = pool[i - 1];
				pool[i - 1] = pool[i];
				pool[i] = temp;
			}
		}

		red[0] = pool[0];
		blue[0] = pool[0];

	}
}