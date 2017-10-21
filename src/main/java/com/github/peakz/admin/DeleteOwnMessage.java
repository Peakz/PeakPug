package com.github.peakz.admin;

import com.darichey.discord.CommandContext;

public class DeleteOwnMessage {

	private CommandContext ctx;

	public DeleteOwnMessage(CommandContext ctx) {
		this.ctx = ctx;
	}

	public void deleteLastMessage(String guild, String channel) {
		if(ctx.getMessage().getAuthor().getStringID().equals(ctx.getClient().getApplicationOwner().getStringID())) {
			ctx.getAuthor().getOrCreatePMChannel().sendMessage("test");
		}
	}
}
