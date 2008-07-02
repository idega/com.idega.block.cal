package com.idega.block.cal.presentation;

import java.util.Locale;

import com.idega.block.cal.business.CalScheduleEntry;
import com.idega.block.cal.business.CalendarConstants;
import com.idega.block.cal.business.ScheduleSession;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Heading4;
import com.idega.presentation.text.Paragraph;
import com.idega.util.CoreConstants;
import com.idega.util.expression.ELUtil;

public class CalendarEntryInfoBlock extends Block {
	
	@Override
	@SuppressWarnings("cast")
	public void main(IWContext iwc) throws Exception {
		Layer main = new Layer();
		add(main);
		
		String entryId = iwc.getParameter("entryid");
		if (entryId == null) {
			addNoEntryLabelToContainer(iwc, main);
			return;
		}
		
		ScheduleSession schedule = ELUtil.getInstance().getBean(ScheduleSession.class);
		
		CalScheduleEntry entry = schedule.getCalendarEntryForInfoWindow(entryId);
		if (entry == null) {
			entry = schedule.getCalendarEntry(entryId, iwc.getParameter("schedule"), iwc.getCurrentLocale());
			
			if (entry == null) {
				addNoEntryLabelToContainer(iwc, main);
				return;
			}
		}
		
		main.add(new Heading1(entry.getEntryName()));
		
		Layer dates = new Layer();
		main.add(dates);
		
		Locale locale = iwc.getCurrentLocale();
		
		String startDate = entry.getEntryDate();
		String startTime = null;
		if (startDate == null) {
			startDate = CoreConstants.SPACE;
			startTime = CoreConstants.EMPTY;
		}
		else {
			String[] start = schedule.getLocalizedDateAndTime(startDate, locale);
			startDate = start[0];
			startTime = start[1];
		}
		
		String endDate = entry.getEntryEndDate();
		String endTime = null;
		if (endDate == null) {
			endDate = CoreConstants.SPACE;
			endTime = CoreConstants.EMPTY;
		}
		else {
			String[] end = schedule.getLocalizedDateAndTime(endDate, locale);
			endDate = end[0];
			endTime = end[1];
		}
		
		StringBuffer datesText = new StringBuffer(startDate);
		if (startDate.equals(endDate)) {
			datesText.append(CoreConstants.COLON).append(CoreConstants.SPACE).append(startTime).append(CoreConstants.SPACE).append(CoreConstants.MINUS);
			datesText.append(CoreConstants.SPACE).append(endTime);
		}
		else {
			datesText.append(CoreConstants.SPACE).append(startTime).append(CoreConstants.SPACE).append(CoreConstants.MINUS);
			datesText.append(CoreConstants.SPACE).append(endDate).append(CoreConstants.SPACE).append(endTime);
		}
		dates.add(new Heading4(datesText.toString()));
		
		Layer description = new Layer();
		Paragraph p = new Paragraph();
		description.add(p);
		p.addText(entry.getEntryDescription());
		main.add(description);
	}
	
	private void addNoEntryLabelToContainer(IWContext iwc, Layer container) {
		container.add(new Heading1(getResourceBundle(iwc).getLocalizedString("noEntriesToDisplay", "There are no entries to display")));
	}
	
	@Override
	public String getBundleIdentifier() {
		return CalendarConstants.IW_BUNDLE_IDENTIFIER;
	}
}
