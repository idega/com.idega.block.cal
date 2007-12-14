package com.idega.block.cal.presentation;

import java.util.Locale;

import com.idega.block.cal.business.CalScheduleEntry;
import com.idega.block.cal.business.CalendarConstants;
import com.idega.block.cal.business.ScheduleSession;
import com.idega.business.SpringBeanLookup;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Heading2;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;

public class EntryInfoBlock extends Block {
	
	public void main(IWContext iwc) throws Exception {
		Layer main = new Layer();
		add(main);
		
		String entryId = iwc.getParameter("entryid");
		if (entryId == null) {
			addNoEntryLabelToContainer(iwc, main);
			return;
		}
		
		ScheduleSession schedule = (ScheduleSession) SpringBeanLookup.getInstance().getSpringBean(iwc, ScheduleSession.class);
		
		CalScheduleEntry entry = schedule.getCalendarEntryForInfoWindow(entryId);
		if (entry == null) {
			addNoEntryLabelToContainer(iwc, main);
			return;
		}
		
		main.add(new Heading1(entry.getEntryName()));
		
		Layer dates = new Layer();
		main.add(dates);
		
		Locale locale = iwc.getCurrentLocale();
		
		String startDate = entry.getEntryDate();
		String startTime = null;
		IWTimestamp date = null;
		if (startDate == null) {
			startDate = CoreConstants.SPACE;
			startTime = CoreConstants.EMPTY;
		}
		else {
			date = new IWTimestamp(startDate);
			startDate = date.getLocaleDate(locale);
			startTime = date.getLocaleTime(locale);
		}
		
		String endDate = entry.getEntryEndDate();
		String endTime = null;
		if (endDate == null) {
			endDate = CoreConstants.SPACE;
			endTime = CoreConstants.EMPTY;
		}
		else {
			date = new IWTimestamp(endDate);
			endDate = date.getLocaleDate(locale);
			endTime = date.getLocaleTime(locale);
		}
		
		StringBuffer datesText = new StringBuffer(startDate).append(CoreConstants.SPACE).append(startTime).append(CoreConstants.SPACE).append(CoreConstants.MINUS);
		datesText.append(CoreConstants.SPACE).append(endDate).append(CoreConstants.SPACE).append(endTime);
		dates.add(new Heading2(datesText.toString()));
		
		Layer description = new Layer();
		main.add(description);
		description.add(entry.getEntryDescription());
	}
	
	private void addNoEntryLabelToContainer(IWContext iwc, Layer container) {
		container.add(new Heading1(getResourceBundle(iwc).getLocalizedString("noEntriesToDisplay", "There are no entries to display")));
	}
	
	public String getBundleIdentifier() {
		return CalendarConstants.IW_BUNDLE_IDENTIFIER;
	}
}
