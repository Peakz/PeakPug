package com.github.peakz.filters;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class LowFilterPug extends Filter<ILoggingEvent> {

	@Override
	public FilterReply decide(ILoggingEvent event) {
		if (event.getMessage().contains("changed presence")) {
			return FilterReply.DENY;
		} else {
			return FilterReply.NEUTRAL;
		}
	}
}
