package com.github.peakz.DAO;

import com.darichey.discord.CommandContext;
import com.github.peakz.queues.QueuePug;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerObject {

	private String id;
	private String primaryRole;
	private String secondaryRole;
	private int rating;
	private String roleFlag;
	private Timer timer;
	private PlayerNotification pn;

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
		this.roleFlag = "";
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

	public String getRoleFlag() {
		return roleFlag;
	}

	public void setRoleFlag(String roleFlag) {
		this.roleFlag = roleFlag;
	}

	public void scheduleNotification(CommandContext ctx, String mode, int status, QueuePug qpug) {
		if (status == 0) {
			try {
				this.timer = new Timer();
				this.pn = new PlayerNotification();
				pn.setAll(ctx, mode, qpug, this);
				timer.scheduleAtFixedRate(pn, 900000, 900000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				timer.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class PlayerNotification extends TimerTask {
		private AtomicInteger notifications;
		private String mode;
		private CommandContext ctx;
		private AtomicReference<QueuePug> qpug;
		private AtomicReference<PlayerObject> player;

		@Override
		public void run() {
			switch (notifications.get()) {
				case 0:
					//ctx.getAuthor().getOrCreatePMChannel().sendMessage("You've been queued to `" + mode.toLowerCase() + "` for `" + 15 + "` minutes in `" + ctx.getGuild().getName()"`");
					break;

				case 1:
					//ctx.getAuthor().getOrCreatePMChannel().sendMessage("You've been queued to `" + mode.toLowerCase() + "` for `" + 30 + "` minutes in `" + ctx.getGuild().getName()+ "`");
					break;

				case 2:
					ctx.getAuthor().getOrCreatePMChannel().sendMessage("You've been queued to `" + mode.toLowerCase() + "` for `" + 45 + "` minutes. You'll be removed from the queue after 60 minutes!");
					break;

				case 3:
					qpug.get().removePlayer(mode, player.get());
					ctx.getAuthor().getOrCreatePMChannel().sendMessage("You've been removed from the queue in `" + ctx.getGuild().getName() + "`");
					notifications.getAndIncrement();
					break;

				case 4:
					timer.cancel();
					break;

				default:
					notifications.getAndIncrement();
					break;
			}
			notifications.getAndIncrement();
		}

		void setAll(CommandContext ctx, String mode, QueuePug qpug, PlayerObject player) {
			this.ctx = ctx;
			this.mode = mode;
			this.notifications = new AtomicInteger();
			this.qpug = new AtomicReference<>(qpug);
			this.player = new AtomicReference<>(player);
		}
	}
}


