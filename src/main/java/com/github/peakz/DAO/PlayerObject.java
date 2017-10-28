package com.github.peakz.DAO;

import com.darichey.discord.CommandContext;
import com.github.peakz.queues.QueuePug;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerObject {

	private String id;
	private String primaryRole;
	private String secondaryRole;
	private int rating;
	private String roleFlag;
	private Timer timer;
	private boolean soloq;
	private boolean ranks;

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
		this.timer = new Timer();
		this.soloq = false;
		this.ranks = false;
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

	public boolean isSoloq() {
		return soloq;
	}

	public void setSoloq(boolean soloq) {
		this.soloq = soloq;
	}

	public boolean isRanks() {
		return ranks;
	}

	public void setRanks(boolean ranks) {
		this.ranks = ranks;
	}

	public void scheduleNotification(CommandContext ctx, String mode, int status, QueuePug qpug) {
		timer = new Timer();
		PlayerNotification pn = new PlayerNotification();
		if (status == 0) {
			try {
				pn.setCtx(ctx);
				pn.setMode(mode);
				pn.setQpug(qpug);
				pn.setPlayer(this);
				timer.scheduleAtFixedRate(pn, 900000, 900000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			timer.cancel();
		}
	}

	private class PlayerNotification extends TimerTask {
		private int notifications;
		private String mode;
		private CommandContext ctx;
		private QueuePug qpug;
		private PlayerObject player;

		@Override
		public void run() {
			switch (notifications) {
				case 0:
					ctx.getAuthor().getOrCreatePMChannel().sendMessage("You've been queued to `" + mode.toLowerCase() + "` for `" + 15 + "` minutes in `" + ctx.getGuild().getName() + "`");
					break;

				case 1:
					ctx.getAuthor().getOrCreatePMChannel().sendMessage("You've been queued to `" + mode.toLowerCase() + "` for `" + 30 + "` minutes in `" + ctx.getGuild().getName() + "`");
					break;

				case 2:
					ctx.getAuthor().getOrCreatePMChannel().sendMessage("You've been queued to `" + mode.toLowerCase() + "` for `" + 45 + "` minutes. You'll be removed from the queue after 60 minutes!");
					break;

				case 3:
					qpug.removePlayer(mode, player);
					ctx.getAuthor().getOrCreatePMChannel().sendMessage("You've been removed from the queue in `" + ctx.getGuild().getName() + "`");
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

		void setMode(String mode) {
			this.notifications = 0;
			this.mode = mode;
		}

		void setQpug(QueuePug qpug) {
			this.qpug = qpug;
		}

		void setPlayer(PlayerObject player) {
			this.player = player;
		}

		public void setCtx(CommandContext ctx) {
			this.ctx = ctx;
		}
	}
}


