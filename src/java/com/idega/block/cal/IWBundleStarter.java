package com.idega.block.cal;
/*
 * $Id: IWBundleStarter.java,v 1.9 2007/12/14 12:42:49 valdas Exp $
 * Created on 2.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */

import com.idega.block.cal.business.CalendarConstants;
import com.idega.block.cal.business.CalendarRSSProducer;
import com.idega.block.rss.business.RSSProducerRegistry;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;

/**
 * 
 * @author justinas
 *
 */

public class IWBundleStarter implements IWBundleStartable {

	public void start(IWBundle starterBundle) {
		addRSSProducers(starterBundle);
	}

	public void stop(IWBundle starterBundle) {
	}
	
	private void addRSSProducers(IWBundle starterBundle) {
		RSSProducerRegistry registry = RSSProducerRegistry.getInstance();
		
		CalendarRSSProducer calendarProducer = new CalendarRSSProducer();
		registry.addRSSProducer(CalendarConstants.RSS_PRODUCER_ID, calendarProducer);
	}	
}