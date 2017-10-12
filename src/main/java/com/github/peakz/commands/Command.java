package com.github.peakz.commands;

import com.darichey.discord.CommandContext;

public abstract class Command {
	private CommandContext ctx;

	public Command(CommandContext ctx){
		this.ctx = ctx;
	}
}
