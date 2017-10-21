package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.DAO.PlayerObject;
import com.github.peakz.queues.QueueHelper;
import com.github.peakz.queues.QueueManager;

import java.util.Iterator;

public class PickCommand {
	private CommandContext ctx;
	private QueueManager queueManager;

	public PickCommand(CommandContext ctx, QueueManager queueManager) {
		this.ctx = ctx;
		this.queueManager = queueManager;
	}

	public void pickPlayer(String number) {
		QueueHelper queueHelper = queueManager.getQueueHelper("RANKS");

		switch(number) {
			case "1":
			case "2":
			case "3":
			case "4":
			case "5":
			case "6":
			case "7":
			case "8":
			case "9":
			case "10":
			case "11":
			case "12":
			case "13":
				if(queueHelper.turnToPick.equals("red")) {
					System.out.println("test1");
					if (pickPlayer(queueHelper, number)) {
						ctx.getMessage().addReaction(":white_check_mark:");
						queueHelper.turnToPick = "blue";
						ctx.getMessage().getChannel().sendMessage(playersLeft(queueHelper) + " Blue's turn to pick");
					}
				} else {
					System.out.println("test2");
					if (pickPlayer(queueHelper, number)) {
						ctx.getMessage().addReaction(":white_check_mark:");
						queueHelper.turnToPick = "red";
						ctx.getMessage().getChannel().sendMessage(playersLeft(queueHelper) + " Red's turn to pick");
					}
				}
				break;

			default:
				System.out.println("test3");
				break;
		}
	}

	private boolean pickPlayer(QueueHelper queueHelper, String number) {
		Iterator<PlayerObject> iter = queueHelper.getRankSplayers().iterator();

		while(iter.hasNext()) {
			PlayerObject player = iter.next();
			if(player.getId().equals(queueHelper.getRankSplayers().get(Integer.parseInt(number) - 1).getId()))
				queueHelper.pickRankS(ctx, player);
				iter.remove();
				return true;
		}
		/** if(queueHelper.restrictQueue) {
			for (PlayerObject player : queueHelper.getRankSplayers()) {
				if (player.getId().equals(id)) {
					queueHelper.pickRankS(ctx, player);
					queueHelper.getRankSplayers().remove(player);
					ctx.getMessage().getChannel().sendMessage(playersLeft(queueHelper));
				}
			}
		}*/
		return false;
	}

	private String playersLeft(QueueHelper queueHelper) {
		String playersLeft = "Players left: ";
		/** for(PlayerObject player : queueHelper.getRankSplayers()) {
			playersLeft += ctx.getGuild().getUserByID(Long.valueOf(player.getId())).getName() + " \n";
		}*/

		return queueHelper.showPlayersNoMention(ctx, queueHelper.getRankSplayers());
	}
}
