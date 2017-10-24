package com.github.peakz.DAO;

import com.darichey.discord.CommandContext;
import com.github.peakz.PugBot;
import com.github.peakz.queues.QueuePug;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerObject {

	private Timer timer = new Timer();

	private String id;
	private String primaryRole;
	private String secondaryRole;
	private int rating;
	private String roleFlag;
	private PlayerNotification pn = new PlayerNotification();

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

	public String getRoleFlag(){return roleFlag;}

	public void setRoleFlag(String roleFlag){
		this.roleFlag = roleFlag;
	}

	public void scheduleNotification(CommandContext ctx, String mode, int status) {
		if (status == 0) {
			pn.setCtx(ctx);
			pn.setMode(mode);
			timer.scheduleAtFixedRate(pn, 900000, 900000);
		} else {
			timer.cancel();
		}
	}

	private class PlayerNotification extends TimerTask {
		private int notifications;
		private String mode;
		private CommandContext ctx;

		@Override
		public void run(){
			switch(notifications) {
				case 0:
					System.out.println("1");
					ctx.getAuthor().getOrCreatePMChannel().sendMessage("You've been queued to " + mode.toLowerCase() + " for " + 15 + " minutes in " + ctx.getGuild().getName());
					break;

				case 1:
					System.out.println("2");
					ctx.getAuthor().getOrCreatePMChannel().sendMessage("You've been queued to " + mode.toLowerCase() + " for " + 30 + " minutes in " + ctx.getGuild().getName());
					break;

				case 2:
					System.out.println("3");
					ctx.getAuthor().getOrCreatePMChannel().sendMessage("You've been queued to " + mode.toLowerCase() + " for " + 45 + " minutes. You'll be removed from the queue after 60 minutes!");
					break;

				case 3:
					System.out.println("4");
					QueuePug qpug = PugBot.queueInstances.get(ctx.getGuild()).getQueuePug(mode);
					qpug.removePlayer(mode, qpug.getPlayer(ctx.getAuthor().getStringID()));
					ctx.getAuthor().getOrCreatePMChannel().sendMessage("You've been removed from the queue in" + ctx.getGuild().getName());
					notifications++;
					break;

				case 4:
					this.cancel();
					break;

				default:
					notifications++;
					break;
			}
			notifications++;
		}

		public void setMode(String mode) {
			notifications = 0;
			this.mode = mode;
		}

		public void setNotifications(int notifications){
			this.notifications = notifications;
		}

		public void setCtx(CommandContext ctx) {
			this.ctx = ctx;
		}
	}
}


